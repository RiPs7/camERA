package perimara.era.interfaces;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by periklismaravelias on 01/09/16.
 */
public class CustomLocationListener implements android.location.LocationListener{

    public double mLatitude, mLongitude;
    public Location mLocation;

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        //Log.d("Location Changes", location.toString());
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Log.d("Status Changed", String.valueOf(status));
    }

    @Override
    public void onProviderEnabled(String provider) {
        //Log.d("Provider Enabled", provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Log.d("Provider Disabled", provider);
    }

}
