package apps.digicity;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

public class MyLocationListener implements LocationListener {

	public TextView distanceText;
	
	@Override
	public void onLocationChanged(Location location) {
		float[] distanceProps = new float[3];
		Location.distanceBetween(location.getLongitude(), location.getLatitude(), 100, 100, distanceProps);
		distanceText.setText(String.valueOf(distanceProps[0]));
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
