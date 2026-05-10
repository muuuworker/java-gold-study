// 第5章: 入出力 (NIO.2)
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class Chapter05_IOAndNIO2 {
    public static void main(String[] args) {
        Path path = Paths.get("test.txt");

        // NIO.2 を使ったファイルの書き込みと読み取り
        try {
            Files.writeString(path, "Hello NIO.2!\nSecond Line");
            List<String> lines = Files.readAllLines(path);
            System.out.println("--- Files.readAllLines ---");
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Stream API を使用したファイルアクセス
        System.out.println("--- Files.lines ---");
        try (var stream = Files.lines(path)) {
            stream.filter(line -> line.contains("Hello"))
                  .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
