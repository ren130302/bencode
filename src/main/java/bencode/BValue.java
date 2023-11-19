package bencode;

import java.io.Serializable;

public interface BValue<T> extends Serializable, Cloneable {

	T getValue();

	BValue<T> clone();

	@Override
	boolean equals(Object obj);

	@Override
	int hashCode();

	@Override
	String toString();
}