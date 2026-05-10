// Comparable<T> インタフェースと <T extends Comparable<T>> の解説
import java.util.*;

public class ComparableExample {

    // ============================================================
    // ① <T extends Comparable<T>> を使った汎用 findMax メソッド
    // ============================================================
    // T は Comparable<T> を実装している型に限定される。
    // → T が持つ compareTo() を安全に呼び出せる。
    // String, Integer, Double などは Comparable を実装済みなので渡せる。
    public static <T extends Comparable<T>> T findMax(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("リストが空です");
        }
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) { // compareTo が使えることが保証されている
                max = item;
            }
        }
        return max;
    }

    // ============================================================
    // ② 独自クラスで Comparable<T> を実装する例
    // ============================================================
    // 独自クラスを compareTo() によって比較可能にするには、
    // Comparable<T> を implements して compareTo() をオーバーライドする。
    static class Student implements Comparable<Student> {
        String name;
        int score;

        Student(String name, int score) {
            this.name = name;
            this.score = score;
        }

        // compareTo() の実装: スコアで比較する
        @Override
        public int compareTo(Student other) {
            // this.score - other.score でも動くが、
            // Integer.compare() を使う方がオーバーフローに対して安全
            return Integer.compare(this.score, other.score);
        }

        @Override
        public String toString() {
            return name + "(" + score + ")";
        }
    }

    public static void main(String[] args) {

        // ============================================================
        // 1. String に対して findMax を使う
        // ============================================================
        // String は Comparable<String> を実装しているので渡せる。
        // 辞書順(アルファベット順)で比較される。
        List<String> strings = Arrays.asList("Banana", "Apple", "Cherry", "Date");
        String maxStr = findMax(strings);
        System.out.println("String の最大値(辞書順): " + maxStr); // Cherry

        // ============================================================
        // 2. Integer に対して findMax を使う
        // ============================================================
        // Integer は Comparable<Integer> を実装しているので渡せる。
        List<Integer> nums = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6);
        Integer maxNum = findMax(nums);
        System.out.println("Integer の最大値: " + maxNum); // 9

        // ============================================================
        // 3. 独自クラス Student に対して findMax を使う
        // ============================================================
        // Student は Comparable<Student> を実装しているので渡せる。
        List<Student> students = Arrays.asList(
            new Student("Alice", 85),
            new Student("Bob", 92),
            new Student("Charlie", 78)
        );
        Student topStudent = findMax(students);
        System.out.println("最高得点の学生: " + topStudent); // Bob(92)

        // ============================================================
        // 4. compareTo() の戻り値を直接確認する
        // ============================================================
        System.out.println("\n--- compareTo() の戻り値 ---");

        // String の compareTo: 辞書順比較
        String s1 = "Apple";
        String s2 = "Cherry";
        System.out.println("\"Apple\".compareTo(\"Cherry\"): " + s1.compareTo(s2));
        // 負の値 → Apple < Cherry (辞書順でAppleが前)

        // Integer の compareTo: 数値比較
        Integer i1 = 10;
        Integer i2 = 20;
        System.out.println("10.compareTo(20): " + i1.compareTo(i2)); // 負の値
        System.out.println("20.compareTo(20): " + i2.compareTo(i2)); // 0
        System.out.println("20.compareTo(10): " + i2.compareTo(i1)); // 正の値

        // ============================================================
        // 5. Collections.sort() は Comparable を前提に動作している
        // ============================================================
        // Collections.sort() は内部で compareTo() を使ってソートする。
        // つまり Comparable を実装していないクラスはソートできない。
        System.out.println("\n--- Collections.sort() ---");
        List<Student> sortable = new ArrayList<>(students);
        Collections.sort(sortable); // Student が Comparable を実装しているのでOK
        System.out.println("スコア昇順でソート: " + sortable);

        // 降順にしたい場合は Comparator.reverseOrder() か reversed() を使う
        sortable.sort(Comparator.reverseOrder()); // Comparable 実装済みならこれでOK
        System.out.println("スコア降順でソート: " + sortable);
    }
}
