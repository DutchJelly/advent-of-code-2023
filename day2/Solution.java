package day2;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;

public class Solution {

  private static void part1(List<String> input) throws Exception {
    int result = 0;

    int red = 12, green = 13, blue = 14;

    GAME: for (String line : input) {
      int gameId = Integer.parseInt(line.split("[: ]")[1]);

      String[] sets = line.split(":")[1].split(";");

      for (String set : sets) {
        set = set.trim();

        for (String draw : set.split(",")) {
          draw = draw.trim();
          int colorCount = Integer.parseInt(draw.split(" ")[0]);
          if (draw.contains("blue")) {
            if (colorCount > blue) {
              continue GAME;
            }
          } else if (draw.contains("red")) {
            if (colorCount > red) {
              continue GAME;
            }
          } else if (draw.contains("green")) {
            if (colorCount > green) {
              continue GAME;
            }
          }
        }
      }
      result += gameId;
    }

    System.out.println("part 1: %d".formatted(result));
  }

  private static void part2(List<String> input) {
    int result = 0;

    for (String line : input) {
      int red = Integer.MIN_VALUE, green = Integer.MIN_VALUE, blue = Integer.MIN_VALUE;
      String[] sets = line.split(":")[1].split(";");

      for (String set : sets) {
        set = set.trim();

        for (String draw : set.split(",")) {
          draw = draw.trim();
          int colorCount = Integer.parseInt(draw.split(" ")[0]);
          if (draw.contains("blue")) {
            if (colorCount > blue) {
              blue = colorCount;
            }
          } else if (draw.contains("red")) {
            if (colorCount > red) {
              red = colorCount;
            }
          } else if (draw.contains("green")) {
            if (colorCount > green) {
              green = colorCount;
            }
          }
        }
      }
      result += red * green * blue;
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