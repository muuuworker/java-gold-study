import java.util.ArrayList;
import java.util.List;

public class GenericsTest {
    public static void main(String[] args) {
        // 1. ダイヤモンド演算子 (<> の中が空)
        // 左側の宣言から型が推論される
        List<String> list1 = new ArrayList<>();
        list1.add("Hello");
        // list1.add(123); // コンパイルエラー: String 以外は入れられない
        String s1 = list1.get(0); // キャスト不要
        System.out.println("Diamond Operator: " + s1);

        // 2. 生型 (Raw Type: <> 自体を書かない)
        // 型パラメータが Object として扱われる
        List list2 = new ArrayList(); 
        list2.add("World");
        list2.add(123); // 警告は出るがコンパイルは通る (型安全ではない)
        
        Object o = list2.get(0);
        String s2 = (String) list2.get(0); // 取り出す時にキャストが必要
        System.out.println("Raw Type: " + s2 + ", " + list2.get(1));
    }
}
