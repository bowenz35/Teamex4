///////////////////////////////////////////////////////////////////////////////
// assignment name: p4
// Author: Tianyuan(Rainer) Yuan
// Partner:BOWEN ZHANG GRIFF ZHANG JICHEN ZHANG JUNGE ZHANG
// Email : tyuan22@wisc.edu
// due date: April 15th 2018
// CS Login: tyuan22
// Credits: none
// known bugs: none
//////////////////////////////////////////////////////////////////////////////

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * This class contains some utility helper methods
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */
public class WordProcessor {

    /**
     * Gets a Stream of words from the filepath.
     * 
     * The Stream should only contain trimmed, non-empty and UPPERCASE words.
     * 
     * @see <a href=
     *      "http://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html">java8
     *      stream blog</a>
     * 
     * @param filepath file path to the dictionary file
     * @return Stream<String> stream of words read from the filepath
     * @throws IOException exception resulting from accessing the filepath
     */
    public static Stream<String> getWordStream(String filepath) throws IOException {

        Stream<String> wStream = Files.lines(Paths.get(filepath));// create a stream objct through
                                                                  // filepaht
        wStream = wStream.filter(x -> x != null && !x.equals(""));// filter out the null and empty
                                                                  // content
        wStream = wStream.map(String::trim);// trim all the line
        wStream = wStream.map(String::toUpperCase);// make them to uppercase
        return wStream;
        /**
         * @see <a href=
         *      "https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html">java.nio.file.Files</a>
         * @see <a href=
         *      "https://docs.oracle.com/javase/8/docs/api/java/nio/file/Paths.html">java.nio.file.Paths</a>
         * @see <a href=
         *      "https://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.html">java.nio.file.Path</a>
         * @see <a href=
         *      "https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html">java.util.stream.Stream</a>
         * 
         *      class Files has a method lines() which accepts an interface Path object and produces
         *      a Stream<String> object via which one can read all the lines from a file as a
         *      Stream.
         * 
         *      class Paths has a method get() which accepts one or more strings (filepath), joins
         *      them if required and produces a interface Path object
         * 
         *      Combining these two methods: Files.lines(Paths.get(<string filepath>)) produces a
         *      Stream of lines read from the filepath
         * 
         *      Once this Stream of lines is available, you can use the powerful operations
         *      available for Stream objects to combine multiple pre-processing operations of each
         *      line in a single statement.
         * 
         *      Few of these features: 1. map( ) [changes a line to the result of the applied
         *      function. Mathematically, line = operation(line)] - trim all the lines - convert all
         *      the lines to UpperCase - example takes each of the lines one by one and apply the
         *      function toString on them as line.toString() and returns the Stream: streamOfLines =
         *      streamOfLines.map(String::toString)
         * 
         *      2. filter( ) [keeps only lines which satisfy the provided condition] - can be used
         *      to only keep non-empty lines and drop empty lines - example below removes all the
         *      lines from the Stream which do not equal the string "apple" and returns the Stream:
         *      streamOfLines = streamOfLines.filter(x -> x != "apple");
         * 
         *      3. collect( ) [collects all the lines into a java.util.List object] - can be used in
         *      the function which will invoke this method to convert Stream<String> of lines to
         *      List<String> of lines - example below collects all the elements of the Stream into a
         *      List and returns the List: List<String> listOfLines =
         *      streamOfLines.collect(Collectors::toList);
         * 
         *      Note: since map and filter return the updated Stream objects, they can chained
         *      together as: streamOfLines.map(...).filter(a -> ...).map(...) and so on
         */
    }

    /**
     * Adjacency between word1 and word2 is defined by: if the difference between word1 and word2 is
     * of 1 char replacement 1 char addition 1 char deletion then word1 and word2 are adjacent else
     * word1 and word2 are not adjacent
     * 
     * Note: if word1 is equal to word2, they are not adjacent
     * 
     * @param word1 first word
     * @param word2 second word
     * @return true if word1 and word2 are adjacent else false
     */
    public static boolean isAdjacent(String word1, String word2) {// when length difference is
                                                                  // larger than 1, return false
        int length1 = word1.length();
        int length2 = word2.length();
        String shorter = null;
        String longer = null;
        if (Math.abs(length1 - length2) >= 2) {
            return false;
        } else if (length1 != length2) {// when the length difference is 1 addition and deletion

            char[] lword;
            char[] sword;
            if (length1 > length2) {
                lword = word1.toCharArray();
                sword = word2.toCharArray();
            } else {
                lword = word2.toCharArray();
                sword = word1.toCharArray();
            }

            int count = 0;
            int l = 0, s = 0;// l is long array index, s is short array index
            while (l < lword.length - 1) {
                if (lword[l] == sword[s]) {
                    count++;
                    if (s < sword.length - 1)// handle the situation when there is a addtion of a
                                             // character inthe end
                        s++;
                }
                l++;
            }

            if (lword[l] == sword[s])// check for last one if the last one found eauqal in short
            {
                count++;
            }
            return count == sword.length;

        } else {// condition that word length is the same
            char[] charry1 = word1.toCharArray();
            char[] charry2 = word2.toCharArray();
            int count2 = 0;
            for (int i = 0; i < charry1.length; i++) {
                if (charry1[i] == charry2[i])
                    count2++;
            }
            return count2 == charry1.length - 1;

        }
    }

}
