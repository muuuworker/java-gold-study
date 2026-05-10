// 第10章: ローカライズ
import java.util.*;
import java.text.*;
import java.time.*;
import java.time.format.*;

public class Chapter10_Localization {
    public static void main(String[] args) {
        // Locale の使用
        Locale localeUS = Locale.US;
        Locale localeJP = Locale.JAPAN;

        // 数値・通貨のフォーマット
        double number = 12345.67;
        NumberFormat usFormat = NumberFormat.getCurrencyInstance(localeUS);
        NumberFormat jpFormat = NumberFormat.getCurrencyInstance(localeJP);
        System.out.println("US Currency: " + usFormat.format(number));
        System.out.println("JP Currency: " + jpFormat.format(number));

        // 日付のフォーマット (DateTimeFormatter)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日", localeJP);
        System.out.println("Formatted Date: " + now.format(dtf));

        // リソースバンドル (動作確認用にはプロパティファイル messages_ja_JP.properties などが必要)
        // ResourceBundle bundle = ResourceBundle.getBundle("messages", localeJP);
        // System.out.println(bundle.getString("greeting"));
    }
}
