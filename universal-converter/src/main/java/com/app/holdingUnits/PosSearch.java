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
