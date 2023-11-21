package bencode.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import bencode.values.BInteger;

public class BIntegerSerializer extends StdSerializer<BInteger> {

	private static final long serialVersionUID = 216720264221714444L;

	public BIntegerSerializer() {
		super(BInteger.class, true);
	}

	@Override
	public void serialize(BInteger value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeNumber(value.getValue());
	}

}
