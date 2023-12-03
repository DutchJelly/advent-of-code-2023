package day3;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.*;
import java.nio.file.*;

public class Solution {

  private static boolean isAdjacent(char[][] matrix, final int i, final int j, Predicate<Character> condition) {
    for (int ii = Math.max(0, i - 1); ii <= i + 1 && ii < matrix.length; ii++) {
      for (int jj = Math.max(0, j - 1); jj <= j + 1 && jj < matrix[ii].length; jj++) {
        if (ii == i && jj == j)
          continue;

        if (condition.test(matrix[ii][jj])) {
          return true;
        }
      }
    }
    return false;
  }

  private static void part1(List<String> input) throws Exception {
    int result = 0;
    char[][] matrix = input.stream().map(x -> x.toCharArray()).toArray(char[][]::new);
    for (int i = 0; i < matrix.length; i++) {
      boolean foundAdjacent = false;
      int currentNumber = 0;
      for (int j = 0; j < matrix[i].length; j++) {
        if (Character.isDigit(matrix[i][j])) {
          currentNumber = currentNumber * 10 + Integer.parseInt(matrix[i][j] + "");
        } else {
          if (currentNumber != 0 && foundAdjacent) {
            result += currentNumber;
          }
          currentNumber = 0;
          foundAdjacent = false;
        }
        if (currentNumber != 0 && isAdjacent(matrix, i, j, (c) -> c != '.' && !Character.isDigit(c))) {
          foundAdjacent = true;
        }
      }
      if (currentNumber != 0 && foundAdjacent) {
        result += currentNumber;
      }
    }

    System.out.println("part 1: %d".formatted(result));
  }

  private static int expandDigit(char[][] matrix, int i, int j) {
    int digit = 0;
    while (j >= 0 && Character.isDigit(matrix[i][j])) {
      j--;
    }
    j++;
    while (j < matrix[i].length && Character.isDigit(matrix[i][j])) {
      digit = 10 * digit + (matrix[i][j] - '0');
      j++;
    }
    return digit;
  }

  private static int getGearRatio(char[][] matrix, int i, int j) {
    List<Integer> numbers = new ArrayList<>();
    // bottom
    if (i - 1 >= 0) {
      if (Character.isDigit(matrix[i - 1][j])) {
        numbers.add(expandDigit(matrix, i - 1, j));
      } else {
        if (j - 1 >= 0 && Character.isDigit(matrix[i - 1][j - 1])) {
          numbers.add(expandDigit(matrix, i - 1, j - 1));
        }
        if (j + 1 < matrix[i].length && Character.isDigit(matrix[i - 1][j + 1])) {
          numbers.add(expandDigit(matrix, i - 1, j + 1));
        }
      }
    }

    // top
    if (i + 1 < matrix.length) {
      if (Character.isDigit(matrix[i + 1][j])) {
        numbers.add(expandDigit(matrix, i + 1, j));
      } else {
        if (j - 1 >= 0 && Character.isDigit(matrix[i + 1][j - 1])) {
          numbers.add(expandDigit(matrix, i + 1, j - 1));
        }
        if (j + 1 < matrix[i].length && Character.isDigit(matrix[i + 1][j + 1])) {
          numbers.add(expandDigit(matrix, i + 1, j + 1));
        }
      }
    }

    // left
    if (j - 1 >= 0 && Character.isDigit(matrix[i][j - 1])) {
      numbers.add(expandDigit(matrix, i, j - 1));
    }

    // right
    if (j + 1 < matrix[i].length && Character.isDigit(matrix[i][j + 1])) {
      numbers.add(expandDigit(matrix, i, j + 1));
    }

    if (numbers.size() != 2)
      return 0;

    return numbers.get(0) * numbers.get(1);
  }

  private static void part2(List<String> input) {
    int result = 0;

    char[][] matrix = input.stream().map(x -> x.toCharArray()).toArray(char[][]::new);

    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        if (matrix[i][j] == '*') {
          result += getGearRatio(matrix, i, j);
        }
      }
    }

    System.out.println("part 2: %d".formatted(result));
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
          part1(Files.readAllLines(Path.of("test.txt")));
          break;
        case "2t":
          part2(Files.readAllLines(Path.of("test.txt")));
          break;
        default:
          System.out.println("unknown argument");
          break;
      }
    }
  }
}