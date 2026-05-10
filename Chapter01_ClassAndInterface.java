// 第1章: クラスとインタフェース
public class Chapter01_ClassAndInterface {
    // Java 17 の主要な機能: レコードクラス (不変なデータを保持するクラスを簡潔に定義)
    public record Person(String name, int age) {}
    
    // シールドクラス (継承できるクラスを制限)
    public sealed class Shape permits Circle, Square {}
    public final class Circle extends Shape {}
    public final class Square extends Shape {}

    public static void main(String[] args) {
        Person p = new Person("Alice", 30);
        System.out.println("Record: " + p.name() + " is " + p.age());
    }
}
