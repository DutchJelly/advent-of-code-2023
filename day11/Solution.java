package day11;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;

public class Solution {

  private static record Galaxy(long i, long j) {
  }

  private static List<Galaxy> parseGalaxies(List<String> input, int multiplier) {
    List<Galaxy> galaxies = new ArrayList<>();
    List<Integer> emptyRows = new ArrayList<>();
    List<Integer> emptyColumns = new ArrayList<>();
    for (int i = 0; i < input.size(); i++) {
      if (!input.get(i).contains("#")) {
        emptyRows.add(i);
      }
    }
    for (int j = 0; j < input.get(0).length(); j++) {
      boolean emptyColumn = true;
      for (int i = 0; i < input.size(); i++) {
        if (input.get(i).charAt(j) == '#') {
          emptyColumn = false;
          break;
        }
      }
      if (emptyColumn) {
        emptyColumns.add(j);
      }
    }
    for (int i = 0; i < input.size(); i++) {
      for (int j = 0; j < input.get(i).length(); j++) {
        if (input.get(i).charAt(j) == '#') {
          final int ii = i;
          final int jj = j;
          galaxies.add(new Galaxy(i + (emptyRows.stream().filter(x -> x < ii).count() * multiplier),
              j + (emptyColumns.stream().filter(x -> x < jj).count() * multiplier)));
        }
      }
    }
    return galaxies;
  }

  private static long getDistance(Galaxy a, Galaxy b) {
    return Math.abs(a.i - b.i) + Math.abs(a.j - b.j);
  }

  private static void part1(List<String> input) throws Exception {
    int result = 0;

    List<Galaxy> galaxies = parseGalaxies(input, 1);

    for (int i = 0; i < galaxies.size(); i++) {
      for (int j = i + 1; j < galaxies.size(); j++) {
        result += getDistance(galaxies.get(i), galaxies.get(j));
      }
    }

    System.out.println("part 1: %d".formatted(result));
  }

  private static void part2(List<String> input) {
    long result = 0;

    List<Galaxy> galaxies = parseGalaxies(input, 1_000_000 - 1);

    for (int i = 0; i < galaxies.size(); i++) {
      for (int j = i + 1; j < galaxies.size(); j++) {
        result += getDistance(galaxies.get(i), galaxies.get(j));
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
    }
  }
}