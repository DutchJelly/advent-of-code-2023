import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;

public class Solution {

  static class Hand {
    String hand;
    int type;
    int bidding;

    public Hand(String hand, int bidding, int type) {
      this.hand = hand;
      this.bidding = bidding;
      this.type = type;
    }

    @Override
    public String toString() {
      return String.format("hand: %s, bidding: %d, type: %d", hand, bidding, type);
    }
  }

  private static int getType(String handStr) {
    char[] hand = handStr.toCharArray();

    Map<Character, Integer> counts = new HashMap<>();

    for (char c : hand) {
      counts.merge(c, 1, (a, b) -> a + b);
    }

    // Five of a kind
    if (counts.size() == 1) {
      return 7;
    }

    // Four of a kind
    if (counts.containsValue(4)) {
      return 6;
    }

    // Full house
    if (counts.containsValue(3) && counts.containsValue(2)) {
      return 5;
    }

    // Three of a kind
    if (counts.containsValue(3)) {
      return 4;
    }

    // Two pair
    if (counts.containsValue(2) && counts.containsValue(1) && counts.size() == 3) {
      return 3;
    }

    // One pair
    if (counts.containsValue(2) && counts.containsValue(1)) {
      return 2;
    }

    // High card
    if (counts.containsValue(1)) {
      return 1;
    }

    throw new RuntimeException("No card type found");

  }

  private static int calculateScore(List<Hand> hands, final String cards) {
    Comparator<Hand> byType = (a, b) -> b.type - a.type;
    Comparator<Hand> byCards = (a, b) -> {

      for (int i = 0; i < a.hand.length(); i++) {
        char aa = a.hand.charAt(i);
        char bb = b.hand.charAt(i);
        if (cards.indexOf(aa) < cards.indexOf(bb)) {
          return -1;
        }
        if (cards.indexOf(aa) > cards.indexOf(bb)) {
          return 1;
        }
      }
      return 0;
    };

    Comparator<Hand> combined = byType.thenComparing(byCards);

    hands.sort(combined);
    int result = 0;
    for (int i = 0; i < hands.size(); i++) {
      result += hands.get(i).bidding * (hands.size() - i);
    }
    return result;
  }

  private static void part1(List<String> input) throws Exception {
    List<Hand> hands = new ArrayList<>();
    input.forEach(x -> {
      String[] parts = x.split(" ");
      hands.add(new Hand(parts[0], Integer.parseInt(parts[1]), getType(parts[0])));
    });

    final String cards = "AKQJT98765432";

    System.out.println("part 1: %d".formatted(calculateScore(hands, cards)));
  }

  private static void part2(List<String> input) {
    final String cards = "AKQT98765432J";

    List<Hand> hands = new ArrayList<>();
    input.forEach(x -> {
      String[] parts = x.split(" ");
      if (parts[0].contains("J")) {
        int maxType = 0;
        for (int i = 0; i < cards.length() - 1; i++) {
          int type = getType(parts[0].replaceAll("J", cards.charAt(i) + ""));
          if (type > maxType)
            maxType = type;
        }
        hands.add(new Hand(parts[0], Integer.parseInt(parts[1]), maxType));

      } else {
        hands.add(new Hand(parts[0], Integer.parseInt(parts[1]), getType(parts[0])));
      }

    });
    System.out.println("part 2: %d".formatted(calculateScore(hands, cards)));
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