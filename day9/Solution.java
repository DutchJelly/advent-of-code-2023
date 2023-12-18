import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;

public class Solution {

  private static enum Direction {
    FORWARDS, BACKWARDS
  }

  private static void part1(List<String> input) {

    int result = 0;

    for (int[] elementHistory : parseInput(input)) {
      result += findNext(elementHistory, Direction.FORWARDS);
    }

    System.out.println("part 1: %d".formatted(result));
  }

  private static void part2(List<String> input) {
    int result = 0;

    for (int[] elementHistory : parseInput(input)) {
      result += findNext(elementHistory, Direction.BACKWARDS);
    }

    System.out.println("part 2: %d".formatted(result));
  }

  private static int findNext(int[] row, Direction direction) {
    if (IntStream.of(row).allMatch(x -> x == 0)) {
      return 0;
    }

    int[] diff = new int[row.length - 1];
    for (int i = 0; i < row.length - 1; i++) {
      diff[i] = row[i + 1] - row[i];
    }

    if (direction == Direction.FORWARDS) {
      return row[row.length - 1] + findNext(diff, direction);
    }

    return row[0] - findNext(diff, direction);
  }

  private static int[][] parseInput(List<String> input) {
    int[][] history = new int[input.size()][];
    for (int i = 0; i < input.size(); i++) {
      history[i] = Arrays.stream(input.get(i).split(" ")).mapToInt(Integer::parseInt).toArray();
    }
    return history;
  }

  public static void main(String[] args) throws Exception {
    for (String arg : args) {
      switch (arg) {
        case "1":
          part1(Files.readAllLines(Path.of("input.txt")));
          break;
        case "2":
          part2(Files.readAllLines(Path.of("input.txt")));
          break;
        case "1t":
          part1(Files.readAllLines(Path.of("test-1.txt")));
          break;
        case "2t":
          part2(Files.readAllLines(Path.of("test-2.txt")));
          break;
        default:
          System.out.println("unknown argument");
          break;
      }
    }
  }
}