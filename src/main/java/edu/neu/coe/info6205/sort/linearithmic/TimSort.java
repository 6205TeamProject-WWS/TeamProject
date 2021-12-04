/*
  (c) Copyright 2018, 2019 Phasmid Software
 */
package edu.neu.coe.info6205.sort.linearithmic;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.SortWithHelper;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.Config;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

import static edu.neu.coe.info6205.util.PinyinUtil.getPinyin;
import static edu.neu.coe.info6205.util.PinyinUtil.readAllChinese;

/**
 * Sorter which delegates to Timsort via Arrays.sort.
 *
 * @param <X>
 */
public class TimSort<X extends Comparable<X>> extends SortWithHelper<X> {

    private static Map<String, String> map = new IdentityHashMap<String, String>();

    public static void trans(String[] s,String[] rs){

        for (int i = 0; i < s.length; i++) {
            rs[i] = getPinyin(s[i], " ");
        }

        for (int i = 0; i < s.length; i++) {
            map.put(rs[i], s[i]);
        }

        TimSort timSort = null;
        try {
            timSort = new TimSort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        timSort.sort(rs,0,rs.length);

        for (String x : rs)
            map.get(x);

    }
    /**
     * Constructor for TimSort
     *
     * @param helper an explicit instance of Helper to be used.
     */
    public TimSort(Helper<X> helper) {
        super(helper);
    }

    /**
     * Constructor for TimSort
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public TimSort(int N, Config config) {
        super(DESCRIPTION, N, config);
    }

    public TimSort() throws IOException {
        this(new BaseHelper<>(DESCRIPTION, Config.load(TimSort.class)));
    }

    public void sort(X[] xs, int from, int to) {
        Arrays.sort(xs, from, to);
    }

    public static final String DESCRIPTION = "Timsort";

    public static void main(String[] args) throws IOException {
        String[] s = readAllChinese();
        String[] rs = new String[s.length];

        TimSort timSort = new TimSort();
//        timSort.sort(rs,0,rs.length);

        Benchmark_Timer<String[]> bm_TimStringSort = new Benchmark_Timer<String[]>("Tim String Sort", f -> {
            timSort.trans(s,rs);
        });
        double time_timStringSort = bm_TimStringSort.runFromSupplier(() -> s, 10);
        System.out.println("Tim String Sort -- average time in milliseconds: " + time_timStringSort);

        int num = 0;
        List<String> sortedChinese = new ArrayList<>();
        for (String sortedPy : rs) {
            String c = map.get(sortedPy);
            sortedChinese.add(c);
            num++;
            if(num>=1000)
                break;
        }

        try {
            FileOutputStream fis = new FileOutputStream("./src/outputChineseTim.txt");
            OutputStreamWriter isr = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(isr);
            for (String i : sortedChinese) {
                bw.write(i);
                bw.newLine();
                bw.flush();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

