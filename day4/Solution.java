package day4;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;

public class Solution {

  private static void part1(List<String> input) throws Exception {
    int result = 0;

    for (String line : input) {
      String[] parts = line.split("[:|]");
      int[] winningNumbers = Arrays.stream(parts[1].trim().split("[ ]+")).mapToInt(x -> Integer.parseInt(x)).toArray();
      int[] numbers = Arrays.stream(parts[2].trim().split("[ ]+")).mapToInt(x -> Integer.parseInt(x)).toArray();
      int cardValue = 0;
      for (int num : numbers) {
        for (int winning : winningNumbers) {
          if (num == winning) {
            if (cardValue == 0) {
              cardValue = 1;
            } else {
              cardValue <<= 1;
            }
            break;
          }
        }
      }
      result += cardValue;
    }

    System.out.println("part 1: %d".formatted(result));
  }

  private static int processWinning(int card, int[][] numbers, int[][] winningNumbers) {
    int count = 0;
    for (int num : numbers[card - 1]) {
      for (int winning : winningNumbers[card - 1]) {
        if (num == winning) {
          count++;
          break;
        }
      }
    }
    int winningCount = 1;
    for (int i = 0; i < count; i++) {
      winningCount += processWinning(card + i + 1, numbers, winningNumbers);
    }
    return winningCount;
  }

  private static void part2(List<String> input) {
    int result = 0;

    int[][] winningNumberLines = new int[input.size()][];
    int[][] numberLines = new int[input.size()][];

    for (int i = 0; i < input.size(); i++) {
      String line = input.get(i);
      String[] parts = line.split("[:|]");
      winningNumberLines[i] = Arrays.stream(parts[1].trim().split("[ ]+")).mapToInt(x -> Integer.parseInt(x)).toArray();
      numberLines[i] = Arrays.stream(parts[2].trim().split("[ ]+")).mapToInt(x -> Integer.parseInt(x)).toArray();
    }

    for (int i = 0; i < input.size(); i++) {
      result += processWinning(i + 1, numberLines, winningNumberLines);
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