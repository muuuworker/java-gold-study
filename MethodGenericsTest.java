import java.util.ArrayList;
import java.util.List;

public class MethodGenericsTest {
    // 1. メソッド定義での省略
    // <T> と書くと、暗黙的に <T extends Object> となる
    public static <T> void printType(T item) {
        System.out.println("Type: " + item.getClass().getSimpleName());
    }

    // 境界を指定する場合
    public static <T extends Number> void printNumber(T item) {
        System.out.println("Number: " + item.doubleValue());
    }

    public static void main(String[] args) {
        // 2. メソッド呼び出しでの省略 (型推論)
        // 明示的に <String> と書かなくても、引数から推論される
        printType("Hello"); // String と推論される
        printType(123);     // Integer と推論される

        // 明示的な型指定 (あまり使われないが、複雑な推論が失敗する場合に使う)
        MethodGenericsTest.<String>printType("Explicit");

        // 3. 境界がある場合
        printNumber(10);    // Integer (Numberのサブクラス) なのでOK
        // printNumber("A"); // コンパイルエラー: String は Number を継承していない

        // 4. 注意：ダイヤモンド演算子 <> はメソッド呼び出しには使えない
        // printType<>("Error"); // コンパイルエラー
    }
}
