package edu.neu.coe.info6205.sort.counting;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.util.Config;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MSDStringSortTest {

    String[] input = "she sells seashells by the seashore the shells she sells are surely seashells".split(" ");
    String[] expected = "are by seashells seashells seashore sells sells she she shells surely the the".split(" ");

    @Test
    public void sort() {
        MSDStringSort.sort(input);
        System.out.println(Arrays.toString(input));
        assertArrayEquals(expected, input);
    }

    @Test
    public void sort1() throws IOException {
        int n = 1000;
        final Helper<String> helper = new BaseHelper<>("test", n, 1L, Config.load(MSDStringSortTest.class));
        helper.init(n);
        String[] words = getWords("/3000-common-words.txt", MSDStringSortTest::lineAsList);
        final String[] xs = helper.random(String.class, r -> words[r.nextInt(words.length)]);
        assertEquals(n, xs.length);
        MSDStringSort.sort(xs);
        assertEquals("African-American", xs[0]);
        assertEquals("Palestinian", xs[16]);
    }

    //unit test by group 9
    @Test
    public void sort2() throws IOException {
        int n = 16;
        Map map;
        String [] rs = new String[n];
        List<String> sortedChinese = new ArrayList<>();
        String[] words =  {"刘持平", "洪文胜", "樊辉辉", "苏会敏", "高民政", "曹玉德", "袁继鹏", "舒冬梅", "杨腊香", "许凤山", "王广风", "黄锡鸿", "罗庆富", "顾芳芳", "宋雪光", "王诗卉"};
        assertEquals(n, words.length);
        map = MSDStringSort.trans(words,rs);
        for (String x : rs) {
            String c = (String) map.get(x);
            sortedChinese.add(c);
        }
        rs = sortedChinese.toArray(rs);
        String[] sorted = {"曹玉德", "樊辉辉", "高民政", "顾芳芳", "洪文胜", "黄锡鸿", "刘持平", "罗庆富", "舒冬梅", "宋雪光", "苏会敏", "王广风", "王诗卉", "许凤山", "杨腊香", "袁继鹏"};
        assertArrayEquals(sorted,rs);
        System.out.println("words: " + Arrays.toString(words));
        System.out.println("sorted  " + Arrays.toString(sorted));
        System.out.println("output  " + Arrays.toString(rs));
    }

    @Test
    public void testGetWords1() {
        String[] words = getWords("/3000-common-words.txt", MSDStringSortTest::lineAsList);
        assertEquals(2998, words.length);
    }

    @Test
    public void testGetWords2() {
        String[] words = getWords("/3000 common words.txt", MSDStringSortTest::lineAsList);
        assertEquals(2998, words.length);
    }

    /**
     * Create a string representing an integer, with commas to separate thousands.
     *
     * @param x the integer.
     * @return a String representing the number with commas.
     */
    public static String formatWhole(final int x) {
        return String.format("%,d", x);
    }

    /**
     * Method to open a resource relative to this class and from the corresponding File, get an array of Strings.
     *
     * @param resource           the URL of the resource containing the Strings required.
     * @param stringListFunction a function which takes a String and splits into a List of Strings.
     * @return an array of Strings.
     */
    static String[] getWords(final String resource, final Function<String, List<String>> stringListFunction) {
        Class<?> clazz = MSDStringSortTest.class;
        try {
            final File file = new File(Objects.requireNonNull(clazz.getResource(resource)).toURI());
            final String[] result = getWordArray(file, stringListFunction, 2);
            System.out.println("getWords: testing with " + formatWhole(result.length) + " unique words: from " + file);
            return result;
        } catch (final URISyntaxException | NullPointerException e) {
            System.out.println("Cannot find resource: " + resource + "  relative to class: " + clazz);
            return new String[0];
        }
    }

    private static List<String> getWordList(final FileReader fr, final Function<String, List<String>> stringListFunction, final int minLength) {
        final List<String> words = new ArrayList<>();
        for (final Object line : new BufferedReader(fr).lines().toArray())
            words.addAll(stringListFunction.apply((String) line));
        return words.stream().distinct().filter(s -> s.length() >= minLength).collect(Collectors.toList());
    }


    /**
     * Method to read given file and return a String[] of its content.
     *
     * @param file               the file to read.
     * @param stringListFunction a function which takes a String and splits into a List of Strings.
     * @param minLength          the minimum acceptable length for a word.
     * @return an array of Strings.
     */
    static String[] getWordArray(final File file, final Function<String, List<String>> stringListFunction, final int minLength) {
        try (final FileReader fr = new FileReader(file)) {
            return getWordList(fr, stringListFunction, minLength).toArray(new String[0]);
        } catch (final IOException e) {
            System.out.println("Cannot open file: " + file);
            return new String[0];
        }
    }

    static List<String> lineAsList(final String line) {
        final List<String> words = new ArrayList<>();
        words.add(line);
        return words;
    }
}