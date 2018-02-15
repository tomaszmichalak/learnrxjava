package learnrxjava.examples;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subscribers.TestSubscriber;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UnitTesting {

  public static void main(String... args) {
    TestScheduler scheduler = new TestScheduler();

    TestObserver<String> test = Observable.interval(200, TimeUnit.MILLISECONDS, scheduler)
        .map(i -> i + " value")
        .test();

    scheduler.advanceTimeBy(200, TimeUnit.MILLISECONDS);
    test.assertOf(c -> c.onNext("0 value"));

    scheduler.advanceTimeTo(1000, TimeUnit.MILLISECONDS);
    test.assertOf(c -> c.onNext("4 value"));
  }

}
