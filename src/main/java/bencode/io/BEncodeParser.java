package bencode.io;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import bencode.io.deserializer.BDictionaryDeserializer;
import bencode.io.deserializer.BIntegerDeserializer;
import bencode.io.deserializer.BListDeserializer;
import bencode.io.deserializer.BStringDeserializer;
import bencode.io.deserializer.BValueDeserializer;
import bencode.io.serializer.BDictionarySerializer;
import bencode.io.serializer.BIntegerSerializer;
import bencode.io.serializer.BListSerializer;
import bencode.io.serializer.BStringSerializer;
import bencode.io.serializer.BValueSerializer;
import bencode.value.BValue;

public final class BEncodeParser implements Serializable {

    private static final long serialVersionUID = 6196383765193868451L;

    private final Map<Class<?>, Serializer<?>> serializers = new HashMap<>();
    private final Map<Class<?>, Deserializer<?>> deserializers = new HashMap<>();

    public BEncodeParser() {
        this.register(BStringSerializer.class, BStringDeserializer.class);
        this.register(BIntegerSerializer.class, BIntegerDeserializer.class);
        this.register(BListSerializer.class, BListDeserializer.class);
        this.register(BDictionarySerializer.class, BDictionaryDeserializer.class);
        this.register(BValueSerializer.class, BValueDeserializer.class);
    }

    private void register(Class<? extends Serializer<?>> sc, Class<? extends Deserializer<?>> dc) {
        try {
            Serializer<?> serializer = sc.getConstructor().newInstance();
            Deserializer<?> deserializer = dc.getConstructor().newInstance();

            this.serializers.put(sc, serializer);
            this.deserializers.put(dc, deserializer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <SC extends Serializer<?>> SC getSerializer(Class<SC> cls) {
        return cls.cast(this.deserializers.get(cls));
    }

    public <DC extends Deserializer<?>> DC getDeserializer(Class<DC> cls) {
        return cls.cast(this.deserializers.get(cls));
    }

    public BStringSerializer getBStringSerializer() {
        return this.getSerializer(BStringSerializer.class);
    }

    public BStringDeserializer getBStringDeserializer() {
        return this.getDeserializer(BStringDeserializer.class);
    }

    public BIntegerSerializer getBIntegerSerializer() {
        return this.getSerializer(BIntegerSerializer.class);
    }

    public BIntegerDeserializer getBIntegerDeserializer() {
        return this.getDeserializer(BIntegerDeserializer.class);
    }

    public BListSerializer getBListSerializer() {
        return this.getSerializer(BListSerializer.class);
    }

    public BListDeserializer getBListDeserializer() {
        return this.getDeserializer(BListDeserializer.class);
    }

    public BDictionarySerializer getBDictionarySerializer() {
        return this.getSerializer(BDictionarySerializer.class);
    }

    public BDictionaryDeserializer getBDictionaryDeserializer() {
        return this.getDeserializer(BDictionaryDeserializer.class);
    }

    public BValueSerializer getBValueSerializer() {
        return this.getSerializer(BValueSerializer.class);
    }

    public BValueDeserializer getBValueDeserializer() {
        return this.getDeserializer(BValueDeserializer.class);
    }

    public <V extends BValue<?>, SC extends Serializer<V>> byte[] serialize(
            Supplier<BEncodeOutputStream> supplier, Class<SC> cls, V value) {
        try (BEncodeOutputStream outputStream = supplier.get()) {
            this.getSerializer(cls).serialize(this, outputStream, value);
            return outputStream.getBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <V extends BValue<?>, DC extends Deserializer<V>> V deserialize(
            Supplier<BEncodeInputStream> supplier, Class<DC> cls) {
        try (BEncodeInputStream inputStream = supplier.get()) {
            return this.getDeserializer(cls).deserialize(this, inputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
