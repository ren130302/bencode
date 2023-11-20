package bencode.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import bencode.values.BString;

public class BStringSerializer extends StdSerializer<BString> {

	private static final long serialVersionUID = -7148799584272012719L;

	public BStringSerializer() {
		super(BString.class, true);
	}

	@Override
	public void serialize(BString value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		serializers.findValueSerializer(String.class).serialize(value.getString(), gen, serializers);
	}

}
