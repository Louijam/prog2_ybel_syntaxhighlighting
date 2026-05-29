package highlighting.presets;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.regex.Token;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MiniJavaTokensCommentsTest {

  static Token lineCommentToken;
  static Token javaDocCommentToken;
  static Token blockCommentToken;

  @BeforeAll
  static void init() {
    lineCommentToken =
        MiniJavaTokens.defaultTokens().stream()
            .filter(t -> t.colour() == MiniJavaColours.LINE_COMMENT_COLOUR)
            .findFirst()
            .orElseThrow();

    javaDocCommentToken =
        MiniJavaTokens.defaultTokens().stream()
            .filter(t -> t.colour() == MiniJavaColours.JAVADOC_COMMENT_COLOUR)
            .findFirst()
            .orElseThrow();

    blockCommentToken =
        MiniJavaTokens.defaultTokens().stream()
            .filter(t -> t.colour() == MiniJavaColours.BLOCK_COMMENT_COLOUR)
            .findFirst()
            .orElseThrow();
  }

  // ---------------- LINE COMMENT ----------------

  @Test
  void line_comment_shouldMatch_simple() {
    String test = "// comment";
    assertTrue(lineCommentToken.pattern().matcher(test).matches());
  }

  @Test
  void line_comment_shouldMatch_inText() {
    String test = "code // comment here";
    assertTrue(lineCommentToken.pattern().matcher(test).find());
  }

  @Test
  void line_comment_shouldMatch_atStart() {
    String test = "// comment at start code";
    assertTrue(lineCommentToken.pattern().matcher(test).find());
  }

  @Test
  void line_comment_shouldNotMatch_plainText() {
    String test = "this is no comment";
    assertFalse(lineCommentToken.pattern().matcher(test).find());
  }

  // ---------------- BLOCK COMMENT ----------------

  @Test
  void block_comment_shouldMatch_simple() {
    String test = "/* comment */";
    assertTrue(blockCommentToken.pattern().matcher(test).matches());
  }

  @Test
  void block_comment_shouldMatch_inText() {
    String test = "code /* comment */ code";
    assertTrue(blockCommentToken.pattern().matcher(test).find());
  }

  @Test
  void block_comment_shouldNotMatch_unclosed() {
    String test = "/* comment";
    assertFalse(blockCommentToken.pattern().matcher(test).find());
  }

  @Test
  void block_comment_shouldMatch_nested_like_content() {
    String test = "/* // not a line comment inside */";
    assertTrue(blockCommentToken.pattern().matcher(test).matches());
  }

  // ---------------- JAVADOC COMMENT ----------------

  @Test
  void javadoc_comment_shouldMatch_simple() {
    String test = "/** javadoc */";
    assertTrue(javaDocCommentToken.pattern().matcher(test).matches());
  }

  @Test
  void javadoc_comment_shouldMatch_inText() {
    String test = "code /** javadoc */ code";
    assertTrue(javaDocCommentToken.pattern().matcher(test).find());
  }

  @Test
  void javadoc_comment_shouldMatch_withAsteriskContent() {
    String test = "/** * important * note */";
    assertTrue(javaDocCommentToken.pattern().matcher(test).matches());
  }

  @Test
  void javadoc_comment_shouldNotMatch_plainBlockComment() {
    String test = "/* just block comment */";
    assertFalse(javaDocCommentToken.pattern().matcher(test).find());
  }
}
