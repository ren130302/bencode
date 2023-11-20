package bencode.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import bencode.values.BInteger;
import bencode.values.BString;

public final class BEncodeJson {
	public static ObjectMapper get() {
		ObjectMapper objMap = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(BString.class, new BStringSerializer());
		module.addSerializer(BInteger.class, new BIntegerSerializer());
		objMap.registerModule(module);
		return objMap;
	}
}
