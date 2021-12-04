package edu.neu.coe.info6205.sort.linearithmic;

import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.Sort;
import edu.neu.coe.info6205.sort.SortWithHelper;
import edu.neu.coe.info6205.sort.counting.MSDStringSort;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import static edu.neu.coe.info6205.util.PinyinUtil.getPinyin;
import static edu.neu.coe.info6205.util.PinyinUtil.readAllChinese;

public class QuickSort_DualPivot<X extends Comparable<X>> extends QuickSort<X> {

    public static final String DESCRIPTION = "QuickSort dual pivot";

    public QuickSort_DualPivot(String description, int N, Config config) {
        super(description, N, config);
        setPartitioner(createPartitioner());
    }

    /**
     * Constructor for QuickSort_3way
     *
     * @param helper an explicit instance of Helper to be used.
     */
    public QuickSort_DualPivot(Helper<X> helper) {
        super(helper);
        setPartitioner(createPartitioner());
    }

    /**
     * Constructor for QuickSort_3way
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public QuickSort_DualPivot(int N, Config config) {
        this(DESCRIPTION, N, config);
    }


    @Override
    public Partitioner<X> createPartitioner() {
        return new Partitioner_DualPivot(getHelper());
    }

    public class Partitioner_DualPivot implements Partitioner<X> {

        public Partitioner_DualPivot(Helper<X> helper) {
            this.helper = helper;
        }

        /**
         * Method to partition the given partition into smaller partitions.
         *
         * @param partition the partition to divide up.
         * @return an array of partitions, whose length depends on the sorting method being used.
         */
        public List<Partition<X>> partition(Partition<X> partition) {
            final X[] xs = partition.xs;
            final int lo = partition.from;
            final int hi = partition.to - 1;
            helper.swapConditional(xs, lo, hi);
            int lt = lo + 1;
            int gt = hi - 1;
            int i = lt;
            // NOTE: we are trying to avoid checking on instrumented for every time in the inner loop for performance reasons (probably a silly idea).
            // NOTE: if we were using Scala, it would be easy to set up a comparer function and a swapper function. With java, it's possible but much messier.
            if (helper.instrumented()) {
                while (i <= gt) {
                    if (helper.compare(xs, i, lo) < 0) helper.swap(xs, lt++, i++);
                    else if (helper.compare(xs, i, hi) > 0) helper.swap(xs, i, gt--);
                    else i++;
                }
                helper.swap(xs, lo, --lt);
                helper.swap(xs, hi, ++gt);
            } else {
                while (i <= gt) {
                    X x = xs[i];
                    if (x.compareTo(xs[lo]) < 0) swap(xs, lt++, i++);
                    else if (x.compareTo(xs[hi]) > 0) swap(xs, i, gt--);
                    else i++;
                }
                swap(xs, lo, --lt);
                swap(xs, hi, ++gt);
            }

            List<Partition<X>> partitions = new ArrayList<>();
            partitions.add(new Partition<>(xs, lo, lt));
            partitions.add(new Partition<>(xs, lt + 1, gt));
            partitions.add(new Partition<>(xs, gt + 1, hi + 1));
            return partitions;
        }

        // CONSIDER invoke swap in BaseHelper.
        private void swap(X[] ys, int i, int j) {
            X temp = ys[i];
            ys[i] = ys[j];
            ys[j] = temp;
        }

        private final Helper<X> helper;
    }

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

        Config config = Config.load(rs.getClass());
        QuickSort<String> quickSort_dualPivot = new QuickSort_DualPivot<String>(DESCRIPTION, rs.length,config);
        quickSort_dualPivot.sort(rs,false);

//        Benchmark_Timer<String[]> bm_QCDStringSort = new Benchmark_Timer<String[]>("QCD String Sort", f -> {
//            quickSort_dualPivot.sort(rs,0,rs.length-1);
//        });
//        double time_QCDStringSort = bm_QCDStringSort.runFromSupplier(() -> rs, 1);
//        System.out.println("QCD String Sort -- average time in milliseconds: " + time_QCDStringSort);

        for (String x : rs)
//            System.out.println(x);
            System.out.println(map.get(x));
    }

}

