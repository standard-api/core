package ai.stapi.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LineFormatter {

  private static final String TAB_INDENT = "\t";
  private static final String SPACE_INDENT = "    ";
  private static final String END_OF_LINE_SYMBOL = "\n";

  public static String createLine(String renderedLine) {
    return LineFormatter.createLine(renderedLine, 0);
  }

  public static String createLines(Stream<String> renderedLines) {
    return renderedLines.map(LineFormatter::createLine).collect(Collectors.joining());
  }

  public static String createLines(List<String> renderedLines) {
    return renderedLines.stream().map(LineFormatter::createLine).collect(Collectors.joining());
  }

  public static String createLines(String... renderedLines) {
    return Arrays.stream(renderedLines).map(LineFormatter::createLine)
        .collect(Collectors.joining());
  }

  public static String createLine(String renderedLine, int indentLevel) {
    return TAB_INDENT.repeat(indentLevel) + renderedLine + END_OF_LINE_SYMBOL;
  }

  public static String createSpaceIndentedLine(String renderedLine, int indentLevel) {
    return SPACE_INDENT.repeat(indentLevel) + renderedLine + END_OF_LINE_SYMBOL;
  }

  public static String createNewLine() {
    return END_OF_LINE_SYMBOL;
  }
}
