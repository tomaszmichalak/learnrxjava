package learnrxjava.examples;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

public class FlowControlThrottleExample {

  public static void main(String args[]) {
    // first item emitted in each time window
    hotStream().throttleFirst(500, TimeUnit.MILLISECONDS).take(10).blockingIterable()
        .forEach(System.out::println);

    // last item emitted in each time window
    hotStream().throttleLast(500, TimeUnit.MILLISECONDS).take(10).blockingIterable()
        .forEach(System.out::println);
  }

  /**
   * This is an artificial source to demonstrate an infinite stream that emits randomly
   */
  private static Observable<Integer> hotStream() {
    return Observable.<Integer>create(emitter -> {
      int i = 0;
      while (!emitter.isDisposed()) {
        emitter.onNext(i++);
        try {
          // sleep for a random amount of time
          // NOTE: Only using Thread.sleep here as an artificial demo.
          Thread.sleep((long) (Math.random() * 100));
        } catch (Exception e) {
          // do nothing
        }
      }
    }).subscribeOn(Schedulers.newThread());
  }

}
