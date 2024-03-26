package bencode.value;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class BDictionary implements BValue<Map<BString, BValue<?>>>, Map<BString, BValue<?>> {

    private static final long serialVersionUID = -7574359365654348201L;

    public static BDictionary create() {
        return new BDictionary();
    }

    public static BDictionary create(Map<BString, BValue<?>> m) {
        return new BDictionary(m);
    }

    private final LinkedHashMap<BString, BValue<?>> value;

    private BDictionary() {
        this.value = new LinkedHashMap<>();
    }

    private BDictionary(Map<BString, BValue<?>> m) {
        this.value = new LinkedHashMap<>(m);
    }

    @Override
    public Map<BString, BValue<?>> getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getValue().equals(obj);
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BDictionary clone() {
        return new BDictionary((Map<BString, BValue<?>>) this.value.clone());
    }

    public BValue<?> get(String key) {
        return this.get(BString.valueOf(key));
    }

    public BDictionary getBDictionary(String key) {
        return (BDictionary) this.get(key);
    }

    public BInteger getBInteger(String key) {
        return (BInteger) this.get(key);
    }

    public BList<?> getBList(String key) {
        return (BList<?>) this.get(key);
    }

    public BString getBString(String key) {
        return (BString) this.get(key);
    }

    public BValue<?> put(String key, BValue<?> value) {
        return this.put(BString.valueOf(key), value);
    }

    public BValue<?> put(String key, Number value) {
        return this.put(BString.valueOf(key), BInteger.valueOf(value));
    }

    public BValue<?> put(String key, byte[] value) {
        return this.put(BString.valueOf(key), BString.valueOf(value));
    }

    public BValue<?> put(String key, Byte[] value) {
        return this.put(BString.valueOf(key), BString.valueOf(value));
    }

    public BValue<?> put(String key, String value) {
        return this.put(BString.valueOf(key), BString.valueOf(value));
    }

    public BValue<?> put(String key, String value, Charset charset) {
        return this.put(BString.valueOf(key), BString.valueOf(value, charset));
    }

    public BValue<?> put(String key, String value, String charset)
            throws UnsupportedEncodingException {
        return this.put(BString.valueOf(key), BString.valueOf(value, charset));
    }

    @Override
    public int size() {
        return this.getValue().size();
    }

    @Override
    public boolean isEmpty() {
        return this.getValue().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.getValue().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.getValue().containsValue(value);
    }

    @Override
    public BValue<?> get(Object key) {
        return this.getValue().get(key);
    }

    @Override
    public BValue<?> put(BString key, BValue<?> value) {
        return this.getValue().put(key, value);
    }

    @Override
    public BValue<?> remove(Object key) {
        return this.getValue().remove(key);
    }

    @Override
    public void putAll(Map<? extends BString, ? extends BValue<?>> m) {
        this.getValue().putAll(m);
    }

    @Override
    public void clear() {
        this.getValue().clear();
    }

    @Override
    public Set<BString> keySet() {
        return this.getValue().keySet();
    }

    @Override
    public Collection<BValue<?>> values() {
        return this.getValue().values();
    }

    @Override
    public Set<Entry<BString, BValue<?>>> entrySet() {
        return this.getValue().entrySet();
    }

}
