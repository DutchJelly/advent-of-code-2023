package day5;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;

public class Solution {

  private static void part1(List<String> input) throws Exception {
    long result = 0;

    long[] seeds = Arrays.stream(input.get(0).replace("seeds: ", "").split(" "))
        .mapToLong(Long::parseLong).toArray();

    boolean[] changed = new boolean[seeds.length];

    for (String line : input) {
      if (line.contains("seeds") || line.isBlank())
        continue;
      if (line.contains("map")) {
        Arrays.fill(changed, false);
        continue;
      }

      String[] parts = line.split(" ");
      long destStart = Long.parseLong(parts[0]);
      long sourceStart = Long.parseLong(parts[1]);
      long rangeLength = Long.parseLong(parts[2]);

      for (int i = 0; i < seeds.length; i++) {
        if (changed[i])
          continue;
        if (seeds[i] >= sourceStart && seeds[i] < sourceStart + rangeLength) {
          seeds[i] = destStart + (seeds[i] - sourceStart);
          changed[i] = true;
        }
      }
    }
    result = seeds[0];
    for (int i = 1; i < seeds.length; i++) {
      if (seeds[i] < result) {
        result = seeds[i];
      }
    }
    System.out.println("part 1: %d".formatted(result));
  }

  private static void part2(List<String> input) {

    long result = 0;

    String[] seedStrings = input.get(0).replace("seeds: ", "").split(" ");

    List<Range> ranges = new ArrayList<>();

    for (int i = 0; i < seedStrings.length; i += 2) {
      ranges.add(new Range(Long.parseLong(seedStrings[i]), Long.parseLong(seedStrings[i + 1])));
    }

    for (String line : input) {
      if (line.contains("seeds") || line.isBlank())
        continue;

      // new mapping, so reset changed flags
      if (line.contains("map")) {
        for (var r : ranges) {
          r.changed = false;
        }
        continue;
      }

      String[] parts = line.split(" ");
      Range dest = new Range(Long.parseLong(parts[0]), Long.parseLong(parts[2]));
      Range source = new Range(Long.parseLong(parts[1]), Long.parseLong(parts[2]));

      for (int i = 0; i < ranges.size(); i++) {
        Range range = ranges.get(i);

        // range is already mapped
        if (range.changed)
          continue;

        // range is left of other range
        if (range.rangeEnd() <= source.from)
          continue;
        // range is right of other range
        if (range.from >= source.rangeEnd())
          continue;

        // range is inside other range
        if (range.from >= source.from && range.rangeEnd() <= source.rangeEnd()) {
          range.from += dest.from - source.from;
          range.changed = true;
        }

        // range is outside other range
        else if (range.from < source.from && range.rangeEnd() > source.rangeEnd()) {
          ranges.add(new Range(range.from, source.from - range.from));
          ranges.add(new Range(source.rangeEnd(), range.rangeEnd() - source.rangeEnd()));
          range.from = dest.from;
          range.length = dest.length;
          range.changed = true;
        }

        // range overlaps range on left
        else if (range.from < source.from && range.rangeEnd() <= source.rangeEnd() && range.rangeEnd() > source.from) {
          long outsideLength = source.from - range.from;
          ranges.add(new Range(range.from, outsideLength));
          range.length = range.length - outsideLength;
          range.from = dest.from;
          range.changed = true;
        }

        // range overlaps range on right
        else if (range.rangeEnd() > source.rangeEnd() && range.from >= source.from && range.from < source.rangeEnd()) {
          long outsideLength = range.rangeEnd() - source.rangeEnd();
          ranges.add(new Range(source.rangeEnd(), outsideLength));
          range.length = range.length - outsideLength;
          range.from = dest.from + (range.from - source.from);
          range.changed = true;
        }
      }

    }

    result = ranges.get(0).from;
    for (int i = 1; i < ranges.size(); i++) {
      if (ranges.get(i).from < result) {
        result = ranges.get(i).from;
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

class Range {
  public long from;
  public long length;
  boolean changed = false;

  public Range(long from, long length) {
    this.from = from;
    this.length = length;
  }

  public long rangeEnd() {
    return from + length;
  }

  @Override
  public String toString() {
    return String.format("from: %d, length: %d, to: %d", from, length, from + length);
  }
}