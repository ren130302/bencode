package bencode.values;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BList<T extends BValue<?>> extends ArrayList<T> implements BValue<List<T>> {

	private static final long serialVersionUID = 1551379075504078235L;

	public static <T extends BValue<?>> BList<T> create() {
		return new BList<>();
	}

	public static BListBString createBString() {
		return new BList.BListBString();
	}

	public static BListBInteger createBInteger() {
		return new BList.BListBInteger();
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

	public static class BListBString extends BList<BString> {

		private static final long serialVersionUID = 4022377281227982481L;

		public boolean add(Byte[] e) {
			return super.add(BString.valueOf(e));
		}

		public boolean add(byte[] e) {
			return super.add(BString.valueOf(e));
		}

		public boolean add(String e) {
			return super.add(BString.valueOf(e));
		}

		public boolean add(String e, String charset) throws UnsupportedEncodingException {
			return super.add(BString.valueOf(e, charset));
		}

		public boolean add(String e, Charset charset) {
			return super.add(BString.valueOf(e, charset));
		}
	}

	public static class BListBInteger extends BList<BInteger> {

		private static final long serialVersionUID = -752519153619738962L;

		public boolean add(Number e) {
			return super.add(BInteger.valueOf(e));
		}
	}
}