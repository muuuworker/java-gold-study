// 第4章: ストリームAPI
import java.util.*;
import java.util.stream.*;

public class Chapter04_StreamAPI {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("apple", "banana", "cherry", "date");

        // 中間操作: filter, map, sorted
        // 終端操作: collect
        List<String> filtered = list.stream()
            .filter(s -> s.length() > 4)
            .map(String::toUpperCase)
            .sorted()
            .collect(Collectors.toList());
        System.out.println("Filtered & Mapped: " + filtered);

        // 集計処理: count
        long count = list.stream().filter(s -> s.startsWith("a")).count();
        System.out.println("Count: " + count);

        // 探索処理: findFirst, anyMatch
        Optional<String> first = list.stream().findFirst();
        first.ifPresent(s -> System.out.println("First: " + s));

        boolean hasApple = list.stream().anyMatch(s -> s.equals("apple"));
        System.out.println("Has Apple: " + hasApple);

        // ParallelStream
        System.out.println("--- Parallel Stream ---");
        list.parallelStream()
            .forEach(s -> System.out.println(s + " processed by " + Thread.currentThread().getName()));
    }
}
