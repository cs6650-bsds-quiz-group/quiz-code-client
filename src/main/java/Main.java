import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
@SuppressWarnings("unchecked")
public class Main {
  private static final int TOTAL_REQUEST = 100000;

  public static void main(String[] args){
    // all input is valid
    int numOfThreads = Integer.parseInt(args[0]);
    int numOfRequestsPerThread = TOTAL_REQUEST / numOfThreads;
    long startTime = System.currentTimeMillis();
    Thread[] threads = new Thread[numOfThreads];
    AtomicInteger count = new AtomicInteger(0);

    for (int i = 0; i < numOfThreads; i++) {
      Runnable runnable = new SingleRunnable(numOfRequestsPerThread, count);
      threads[i] = new Thread(runnable);
      threads[i].start();
    }

    try {
      for (int i = 0;i < numOfThreads; i++) {
        threads[i].join();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    long endTime = System.currentTimeMillis();
    long throughput = (long)TOTAL_REQUEST * 1000 / (endTime-startTime) ;
    int notPrime = count.get();
    int prime = TOTAL_REQUEST - notPrime;
    System.out.println("Total number of requests: " + TOTAL_REQUEST);
    System.out.println("Wall time: " + (endTime - startTime) + " milliseconds");
    System.out.println("Not prime numbers: " + count.toString());
    System.out.println("The number of prime numbers: " + prime);
    System.out.println("Throughput: " + throughput + "requests/second");

  }
}
