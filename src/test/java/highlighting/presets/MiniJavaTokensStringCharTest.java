package highlighting.presets;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.regex.Token;
import java.util.regex.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MiniJavaTokensStringCharTest {

  static Token stringToken;
  static Token charToken;

  @BeforeAll
  static void init() {
    stringToken =
        MiniJavaTokens.defaultTokens().stream()
            .filter(t -> t.colour() == MiniJavaColours.STRING_LITERAL_COLOUR)
            .findFirst()
            .orElseThrow();

    charToken =
        MiniJavaTokens.defaultTokens().stream()
            .filter(t -> t.colour() == MiniJavaColours.CHAR_LITERAL_COLOUR)
            .findFirst()
            .orElseThrow();
  }

  // ---------------- Strings ----------------

  @Test
  void string_token_shouldMatch() {
    String testString = "\"string\"";
    assertTrue(stringToken.pattern().matcher(testString).matches());
  }

  @Test
  void string_token_shouldNotMatch() {
    String testString = "string";
    assertFalse(stringToken.pattern().matcher(testString).matches());
  }

  @Test
  void string_token_shouldMatch_atStart() {
    String testString = "\"string\" at beginning";
    assertTrue(stringToken.pattern().matcher(testString).find());
  }

  @Test
  void string_token_shouldMatch_atMiddle() {
    String testString = "A \"string\" in the middle";
    assertTrue(stringToken.pattern().matcher(testString).find());
  }

  @Test
  void string_token_shouldMatch_atEnd() {
    String testString = "At the end a \"string\"";
    assertTrue(stringToken.pattern().matcher(testString).find());
  }

  @Test
  void string_token_shouldMatch_multipleTimes() {
    String testString = "\"string\" \"string\" \"string\" ";
    Matcher matcher = stringToken.pattern().matcher(testString);

    int count = 0;
    while (matcher.find()) {
      count++;
    }

    assertEquals(3, count);
  }

  @Test
  void string_token_shouldMatch_with_commentChars() {
    String lineCommentString = "\"//string\"";
    String blockCommentString = "\"/*string*/\"";

    assertAll(
        () -> assertTrue(stringToken.pattern().matcher(lineCommentString).find()),
        () -> assertTrue(stringToken.pattern().matcher(blockCommentString).find()));
  }

  @Test
  void string_token_shouldNotMatch_whenNotClosed() {
    String testString = "\"string";
    assertFalse(stringToken.pattern().matcher(testString).find());
  }

  // ---------------- Char ----------------

  @Test
  void char_token_shouldMatch() {
    String testString = "'a'";
    assertTrue(charToken.pattern().matcher(testString).matches());
  }

  @Test
  void char_token_shouldNotMatch_with_multipleChars() {
    String testString = "'abc'";
    assertFalse(charToken.pattern().matcher(testString).find());
  }

  @Test
  void char_token_shouldNotMatch_withComment() {
    String testString = "'//'";
    assertFalse(charToken.pattern().matcher(testString).find());
  }

  @Test
  void char_token_shouldNotMatch_withEmptyChar() {
    String testString = "''";
    assertFalse(charToken.pattern().matcher(testString).find());
  }

  @Test
  void char_token_shouldMatch_escapedBackslash() {
    String testString = "'\\\\'";
    assertTrue(charToken.pattern().matcher(testString).matches());
  }

  @Test
  void char_token_shouldMatch_escapedChar() {
    String testString = "'\\n'";
    assertTrue(charToken.pattern().matcher(testString).matches());
  }
}
