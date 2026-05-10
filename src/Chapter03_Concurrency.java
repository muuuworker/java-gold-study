// 第3章: 並列処理
import java.util.concurrent.*;
import java.util.List;

public class Chapter03_Concurrency {
    public static void main(String[] args) {
        // ExecutorService を使用した並列処理
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        Runnable task1 = () -> System.out.println("Task 1 by " + Thread.currentThread().getName());
        Callable<String> task2 = () -> "Task 2 Result by " + Thread.currentThread().getName();
        
        executor.submit(task1);
        Future<String> future = executor.submit(task2);
        
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        // スレッドセーフなコレクション
        List<String> safeList = new CopyOnWriteArrayList<>();
        safeList.add("Concurrent");
        
        ConcurrentMap<String, Integer> safeMap = new ConcurrentHashMap<>();
        safeMap.put("Key", 1);
    }
    
    // synchronized を使用したスレッド制御の例
    private int count = 0;
    public synchronized void increment() {
        count++;
    }
}
