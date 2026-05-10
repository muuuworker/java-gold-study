// ============================================================
// 第7章: ジェネリクスとコレクション
// 対象試験: Java SE 17 Gold
// ============================================================
//
// 【前提知識】
//   Java の型は「プリミティブ型」と「参照型」に大別される。
//   int, double, boolean などはプリミティブ型であり、オブジェクトではない。
//   Integer, Double, Boolean などはプリミティブ型に対応する「ラッパークラス(参照型)」である。
//   コレクション・フレームワークは参照型のみを扱うため、この変換の仕組みが重要になる。
//
// 【Object クラスと継承階層】
//   Java では全てのクラスが暗黙的に java.lang.Object を継承する。
//   明示的に extends を書かなくても、コンパイラが自動的に extends Object を補う。
//   つまり「自作クラス含む全てのクラス」は Object のサブクラスである。
//
//   java.lang.Object
//     ├── String          （標準ライブラリ: java.lang パッケージ）
//     ├── Number          （標準ライブラリ: java.lang パッケージ）
//     │    ├── Integer
//     │    ├── Double
//     │    ├── Long ...
//     ├── Dog             ← 自作クラスも同じ Object の直接サブクラス
//     ├── Student         ← インタフェース実装の有無に関わらず Object を継承する
//     └── ...（全てのクラスが Object を継承する）
//
// 【パッケージとモジュールの用語整理】
//   ┌──────────────────────┬────────────────────────────────────────────────────────────────┐
//   │ 用語                 │ 意味                                                           │
//   ├──────────────────────┼────────────────────────────────────────────────────────────────┤
//   │ java.lang パッケージ │ Object, String, Integer, Math 等が入るパッケージ。             │
//   │                      │ 唯一 import なしで使える特別なパッケージ。                     │
//   │ 標準ライブラリ       │ JDK に付属する全パッケージの総称。                             │
//   │ （クラスライブラリ） │ java.lang, java.util, java.io 等を含む。                       │
//   │ モジュール           │ Java 9 で導入された仕組み。パッケージをまとめた単位。           │
//   │ java.base モジュール │ 最も基本的なモジュール。全 Java プログラムが暗黙的に依存する。  │
//   └──────────────────────┴────────────────────────────────────────────────────────────────┘
//
//   java.base モジュール（Java 9 以降の区分け）
//     ├── java.lang パッケージ → Object, String, Integer, Math ...
//     ├── java.util パッケージ → List, Map, Collections, Arrays ...
//     ├── java.io   パッケージ → InputStream, File ...
//     └── ...
//
//   ※ モジュールの詳細は第11章（Chapter11_ModuleSystem.java）を参照
//

// ============================================================
// ■ 1. オートボクシング (Auto-boxing) と アンボクシング (Unboxing)
// ============================================================
//
//   コンパイラが自動的に行う「プリミティブ型 ⇔ ラッパークラス」の変換のこと。
//
//   Boxing   : プリミティブ型 → ラッパークラス  (例: int → Integer)
//   Unboxing : ラッパークラス → プリミティブ型  (例: Integer → int)
//
//   【注意点】ラッパークラスは参照型なので null を持てる。
//   null を int にアンボクシングしようとすると NullPointerException が発生する。
//
// ============================================================
// ■ 2. ジェネリクス (Generics)
// ============================================================
//
//   「型をパラメータとして受け取る」仕組み。Java 5 で導入。
//   記法: クラス名<型パラメータ> (例: List<String>, Map<Integer, String>)
//
//   【目的】
//   ジェネリクス導入以前は、コレクションは Object 型として要素を保持していた。
//   そのため、取り出す際に毎回キャスト(型変換)が必要で、型の誤りが実行時まで判明しなかった。
//   ジェネリクスを使うことで「型安全性」をコンパイル時に担保できる。
//
//   【型消去 (Type Erasure)】
//   ジェネリクスの型情報はコンパイル後に消去される(実行時には存在しない)。
//   これを「型消去」と呼ぶ。試験でも問われる重要な概念。
//   例: List<String> も List<Integer> も実行時は List として扱われる。
//
//   【型パラメータの命名慣習】
//   T : Type (任意の型)
//   E : Element (コレクションの要素)
//   K : Key (マップのキー)
//   V : Value (マップの値)
//
//   【境界型パラメータ (Bounded Type Parameters)】
//   <T extends Number> : T は Number またはそのサブクラスに限定する(上限境界)
//   ワイルドカード「?」も重要。
//   <?>             : 非境界ワイルドカード (任意の型)
//   <? extends X>   : X およびそのサブクラス (共変, 読み取り専用向き)
//   <? super X>     : X およびそのスーパークラス (反変, 書き込み向き)
//
//   【読み取り / 書き込み まとめ表】
//   ┌──────────────────┬──────────────────────────────┬────────────────┬──────────────────────┐
//   │ 記法             │ 型の状態                      │ 読み取り       │ 書き込み             │
//   ├──────────────────┼──────────────────────────────┼────────────────┼──────────────────────┤
//   │ <?>              │ 不明（コンパイラも断定不可）  │ Object のみ    │ ❌ 不可              │
//   │ <T>              │ 確定（呼び出し時に決まる）    │ T として可     │ ✅ T 型なら可        │
//   │ <T extends X>    │ 確定（X 以下のサブクラス）    │ X として可     │ ✅ T 型なら可        │
//   │ <? extends X>    │ 不明（X のどのサブクラスか）  │ X として可     │ ❌ 不可              │
//   │ <? super X>      │ 不明（X のどの親クラスか）    │ Object のみ    │ ✅ X 型なら可        │
//   │ <T super X>      │ ❌ Java に存在しない文法       │ -              │ -                    │
//   └──────────────────┴──────────────────────────────┴────────────────┴──────────────────────┘
//
//   【<T extends X> の補足】
//   T は呼び出し時に確定するため、<T> と同様に読み書き両方できる。
//   さらに X のメソッドを呼び出せる点が <T> との違い。
//   例: <T extends Number> → メソッド内で T 型変数に対して doubleValue() 等が呼べる。
//
//   【<T super X> が存在しない理由】
//   T に super を使っても実益がないため Java の文法として認められていない。
//   理由①: super を使うと取り出した要素は Object にしかならない（T の名前を再利用する意味がない）
//   理由②: 書き込みは X 型のみ保証されるが、それなら <? super X> で十分
//   → 「型名 T を後で使い回す」というジェネリクスの目的を果たせないため、不要な文法として除外された。
//
//   【覚え方: PECS 原則】
//   Producer Extends, Consumer Super
//   = データを「生産（読み出す）」側は extends、「消費（書き込む）」側は super
//
// ============================================================
// ■ 3. コレクション・フレームワーク (Collection Framework)
// ============================================================
//
//   java.util パッケージで提供される、データ構造を統一的に扱うためのAPIの総称。
//
//   ┌──────────────────────────────────────┐
//   │             Iterable<E>              │  (最上位)
//   │               ↓                     │
//   │          Collection<E>              │
//   │         /     |      \              │
//   │      List<E> Set<E> Queue<E>        │
//   │               |      ↓             │
//   │           SortedSet<E> Deque<E>    │
//   └──────────────────────────────────────┘
//   ※ Map<K,V> は Collection を継承しない独立した階層を持つ
//
//   主要インタフェースと特徴:
//   ┌──────────────┬──────────────────────────────────────────────────┐
//   │ インタフェース │ 特徴                                             │
//   ├──────────────┼──────────────────────────────────────────────────┤
//   │ List<E>      │ 順序あり、重複許可、インデックスによるアクセス可   │
//   │ Set<E>       │ 順序なし(一部例外)、重複不可                      │
//   │ Map<K,V>     │ キーと値のペア、キーの重複不可                    │
//   │ Queue<E>     │ FIFO(先入れ先出し)の待ち行列                      │
//   │ Deque<E>     │ 両端から追加/削除可能(スタックとしても使える)      │
//   └──────────────┴──────────────────────────────────────────────────┘

import java.util.*;
import java.util.stream.Collectors;

public class Chapter07_GenericsAndCollections {

    // ============================================================
    // ■ ジェネリクス: 境界型パラメータの例
    // ============================================================
    // <T extends Number> により、T は Number のサブクラス(Integer, Double等)に限定される。
    // これにより、メソッド内で Number のメソッド(doubleValue()等)を安全に呼び出せる。
    public static <T extends Number> double sum(List<T> list) {
        double total = 0;
        for (T item : list) {
            total += item.doubleValue(); // Number のメソッドを呼べる
        }
        return total;
    }

    // ============================================================
    // ■ ジェネリクス: 非境界型パラメータの例
    // ============================================================
    // <T> は任意の型を受け取る。型安全に動作するが、Object 以外のメソッドは呼べない。
    public static <T> void printItem(T item) {
        System.out.println("Item: " + item + " (型: " + item.getClass().getSimpleName() + ")");
    }

    public static void main(String[] args) {

        // ============================================================
        // 1. オートボクシング / アンボクシング
        // ============================================================
        System.out.println("=== 1. オートボクシング ===");

        // Boxing: コンパイラが内部で Integer.valueOf(10) を呼び出している
        Integer boxed = 10;
        Integer boxed2 = Integer.valueOf(10); // 前述でコンパイラがやることを明示的に書いた例
        System.out.println("boxed: " + boxed);

        // Unboxing: コンパイラが内部で boxed.intValue() を呼び出している
        int unboxed = boxed;
        int unboxed2 = boxed.intValue(); // 前述でコンパイラがやることを明示的に書いた例
        System.out.println("unboxed: " + unboxed);

        // 【注意】Integer のキャッシュ (-128 ～ 127)
        // Integer.valueOf() はこの範囲の値をキャッシュするため、== 比較で true になる場合がある。
        // しかしこの範囲外では新しいオブジェクトが生成されるため false になる。
        // 値の比較には必ず equals() を使うこと。
        //
        // 【キャッシュ範囲の詳細】
        // 下限: -128 固定（変更不可）
        // 上限: デフォルト 127。JVM 起動オプションで変更可能。
        //   java -XX:AutoBoxCacheMax=1000 MyClass
        //   → 上限を 1000 まで広げられる（試験には出ないが知識として有用）
        // ※ 他のラッパークラスのキャッシュ:
        //   Byte, Short, Integer, Long : -128 ～ 127 をキャッシュ
        //   Character                  :    0 ～ 127 をキャッシュ
        //   Boolean                    : true / false の2値をキャッシュ
        //   Float, Double              : キャッシュなし（毎回新しいオブジェクト）
        Integer a = 127;
        Integer b = 127;
        System.out.println("127 == 127 (参照比較): " + (a == b));   // true (キャッシュ)

        Integer c = 128;
        Integer d = 128;
        System.out.println("128 == 128 (参照比較): " + (c == d));   // false (キャッシュ外)
        System.out.println("128 == 128 (値比較):   " + c.equals(d)); // true (equals推奨)

        System.out.println();

        // ============================================================
        // 2. List<E> - 順序付きリスト (重複許可)
        // ============================================================
        System.out.println("=== 2. List ===");

        // ArrayList: 内部的に配列で実装。インデックスアクセスが O(1) で高速。
        //            末尾への追加は O(1)（平均）、中間への挿入は O(n)。
        List<String> arrayList = new ArrayList<>();
        arrayList.add("Cherry");
        arrayList.add("Apple");
        arrayList.add("Banana");
        arrayList.add("Apple"); // 重複を許可
        System.out.println("ArrayList: " + arrayList);
        // → ArrayList: [Cherry, Apple, Banana, Apple]
        System.out.println("インデックスアクセス [1]: " + arrayList.get(1));
        // → インデックスアクセス [1]: Apple

        // LinkedList: 内部的に双方向連結リストで実装。
        //             中間への挿入/削除は O(1)が高速。
        //             インデックスアクセスは O(n)。
        //             List と Deque の両方を実装している。
        List<String> linkedList = new LinkedList<>(arrayList);
        System.out.println("LinkedList: " + linkedList);
        // → LinkedList: [Cherry, Apple, Banana, Apple]
        // ※ arrayList をコピーしているので要素・順序は同じ。内部実装（配列 vs 連結リスト）が異なるだけ。

        // List.of() : Java 9以降。不変(immutable)なリストを生成する。
        //             null要素不可、追加/変更/削除は UnsupportedOperationException をスロー。
        List<String> immutableList = List.of("X", "Y", "Z");
        System.out.println("List.of(): " + immutableList);
        // → List.of(): [X, Y, Z]

        // ============================================================
        // 【不変リストの作り方まとめ表】(試験頻出)
        // ============================================================
        //
        //   ┌──────────────────────┬──────────────┬──────────┬────────────────────────────────────────────┐
        //   │ 生成方法             │ 変更可否      │ null要素 │ 特徴                                       │
        //   ├──────────────────────┼──────────────┼──────────┼────────────────────────────────────────────┤
        //   │ new ArrayList<>()   │ ✅ 全て可     │ ✅ 可    │ 通常の可変リスト                           │
        //   │ Arrays.asList(...)  │ ⚠️ set のみ可 │ ✅ 可    │ 固定サイズ。add/remove は例外              │
        //   │ List.of(...)        │ ❌ 全て不可   │ ❌ 不可  │ Java 9以降。完全不変                       │
        //   │ List.copyOf(list)   │ ❌ 全て不可   │ ❌ 不可  │ Java 10以降。既存リストを不変コピー        │
        //   └──────────────────────┴──────────────┴──────────┴────────────────────────────────────────────┘
        //
        //   【Arrays.asList() の罠 (試験頻出)】
        //   Arrays.asList() が返すリストは「固定サイズリスト」であり、完全な可変リストではない。
        //     list.set(0, "NEW")  → ✅ OK（要素の上書きは可能）
        //     list.add("X")       → ❌ UnsupportedOperationException（サイズ変更は不可）
        //     list.remove("X")    → ❌ UnsupportedOperationException（サイズ変更は不可）
        //
        //   可変リストとして使いたい場合は、必ず ArrayList でラップすること:
        //     List<String> mutable = new ArrayList<>(Arrays.asList("A", "B", "C")); // ✅
        //

        System.out.println();

        // ============================================================
        // 3. Set<E> - 重複不可のコレクション
        // ============================================================
        System.out.println("=== 3. Set ===");

        // HashSet: ハッシュテーブルで実装。順序は保証されない。
        //          追加・検索・削除が O(1)（平均）。
        //          要素の重複判定は hashCode() と equals() を使用する。
        //          → 独自クラスを Set に入れる場合は両メソッドをオーバーライドすること。
        Set<String> hashSet = new HashSet<>(Arrays.asList("Dog", "Cat", "Bird", "Cat"));
        System.out.println("HashSet (重複が排除される): " + hashSet);
        // → HashSet (重複が排除される): [Cat, Bird, Dog] ※順序は実行ごとに異なる（保証なし）
        // ※ 「Cat」の重複が排除され3要素になる。ただし順序は HashSet の内部実装依存。

        // LinkedHashSet: 挿入順序を保持する HashSet。
        Set<String> linkedHashSet = new LinkedHashSet<>(Arrays.asList("Dog", "Cat", "Bird", "Cat"));
        System.out.println("LinkedHashSet (挿入順序を保持): " + linkedHashSet);
        // → LinkedHashSet (挿入順序を保持): [Dog, Cat, Bird]
        // ※ 重複「Cat」は排除されるが、最初に追加された順序（Dog→Cat→Bird）は保持される。

        // TreeSet: 赤黒木(Red-Black Tree)で実装。自然順序(昇順)でソートされる。
        //          Comparable または Comparator が必要。操作は O(log n)。
        Set<String> treeSet = new TreeSet<>(Arrays.asList("Dog", "Cat", "Bird"));
        System.out.println("TreeSet (自然順序でソート): " + treeSet);
        // → TreeSet (自然順序でソート): [Bird, Cat, Dog]
        // ※ アルファベット昇順（自然順序）で自動ソートされる。B→C→D の順。

        // 【Set 3種の使い分けまとめ】
        //   HashSet       : とにかく速い。順序は捨てる。
        //   LinkedHashSet : 挿入順序を覚えておきたい。
        //   TreeSet       : 常にソートされた状態を保ちたい。
        System.out.println();

        // ============================================================
        // 4. Map<K,V> - キーと値のペア (キーは重複不可)
        // ============================================================
        System.out.println("=== 4. Map ===");

        // HashMap: ハッシュテーブルで実装。順序は保証されない。
        //          キーと値にnullを許可する。操作は O(1)（平均）。
        Map<Integer, String> hashMap = new HashMap<>();
        hashMap.put(3, "Three");
        hashMap.put(1, "One");
        hashMap.put(2, "Two");
        hashMap.put(1, "One_Updated"); // キーが重複した場合、値が上書きされる
        System.out.println("HashMap: " + hashMap);
        // → HashMap: {1=One_Updated, 2=Two, 3=Three} ※順序は保証なし
        // ※ キー1 は put(1,"One") → put(1,"One_Updated") で上書きされている

        // getOrDefault(): キーが存在しない場合にデフォルト値を返す。
        System.out.println("getOrDefault(99, \"未登録\"): " + hashMap.getOrDefault(99, "未登録"));
        // → getOrDefault(99, "未登録"): 未登録
        // ※ キー99 は存在しないのでデフォルト値「未登録」が返る

        // putIfAbsent(): キーが存在しない場合のみ追加する。
        hashMap.putIfAbsent(1, "Won't overwrite");
        System.out.println("putIfAbsent(1, ...): " + hashMap.get(1));
        // → putIfAbsent(1, ...): One_Updated
        // ※ キー1 はすでに存在するため putIfAbsent は何もせず、元の値「One_Updated」のまま

        // LinkedHashMap: 挿入順序を保持する HashMap。
        // TreeMap: 自然順序(キー昇順)でソートされる HashMap。

        // Map.of(): Java 9以降。不変なマップを生成する。
        Map<String, Integer> immutableMap = Map.of("A", 1, "B", 2);
        System.out.println("Map.of(): " + immutableMap);
        // → Map.of(): {A=1, B=2} ※順序は保証なし（{B=2, A=1} になる場合もある）

        // エントリの反復処理
        System.out.println("--- Map の反復処理 ---");
        hashMap.forEach((key, value) -> System.out.println(key + " -> " + value));
        // → 1 -> One_Updated
        // → 2 -> Two
        // → 3 -> Three
        // ※ HashMap は順序を保証しないため、出力順序は実行ごとに変わる可能性がある

        System.out.println();

        // ============================================================
        // 5. Queue<E> / Deque<E> - キューとデック
        // ============================================================
        System.out.println("=== 5. Queue / Deque ===");

        // Queue: FIFO (First In, First Out) の待ち行列。
        //   offer(e) : 末尾に追加 (失敗時 false を返す)
        //   poll()   : 先頭から取得して削除 (空の場合 null を返す)
        //   peek()   : 先頭を参照するのみ (空の場合 null を返す)
        Queue<String> queue = new LinkedList<>();
        queue.offer("First");
        queue.offer("Second");
        queue.offer("Third");
        System.out.println("Queue peek (先頭参照): " + queue.peek());
        System.out.println("Queue poll (先頭取得&削除): " + queue.poll());
        System.out.println("Queue 残り: " + queue);

        System.out.println();

        // Deque: 両端キュー (Double-Ended Queue)。
        //   スタック(LIFO)としても使用可能。
        //   Deque インタフェースは Stack クラスより推奨される。
        //   ArrayDeque: 配列ベースの Deque。null 不可。
        Deque<String> deque = new ArrayDeque<>();
        deque.offerFirst("Middle"); // 先頭に追加
        deque.offerFirst("First");  // さらに先頭に追加
        deque.offerLast("Last");    // 末尾に追加
        System.out.println("Deque: " + deque);

        // スタックとして使用する場合の主要メソッド
        deque.push("Pushed"); // 先頭に追加 (= offerFirst)
        System.out.println("push後: " + deque);
        System.out.println("pop (先頭取得&削除): " + deque.pop()); // = pollFirst
        System.out.println("pop後: " + deque);

        System.out.println();

        // ============================================================
        // 6. Comparator を使用したコレクションのソート
        // ============================================================
        System.out.println("=== 6. Comparator によるソート ===");

        // Comparator は関数型インタフェース。ラムダ式やメソッド参照で記述できる。
        // compare(o1, o2) が返す値:
        //   負の値 → o1 が o2 より前
        //   0      → 等しい
        //   正の値 → o1 が o2 より後

        List<String> names = new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob", "Eve"));

        // 自然順序(アルファベット昇順)
        names.sort(Comparator.naturalOrder());
        System.out.println("naturalOrder (昇順): " + names);

        // 逆順
        names.sort(Comparator.reverseOrder());
        System.out.println("reverseOrder (降順): " + names);

        // 文字列長でソート → 同じ長さの場合は自然順序で
        names.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
        System.out.println("文字列長→自然順序: " + names);

        // ラムダ式による独自比較 (大文字小文字無視)
        names.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
        System.out.println("大文字小文字無視: " + names);

        System.out.println();

        // ============================================================
        // 7. コレクションの便利なメソッド (Collections ユーティリティクラス)
        // ============================================================
        System.out.println("=== 7. Collections ユーティリティ ===");

        List<Integer> nums = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6));

        System.out.println("元のリスト: " + nums);
        System.out.println("max: " + Collections.max(nums));
        System.out.println("min: " + Collections.min(nums));
        System.out.println("frequency(1): " + Collections.frequency(nums, 1)); // 出現回数

        Collections.sort(nums);
        System.out.println("sort後: " + nums);

        Collections.reverse(nums);
        System.out.println("reverse後: " + nums);

        Collections.shuffle(nums);
        System.out.println("shuffle後: " + nums);

        // 不変コレクションのラッピング
        List<Integer> unmodifiable = Collections.unmodifiableList(nums);
        System.out.println("unmodifiableList: " + unmodifiable);

        System.out.println();

        // ============================================================
        // 8. ジェネリクスの実例
        // ============================================================
        System.out.println("=== 8. ジェネリクスの実例 ===");

        // 型安全なメソッド呼び出し (型推論により <Integer> は省略可)
        printItem("Hello Generics");
        printItem(42);
        printItem(3.14);

        // 境界型パラメータ: Number のサブクラスのみ受け付ける
        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5);
        List<Double>  dblList = Arrays.asList(1.1, 2.2, 3.3);
        System.out.println("sum(intList): " + sum(intList));
        System.out.println("sum(dblList): " + sum(dblList));

        // ワイルドカード: List<?> は「任意の型のList」を受け取れる
        printListSize(intList);
        printListSize(dblList);
    }

    // ワイルドカード <?> を使ったメソッド
    // List<Object> とは異なり、List<Integer> や List<String> など任意の型のListを受け取れる。
    // ただし、要素の追加はできない (コンパイルエラー)。読み取り専用として扱う。
    public static void printListSize(List<?> list) {
        System.out.println("リストのサイズ: " + list.size() + " / 内容: " + list);
    }

    // ============================================================
    // 9. プリミティブ型 と ラッパークラスの違い
    // ============================================================
    //
    //   【見分け方の原則】
    //   先頭が小文字 → プリミティブ型（値を直接変数に格納する）
    //   先頭が大文字 → 参照型・クラス（ラッパークラスを含む）
    //
    //   【対応表】
    //   プリミティブ型   ラッパークラス   ※注意
    //   byte          → Byte
    //   short         → Short
    //   int           → Integer    ← 名前が変わる ("Int" ではない)
    //   long          → Long
    //   float         → Float
    //   double        → Double
    //   char          → Character  ← 名前が変わる ("Char" ではない)
    //   boolean       → Boolean
    //
    //   試験ポイント: int→Integer, char→Character の2つだけ名前が異なる。
    //   残り6つは頭文字を大文字にするだけ。
    //
    public static void primitiveVsWrapper() {
        System.out.println("=== 9. プリミティブ型 vs ラッパークラス ===");

        // ① 先頭が小文字 → プリミティブ型（変数に値が直接入る）
        int     i = 10;
        double  d = 3.14;
        char    c = 'A';
        boolean b = true;
        System.out.println("プリミティブ i=" + i + ", d=" + d + ", c=" + c + ", b=" + b);

        // ② 先頭が大文字 → ラッパークラス（オブジェクト。new でも生成できる）
        //   実際には new Integer(10) は Java 9 以降非推奨。
        //   Integer.valueOf(10) またはオートボクシング（代入）が推奨される。
        Integer   I = 10;      // オートボクシング: コンパイラが Integer.valueOf(10) に変換
        Double    D = 3.14;
        Character C = 'A';
        Boolean   B = true;
        System.out.println("ラッパー I=" + I + ", D=" + D + ", C=" + C + ", B=" + B);

        // ③ null を代入できるかで判断できる
        // int n = null;   // コンパイルエラー！ プリミティブ型は null 不可
        Integer n = null;   // OK。ラッパークラスはオブジェクトなので null を持てる
        System.out.println("null代入: n=" + n);

        // 【重要な落とし穴】null を int にアンボクシングすると NullPointerException!
        try {
            int danger = n;  // n が null のため NullPointerException が発生する
        } catch (NullPointerException e) {
            System.out.println("null のアンボクシングは NullPointerException: " + e.getClass().getSimpleName());
        }

        // ④ メソッドを持つかで判断できる
        // i.toString();        // コンパイルエラー！ プリミティブ型はメソッドなし
        System.out.println("I.toString(): " + I.toString());          // OK: ラッパークラスはメソッドを持つ
        System.out.println("I.compareTo(20): " + I.compareTo(20));    // 比較メソッド

        // ラッパークラスが持つ静的ユーティリティメソッド（試験頻出）
        int parsed = Integer.parseInt("42");          // String → int
        String str = Integer.toString(42);            // int → String
        int max    = Integer.max(10, 20);             // 大きい方を返す
        int binary = Integer.parseInt("1010", 2);     // 2進数文字列 → int
        System.out.println("parseInt: " + parsed);
        System.out.println("toString: " + str);
        System.out.println("max: " + max);
        System.out.println("parseBinary: " + binary);

        // Integer の定数（試験でも問われる）
        System.out.println("Integer.MAX_VALUE: " + Integer.MAX_VALUE);  // 2147483647
        System.out.println("Integer.MIN_VALUE: " + Integer.MIN_VALUE);  // -2147483648
    }
}
