// 第8章: アノテーション
import java.lang.annotation.*;

public class Chapter08_Annotations {
    
    // カスタムアノテーションの定義
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface TestMethod {
        String value() default "Default Test";
    }

    @TestMethod("Run Example")
    public void execute() {
        System.out.println("Executing...");
    }

    public static void main(String[] args) throws Exception {
        // リフレクションを用いたアノテーションの取得
        var method = Chapter08_Annotations.class.getMethod("execute");
        if (method.isAnnotationPresent(TestMethod.class)) {
            TestMethod annotation = method.getAnnotation(TestMethod.class);
            System.out.println("Annotation Value: " + annotation.value());
        }
    }
}
