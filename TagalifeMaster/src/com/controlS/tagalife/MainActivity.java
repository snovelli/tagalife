package com.controlS.tagalife;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

// Classe page accueil de l'application TAL

public class MainActivity extends Activity implements OnClickListener {
	
	ImageButton marcheAS = null;
	ImageButton location = null;
	ImageButton appel = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        location = (ImageButton)findViewById(R.id.imageButtonLocation);
        location.setBackgroundDrawable(getResources().getDrawable(R.drawable.effect_location));
        location.setOnClickListener(this);
        
        marcheAS = (ImageButton)findViewById(R.id.imageButtonNote);
        marcheAS.setBackgroundDrawable(getResources().getDrawable(R.drawable.effect_note));
        marcheAS.setOnClickListener(this);
        
        appel = (ImageButton)findViewById(R.id.imageButtonPhone);
        appel.setBackgroundDrawable(getResources().getDrawable(R.drawable.effect_appel));
        appel.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onClick(View v) {
    	if(v == marcheAS) {
    		Intent intentMarcheAS = new Intent(this,MarcheASActivity.class);
    		startActivity(intentMarcheAS);
    	}else if(v == appel){
    		Intent intentAppel = new Intent(this,AppelActivity.class);
    		startActivity(intentAppel);
    	}
    }
}
