// 第11章: モジュール・システム
public class Chapter11_ModuleSystem {
    /*
     * モジュール・システムの基本 (Java 9以降)
     * 
     * [module-info.java の例]
     * module com.example.myapp {
     *     requires java.sql;          // 他のモジュールへの依存を宣言
     *     exports com.example.utils;  // 他のモジュールにパッケージを公開
     *     
     *     // ServiceLoader 用の設定
     *     uses com.example.spi.MyService;
     *     provides com.example.spi.MyService with com.example.impl.MyServiceImpl;
     * }
     * 
     * [重要なコマンド]
     * javac -d mods/com.example.myapp src/com.example.myapp/module-info.java ...
     * java --module-path mods -m com.example.myapp/com.example.myapp.Main
     * 
     * jdeps コマンド: クラスファイルの依存関係を解析するツール。
     * ServiceLoader: インタフェースの実装を動的にロードする仕組み。
     */
    public static void main(String[] args) {
        System.out.println("=== 第11章: モジュール・システム ===");
        System.out.println("モジュール・システムは主に module-info.java で定義されます。");
        System.out.println("この章ではモジュール宣言(requires, exports)や jdeps、ServiceLoaderの仕様が問われます。");
    }
}
