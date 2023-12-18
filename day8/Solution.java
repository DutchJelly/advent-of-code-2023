import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.util.function.*;

public class Solution {

  private static long lcm(long x, long y) {
    long max = Math.max(x, y);
    long min = Math.min(x, y);
    long result = max;
    while (result % min != 0) {
      result += max;
    }
    return result;
  }

  private static void part1(List<String> input) {
    Network network = Network.parse(input);
    System.out.println("part 1: %d".formatted(network.getSteps("AAA", (x) -> x.equals("ZZZ"))));
  }

  private static void part2(List<String> input) {
    Network network = Network.parse(input);

    List<String> startingList = network.origins().stream().filter(x -> x.endsWith("A"))
        .collect(Collectors.toList());

    List<Long> distances = startingList.stream().parallel()
        .map(x -> network.getSteps(x, (y -> y.endsWith("Z"))))
        .collect(Collectors.toList());

    long result = distances.stream().reduce(Solution::lcm)
        .orElseThrow(() -> new RuntimeException("Could not find distance"));

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

class Network {
  private Network() {
  }

  private Map<String, String[]> network;
  private String instructions;

  public static Network parse(List<String> input) {
    Network network = new Network();
    network.network = getNetwork(input);
    network.instructions = input.get(0);
    return network;
  }

  private static Map<String, String[]> getNetwork(List<String> input) {
    Map<String, String[]> network = new HashMap<>();

    input.stream().skip(2).forEach(x -> {
      String[] parts = x.split(" = ");
      String[] to = parts[1].split(", ");
      String[] parsedTo = new String[] { to[0].substring(1), to[1].substring(0, to[1].length() - 1) };
      network.put(parts[0], parsedTo);
    });

    return network;
  }

  public Set<String> origins() {
    return network.keySet();
  }

  public long getSteps(String from, Predicate<String> to) {
    long result = 0;
    String current = from;
    while (!to.test(current)) {
      current = switch (instructions.charAt((int) (result % instructions.length()))) {
        case 'R' -> network.get(current)[1];
        default -> network.get(current)[0];
      };
      result++;
    }
    return result;
  }
}