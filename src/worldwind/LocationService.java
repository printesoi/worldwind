package worldwind;

import gov.nasa.worldwind.poi.YahooGazetteer;
import gov.nasa.worldwind.poi.PointOfInterest;

import java.util.List;

public class LocationService {
    
    private YahooGazetteer gazetteer;
    
    public LocationService() {
        gazetteer = new YahooGazetteer();    
    }


    public List<PointOfInterest> queryService(String lookupString) {
        List<PointOfInterest> results = this.gazetteer.findPlaces(lookupString);
        if (results == null || results.size() == 0) {
            return null;
        } else {
            return results;
        }
    }
}
