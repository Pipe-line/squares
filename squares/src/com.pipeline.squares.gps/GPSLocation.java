package com.pipeline.squares.gps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.google.android.maps.GeoPoint;

public class GPSLocation  implements LocationListener {

	private static GeoPoint p = null;
	private static double latitude;
	private static double longitude;
	
	/* This method is called when use position will get changed */
	public void onLocationChanged(Location location) {
		if (location != null) {
			latitude= location.getLatitude();
			longitude = location.getLongitude();

			p = new GeoPoint((int) (latitude * 1E6),
					(int) (longitude * 1E6));
			

		}
	}
	public void onProviderDisabled(String provider) {
	}
	public void onProviderEnabled(String provider) {
	}
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public static double getLatitude() {
        return latitude;
    }

    public static double getLongitude() {
        return longitude;
    }
    
    public static GeoPoint getGeoPoint() {
    
        return p;
    }

}
