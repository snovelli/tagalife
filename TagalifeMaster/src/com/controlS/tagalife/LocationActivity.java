package com.controlS.tagalife;



import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class LocationActivity extends Activity implements OnClickListener, LocationListener {

	private LocationManager lManager;
	private Location location;
	String bestProvider = null;
	ImageButton validate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
		findViewById(R.id.imageButtonValider).setOnClickListener(this);
		
		lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		getProvider();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tag, menu);
		return true;
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButtonValider:
			getProvider();
    		break;
		}
	}
	
	protected String getProvider() {
		setProgressBarIndeterminateVisibility(true);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = lManager.getBestProvider(criteria, true);
				
		if (bestProvider == null) {
			Log.i("Localisation message", "Aucun gestionnaire gps trouv�, veuillez activer la localisation");
			Intent intentMain = new Intent(this, MainActivity.class);
			startActivity(intentMain);
		} else {
//			bestProvider = lManager.getBestProvider(criteria, false);
			lManager.requestLocationUpdates(bestProvider, 5000, 0, this);
			Intent intentResult = new Intent(this,ResultActivity.class);
    		startActivity(intentResult);
		}
		return bestProvider;
	}

	public void onLocationChanged(Location location) {
		Log.i("Tag mode", "La position a chang�e.");
		setProgressBarIndeterminateVisibility(false);
		this.location = location;
		
	}

	public void onProviderDisabled(String provider) {
		Log.i("Tag mode", "La source a �t� d�sactiv�e");
		Toast.makeText(LocationActivity.this,
						String.format("La source \"%s\" a �t� d�sactiv�e", provider),
						Toast.LENGTH_SHORT).show();
		lManager.removeUpdates(this);
		setProgressBarIndeterminateVisibility(false);
		
	}

	public void onProviderEnabled(String provider) {
		Log.i("Tag mode", "La source a �t� activ�e");
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i("Tag mode", "Le statut de la source a chang�");
		
	}

}
