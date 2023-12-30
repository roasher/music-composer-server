package MusicComposer;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class CompositionProcess implements Runnable {

  private final String compositionId = "composition-" + new Random().nextInt(100000);
  private final HttpClient client = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_1_1)
      .followRedirects(HttpClient.Redirect.NORMAL)
      .connectTimeout(Duration.ofSeconds(20))
      .build();
  private final int numberOfBars;

  public CompositionProcess(int numberOfBars) {
    this.numberOfBars = numberOfBars;
  }

  @Override
  public void run() {
    for (int i = 0; i < 1000; i++) {
      try {
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8888/getBars")
            .queryParam("compositionId", compositionId)
            .queryParam("numberOfBars", numberOfBars)
            .build()
            .toUri();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();
        Instant before = Instant.now();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Instant after = Instant.now();
        Duration duration = Duration.between(before, after);
        log.info("Responded with status code:{}. It took: {} millis", response.statusCode(), duration.toMillis());
        // 60 bpm -> 1 beat per second -> 4 beats = 1 bar per 4 seconds
        long waitForPlayTime = numberOfBars * 4000 - duration.toMillis();
        if (waitForPlayTime < 0) {
          log.warn("Composing lag. Missing {} milliseconds time", waitForPlayTime);
        } else {
          Thread.sleep(waitForPlayTime);
        }
      } catch (Exception e) {
        log.error("Exception during request", e);
        throw new RuntimeException(e);
      }
    }
  }

}
