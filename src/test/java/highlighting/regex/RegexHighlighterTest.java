package highlighting.regex;

import highlighting.core.HighlightRegion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RegexHighlighterTest {

    static RegexHighlighter highlighter;

    @BeforeAll
    static void init() {
        highlighter = new RegexHighlighter();
    }

    // ---------------- basic ----------------

    @Test
    void shouldReturnEmpty_forEmptyString() {
        List<HighlightRegion> result = highlighter.computeRegions("");
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmpty_forNoMatches() {
        List<HighlightRegion> result = highlighter.computeRegions("abcdef");
        assertTrue(result.isEmpty());
    }

    // ---------------- simple matches ----------------

    @Test
    void shouldDetect_stringLiteral() {
        List<HighlightRegion> result = highlighter.computeRegions("\"test\"");
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldDetect_charLiteral() {
        List<HighlightRegion> result = highlighter.computeRegions("'a'");
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldDetect_lineComment() {
        List<HighlightRegion> result = highlighter.computeRegions("// comment");
        assertFalse(result.isEmpty());
    }

    // ---------------- adjacency ----------------

    @Test
    void shouldAllow_adjacentRegions() {
        List<HighlightRegion> result = highlighter.computeRegions("\"a\"\"b\"");

        assertTrue(result.size() >= 2);

        for (int i = 0; i < result.size(); i++) {
            for (int j = i + 1; j < result.size(); j++) {
                assertFalse(overlap(result.get(i), result.get(j)));
            }
        }
    }

    // ---------------- overlap cases ----------------

    @Test
    void shouldResolve_overlap_comment_vs_keywordLikeText() {
        List<HighlightRegion> result = highlighter.computeRegions("/* return */");

        assertFalse(result.isEmpty());

        for (int i = 0; i < result.size(); i++) {
            for (int j = i + 1; j < result.size(); j++) {
                assertFalse(overlap(result.get(i), result.get(j)));
            }
        }
    }

    @Test
    void shouldResolve_javadoc_vs_blockComment() {
        List<HighlightRegion> result = highlighter.computeRegions("/** comment */");

        assertFalse(result.isEmpty());

        for (int i = 0; i < result.size(); i++) {
            for (int j = i + 1; j < result.size(); j++) {
                assertFalse(overlap(result.get(i), result.get(j)));
            }
        }
    }

    // ---------------- helper ----------------

    private boolean overlap(HighlightRegion a, HighlightRegion b) {
        return a.start() < b.end() && b.start() < a.end();
    }
}
