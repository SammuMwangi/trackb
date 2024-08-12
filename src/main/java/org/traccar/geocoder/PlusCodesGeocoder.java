
package org.traccar.geocoder;

import com.google.openlocationcode.OpenLocationCode;
import org.traccar.database.StatisticsManager;

public class PlusCodesGeocoder implements Geocoder {

    @Override
    public void setStatisticsManager(StatisticsManager statisticsManager) {
    }

    @Override
    public String getAddress(double latitude, double longitude, ReverseGeocoderCallback callback) {
        String address = new OpenLocationCode(latitude, longitude).getCode();
        if (callback != null) {
            callback.onSuccess(address);
            return null;
        }
        return address;
    }

}
