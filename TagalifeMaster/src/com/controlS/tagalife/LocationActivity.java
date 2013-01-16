package com.controlS.tagalife;



import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity implements OnClickListener, LocationListener, OnSeekBarChangeListener {

	private LocationManager lManager;
	private Location location;
	String bestProvider = null;
	ImageButton validate;
	private SeekBar bar; 
    private TextView textProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
		 bar = (SeekBar)findViewById(R.id.seekBarAge); // make seekbar object
	        bar.setOnSeekBarChangeListener(this); // set seekbar listener.
	        // since we are using this class as the listener the class is "this"
	        
	        // make text label for progress value
	        textProgress = (TextView)findViewById(R.id.textViewAge);

		
		findViewById(R.id.imageButtonValider).setOnClickListener(this);
		
		lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		getProvider();
		
	}
	

	public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {
	    	
	    	// change progress text label with current seekbar value
	    	textProgress.setText("Age : "+progress);
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
	
	private void createXMLDocument() throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		
		Document document = documentBuilder.newDocument();
		
		Element rootElement = document.createElement("Tag");
		Element sexeElement = document.createElement("sexe");
//		sexeElement.setAttribute("sexe", R.id.textViewEtat);
		Element ageElement 	= document.createElement("age");
		Element etatElement = document.createElement("etat");
		
		rootElement.appendChild(sexeElement).appendChild(ageElement).appendChild(etatElement);
		
		
		
	}
	
	protected String getProvider() {
		setProgressBarIndeterminateVisibility(true);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = lManager.getBestProvider(criteria, true);
				
		if (bestProvider == null) {
			Log.i("Localisation message", "Aucun gestionnaire gps trouvé, veuillez activer la localisation");
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
		Log.i("Tag mode", "La position a changée.");
		setProgressBarIndeterminateVisibility(false);
		this.location = location;
		
	}

	public void onProviderDisabled(String provider) {
		Log.i("Tag mode", "La source a été désactivée");
		Toast.makeText(LocationActivity.this,
						String.format("La source \"%s\" a été désactivée", provider),
						Toast.LENGTH_SHORT).show();
		lManager.removeUpdates(this);
		setProgressBarIndeterminateVisibility(false);
		
	}

	public void onProviderEnabled(String provider) {
		Log.i("Tag mode", "La source a été activée");
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i("Tag mode", "Le statut de la source a changé");
		
	}


	public void onStartTrackingTouch(SeekBar seekBar) {
		
		
	}


	public void onStopTrackingTouch(SeekBar seekBar) {
		seekBar.setSecondaryProgress(seekBar.getProgress());
		
	}


	
	

}
