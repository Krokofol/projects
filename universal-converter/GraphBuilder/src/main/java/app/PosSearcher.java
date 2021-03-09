package app;

import java.util.ArrayList;

public class PosSearcher {

    public static int searchNamePosInNodeArray(String name, ArrayList<Node> array) {
        int leftPos = 0;
        int rightPos = array.size();
        int mid = (leftPos + rightPos) / 2;

        while (leftPos < rightPos) {
            int compare = array.get(mid).getName().compareTo(name);
            if (compare == 0) {
                return mid;
            }
            if (compare > 0) {
                rightPos = mid;
            } else {
                leftPos = mid + 1;
            }
            mid = (leftPos + rightPos) / 2;
        }
        return Math.max(leftPos, rightPos);
    }

}
