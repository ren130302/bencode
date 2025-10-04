package bencode;

public final class BEncodeConstants {

  private BEncodeConstants() {
    throw new AssertionError();
  }

  public static final byte INTEGER = 'i';
  public static final byte LIST = 'l';
  public static final byte DICTIONARY = 'd';
  public static final byte END = 'e';
  public static final byte CORON = ':';
  public static final byte NEGA = '-';
}
