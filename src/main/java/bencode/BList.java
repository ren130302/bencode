package bencode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class BList<T extends BValue<?>> extends ArrayList<T> implements BValue<List<T>> {

	private static final long serialVersionUID = 1551379075504078235L;

	public static <T extends BValue<?>> BList<T> create() {
		return new BList<>();
	}

	public static <T extends BValue<?>> BList<T> create(Collection<T> c) {
		return new BList<>(c);
	}

	private BList() {
		super();
	}

	private BList(Collection<T> c) {
		super(c);
	}

	@Override
	public List<T> getValue() {
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BList<T> clone() {
		return (BList<T>) super.clone();
	}

}