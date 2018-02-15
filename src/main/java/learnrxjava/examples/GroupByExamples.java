package learnrxjava.examples;

import io.reactivex.Observable;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class GroupByExamples {

  public static void main(String args[]) {

    Observable.range(1, 100)
        .groupBy(n -> n % 2 == 0)
        .flatMapSingle(Observable::toList)
        .forEach(System.out::println);

    System.out.println("2--------------------------------------------------------------------------------------------------------");

    // takes first 10 elements, then onComplete, then takes next 10 element, then onComplete, ...
    Observable.range(1, 100)
        .groupBy(n -> n % 2 == 0)
        .flatMapSingle(g -> g.take(10).toList())
        .forEach(System.out::println);

    System.out.println("3--------------------------------------------------------------------------------------------------------");

    Observable.range(1, 100)
        .groupBy(n -> n % 2 == 0)
        .flatMapSingle(g -> g.filter(i -> i <= 20).toList())
        .forEach(System.out::println);

    System.out.println("4--------------------------------------------------------------------------------------------------------");

    //odd/even into lists of 20 but only take the first 2 groups
    Observable.range(1, 100)
        .groupBy(n -> n % 2 == 0)
        .flatMapSingle(g -> g.take(20).toList())
        .take(2)
        .forEach(System.out::println);

    System.out.println("5--------------------------------------------------------------------------------------------------------");

    //odd/even into 2 lists with numbers less than 30
    Observable.range(1, 100)
        .groupBy(n -> n % 2 == 0)
        .flatMapSingle(g -> g.takeWhile(i -> i < 30).toList())
        .filter(l -> !l.isEmpty())
        .forEach(System.out::println);

    System.out.println("6--------------------------------------------------------------------------------------------------------");

    Observable.fromIterable(Arrays.asList("a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c"))
        .groupBy(n -> n)
        .flatMapMaybe(g -> g.take(3).reduce((s, v) -> s + v))
        .forEach(System.out::println);

    System.out.println("7--------------------------------------------------------------------------------------------------------");

    Observable.timer(1, TimeUnit.MILLISECONDS)
        .map(val -> val + 1)
        .groupBy(n -> n % 2 == 0)
        .flatMapSingle(Observable::toList)
        .blockingIterable()
        .forEach(System.out::println);
  }
}
