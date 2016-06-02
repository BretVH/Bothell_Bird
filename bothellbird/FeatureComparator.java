/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bothell_bird;

import java.util.Comparator;

/**
 *
 * @author Bret
 */
public class FeatureComparator implements Comparator {

    /**
     *
     * @param f1
     * @param f2
     * @return
     */
    public int compare(Object f1, Object f2) {
        return ((Feature) f1).compareTo(f2);
    }   
}
