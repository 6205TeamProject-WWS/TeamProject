package edu.neu.coe.info6205.sort.counting;

import edu.neu.coe.info6205.sort.elementary.InsertionSort;
import edu.neu.coe.info6205.sort.elementary.InsertionSortMSD;

import java.util.IdentityHashMap;
import java.util.Map;

import static edu.neu.coe.info6205.util.PinyinUtil.getPinyin;
import static edu.neu.coe.info6205.util.PinyinUtil.readAllChinese;

/**
 * Class to implement Most significant digit string sort (a radix sort).
 */
public class MSDStringSort {

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
        if (hi <= lo + cutoff) InsertionSortMSD.sort(a, lo, hi, d);
//        else {
//            int[] count = new int[radix + 2];        // Compute frequency counts.
//            for (int i = lo; i < hi; i++)
//                count[charAt(a[i], d) + 2]++;
//            for (int r = 0; r < radix + 1; r++)      // Transform counts to indices.
//                count[r + 1] += count[r];
//            for (int i = lo; i < hi; i++)     // Distribute.
//                aux[count[charAt(a[i], d) + 1]++] = a[i];
//            // Copy back.
//            if (hi - lo >= 0) System.arraycopy(aux, 0, a, lo, hi - lo);
//            // Recursively sort for each character value.
//            // TO BE IMPLEMENTED
//
//            for (int i = lo; i <= hi; i++)
//                a[i] = aux[i - lo];
//
//            for (int r = 0; r < radix; r++) {
//                sort(a, lo + count[r], lo + count[r + 1], d + 1);
//            }
//
//
//        }

        else{
            int[] count = new int[radix+2];

            for (int i = lo; i <= hi; i++)
                count[charAt(a[i], d) + 2]++;
            for (int r = 0; r < radix+1; r++)
                count[r + 1] += count[r];
            for (int i = lo; i <= hi; i++)
                aux[count[charAt(a[i], d) + 1]++] = a[i];
            for (int i = lo; i <= hi; i++)
                a[i] = aux[i - lo];
            for (int r = 0; r < radix; r++)
                sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1);
        }
    }

    private static int charAt(String s, int d) {
        if (d < s.length()) return s.charAt(d);
        else return -1;
    }

    public static void main(String[] args) {
        String[] s = readAllChinese();
        String[] rs = new String[s.length];
        for (int i = 0; i < s.length; i++) {
            rs[i] = getPinyin(s[i], " ");
        }

        Map<String, String> map = new IdentityHashMap<String, String>();
        for (int i = 0; i < s.length; i++) {
            map.put(rs[i], s[i]);
        }

        MSDStringSort msdStringSort = new MSDStringSort();
        msdStringSort.sort(rs);

        for (String x : rs)
            System.out.println(map.get(x));
    }

    private static final int radix = 256;
    private static final int cutoff = 15;
    private static String[] aux;       // auxiliary array for distribution
}