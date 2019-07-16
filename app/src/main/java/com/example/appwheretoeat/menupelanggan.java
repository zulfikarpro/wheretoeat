package com.example.appwheretoeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

public class menupelanggan extends Activity {


	//String id_user,nama_user;
	@Override  
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuutamabenar);

//		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(menuutama.this);
//		Boolean Registered = sharedPref.getBoolean("Registered", false);
//		if (!Registered) {
//			finish();
//		} else {
//			id_user = sharedPref.getString("id_user", "");
//			nama_user = sharedPref.getString("nama_user", "");
//		}


		ImageView btnProfil= (ImageView) findViewById(R.id.btnProfile);
		btnProfil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), Profil_pelanggan.class);
				startActivityForResult(i, 100);
			}});

		ImageView btnCarinama= (ImageView) findViewById(R.id.btnCarinama);
		btnCarinama.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), Profil_pelanggan.class);
				startActivityForResult(i, 100);
			}});



  }
	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
			}
			return super.onKeyDown(keyCode, event);
			}   		 
}
