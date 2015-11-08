package com.tc;

import java.util.Comparator;

/**
 * Created by wu on 11/8/15.
 */
public class SortHostList implements Comparator {

    public SortHostList(){
        super();
    }

    public int compare(Object o1, Object o2) {
        HostMode s1 = (HostMode) o1;
        HostMode s2 = (HostMode) o2;

        if (s1.timeMs > s2.timeMs) {
            return 1;
        } else {
            return -1;
        }
    }
}