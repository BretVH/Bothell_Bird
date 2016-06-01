package bothell_bird;

/**
 *
 * @author Bret
 */
public class Feature {
    private final int featureId;
    private final String featureName;
    
    Feature(int featureId, String featureName){
        this.featureId = featureId;
        this.featureName = featureName;
    }
    
    public String getFeatureName() {
        return featureName;
    }
    
    public int getFeatureId() {
        return featureId;
    }
}
