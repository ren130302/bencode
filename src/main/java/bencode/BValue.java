package bencode;

import java.io.Serializable;

public interface BValue<T> extends Cloneable, Serializable {

	T getValue();

	@Override
	String toString();

	@Override
	boolean equals(Object obj);

	@Override
	int hashCode();

	BValue<T> clone();

	BValueType getType();
}