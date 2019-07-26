package FixerIO_Example;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*


====> KNOWN ISSUES

All character in search phrase must be unique, System will compare to the first instance of the word in the phrase, not its correct expected position

Multiple Streak sentances in the search phrase messes it up
    Some reason ball was added to the result streak list
        the second streak sentance will made into an array that is alot longer than necessary becuase it is adding with the exisitng streak number
            so a result that should show 2 streak sentances is messed up

Streak matching can be greatly improved
    And then there can be some good refactoring, not as much code is needed

 */
public class ThreadSearching {
    public static String searchPhrase = "Oliver William Hall";
    private static final int MYTHREADS = 7;

    public static File f;
    public static ArrayList<File> xFiles;
    private static boolean allThreadsStarted = false;

    public static int highTotalComparisons;

    private static ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);

    public static void main(String args[]) throws Exception {
        int availableProcesses = Thread.activeCount();
        System.out.println("===> " + availableProcesses + " Threads Currently Running, Starting " + MYTHREADS + " New Threads" );
        DecimalFormat formatter = new DecimalFormat("#,###");
        System.out.println("===================================");
        double startTime = System.currentTimeMillis();
        f = new File("/home/hallo/Downloads/Reader");
//        ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));
        xFiles = new ArrayList<File>(Arrays.asList(f.listFiles()));


        int fileSize = xFiles.size();
        System.out.println("FILES TO SEARCH = " + fileSize);
        int leftOverFiles = fileSize;

        Double fileSize2 = Double.valueOf(xFiles.size());
        Double mythreads2 = Double.valueOf(MYTHREADS);
        int packSize = fileSize/MYTHREADS;
        Double packsize2 = fileSize2/mythreads2;



        System.out.println(MYTHREADS + " Threads with an average of " + packSize + " files each to search");
        int startpostion = 1;
        int endPostion;

        int leftOverThreads = MYTHREADS;

        int tally = 0;
        for (int i = 1; i <= MYTHREADS; i++) {
            int xpackSize = leftOverFiles/leftOverThreads;
            Double xpacksize2 = leftOverFiles/Double.valueOf(leftOverThreads);
            double xdif = xpacksize2 - xpackSize;
            if (xdif != 0.00){
                packSize = xpackSize + 1;
                leftOverFiles = leftOverFiles - packSize;
                leftOverThreads--;
            } else {
                packSize = xpackSize;
            }
//            System.out.println("going for " + packSize);
            tally = tally + packSize;
//            System.out.println("Tally is " + tally);


            endPostion = startpostion + packSize - 1;
//            System.out.println("Pack " + i + " is from " + startpostion + " to " + endPostion);
            Runnable worker = new ThreadSearching.MyRunnable(startpostion, endPostion, i);


            startpostion = endPostion + 1;

//            startpostion = endPostion + 1;
            executor.execute(worker);
        }
        allThreadsStarted = true;
        availableProcesses = Thread.activeCount();
//        System.out.println("===> " + availableProcesses + " Threads Running");
//
//        System.out.println("===================================");
        executor.shutdown();
        while (!executor.isTerminated()) {

        }
        System.out.println("Finished all threads");
        double endTime = System.currentTimeMillis();
        double duration = (endTime - startTime);
        double xdurationWhole = (duration / 1000) % 60;
        System.out.println("Time Taken = " + xdurationWhole + " Seconds");
        System.out.println("Total Number Of Comparisons Made " + formatter.format(highTotalComparisons));
    }

    public static class MyRunnable implements Runnable {
        private int startNumber;
        private int endNumber;
        private int threadNumber;

        MyRunnable(Integer sN, Integer eN, Integer threadNumber) {
            this.startNumber = sN;
            this.endNumber = eN;
            this.threadNumber = threadNumber;
        }

//////////////////////////////////////////////////////////

        @Override
        public void run() {
            DecimalFormat formatter = new DecimalFormat("#,###");

//            String searchPhrase = "Oliver William Hall";
            searchPhrase = searchPhrase.replace(".", "");
            searchPhrase = searchPhrase.replace(";", "");
            List<String> phraseToSearch = Arrays.asList(searchPhrase.split(" "));

            for (int a = this.startNumber - 1, b = this.endNumber - 1; a <= b; a++) {

            BufferedReader br = null;
            boolean printResult = false;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            long startTime = System.nanoTime();
            int lineCount = 0;
            int wordCount = 0;
            int allMatch = 0;
            int comp = 0;
            int streak = 0;
            ArrayList<String> matchedPairs = new ArrayList<>();
            ArrayList<String> matchedSequencesFinal = new ArrayList<>();
            ArrayList<String> longestMatch = new ArrayList();
            ArrayList<String> wipStreakWord = new ArrayList();

            try {
                br = new BufferedReader(new FileReader(xFiles.get(a)));
            } catch (FileNotFoundException e) {
                System.out.println("1");
                e.printStackTrace();
                System.out.println("ERROR");
            }
            String st = null;
            while (true) {
                try {
                    if (!((st = br.readLine()) != null)) break;
                } catch (IOException e) {
                    System.out.println("2");
                    e.printStackTrace();
                }
                if (st != null && !st.isEmpty()) {
                    String textLine = st.replace(".", "");
                    textLine = textLine.replace(";", "");
                    List<String> lineOfText = Arrays.asList(textLine.split(" "));
                    lineCount++;
                    for (int x = 0; x < lineOfText.size(); x++) {
                        wordCount++;
                        int variation = (int) (lineOfText.get(x).length() * (20.0f / 100.0f));
                        longestMatch = new ArrayList<>();
                        streak = 0;
                        for (int y = 0; y < phraseToSearch.size(); y++) {
                            int calculatedInt = minDistance(phraseToSearch.get(y).toLowerCase(), lineOfText.get(x).toLowerCase());
                            comp++;
                            highTotalComparisons++;
                            String searchWord = phraseToSearch.get(y);
                            String textWord = lineOfText.get(x);
                            if (calculatedInt <= variation) {
                                allMatch++;

                                boolean loop = true;
                                for (int w = y + 1, z = x + 1; w < phraseToSearch.size() && z < lineOfText.size() && loop == true; w++, z++) {
                                    int variation2 = (int) (lineOfText.get(z).length() * (20.0f / 100.0f));
                                    String word1 = phraseToSearch.get(w);
                                    String word2 = lineOfText.get(z);
                                    int dif2 = minDistance(phraseToSearch.get(w).toLowerCase(), lineOfText.get(z).toLowerCase());
                                    if (dif2 <= variation2 && !matchedPairs.contains(z + " " + w)) {
                                        streak++;
                                        allMatch++;

                                        if (streak == 1) {//System.out.println(lineOfText.get(x) + " == " + phraseToSearch.get(y) + " // Line Of Text=" + x + " PhraseToSearch=" + y);
//                                           x = x -1;
                                            matchedPairs.add(x + " " + y);
                                            wipStreakWord.add(lineOfText.get(x));
                                        }
//                                               System.out.println(lineOfText.get(z) + " = " + phraseToSearch.get(w) + " // Line Of Text=" + z + " PhraseToSearch=" + w);
                                        matchedPairs.add(z + " " + w);
                                        wipStreakWord.add(lineOfText.get(z));

                                        //y = y + 1;
//                                           x = x + 1;
                                    } else {
                                        loop = false;
                                        streak = 0;
                                    }
                                }

                                if (wipStreakWord.size() > longestMatch.size()) {
                                    longestMatch = new ArrayList<>(wipStreakWord);
                                    // this is triggered quite a bit becuase the minimum for a word streak is 2
                                    //System.out.println("====> Longest match is " + longestMatch);
                                }
                                wipStreakWord = new ArrayList<>();

//                                   if (!longestMatch.isEmpty()) {
//                                       matchedSequencesFinal.addAll(longestMatch);
//                                   }
                            }


                        }
                        if (!longestMatch.isEmpty() && longestMatch.size() > 2) {
                            //System.out.println("FOUND ======================================================> Longest match is " + longestMatch);
                            printResult = true;
//                            if (!matchedSequencesFinal.isEmpty()){matchedSequencesFinal.add(longestMatch.toString());}
                            matchedSequencesFinal.add(longestMatch.toString().replace(",", ""));
//                            matchedSequencesFinal.addAll(longestMatch);
                            //WORK TO DO HERE
                            //System.out.println(matchedPairs);fds
                        }
                    }

                }

            }
            if (printResult == true) {
                printResult = false;
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                System.out.println(" ");
                System.out.println("File: " + xFiles.get(a));
//                System.out.println("Time Taken To Execute " + duration);
//                System.out.println("Close Matchning Words " + formatter.format(neWordMatchNumberofOccurnencecess));
//                System.out.println("Close Matches " + closeMatch);
//                System.out.println("Exact Matches " + exactMatch);
                System.out.println(allMatch + " Matching Words");
                //System.out.println("Streak is " + highestStreak);
                System.out.println(" ");
//                System.out.println(searchPhrase);
//                System.out.println(bestMatches);
//                System.out.println(" ");
                System.out.println("Lines Counted " + formatter.format(lineCount));
                System.out.println("Words Counted " + formatter.format(wordCount));
                System.out.println(formatter.format(comp) + " Comparisons Made");
                System.out.println(" ");
                System.out.println("Found Phrases are " + matchedSequencesFinal);
                System.out.println(">> found on thread " + this.threadNumber);
                System.out.println(" ");
                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//                System.out.println(" ");
//                System.out.println(matchedPairs);

            }

//END OF FILE SEARCH OPERATION////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
//            System.out.println("><><><><><><><><><><><><><><><><><><><><><><><><><><><");
//            System.out.println(" >> Thread " + this.threadNumber + " has finished << ");
//            System.out.println("><><><><><><><><><><><><><><><><><><><><><><><><><><><");
            if (allThreadsStarted == false) {
                allThreadsStarted = true;
                System.out.println("not all threads finished starting");
            }
        }

        public static int minDistance(String word1, String word2) {
            int len1 = word1.length();
            int len2 = word2.length();
            int[][] dp = new int[len1 + 1][len2 + 1];
            for (int i = 0; i <= len1; i++) {
                dp[i][0] = i;
            }
            for (int j = 0; j <= len2; j++) {
                dp[0][j] = j;
            }
            for (int i = 0; i < len1; i++) {
                char c1 = word1.charAt(i);
                for (int j = 0; j < len2; j++) {
                    char c2 = word2.charAt(j);

                    //if last two chars equal
                    if (c1 == c2) {
                        //update dp value for +1 length
                        dp[i + 1][j + 1] = dp[i][j];
                    } else {
                        int replace = dp[i][j] + 1;
                        int insert = dp[i][j + 1] + 1;
                        int delete = dp[i + 1][j] + 1;

                        int min = replace > insert ? insert : replace;
                        min = delete > min ? min : delete;
                        dp[i + 1][j + 1] = min;
                    }
                }
            }
            return dp[len1][len2];
        }
    }
}

