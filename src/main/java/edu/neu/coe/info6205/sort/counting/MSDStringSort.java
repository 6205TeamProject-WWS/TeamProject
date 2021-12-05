package edu.neu.coe.info6205.sort.counting;

import edu.neu.coe.info6205.sort.elementary.InsertionSort;
import edu.neu.coe.info6205.sort.elementary.InsertionSortMSD;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.PinyinUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import static edu.neu.coe.info6205.util.PinyinUtil.getPinyin;
import static edu.neu.coe.info6205.util.PinyinUtil.readAllChinese;

/**
 * Class to implement Most significant digit string sort (a radix sort).
 */
public class MSDStringSort {

    private static Map<String, String> map = new IdentityHashMap<String, String>();

    public static Map trans(String[] s,String[] rs){
//        for (int j = 0; j < 4; j++){

            for (int i = 0; i < s.length; i++) {
                rs[i] = getPinyin(s[i], " ");
//                System.out.println(i);
            }

            for (int i = 0; i < s.length; i++) {
                map.put(rs[i], s[i]);
            }

            MSDStringSort msdStringSort = new MSDStringSort();
            msdStringSort.sort(rs);

            for (String x : rs){
                map.get(x);
            }
//        }

        return map;
    }

    /**
     * Sort an array of Strings using MSDStringSort.
     *
     * @param a the array to be sorted.
     */

    public static void sort(String[] a) {
        int n = a.length;
        aux = new String[n];
        sort(a, 0, n - 1, 0);
    }

    /**
     * Sort from a[lo] to a[hi] (exclusive), ignoring the first d characters of each String.
     * This method is recursive.
     *
     * @param a  the array to be sorted.
     * @param lo the low index.
     * @param hi the high index (one above the highest actually processed).
     * @param d  the number of characters in each String to be skipped.
     */
    private static void sort(String[] a, int lo, int hi, int d) {
        if (hi <= lo + cutoff) {
            InsertionSortMSD.sort(a, lo, hi, d);
            return;
        }
        int[] count = new int[radix + 2];        // Compute frequency counts.
        for (int i = lo; i <= hi; i++)
            count[charAt(a[i], d) + 2]++;
        for (int r = 0; r < radix + 1; r++)      // Transform counts to indices.
            count[r + 1] += count[r];
        for (int i = lo; i <= hi; i++)     // Distribute.
            aux[count[charAt(a[i], d) + 1]++] = a[i];
//             Copy back.
//            // Recursively sort for each character value.
//            // TO BE IMPLEMENTED
//

        for (int i = lo; i <= hi; i++)
            a[i] = aux[i - lo];
        for (int r = 0; r < radix; r++)
            sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1);
    }

    private static int charAt(String s, int d) {
        if (d < s.length()) return s.charAt(d);
        else return -1;
    }

    public static void main(String[] args) {
        String[] s = PinyinUtil.readAllChinese();
//        System.out.println(s.length);
        String[] rs = new String[s.length];

        MSDStringSort msdStringSort = new MSDStringSort();

        // MSD
        Benchmark_Timer<String[]> bm_msdStringSort = new Benchmark_Timer<String[]>("MSD String Sort", f -> {
//            for (int i = 0; i <4; i++)
                msdStringSort.trans(s,rs);
        });
        double time_msdStringSort = bm_msdStringSort.runFromSupplier(() -> s, 10);
        System.out.println("Msd String Sort -- average time in milliseconds: " + time_msdStringSort);

        int num = 0;
        List<String> sortedChinese = new ArrayList<>();
        for (String sortedPy : rs) {
//            System.out.println(sortedPy);
            String c = map.get(sortedPy);
            sortedChinese.add(c);
            num++;
            if(num>=1000)
                break;
        }

        try {
            FileOutputStream fis = new FileOutputStream("./src/outputChineseMSD.txt");
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

    private static final int radix = 256;
    private static final int cutoff = 15;
    private static String[] aux;       // auxiliary array for distribution
}