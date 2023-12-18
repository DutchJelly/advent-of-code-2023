package day4;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;

public class Solution {

  private static void part1(List<String> input) throws Exception {
    int result = 0;

    System.out.println("part 1: %d".formatted(result));
  }

  private static void part2(List<String> input) {
    int result = 0;

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