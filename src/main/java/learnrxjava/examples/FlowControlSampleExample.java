package learnrxjava.examples;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

public class FlowControlSampleExample {

  public static void main(String args[]) {
    hotStream().sample(500, TimeUnit.MILLISECONDS).take(100).blockingIterable()
        .forEach(System.out::println);
  }

  /**
   * This is an artificial source to demonstrate an infinite stream that emits randomly
   */
  private static Observable<Integer> hotStream() {

    return Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
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
