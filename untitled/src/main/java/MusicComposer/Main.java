package MusicComposer;

import java.util.stream.IntStream;

public class Main {
  public static void main(String[] args) {
    IntStream.range(0, 30).mapToObj(i -> new Thread(new CompositionProcess(1)))
        .forEach(Thread::start);
  }
}