package edu.neu.coe.info6205.sort.counting;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.util.Config;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

//@RunWith(Cucumber.class)
//@CucumberOptions(
//        features = "classpath:edu/neu/coe/info6205/sort/counting/lsdsort/LSDStringSort.feature",
//        glue = "classpath:edu.neu.coe.info6205.sort.counting.LSDStringSortStepDefinition",
//        plugin = "html:target/LSDStringSort-report", strict = true
//)


public class LSDStringSortTestRunner {

    //unit test by group 9
    @Test
    public void sort1() throws IOException {
        int n = 16;
        Map map;
        String [] rs = new String[n];
        List<String> sortedChinese = new ArrayList<>();
        String[] words =  {"刘持平", "洪文胜", "樊辉辉", "苏会敏", "高民政", "曹玉德", "袁继鹏", "舒冬梅", "杨腊香", "许凤山", "王广风", "黄锡鸿", "罗庆富", "顾芳芳", "宋雪光", "王诗卉"};
        System.out.println("words:  " + Arrays.toString(words));
        assertEquals(n, words.length);
        map = LSDStringSort.trans(words,rs);
        for (String x : rs) {
            String c = (String) map.get(x);
            sortedChinese.add(c);
        }
        rs = sortedChinese.toArray(rs);
        String[] sorted = {"曹玉德", "樊辉辉", "高民政", "顾芳芳", "洪文胜", "黄锡鸿", "刘持平", "罗庆富", "舒冬梅", "宋雪光", "苏会敏", "王广风", "王诗卉", "许凤山", "杨腊香", "袁继鹏"};
        System.out.println("sorted  " + Arrays.toString(sorted));
        System.out.println("output  " + Arrays.toString(rs));
        assertArrayEquals(sorted,rs);
    }

    //unit test by group 9
    // this test for very similar pinyin order
    @Test
    public void sort2() throws IOException {
        int n = 18;
        Map map;
        String [] rs = new String[n];
        List<String> sortedChinese = new ArrayList<>();
        String[] words =  {"阿斌", "阿冰", "阿弛", "阿兵", "阿朝", "阿冰冰", "阿婵", "阿安", "阿超", "阿琛", "阿滨", "阿臣", "阿晨", "阿辰", "阿称", "阿诚", "阿彬", "阿澄"};
        assertEquals(n, words.length);
        map = LSDStringSort.trans(words,rs);
        for (String x : rs) {
            String c = (String) map.get(x);
            sortedChinese.add(c);
        }
        rs = sortedChinese.toArray(rs);
        String[] sorted = {"阿安", "阿斌", "阿滨", "阿彬", "阿冰", "阿兵", "阿冰冰", "阿婵", "阿超", "阿朝", "阿琛", "阿臣", "阿晨", "阿辰", "阿称", "阿诚", "阿澄", "阿弛"};
        assertArrayEquals(sorted,rs);
        System.out.println("words:  " + Arrays.toString(words));
        System.out.println("sorted  " + Arrays.toString(sorted));
        System.out.println("output  " + Arrays.toString(rs));
    }
}
