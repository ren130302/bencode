package bencode;

import java.io.Serializable;

public interface BValue<T> extends Serializable, Cloneable {

	BValue<T> clone();

	@Override
	boolean equals(Object obj);

	T getValue();

	@Override
	int hashCode();

	@Override
	String toString();
}