package FixerIO_Example;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


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
public class SearchMaster {
    public static String success;
    static int i =1;
    private static Double Doubleinput;
    private static FixerHTTPManager manager = new FixerHTTPManager();
    private static Double input;
    private static String stringInput;
    private ArrayList<File> files;
    private static HashMap<String, File> fileMap = new HashMap<>();
    private static HashMap<String, File> resultsFileMap = new HashMap<>();

    public void search(){
        DecimalFormat formatter = new DecimalFormat("#,###");
        double startTimeWhole = System.currentTimeMillis();
        //create threads for each file being searched
        //if a word matches in a row, it tallies a sequence score
        // if it finds all words in the same sequence it gives bonus points? or does it give a seperate identify value

        System.out.println("SEARCHING");
        File f = new File("/home/hallo/Downloads/Reader");
        files = new ArrayList<File>(Arrays.asList(f.listFiles()));
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
        System.out.println("Number of files to search is " + files.size());
        System.out.println("!! Files must be in .txt format");
        System.out.println("_______________________________________________________");
        System.out.println("_______________________________________________________");
        System.out.println(" ");


        if (files.size() == names.size()){
            for (int x = 0; x < names.size(); x++){
                fileMap.put(names.get(x), files.get(x));
            }
        }
                                                                //On some longer arrays ==
        //String searchPhrase = "c z j d e";
        String searchPhrase = "cardse oliver william mckey Tooth oliver william mckey hall Fairy oliver william mckey Math Oliver William Mckey Gibber Jabber Tony Find survival of the fittest Something Here Marksman Oliver William Mckey Hall Her";
//        Curve Ball Oliver William Mckey Hall Test James Bond Is A Good Movie
        //maybe do list array of duplicates.
        //set a = {0, 3}
            //if set a.contains(y) its a streakmatch
        searchPhrase = searchPhrase.replace(".", "");
        searchPhrase = searchPhrase.replace(";", "");
        List<String> phraseToSearch = Arrays.asList(searchPhrase.split(" "));

        BufferedReader br = null;
        String line;
        int highTotalComparisons = 0;

        for (int a = 0; a < files.size(); a++) {
            long startTime = System.nanoTime();
            ArrayList <String> bestMatches = new ArrayList();
            int neWordMatchNumberofOccurnencecess = 0;
            int lineCount = 0;
            int wordCount = 0;
            int exactMatch = 0;
            int closeMatch = 0;
            int allMatch = 0;
            int comp = 0;
            int streak = 0;
            int highestStreak = 0;
            ArrayList<String> matchedPairs = new ArrayList<>();
            ArrayList<String> matchedSequencesFinal = new ArrayList<>();
            ArrayList<String> longestMatch = new ArrayList();
            ArrayList<String> wipStreakWord = new ArrayList();


            try {
                br = new BufferedReader(new FileReader(files.get(a)));
            } catch (FileNotFoundException e) {
                System.out.println("1");
                e.printStackTrace();
            }
            String st = null;
            while (true) {
                try {
                    if (!((st = br.readLine()) != null)) break;

                } catch (IOException e) {
                    System.out.println("2");
                    e.printStackTrace();
                }
                if (st != null && !st.isEmpty()){
                    String textLine = st.replace(".", "");
                    textLine = textLine.replace(";", "");
                    List <String> lineOfText = Arrays.asList(textLine.split(" "));
                    lineCount++;


                    for (int x = 0; x < lineOfText.size(); x++){
                            wordCount++;
                            int variation = (int) (lineOfText.get(x).length()*(20.0f/100.0f));
//                    if (streak != 0){
//                        x = streak;
//                    }
                        longestMatch = new ArrayList<>();
                            streak = 0;
                            for (int y = 0; y < phraseToSearch.size(); y++){
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

                                           if (streak == 1){//System.out.println(lineOfText.get(x) + " == " + phraseToSearch.get(y) + " // Line Of Text=" + x + " PhraseToSearch=" + y);
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

                                   if (wipStreakWord.size() > longestMatch.size()){
                                       longestMatch = new ArrayList<>(wipStreakWord);
                                       // this is triggered quite a bit becuase the minimum for a word streak is 2
                                       //System.out.println("====> Longest match is " + longestMatch);
                                   }
                                   wipStreakWord = new ArrayList<>();

                                   if (!longestMatch.isEmpty()) {
                                       matchedSequencesFinal.addAll(longestMatch);
                                   }
                               }


                            }
                        if (!longestMatch.isEmpty() && longestMatch.size() > 2){System.out.println("Actual ======================================================> Longest match is " + longestMatch);
                        //WORK TO DO HERE
                        //System.out.println(matchedPairs);
                        }
                    }

                }

            }
            if (bestMatches.size() < 2) {
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                System.out.println("File: " + files.get(a));
                System.out.println("Time Taken To Execute " + duration);
                System.out.println("Close Matchning Words " + formatter.format(neWordMatchNumberofOccurnencecess));
                System.out.println("Close Matches " + closeMatch);
                System.out.println("Exact Matches " + exactMatch);
                System.out.println("All Matches " + allMatch);
                System.out.println("Streak is " + highestStreak);
                System.out.println(" ");
                System.out.println(searchPhrase);
                System.out.println(bestMatches);
                System.out.println(" ");
                System.out.println("Lines Counted " + formatter.format(lineCount));
                System.out.println("Words Counted " + formatter.format(wordCount));
                System.out.println(formatter.format(comp) + " Comparisons Made");
                System.out.println("_______________________________________________________");
                System.out.println(" ");
                System.out.println("longest matches are is " + matchedSequencesFinal);
                System.out.println("longest match is " + longestMatch);

            }
        }

        double endTimeWhole = System.currentTimeMillis();
        double durationWhole = (endTimeWhole - startTimeWhole);
        double xdurationWhole = (durationWhole / 1000) % 60;
        System.out.println("========================================================");
        System.out.println("Search Through " + formatter.format(files.size()) + " Files");
        System.out.println("Complete Whole Time Taken To Execute " + xdurationWhole + " Seconds");
        System.out.println("Total Number Of Comparisons Made " + formatter.format(highTotalComparisons));
        System.out.println("========================================================");



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
