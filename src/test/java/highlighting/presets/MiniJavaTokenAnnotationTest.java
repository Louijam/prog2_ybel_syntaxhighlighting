package highlighting.presets;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.regex.Token;
import java.util.regex.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MiniJavaTokenAnnotationTest {

  static Token annotationToken;

  @BeforeAll
  static void init() {
    annotationToken =
        MiniJavaTokens.defaultTokens().stream()
            .filter(t -> t.colour() == MiniJavaColours.ANNOTATION_COLOUR)
            .findFirst()
            .orElseThrow();
  }

  @Test
  void annotation_shouldMatch_simpleAnnotation() {
    String testString = "@Test";
    assertTrue(annotationToken.pattern().matcher(testString).matches());
  }

  @Test
  void annotation_shouldMatch_withMinus() {
    String testString = "@-Test";
    assertTrue(annotationToken.pattern().matcher(testString).matches());
  }

  @Test
  void annotation_shouldMatch_override() {
    String testString = "@Override";
    assertTrue(annotationToken.pattern().matcher(testString).matches());
  }

  @Test
  void annotation_shouldNotMatch_withoutAtSymbol() {
    String testString = "Test";
    assertFalse(annotationToken.pattern().matcher(testString).matches());
  }

  @Test
  void annotation_shouldMatch_inText_start() {
    String testString = "@Test class A";
    Matcher matcher = annotationToken.pattern().matcher(testString);
    assertTrue(matcher.find());
  }

  @Test
  void annotation_shouldMatch_inText_middle() {
    String testString = "public @Test void method()";
    Matcher matcher = annotationToken.pattern().matcher(testString);
    assertTrue(matcher.find());
  }

  @Test
  void annotation_shouldMatch_inText_end() {
    String testString = "method() @Test";
    Matcher matcher = annotationToken.pattern().matcher(testString);
    assertTrue(matcher.find());
  }

  @Test
  void annotation_shouldMatch_multipleTimes() {
    String testString = "@Test @Override @Deprecated";
    Matcher matcher = annotationToken.pattern().matcher(testString);

    int count = 0;
    while (matcher.find()) {
      count++;
    }

    assertEquals(3, count);
  }

  @Test
  void annotation_shouldNotMatch_invalidCharacters() {
    String testString = "@123";
    assertFalse(annotationToken.pattern().matcher(testString).matches());
  }

  @Test
  void annotation_shouldNotMatch_partialAtSymbol() {
    String testString = "@";
    assertFalse(annotationToken.pattern().matcher(testString).matches());
  }
}
