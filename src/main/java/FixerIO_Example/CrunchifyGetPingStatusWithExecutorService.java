package FixerIO_Example;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrunchifyGetPingStatusWithExecutorService {
    private static final int MYTHREADS = 30;
    private static ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);

    public static int iteration = 0;

    public static void main(String args[]) throws Exception {
        System.out.println("===================================");
        double startTime = System.currentTimeMillis();
//        ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
        String[] hostList = { "http://yahoo.com", "http://www.ebay.com", "http://google.com",
                 "http://bing.com/", "http://techcrunch.com/", "http://mashable.com/", "http://bbc.com/", "http://asdafsdafsdfdf.com/", "http://facebook.com/", "http://amazon.com/" };

        for (int i = 0; i < hostList.length; i++) {

            String url = hostList[i];
            Runnable worker = new MyRunnable(url);
            executor.execute(worker);
        }
        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {

        }
        System.out.println("Finished all threads");
        double endTime = System.currentTimeMillis();
        double duration = (endTime - startTime);
        double xdurationWhole = (duration / 1000) % 60;
        System.out.println("Time Taken = " + xdurationWhole + " Seconds");
    }

    public static class MyRunnable implements Runnable {
        private final String url;

        MyRunnable(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            iteration++;
            System.out.println("Thread: " + iteration + " Started");


            String result = "";
            int code = 200;
            try {
                URL siteURL = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(3000);
                connection.connect();

                code = connection.getResponseCode();
                if (code == 200) {
                    result = "-> Green <-\t" + "Code: " + code;
                } else {
                    result = "-> Yellow <-\t" + "Code: " + code;
                }
            } catch (Exception e) {
                result = "-> Red <-\t" + "Wrong domain - Exception: " + e.getMessage();
            }
            System.out.println(url + "\t\tStatus:" + result + " // Iteration: " + iteration);
        }
    }
}