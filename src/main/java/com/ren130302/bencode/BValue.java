package com.ren130302.bencode;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

public interface BValue
	extends Cloneable, Serializable {

	char INTEGER = 'i';
	char LIST = 'l';
	char DICTIONARY = 'd';
	char END = 'e';
	char CORON = ':';
	char ZERO = '0';
	char ONE = '1';
	char NINE = '9';
	char NEGA = '-';

	@Override
	String toString();

	@Override
	boolean equals(Object obj);

	@Override
	int hashCode();

	BValue clone() throws CloneNotSupportedException;

	default ByteBuffer toByteBuffer() {
		return writeToByteBuffer(this);
	}

	default byte[] toBytes() {
		return writeToBytes(this);
	}

	static String print(final @Nonnull BValue value) {
		if (value instanceof BString v) {
			return BString.print(v);
		}
		else if (value instanceof BInteger v) {
			return BInteger.print(v);
		}
		else if (value instanceof BList v) {
			return BList.print(v);
		}
		else if (value instanceof BDictionary v) {
			return BDictionary.print(v);
		}

		throw new IllegalArgumentException("Unknown class '" + value.getClass().getSimpleName() + "'");
	}

	static String writeToString(final @Nonnull BValue value) {
		if (value instanceof BString v) {
			return BString.writeToString(v);
		}
		else if (value instanceof BInteger v) {
			return BInteger.writeToString(v);
		}
		else if (value instanceof BList v) {
			return BList.writeToString(v);
		}
		else if (value instanceof BDictionary v) {
			return BDictionary.writeToString(v);
		}

		throw new IllegalArgumentException("Unknown class '" + value.getClass().getSimpleName() + "'");
	}

	static byte[] writeToBytes(final @Nonnull BValue value) {
		return writeToString(value).getBytes();
	}

	static ByteBuffer writeToByteBuffer(final @Nonnull BValue value) {
		return ByteBuffer.wrap(writeToBytes(value));
	}

	static BValue readFromByteBuffer(final @Nonnull ByteBuffer data) throws IOException {
		return readFromByteBuffer(data, new AtomicInteger());
	}

	static BValue readFromByteBuffer(final @Nonnull ByteBuffer byteBuffer, final @Nonnull AtomicInteger indicator) throws IOException {
		if (BValueUtils.getNextIndicator(byteBuffer, indicator) == -1) {
			throw BValueUtils.createEndStreamException();
		}

		if (ZERO <= indicator.get() && indicator.get() <= NINE) {
			return BString.readFromByteBuffer(byteBuffer, indicator);
		}
		else if (indicator.get() == INTEGER) {
			return BInteger.readFromByteBuffer(byteBuffer, indicator);
		}
		else if (indicator.get() == LIST) {
			return BList.readFromByteBuffer(byteBuffer, indicator);
		}
		else if (indicator.get() == DICTIONARY) {
			return BDictionary.readFromByteBuffer(byteBuffer, indicator);
		}

		throw new IllegalArgumentException("Unknown indicator '" + indicator + "'");
	}

	static BValue readFromBytes(final @Nonnull byte[] data) throws IOException {
		return readFromByteBuffer(ByteBuffer.wrap(data));
	}

	static BValue readFromString(final @Nonnull String data) throws IOException {
		return readFromBytes(data.getBytes());
	}
}