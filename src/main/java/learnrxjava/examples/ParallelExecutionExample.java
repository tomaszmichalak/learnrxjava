package learnrxjava.examples;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ParallelExecutionExample {

  public static void main(String[] args) {
    final long startTime = System.currentTimeMillis();

    Observable<Tile> searchTile = getSearchResults()
        // 3 items
        .doOnSubscribe(consumer -> logTime("Search started ", startTime))
        .doOnComplete(() -> logTime("Search completed ", startTime));

    Observable<TileResponse> populatedTiles = searchTile.flatMap(t -> {
      Observable<Reviews> reviews = getSellerReviews(t.getSellerId())
          .doOnComplete(() -> logTime("getSellerReviews[" + t.id + "] completed ", startTime));
      Observable<String> imageUrl = getProductImage(t.getProductId())
          .doOnComplete(() -> logTime("getProductImage[" + t.id + "] completed ", startTime));

      return Observable
          .zip(reviews, imageUrl, (r, u) -> new TileResponse(t, r, u))
          .doOnComplete(() -> logTime("zip[" + t.id + "] completed ", startTime));
    });

    populatedTiles.toList()
        .doOnDispose(() -> logTime("All Tiles Completed ", startTime))
        // subscribe
        .blockingGet();
  }

  private static Observable<Tile> getSearchResults() {
    return mockClient(new Tile(1), new Tile(2), new Tile(3));
  }

  private static Observable<Reviews> getSellerReviews(int id) {
    return mockClient(new Reviews(id));
  }

  private static Observable<String> getProductImage(int id) {
    return mockClient("image_" + id);
  }

  private static void logTime(String message, long startTime) {
    System.out.println("[" + Thread.currentThread() + "] " + message + " => " + (System.currentTimeMillis() - startTime) + "ms");
  }

  @SafeVarargs
  private static <T> Observable<T> mockClient(T... ts) {
    return Observable.<T>create(emitter -> {
      // simulate latency
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
      for (T t : ts) {
        emitter.onNext(t);
      }
      emitter.onComplete();
    }).subscribeOn(Schedulers.io());
    // note the use of subscribeOn to make an otherwise synchronous Observable async
  }

  public static class TileResponse {

    private Tile tile;
    private Reviews reviews;
    private String u;

    TileResponse(Tile t, Reviews r, String u) {
      this.tile = t;
      this.reviews = r;
      this.u = u;
    }

    @Override
    public String toString() {
      return "TileResponse{" +
          "tile=" + tile +
          ", reviews=" + reviews +
          ", u='" + u + '\'' +
          '}';
    }
  }

  static class Tile {

    private final int id;

    Tile(int i) {
      this.id = i;
    }

    int getSellerId() {
      return id;
    }

    int getProductId() {
      return id;
    }

    @Override
    public String toString() {
      return "Tile{" +
          "id=" + id +
          '}';
    }
  }

  static class Reviews {

    private int id;

    Reviews(int id) {
      this.id = id;
    }

    @Override
    public String toString() {
      return "Reviews{" +
          "id=" + id +
          '}';
    }
  }
}
