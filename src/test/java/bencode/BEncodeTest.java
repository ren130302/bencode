package bencode;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.Test;

class BEncodeTest {

  // ---------------- BBytes ----------------
  @Test
  void testBBytesWriteAndRead() throws IOException {
    BBytes original = BBytes.valueOf("hello");
    BEncodeOutputStream out = new BEncodeOutputStream();
    out.writeBBytes(original);

    // 書き込み直後の形式確認
    String written = new String(out.toByteArray(), StandardCharsets.US_ASCII);
    assertEquals("5:hello", written);

    // 読み込み確認
    BBytes deserialized = new BEncodeInputStream(out.toByteArray()).readBBytes();
    assertArrayEquals(original.getValue(), deserialized.getValue());
    assertEquals(original, deserialized);
  }

  // ---------------- BInteger ----------------
  @Test
  void testBIntegerWriteAndRead() throws IOException {
    BInteger original = BInteger.valueOf(12345);
    BEncodeOutputStream out = new BEncodeOutputStream();
    out.writeBInt(original);

    String written = new String(out.toByteArray(), StandardCharsets.US_ASCII);
    assertEquals("i12345e", written);

    BInteger deserialized = new BEncodeInputStream(out.toByteArray()).readBInteger();
    assertEquals(original.getValue(), deserialized.getValue());
    assertEquals(original, deserialized);
  }

  // ---------------- 空のリストと辞書 ----------------
  @Test
  void testEmptyBListAndBDictWriteAndRead() throws IOException {
    BList<BValue<?>> emptyList = BList.<BValue<?>>builder().build();
    BDictionary emptyDict = BDictionary.builder().build();

    BEncodeOutputStream out = new BEncodeOutputStream();
    out.writeBList(emptyList);
    out.writeBDict(emptyDict);

    String written = new String(out.toByteArray(), StandardCharsets.US_ASCII);
    assertEquals("le" + "de", written); // 空リスト: "le", 空辞書: "de"

    BEncodeInputStream in = new BEncodeInputStream(out.toByteArray());
    BList<?> deserializedList = in.readBList();
    BDictionary deserializedDict = in.readBDict();

    assertEquals(emptyList, deserializedList);
    assertEquals(emptyDict, deserializedDict);
  }

  // ---------------- BList ----------------
  @Test
  void testBListWriteAndRead() throws IOException {
    BList<BValue<?>> original =
        BList.<BValue<?>>builder().add(BBytes.valueOf("a")).add(BInteger.valueOf(42)).build();

    BEncodeOutputStream out = new BEncodeOutputStream();
    out.writeBList(original);

    String written = new String(out.toByteArray(), StandardCharsets.US_ASCII);
    assertEquals("l1:ai42ee", written); // "l<elem1><elem2>e"

    BList<?> deserialized = new BEncodeInputStream(out.toByteArray()).readBList();
    assertEquals(original, deserialized);
  }

  // ---------------- BDictionary ----------------
  @Test
  void testBDictionaryWriteAndRead() throws IOException {
    BDictionary original = BDictionary.builder().put("key1", BBytes.valueOf("value1"))
        .put("key2", BInteger.valueOf(99))
        .put("key3",
            BList.<BValue<?>>builder().add(BBytes.valueOf("x")).add(BInteger.valueOf(5)).build())
        .build();

    BEncodeOutputStream out = new BEncodeOutputStream();
    out.writeBDict(original);

    BDictionary deserialized = new BEncodeInputStream(out.toByteArray()).readBDict();
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

  // ---------------- ネスト構造 ----------------
  @Test
  void testNestedStructuresWriteAndRead() throws IOException {
    BDictionary nested = BDictionary.builder()
        .put("list",
            BList.<BValue<?>>builder().add(BInteger.valueOf(1)).add(BInteger.valueOf(2))
                .add(BInteger.valueOf(3)).build())
        .put("dict", BDictionary.builder().put("a", BBytes.valueOf("x"))
            .put("b", BInteger.valueOf(10)).build())
        .build();

    BEncodeOutputStream out = new BEncodeOutputStream();
    out.writeBDict(nested);

    BDictionary deserialized = new BEncodeInputStream(out.toByteArray()).readBDict();
    assertEquals(nested, deserialized);
  }

  // ---------------- 深いネスト構造 ----------------
  @Test
  void testDeeplyNestedStructureWriteAndRead() throws IOException {
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

    BDictionary deserialized = new BEncodeInputStream(out.toByteArray()).readBDict();
    assertEquals(nestedDict, deserialized);

    // 追加チェック
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
