package bencode;

import java.io.Serializable;

public interface IBValue<T> extends Cloneable, Serializable {

	T getValue();

	@Override
	String toString();

	@Override
	boolean equals(Object obj);

	@Override
	int hashCode();

	IBValue<T> clone();

	BValueType getType();
}