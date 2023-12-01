package day1;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;

public class Solution {

  private static void part1(List<String> input) throws Exception {
    int result = 0;

    for (var line : input) {
      char[] cs = line.toCharArray();
      for (int i = 0; i < cs.length; i++) {
        if (Character.isDigit(cs[i])) {
          result += 10 * Integer.parseInt(cs[i] + "");
          break;
        }
      }
      for (int i = cs.length - 1; i >= 0; i--) {
        if (Character.isDigit(cs[i])) {
          result += Integer.parseInt(cs[i] + "");
          break;
        }
      }
    }
    System.out.println("part 1: %d".formatted(result));
  }

  private static void part2(List<String> input) {
    int result = 0;

    for (var line : input) {
      line = line.replaceAll("one", "one1one");
      line = line.replaceAll("two", "two2two");
      line = line.replaceAll("three", "three3three");
      line = line.replaceAll("four", "four4four");
      line = line.replaceAll("five", "five5five");
      line = line.replaceAll("six", "six6six");
      line = line.replaceAll("seven", "seven7seven");
      line = line.replaceAll("eight", "eight8eight");
      line = line.replaceAll("nine", "nine9nine");

      char[] cs = line.toCharArray();
      for (int i = 0; i < cs.length; i++) {
        if (Character.isDigit(cs[i])) {
          result += 10 * Integer.parseInt(cs[i] + "");
          break;
        }
      }
      for (int i = cs.length - 1; i >= 0; i--) {
        if (Character.isDigit(cs[i])) {
          result += Integer.parseInt(cs[i] + "");
          break;
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
          part1(Files.readAllLines(Path.of("test-1.txt")));
          break;
        case "2t":
          part2(Files.readAllLines(Path.of("test-2.txt")));
          break;
        default:
          System.out.println("unknown argument");
          break;
      }
      if (arg.equals("1")) {

      }
    }
  }
}