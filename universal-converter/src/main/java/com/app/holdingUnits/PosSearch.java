package com.app.holdingUnits;

import java.util.ArrayList;

/**
 * Class which have only one duty - to find position (index) in arrays of
 * class's instances, which implements interface Comparable.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class PosSearch {

    /**
     * searches position in array ordered by name where instance is, or where
     * it should be added if it does not exist.
     * @param name name of the instance.
     * @param array array of the instances.
     * @param <T> template which should extend Comparable.
     * @return index in the array.
     */
    public static <T extends Comparable<String>> int searchPos(String name,
            ArrayList<T> array) {

        int leftPos = 0;
        int rightPos = array.size();
        int mid = (leftPos + rightPos) / 2;

        while (leftPos < rightPos) {
            int compare = array.get(mid).compareTo(name);
            if (compare == 0) { return mid; }
            if (compare > 0) { rightPos = mid; }
            else { leftPos = mid + 1; }
            mid = (leftPos + rightPos) / 2;
        }
        return Math.max(leftPos, rightPos);
    }

}
