package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaTokens;
import java.util.*;

// TODO: Implement a scanning-based highlighting strategy that reads the input from left to right.
// At each position, select the longest token that matches at this position. If there is a tie, the
// token that appears earlier in the token list should be preferred.

// TODO: Make this class inherit from {@code SyntaxHighlighter} and implement the abstract method
// {@code collectMatches}. The scanning algorithm should ensure that the resulting list of regions
// is already sorted, non-overlapping and contains only valid regions, so that no additional
// normalisation or conflict resolution is required. Therefore, {@code resolveConflicts} can be left
// as is, and {@code normalize} should be overridden as the identity function.
public class ScanningHighlighter extends SyntaxHighlighter {

  // TODO: Implement the scanning-based matching strategy here. Iterate from left to right over the
  // input, determine the best matching token at each position, and collect all resulting highlight
  // regions in order.
  @Override
  public List<HighlightRegion> collectMatches(String text) {

    List<HighlightRegion> result = new ArrayList<>();
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    int i = 0;

    while (i < text.length()) {

      HighlightRegion best = null;
      Token bestToken = null;

      for (Token token : tokens) {

        var matcher = token.pattern().matcher(text);
        matcher.region(i, text.length());

        if (matcher.lookingAt()) {

          HighlightRegion r = new HighlightRegion(matcher.start(), matcher.end(), token.colour());

          if (best == null || (r.end() - r.start()) > (best.end() - best.start())) {

            best = r;
            bestToken = token;
          }
        }
      }

      if (best != null) {
        result.add(best);
        i = best.end();
      } else {
        i++;
      }
    }

    return result;
  }

  // TODO: Implement the identity function here.
  @Override
  public List<HighlightRegion> normalize(List<HighlightRegion> candidates) {
    return candidates;
  }
}
