import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;

public class Solution {

  static record Point(int i, int j) {
    boolean isValidOn(Element[][] grid) {
      return i >= 0 && i < grid.length && j >= 0 && j < grid[0].length;
    }

    Element get(Element[][] grid) {
      return grid[i][j];
    }
  }

  static enum Direction {
    NORTH, SOUTH, EAST, WEST
  }

  private static Direction getComp(Direction c) {
    return switch (c) {
      case NORTH -> Direction.SOUTH;
      case SOUTH -> Direction.NORTH;
      case WEST -> Direction.EAST;
      case EAST -> Direction.WEST;
    };
  }

  static class Element {
    int value = -1;

    Direction[] connections;

    public boolean hasConnection(Direction dir) {
      for (Direction d : connections) {
        if (d == getComp(dir))
          return true;
      }
      return false;
    }

    public String toString() {
      return "" + value;
    }

    public Element(Direction... connections) {
      this.connections = connections;
    }

    Element(int initialValue, Direction... connections) {
      this.value = initialValue;
      this.connections = connections;
    }
  }

  private static void part1(List<String> input) throws Exception {
    Element[][] grid = parseInput(input);

    Point start = findStart(grid);

    if (start == null)
      throw new RuntimeException("No starting point found");

    flow(Arrays.asList(start), grid);

    int max = -1;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (grid[i][j].value > max) {
          max = grid[i][j].value;
        }
      }
    }

    System.out.println("part 1: %d".formatted(max));
  }

  private static void part2(List<String> input) {
    int result = 0;

    Element[][] grid = parseInput(input);

    Point start = findStart(grid);

    if (start == null)
      throw new RuntimeException("No starting point found");

    flow(Arrays.asList(start), grid);

    Element[][] largeGrid = getSpacedGrid(grid);

    List<Point> outers = new ArrayList<>();
    for (int i = 0; i < largeGrid.length; i++) {
      outers.add(new Point(i, -1));
      outers.add(new Point(i, largeGrid[i].length));
    }
    for (int j = 0; j < largeGrid[0].length; j++) {
      outers.add(new Point(-1, j));
      outers.add(new Point(largeGrid.length, j));
    }

    flowOuters(outers, largeGrid);

    for (int i = 0; i < largeGrid.length; i += 2) {
      for (int j = 0; j < largeGrid[i].length; j += 2) {
        if (largeGrid[i][j].value == -1) {
          result++;
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
    }
  }

  private static void flow(List<Point> fromList, Element[][] grid) {
    // find connecting nodes that are not visited yet

    while (!fromList.isEmpty()) {
      List<Point> connections = new ArrayList<>();
      for (int fromIndex = 0; fromIndex < fromList.size(); fromIndex++) {
        Point from = fromList.get(fromIndex);
        for (Direction to : grid[from.i][from.j].connections) {
          Point p = switch (to) {
            case NORTH -> new Point(from.i - 1, from.j);
            case EAST -> new Point(from.i, from.j + 1);
            case SOUTH -> new Point(from.i + 1, from.j);
            case WEST -> new Point(from.i, from.j - 1);
            default -> new Point(-1, -1);
          };

          if (!p.isValidOn(grid)) {
            continue;
          }
          Element pElement = p.get(grid);
          if (!pElement.hasConnection(to)) {
            continue;
          }

          if (pElement.value == -1) {
            pElement.value = from.get(grid).value + 1;
            connections.add(p);
          }
        }
      }
      fromList = connections;
    }
  }

  private static void flowOuters(List<Point> fromList, Element[][] largeGrid) {
    while (!fromList.isEmpty()) {
      List<Point> connections = new ArrayList<>();
      for (int fromIndex = 0; fromIndex < fromList.size(); fromIndex++) {
        Point from = fromList.get(fromIndex);
        for (Direction to : Arrays.asList(Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH)) {
          Point p = switch (to) {
            case NORTH -> new Point(from.i - 1, from.j);
            case EAST -> new Point(from.i, from.j + 1);
            case SOUTH -> new Point(from.i + 1, from.j);
            case WEST -> new Point(from.i, from.j - 1);
            default -> new Point(-1, -1);
          };

          if (!p.isValidOn(largeGrid)) {
            continue;
          }
          Element pElement = p.get(largeGrid);
          if (pElement.value == -1) {
            pElement.value = -2;
            connections.add(p);
          }
        }
      }
      fromList = connections;
    }
  }

  private static Element[][] parseInput(List<String> input) {
    Element[][] grid = new Element[input.size()][];
    for (int i = 0; i < input.size(); i++) {
      char[] elementsChars = input.get(i).toCharArray();
      grid[i] = new Element[elementsChars.length];
      for (int j = 0; j < elementsChars.length; j++) {
        grid[i][j] = switch (elementsChars[j]) {
          case 'S' -> new Element(0, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
          case '|' -> new Element(Direction.NORTH, Direction.SOUTH);
          case '-' -> new Element(Direction.EAST, Direction.WEST);
          case 'L' -> new Element(Direction.NORTH, Direction.EAST);
          case 'J' -> new Element(Direction.NORTH, Direction.WEST);
          case '7' -> new Element(Direction.SOUTH, Direction.WEST);
          case 'F' -> new Element(Direction.SOUTH, Direction.EAST);
          default -> new Element();
        };
      }
    }
    return grid;
  }

  private static Point findStart(Element[][] grid) {
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (grid[i][j].value == 0) {
          return new Point(i, j);
        }
      }
    }
    return null;
  }

  private static Element[][] getSpacedGrid(Element[][] grid) {
    Element[][] largeGrid = new Element[grid.length * 2 - 1][grid[0].length * 2 - 1];
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        largeGrid[i * 2][j * 2] = grid[i][j];
        // 0 -> 0, 1 -> 2, 2 -> 4, 3 -> 6
      }
    }
    for (int i = 0; i < largeGrid.length; i++) {
      for (int j = 0; j < largeGrid[i].length; j++) {
        // look for connections between top and bottom
        if (i % 2 == 1 && j % 2 == 0) {
          if (largeGrid[i - 1][j].hasConnection(Direction.NORTH)
              && largeGrid[i + 1][j].hasConnection(Direction.SOUTH)) {
            largeGrid[i][j] = new Element(Direction.NORTH, Direction.SOUTH);
            largeGrid[i][j].value = 0;
          }
        }
        // look for connections betweenleft and right
        else if (i % 2 == 0 && j % 2 == 1) {
          if (largeGrid[i][j - 1].hasConnection(Direction.WEST) && largeGrid[i][j + 1].hasConnection(Direction.EAST)) {
            largeGrid[i][j] = new Element(Direction.EAST, Direction.WEST);
            largeGrid[i][j].value = 0;
          }
        }
        if (largeGrid[i][j] == null) {
          largeGrid[i][j] = new Element(-1);
        }
      }
    }
    return largeGrid;
  }
}