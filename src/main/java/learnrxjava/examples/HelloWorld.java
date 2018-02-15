package learnrxjava.examples;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscription;

/**
 * FIXME
 */
public class HelloWorld {

  public static void main(String[] args) {

    // Hello World
    Observable.create(subscriber -> {
      subscriber.onNext("Hello World!");
      subscriber.onComplete();
    }).subscribe(System.out::println);

    // shorten by using helper method
    Observable.just("Hello", "World!")
        .subscribe(System.out::println);

    // add onError and onComplete listeners
    Observable.just("Hello World!")
        .subscribe(System.out::println,
            Throwable::printStackTrace,
            () -> System.out.println("Done"));

    // error propagation
    Observable.create(subscriber -> {
      throw new IllegalStateException("Some exception");
    }).subscribe(System.out::println, error -> System.out.println("Exception found!"),
        () -> System.out.print("Done"));

    // error propagation
    Observable.create(subscriber -> {
      subscriber.onNext("Hello world");
      subscriber.onComplete();
    }).map(item -> {
      throw new IllegalStateException("Some exception");
    }).subscribe(System.out::println, error -> System.out.println("Exception found!"),
        () -> System.out.print("Done"));

    // add concurrency (manually)
    Observable.create(subscriber -> Executors
        .newSingleThreadExecutor().execute(() -> {
          try {
            subscriber.onNext(getData());
            subscriber.onComplete();
          } catch (Exception e) {
            subscriber.onError(e);
          }
        }))
        .subscribe(System.out::println);

    // add concurrency (using a Scheduler)
    Observable.create(subscriber -> {
      try {
        subscriber.onNext(getData());
        subscriber.onComplete();
      } catch (Exception e) {
        subscriber.onError(e);
      }
    }).subscribeOn(Schedulers.io())
        .subscribe(System.out::println);

    // add operator
    Observable.create(subscriber -> {
      try {
        subscriber.onNext(getData());
        subscriber.onComplete();
      } catch (Exception e) {
        subscriber.onError(e);
      }
    }).subscribeOn(Schedulers.io())
        .map(data -> data + " --> at " + System.currentTimeMillis())
        .subscribe(System.out::println);

    // add error handling
    Observable.create(subscriber -> {
      try {
        subscriber.onNext(getData());
        subscriber.onComplete();
      } catch (Exception e) {
        subscriber.onError(e);
      }
    }).subscribeOn(Schedulers.io())
        .map(data -> data + " --> at " + System.currentTimeMillis())
        .onErrorResumeNext(
            e -> (ObservableSource<? extends String>) Observable.just("Fallback Data"))
        .subscribe(System.out::println);

    // infinite
    Observable.create(subscriber -> {
      int i = 0;
      while (!subscriber.isDisposed()) {
        subscriber.onNext(i++);
      }
    }).take(10).subscribe(System.out::println);

    //Hello World
    Observable.create(subscriber -> {
      throw new RuntimeException("failed!");
    }).onErrorResumeNext(throwable -> {
      return Observable.just("fallback value");
    }).subscribe(System.out::println);

    Observable.create(subscriber -> {
      throw new RuntimeException("failed!");
    }).onErrorResumeNext(Observable.just("fallback value"))
        .subscribe(System.out::println);

    Observable.create(subscriber -> {
      throw new RuntimeException("failed!");
    }).onErrorReturn(throwable -> "fallback value").subscribe(System.out::println);

    Observable.create(subscriber -> {
      throw new RuntimeException("failed!");
    }).retryWhen(attempts -> attempts.zipWith(Observable.range(1, 3), (throwable, i) -> i)
        .flatMap(i -> {
          System.out.println("delay retry by " + i + " second(s)");
          return Observable.timer(i, TimeUnit.SECONDS);
        }).concatWith(Observable.error(new RuntimeException("Exceeded 3 retries"))))
        .subscribe(System.out::println, Throwable::printStackTrace);

    // Flowable
    Flowable.create((FlowableOnSubscribe<String>) emitter -> {
      emitter.onNext("1234");
      emitter.onNext("2345");
      emitter.onComplete();
    }, BackpressureStrategy.BUFFER)
        .subscribe(new FlowableSubscriber<String>() {

          @Override
          public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
          }

          @Override
          public void onComplete() {
            System.out.println("Done");
          }

          @Override
          public void onError(Throwable e) {
            e.printStackTrace();
          }

          @Override
          public void onNext(String t) {
            System.out.println(t);
          }

        });

    try {
      Thread.sleep(20000);
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }

  }

  private static String getData() {
    return "Got Data!";
  }
}
