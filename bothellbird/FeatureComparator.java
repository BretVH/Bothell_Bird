package bothell_bird;

import java.util.Comparator;

/**
 *
 * @author Bret
 */
public class FeatureComparator implements Comparator<Feature> {    
     /**
     *
     * @param f1
     * @param f2
     * @return
     */
    @Override
    public int compare(Feature f1, Feature f2) {
        return f1.compareTo(f2);
    }
}
