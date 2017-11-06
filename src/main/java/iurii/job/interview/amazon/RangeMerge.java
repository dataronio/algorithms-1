package iurii.job.interview.amazon;

import java.util.LinkedHashMap;

/**
 * You have sorted array list of integers.
 * 1,2,2,3,5,8,9,11,12
 * Result 1-3, 5-5, 8-9, 11-12
 * <p>
 * Created by iurii.dziuban on 27/10/2017.
 */
public class RangeMerge {

    public LinkedHashMap<Integer, Integer> encodeRanges(int[] A) {
        LinkedHashMap<Integer, Integer> pairs = new LinkedHashMap<>();
        if (A == null || A.length == 0) {
            return pairs;
        }
        int first = A[0];
        int previous = A[0];
        for (int value : A) {
            if (previous != value && previous + 1 != value) {
                pairs.put(first, previous);
                first = value;
            }
            previous = value;
        }
        pairs.put(first, previous);
        return pairs;
    }
}