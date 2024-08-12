
package org.traccar.geolocation;

import org.traccar.model.Network;

public interface GeolocationProvider {

    interface LocationProviderCallback {

        void onSuccess(double latitude, double longitude, double accuracy);

        void onFailure(Throwable e);

    }

    void getLocation(Network network, LocationProviderCallback callback);

}
