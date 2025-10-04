package bencode;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;

class BEncodeTest {

  @Test
  void testBBytes() throws IOException {
    BBytes original = BBytes.valueOf("hello");
    BEncodeOutputStream out = new BEncodeOutputStream();
    out.writeBBytes(original);

    byte[] bytes = out.toByteArray();
    BBytes deserialized = new BEncodeInputStream(bytes).readBBytes();

    assertArrayEquals(original.getValue(), deserialized.getValue());
    assertEquals(original, deserialized);
  }

  @Test
  void testBInteger() throws IOException {
    BInteger original = BInteger.valueOf(12345);
    BEncodeOutputStream out = new BEncodeOutputStream();
    out.writeBInt(original);

    byte[] bytes = out.toByteArray();
    BInteger deserialized = new BEncodeInputStream(bytes).readBInteger();

    assertEquals(original.getValue(), deserialized.getValue());
    assertEquals(original, deserialized);
  }

  @Test
  void testBList() throws IOException {
    BList<BValue<?>> original =
        BList.<BValue<?>>builder().add(BBytes.valueOf("a")).add(BInteger.valueOf(42)).build();

    BEncodeOutputStream out = new BEncodeOutputStream();
    out.writeBList(original);

    byte[] bytes = out.toByteArray();
    BList<?> deserialized = new BEncodeInputStream(bytes).readBList();

    assertEquals(original.getValue().size(), deserialized.getValue().size());
    assertEquals(original, deserialized);
  }

  @Test
  void testBDictionary() throws IOException {
    BDictionary original = BDictionary.builder().put("key1", BBytes.valueOf("value1"))
        .put("key2", BInteger.valueOf(99))
        .put("key3",
            BList.<BValue<?>>builder().add(BBytes.valueOf("x")).add(BInteger.valueOf(5)).build())
        .build();

    BEncodeOutputStream out = new BEncodeOutputStream();
    out.writeBDict(original);

    byte[] bytes = out.toByteArray();
    BDictionary deserialized = new BEncodeInputStream(bytes).readBDict();

    assertEquals(original.getValue().size(), deserialized.getValue().size());

    for (Map.Entry<BBytes, BValue<?>> entry : original.getValue().entrySet()) {
      BValue<?> v1 = entry.getValue();
      BValue<?> v2 = deserialized.getValue().get(entry.getKey());
      assertNotNull(v2);
      if (v1 instanceof BBytes b1 && v2 instanceof BBytes b2) {
        assertArrayEquals(b1.getValue(), b2.getValue());
      } else {
        assertEquals(v1, v2);
      }
    }
    assertEquals(original, deserialized);
  }

  @Test
  void testNestedStructures() throws IOException {
    BDictionary nested = BDictionary.builder()
        .put("list",
            BList.<BValue<?>>builder().add(BInteger.valueOf(1)).add(BInteger.valueOf(2))
                .add(BInteger.valueOf(3)).build())
        .put("dict", BDictionary.builder().put("a", BBytes.valueOf("x"))
            .put("b", BInteger.valueOf(10)).build())
        .build();

    BEncodeOutputStream out = new BEncodeOutputStream();
    out.writeBDict(nested);

    byte[] bytes = out.toByteArray();
    BDictionary deserialized = new BEncodeInputStream(bytes).readBDict();

    assertEquals(nested, deserialized);
  }

  @Test
  void testDeeplyNestedStructure() throws IOException {
    BDictionary nestedDict = BDictionary.builder().put("level1_dict", BDictionary.builder().put(
        "level2_list",
        BList.<BValue<?>>builder().add(BBytes.valueOf("alpha")).add(BInteger.valueOf(123))
            .add(BDictionary.builder().put("level3_key", BBytes.valueOf("omega")).build()).build())
        .put("level2_value", BInteger.valueOf(999)).build())
        .put("root_list",
            BList.<BValue<?>>builder().add(BInteger.valueOf(42)).add(BBytes.valueOf("hello"))
                .add(BList.<BValue<?>>builder().add(BBytes.valueOf("nested"))
                    .add(BInteger.valueOf(7)).build())
                .build())
        .put("simple_key", BBytes.valueOf("simple_value")).build();

    BEncodeOutputStream out = new BEncodeOutputStream();
    out.writeBDict(nestedDict);

    byte[] bytes = out.toByteArray();
    BDictionary deserialized = new BEncodeInputStream(bytes).readBDict();

    assertEquals(nestedDict, deserialized);

    // ルートとネスト値の追加チェック
    BList<?> rootList =
        deserialized.getValue().get(BBytes.valueOf("root_list")) instanceof BList<?> l ? l : null;
    assertNotNull(rootList);
    assertEquals(3, rootList.getValue().size());

    BDictionary level1Dict =
        deserialized.getValue().get(BBytes.valueOf("level1_dict")) instanceof BDictionary d ? d
            : null;
    assertNotNull(level1Dict);
    BList<?> level2List =
        level1Dict.getValue().get(BBytes.valueOf("level2_list")) instanceof BList<?> l2 ? l2 : null;
    assertNotNull(level2List);
    assertEquals(3, level2List.getValue().size());
  }
}
