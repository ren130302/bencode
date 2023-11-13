package bencode;

import java.util.Comparator;

public final class BValueComparator implements Comparator<BValue<?>> {

	@Override
	public int compare(BValue<?> o1, BValue<?> o2) {
		return o1.getType().ordinal() - o2.getType().ordinal();
	}
}
