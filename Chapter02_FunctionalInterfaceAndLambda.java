// 第2章: 関数型インタフェースとラムダ式
import java.util.function.*;

public class Chapter02_FunctionalInterfaceAndLambda {
    public static void main(String[] args) {
        // 1. Predicate (引数を取り、booleanを返す)
        Predicate<String> isEmpty = String::isEmpty; // メソッド参照
        System.out.println("isEmpty: " + isEmpty.test(""));

        // 2. Consumer (引数を取り、何も返さない)
        Consumer<String> print = System.out::println;
        print.accept("Hello Consumer");

        // 3. Function (引数を取り、結果を返す)
        Function<String, Integer> length = String::length;
        System.out.println("Length: " + length.apply("Java"));

        // 4. Supplier (引数を取らず、結果を返す)
        Supplier<Double> random = Math::random;
        System.out.println("Random: " + random.get());

        // 内部クラスと無名クラスの例
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("Anonymous class running");
            }
        };
        r.run();
        
        // ラムダ式による置き換え
        Runnable rLambda = () -> System.out.println("Lambda running");
        rLambda.run();
    }
}
