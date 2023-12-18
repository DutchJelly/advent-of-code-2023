package day4;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;

public class Solution {

  private static void part1(List<String> input) throws Exception {
    int result = -1;

    int[] timeList = Arrays.stream(input.get(0).split("[ ]+")).skip(1).mapToInt(Integer::parseInt).toArray();
    int[] distList = Arrays.stream(input.get(1).split("[ ]+")).skip(1).mapToInt(Integer::parseInt).toArray();

    for (int i = 0; i < timeList.length; i++) {
      int roundResult = 0;
      for (int time = 0; time <= timeList[i]; time++) {
        int dist = (timeList[i] - time) * time;
        if (dist > distList[i])
          roundResult++;
      }
      if (result == -1)
        result = roundResult;
      else
        result *= roundResult;
    }

    System.out.println("part 1: %d".formatted(result));
  }

  private static void part2(List<String> input) {
    long result = 0;

    long times = Long.parseLong(Arrays.stream(input.get(0).split("[ ]+")).skip(1).collect(Collectors.joining("")));
    long dists = Long.parseLong(Arrays.stream(input.get(1).split("[ ]+")).skip(1).collect(Collectors.joining("")));

    for (long time = 0; time <= times; time++) {
      long dist = (times - time) * time;
      if (dist >= dists)
        result++;
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