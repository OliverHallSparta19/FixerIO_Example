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
        System.out.println("_______________________________________________________");
        System.out.println("_______________________________________________________");
        System.out.println(" ");


        if (files.size() == names.size()){
            for (int x = 0; x < names.size(); x++){
                fileMap.put(names.get(x), files.get(x));
            }
        }

        //String searchPhrase = "c z j d e";
        String searchPhrase = "Curve Ball Oliver William Mckey Hall Test James Bond Is A Good Movie";
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
            int comp = 0;
            int streak = 1;
            int highestStreak = 0;
            int searchWordMatchPosition = 0;
            int position = 0;

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
                    int matchRequirmentBuffer = 2;

                    int existingMatched = 2;
                    lineCount++;

                    boolean sreakMatch = false;
                    boolean repeatSearch = true;
                    for (int i = 0; i < lineOfText.size(); i++){
                        wordCount++;
                        List<String> wordsToCompareTo = Arrays.asList(lineOfText.get(i).split(" "));

                        for (int x = 0; x < wordsToCompareTo.size(); x++){
                            sreakMatch = false;
                            repeatSearch = true;
                            //System.out.println("TEST " + x);
//                            System.out.println("WORD LOOKS LIKE " + wordsToCompareTo.get(x));

                            for (int y = 0; y < phraseToSearch.size() && sreakMatch != true && repeatSearch == true; y++){
                                int calculatedInt = minDistance(phraseToSearch.get(y).toLowerCase(), wordsToCompareTo.get(x).toLowerCase());
                                comp++;
                                highTotalComparisons++;
                                String searchWord = phraseToSearch.get(y);
                                String textWord = wordsToCompareTo.get(x);
//                                System.out.println("Word 1 " + wordsToSearch.get(y) + " Word 2 " + wordsToCompareTo.get(x));
                                if (calculatedInt < 1) {

                                    //if it is a expected streak? just check again the expected postion first?
                                        //Number the postion of the first matches?
                                            //Do i need an array of arrawys for matching words and postions
                                                //If i have two sequence it could be two of the ways, but if a charecter is addded then 1 of those sequences could be elimianted as a possibilty becuase it does not contain the extra charecter/ search word postion number

                                    neWordMatchNumberofOccurnencecess++;
//                                    System.out.println("MATCH MATCH MATCH " + phraseToSearch.get(y));
                                    if (searchWordMatchPosition == y && searchWordMatchPosition < phraseToSearch.size() && position != y){
                                        sreakMatch = true;
                                        position = y;
                                    }
                                    repeatSearch = false;

                                    searchWordMatchPosition = y +1;
                                    //System.out.println("LOOK " + y);
                                } else {
                                    sreakMatch = false;
                                }
                            }
// if the next word isnt a match reset the streak?

                            //for streak to work, the match
                            // the next match must be next after the previous match

                            //if first streak, get postion of word in search sentance
                                //take position of its first occurance

                            //if streak number matches the position of the orignal search sentace?
                            if (sreakMatch == true){
                                streak++;
                                if (streak > highestStreak){
                                    highestStreak = streak;
//                                    System.out.println("======================> STREAK of " + streak + " Words");
                                    bestMatches = new ArrayList();
                                    for (int d = (position + 1 - streak); d < position + 1 ; d++) {
                                        bestMatches.add(phraseToSearch.get(d));
                                    }
                                }
                            } else {
                                //streak = 0;
                            }
                        }
                    }
                }
            }
            if (bestMatches.size() > 2) {
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                System.out.println("File: " + files.get(a));
                System.out.println("Time Taken To Execute " + duration);
                System.out.println("Close Matchning Words " + formatter.format(neWordMatchNumberofOccurnencecess));
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
