package learnrxjava.examples;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Example of a "cold Observable" using "reactive pull" to emit only as many items as requested by Subscriber.
 */
public class FlowControlReactivePullCold {

//  public static void main(String[] args) {
//    getData(1).observeOn(Schedulers.computation()).blockingIterable().forEach(System.out::println);
//  }
//
//  /**
//   * This is a simple example of an Observable Iterable using "reactive pull".
//   */
//  public static Observable<Integer> getData(int id) {
//    // simulate a finite, cold data source
//    final ArrayList<Integer> data = new ArrayList<>();
//    for (int i = 0; i < 5000; i++) {
//      data.add(i + id);
//    }
//    return fromIterable(data);
//  }
//
//  /**
//   * A more performant but more complicated implementation can be seen at: https://github.com/Netflix/RxJava/blob/master/rxjava-core/src/main/java/rx/internal/operators/OnSubscribeFromIterable.java
//   * <p> Real code should just use Observable.from(Iterable iter) instead of re-implementing this logic. <p> This is being shown as a simplified
//   * version to demonstrate how "reactive pull" data sources are implemented.
//   */
//  public static Observable<Integer> fromIterable(Iterable<Integer> it) {
//    // return as Observable (real code would likely do IO of some kind)
//    return Observable.create(s -> {
//      final Iterator<Integer> iter = it.iterator();
//      final AtomicLong requested = new AtomicLong();
//
////      s.onNext();
//      s.setProducer((long request) -> {
//        /*
//         * We add the request but only kick off work if at 0.
//         *
//         * This is done because over async boundaries `request(n)` can be called multiple times by
//         * another thread while this `Producer` is still emitting. We only want one thread ever emitting.
//         */
//        if (requested.getAndAdd(request) == 0) {
//          do {
//            if (s.isDisposed()) {
//              return;
//            }
//            if (iter.hasNext()) {
//              s.onNext(iter.next());
//            } else {
//              s.onComplete();
//            }
//          } while (requested.decrementAndGet() > 0);
//        }
//      });
//    });
//  }

}
