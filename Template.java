import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;

public class Template {
  public static void main(String[] args) throws IOException {
    List<String> input = Files.readAllLines(Path.of("template-input.txt"));
    System.out.println(input.stream().map(x -> {
      var fields = x.split("[ ,-]");
      return new Row(fields[0], Integer.parseInt(fields[1]));
    }).collect(Collectors.toList()));
  }
}

record Row(String label, int index) {

  @Override
  public String toString() {
    return String.format("[label: %s, index: %d]", label, index);
  }
}