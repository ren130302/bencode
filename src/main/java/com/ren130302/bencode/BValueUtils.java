package com.ren130302.bencode;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

/* package */final class BValueUtils {
	/* package */ static EOFException createEndStreamException() {
		return new EOFException("The end of the stream was reached.");
	}

	/* package */ static int read(final @Nonnull ByteBuffer byteBuffer) throws IOException {
		if (!byteBuffer.hasRemaining()) {
			throw createEndStreamException();
		}

		return byteBuffer.get() & 0xff;
	}

	/* package */ static int getNextIndicator(final @Nonnull ByteBuffer byteBuffer, final @Nonnull AtomicInteger indicator) throws IOException {
		if (indicator.get() == 0) {
			indicator.set(read(byteBuffer));
		}

		return indicator.get();
	}
}
