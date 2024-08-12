
package org.traccar.geocoder;

import org.traccar.database.StatisticsManager;

public interface Geocoder {

    interface ReverseGeocoderCallback {

        void onSuccess(String address);

        void onFailure(Throwable e);

    }

    String getAddress(double latitude, double longitude, ReverseGeocoderCallback callback);

    void setStatisticsManager(StatisticsManager statisticsManager);

}
