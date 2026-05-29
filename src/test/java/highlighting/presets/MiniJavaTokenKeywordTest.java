package highlighting.presets;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.regex.Token;
import java.util.List;
import java.util.regex.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class MiniJavaTokenKeywordTest {

  static Token keywordToken;

  @BeforeAll
  static void init() {
    keywordToken =
        MiniJavaTokens.defaultTokens().stream()
            .filter(t -> t.colour() == MiniJavaColours.KEYWORD_COLOUR)
            .findFirst()
            .orElseThrow();
  }

  @Test
  void keyword_token_shouldMatch() {
    List<String> keywords =
        List.of(
            "package", "import", "class", "public", "private", "final", "return", "null", "new");

    assertAll(
        keywords.stream()
            .map(
                k ->
                    (Executable)
                        () -> {
                          boolean matches = keywordToken.pattern().matcher(k).matches();
                          assertTrue(matches, "Keyword failed:" + k);
                        })
            .toList());
  }

  @Test
  void keyword_token_shouldMatch_atStart() {
    String satz = "new but with text behind";
    assertTrue(keywordToken.pattern().matcher(satz).find());
  }

  @Test
  void keyword_token_shouldMatch_atMiddle() {
    String satz = "Word new between text";
    assertTrue(keywordToken.pattern().matcher(satz).find());
  }

  @Test
  void keyword_token_shouldMatch_atEnd() {
    String satz = "Word at end of text new";
    assertTrue(keywordToken.pattern().matcher(satz).find());
  }

  @Test
  void keyword_token_shouldMatch_mutlipleTimes() {
    String satz = "new new new import";

    Matcher matcher = keywordToken.pattern().matcher(satz);
    int count = 0;
    while (matcher.find()) {
      count++;
    }
    assertEquals(4, count);
  }

  @Test
  void keyword_token_shouldNotMatch_withSpaces() {
    String wrong = " new ";

    assertFalse(keywordToken.pattern().matcher(wrong).matches());
  }

  @Test
  void keyword_token_shouldNotMatch_wrongWord() {
    String wrong = "Hallo";

    assertFalse(keywordToken.pattern().matcher(wrong).matches());
  }

  @Test
  void keyword_token_shouldNotMatch_insideWord() {
    String wrong = "newspaper";
    assertFalse(keywordToken.pattern().matcher(wrong).find());
  }
}
