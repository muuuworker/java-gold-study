// 第12章: セキュアコーディング
import java.util.*;

public class Chapter12_SecureCoding {
    public static void main(String[] args) {
        System.out.println("=== 第12章: セキュアコーディング ===");
        
        // カプセル化と不変 (Immutable) オブジェクトの利用による防御
        List<String> mutableList = new ArrayList<>(Arrays.asList("A", "B"));
        
        // List.copyOf() で不変リストを作成 (防御的コピー)
        List<String> immutableList = List.copyOf(mutableList);
        
        System.out.println("元のリスト(変更前): " + mutableList);
        try {
            immutableList.add("C"); // UnsupportedOperationException が発生
        } catch (UnsupportedOperationException e) {
            System.out.println("不変リストへの変更は UnsupportedOperationException によってブロックされます。");
        }
        
        // 元のリストを変更しても、copyOfで作成されたリストには影響しない
        mutableList.add("C");
        System.out.println("元のリスト(変更後): " + mutableList);
        System.out.println("不変リスト(変更後): " + immutableList);

        // その他の対策
        System.out.println("・SQLインジェクション対策としての PreparedStatement (Chapter06参照)");
        System.out.println("・機密情報のメモリ上の扱い (Stringよりもchar[]を使う等)");
    }
}
