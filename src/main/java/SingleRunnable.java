import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class SingleRunnable implements Runnable {
  private int numOfRequests;
  private AtomicInteger count;

  public SingleRunnable(int numOfRequests, AtomicInteger count) {
    this.numOfRequests = numOfRequests;
    this.count = count;
  }

  @Override
  public void run() {
    for (int i = 0; i < numOfRequests; i++) {
      try {
        doPost();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void doPost() throws Exception
  {
    DefaultHttpClient httpClient = new DefaultHttpClient();
    try
    {
      Random random = new Random();
      final int N = random.nextInt(10000);
      int r = (2 * N + 1) % 10000;
      String uri = "http://54.175.212.13:8080/prime/" + r;
      HttpGet getRequest = new HttpGet(uri);

      //Set the API media type in http accept header
      getRequest.addHeader("accept", "application/xml");

      //Send the request; It will immediately return the response in HttpResponse object
      HttpResponse response = httpClient.execute(getRequest);

      //verify the valid error code first
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode != 200)
      {
        count.incrementAndGet();
      }

    }
    finally
    {
      //Important: Close the connect
      httpClient.getConnectionManager().shutdown();
    }
  }


}
