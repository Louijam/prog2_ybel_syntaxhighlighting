package highlighting.regex;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.core.HighlightRegion;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ScanningHighlighterTest {

  static ScanningHighlighter highlighter;

  @BeforeAll
  static void init() {
    highlighter = new ScanningHighlighter();
  }

  // ---------------- 1. longest match wins ----------------

  @Test
  void longest_match_shouldWin() {
    // typische Idee: "==" vs "=" oder ähnliches
    // oder block vs line comment prefix etc.
    String text = "/* comment */";

    List<HighlightRegion> result = highlighter.computeRegions(text);

    assertFalse(result.isEmpty());

    // sollte exakt eine zusammenhängende Kommentarregion sein
    HighlightRegion r = result.getFirst();
    assertEquals(0, r.start());
    assertEquals(text.length(), r.end());
  }

  // ---------------- 2. tie breaker: token order ----------------

  @Test
  void tie_shouldPreferEarlierToken() {
    // Idee: zwei Token könnten an gleicher Position matchen
    // z.B. keyword vs identifier-artiger fallback
    String text = "class";

    List<HighlightRegion> result = highlighter.computeRegions(text);

    assertFalse(result.isEmpty());

    // wir prüfen nur: genau ein Match und es ist stabil
    assertEquals(1, result.size());
    assertEquals(0, result.getFirst().start());
    assertEquals(5, result.getFirst().end());
  }

  // ---------------- 3. scanner must not block ----------------

  @Test
  void scanner_shouldSkipNonMatchingParts() {
    String text = "abc \"string\" def";

    List<HighlightRegion> result = highlighter.computeRegions(text);

    assertFalse(result.isEmpty());

    // es darf mehrere Regionen geben
    // und der Scan darf nicht "hängen bleiben"
    for (int i = 1; i < result.size(); i++) {
      assertTrue(result.get(i).start() >= result.get(i - 1).end());
    }
  }

  // ---------------- 4. mixed text ----------------

  @Test
  void shouldHandle_mixedMatchesAndNonMatches() {
    String text = "abc // comment\n\"string\" xyz";

    List<HighlightRegion> result = highlighter.computeRegions(text);

    assertFalse(result.isEmpty());

    // keine Überlappung
    for (int i = 0; i < result.size(); i++) {
      for (int j = i + 1; j < result.size(); j++) {
        assertFalse(overlap(result.get(i), result.get(j)));
      }
    }

    // scan muss monoton voranschreiten
    for (int i = 1; i < result.size(); i++) {
      assertTrue(result.get(i).start() >= result.get(i - 1).end());
    }
  }

  // ---------------- helper ----------------

  private boolean overlap(HighlightRegion a, HighlightRegion b) {
    return a.start() < b.end() && b.start() < a.end();
  }
}
