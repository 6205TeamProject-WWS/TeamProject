/*
  (c) Copyright 2018, 2019 Phasmid Software
 */
package edu.neu.coe.info6205.sort.linearithmic;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.SortWithHelper;
import edu.neu.coe.info6205.sort.counting.MSDStringSort;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.Config;

import java.io.IOException;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;

import static edu.neu.coe.info6205.util.PinyinUtil.getPinyin;
import static edu.neu.coe.info6205.util.PinyinUtil.readAllChinese;

/**
 * Sorter which delegates to Timsort via Arrays.sort.
 *
 * @param <X>
 */
public class TimSort<X extends Comparable<X>> extends SortWithHelper<X> {

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
        for (int i = 0; i < s.length; i++) {
            rs[i] = getPinyin(s[i], " ");
        }

        Map<String, String> map = new IdentityHashMap<String, String>();
        for (int i = 0; i < s.length; i++) {
            map.put(rs[i], s[i]);
        }

        TimSort timSort = new TimSort();
//        timSort.sort(rs,0,rs.length);

        Benchmark_Timer<String[]> bm_TimStringSort = new Benchmark_Timer<String[]>("Tim String Sort", f -> {
            timSort.sort(rs, 0, rs.length);
        });
        double time_timStringSort = bm_TimStringSort.runFromSupplier(() -> rs, 10);
        System.out.println("Tim String Sort -- average time in milliseconds: " + time_timStringSort);

        for (String x : rs)
//            System.out.println(x);
            System.out.println(map.get(x));
    }
}

