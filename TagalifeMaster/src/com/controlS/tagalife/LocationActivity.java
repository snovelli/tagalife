package com.controlS.tagalife;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

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
		createXMLDocument();
		
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
	
	private void createXMLDocument() {
		
		File tagXML;
		
		System.out.println(Environment.getRootDirectory().getPath() + "/sdcardo/tagXML.xml");
		
		if (!Environment.getExternalStorageDirectory().exists()) {
			tagXML = new File(Environment.getRootDirectory().getPath() + "/sdcardo/tagXML.xml");
		} else {
			tagXML = new File(Environment.getExternalStorageDirectory() + "/tagXML.xml");
		}
		
		try {
			tagXML.createNewFile();
		} catch (IOException ioe) {
			Log.e("IOException", "exception in createNewFile() method");
		}
		
		FileOutputStream fileos = null;
		try {
			fileos = new FileOutputStream(tagXML);
		} catch (FileNotFoundException fnfe) {
			Log.e("FileNotFoundException", "can't create FileOutputStream");
		}
		
		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(fileos, "UTF-8");
			serializer.startDocument(null, Boolean.valueOf(true));
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

			RadioGroup radioGroupSexe = (RadioGroup) findViewById(R.id.radioGroupSexe);
	        radioGroupSexe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

	            public void onCheckedChanged(RadioGroup group, int checkedId) 
	            {
	                RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);
	                String text = checkedRadioButton.getText().toString();
	                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	            }
	        });
	        
			RadioGroup radioGroupEtat = (RadioGroup) findViewById(R.id.radioGroupEtat);
	        radioGroupEtat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

	            public void onCheckedChanged(RadioGroup group, int checkedId) 
	            {
	                RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);
	                String text = checkedRadioButton.getText().toString();
	                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	            }
	        });
			
			serializer.startTag(null, "tag");
			
				serializer.startTag(null, "sexe");
				serializer.text(radioGroupSexe.toString());
				serializer.endTag(null, "sexe");
           
				serializer.startTag(null, "age");
				serializer.text("unknow");
				serializer.endTag(null, "age");
   
				serializer.startTag(null, "etat");
            	serializer.text(radioGroupEtat.toString());
            	serializer.endTag(null, "etat");
            	
            	serializer.startTag(null, "longitude");
            	serializer.text("");
            	serializer.endTag(null, "longitude");
            	
            	serializer.startTag(null, "latitude");
            	serializer.text("");
            	serializer.endTag(null, "latitude");
           
            serializer.endTag(null, "tag");
            
            serializer.endDocument();
            
            serializer.flush();
            
            fileos.close();
            
		} catch (Exception e) {
            Log.e("Exception","error occurred while creating xml file");
		}
	}
	
	//TODO refactor this to find a way to make it functionnal
	protected String getProvider() {
		setProgressBarIndeterminateVisibility(true);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = lManager.getBestProvider(criteria, true);
				
		if (bestProvider == null) {
			Log.i("Localisation message", "Aucun gestionnaire gps trouvé, veuillez activer la localisation");
			Intent intentMain = new Intent(this, MainActivity.class);
			createXMLDocument();
			startActivity(intentMain);
		} else {
//			bestProvider = lManager.getBestProvider(criteria, false);
			lManager.requestLocationUpdates(bestProvider, 5000, 0, this);
			Intent intentResult = new Intent(this,ResultActivity.class);
			createXMLDocument();
    		startActivity(intentResult);
		}
		System.out.println(bestProvider);
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
