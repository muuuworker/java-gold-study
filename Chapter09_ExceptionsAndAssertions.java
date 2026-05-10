// 第9章: 例外とアサーション
import java.io.*;

public class Chapter09_ExceptionsAndAssertions {
    public static void main(String[] args) {
        // try-with-resources 文 (自動的にリソースを閉じる)
        try (StringReader reader = new StringReader("Test")) {
            System.out.println("Read char: " + (char) reader.read());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // アサーション (実行時に java -ea オプションが必要)
        int score = 150;
        // assert score <= 100 : "Score cannot exceed 100";
        System.out.println("Score: " + score);

        // カスタム例外の利用
        try {
            checkAge(15);
        } catch (InvalidAgeException e) {
            System.out.println("Caught custom exception: " + e.getMessage());
        }
    }

    public static void checkAge(int age) throws InvalidAgeException {
        if (age < 18) {
            throw new InvalidAgeException("Age must be 18 or older.");
        }
    }
}

// カスタム例外クラス
class InvalidAgeException extends Exception {
    public InvalidAgeException(String message) {
        super(message);
    }
}
