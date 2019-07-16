package com.example.appwheretoeat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class menuutama2 extends Activity {

	String myLati = "-6.353370";
	String myLongi = "106.832349";
	String myPosisi = "Lp2m aray Jkt";

	String id_pelanggan, nama_pelanggan;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainmenu);

		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(menuutama2.this);
		Boolean Registered = sharedPref.getBoolean("Registered", false);
		if (!Registered) {
			finish();
		} else {
			id_pelanggan = sharedPref.getString("id_pelanggan", "");
			nama_pelanggan = sharedPref.getString("nama_pelanggan", "");
		}

		BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
		bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
		bottomNavigationView.setSelectedItemId(R.id.profile);
		Menu menu = bottomNavigationView.getMenu();
		bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				switch (menuItem.getItemId()){
					case R.id.profile:
						Intent i = new Intent(getApplicationContext(), Profil_pelanggan.class);
						i.putExtra("pk", id_pelanggan);
						startActivityForResult(i, 100);
						break;

					case R.id.carinama:
						Intent a = new Intent(getApplicationContext(), List_restoranNama.class);
						a.putExtra("myLati", myLati);
						a.putExtra("myLongi", myLongi);
						a.putExtra("myPosisi", myPosisi);
						startActivityForResult(a, 100);
						break;

					case R.id.caribudget:
					Intent b = new Intent(getApplicationContext(), List_restoranBudget.class);
					b.putExtra("myLati", myLati);
					b.putExtra("myLongi", myLongi);
					b.putExtra("myPosisi", myPosisi);
					startActivityForResult(b, 100);
						break;
					case R.id.maps:
					Intent d = new Intent(getApplicationContext(), MapsActivity.class);//MapsActivity
					d.putExtra("myLati", myLati);
					d.putExtra("myLongi", myLongi);
					d.putExtra("myPosisi", myPosisi);
					startActivityForResult(d, 100);

					default:
						break;
				}
				return false;
			}
		});


/*		ImageView btnProfile = (ImageView) findViewById(R.id.btnProfile);
		btnProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), Profil_pelanggan.class);
				i.putExtra("pk", id_pelanggan);
				startActivityForResult(i, 100);
			}
		});

	ImageView btnMaps = (ImageView) findViewById(R.id.btnMaps);
		btnMaps.setOnClickListener(new View.OnClickListener() {
			@Override
				Intent i = new Intent(getApplicationContext(), MapsActivity.class);//MapsActivity
				i.putExtra("myLati", myLati);
				i.putExtra("myLongi", myLongi);
				i.putExtra("myPosisi", myPosisi);
				startActivityForResult(i, 100);
			}
		});

		ImageView btnCarinama = (ImageView) findViewById(R.id.btnCarinama);
		btnCarinama.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), List_restoranNama.class);
				i.putExtra("myLati", myLati);
				i.putExtra("myLongi", myLongi);
				i.putExtra("myPosisi", myPosisi);
				startActivityForResult(i, 100);
			}
		});

		ImageView btnCaribudget = (ImageView) findViewById(R.id.btnCaribudget);
		btnCaribudget.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), List_restoranBudget.class);
				i.putExtra("myLati", myLati);
				i.putExtra("myLongi", myLongi);
				i.putExtra("myPosisi", myPosisi);
				startActivityForResult(i, 100);
			}
		});


*/
		LocationManager locationManager;
		String context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(context);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = locationManager.getBestProvider(criteria, true);

		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		Location location = locationManager.getLastKnownLocation(provider);
		updateWithNewLocation(location);

		locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider){
			updateWithNewLocation(null);
		}

		public void onProviderEnabled(String provider){ }
		public void onStatusChanged(String provider, int status,
									Bundle extras){ }
	};

	private void updateWithNewLocation(Location location) {
		double latitude=Double.parseDouble(myLati);
		double longitude=Double.parseDouble(myLongi);
		String addressString = "No address found";

		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			Geocoder gc = new Geocoder(this, Locale.getDefault());
			try {
				List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
				StringBuilder sb = new StringBuilder();
				if (addresses.size() > 0) {
					Address address = addresses.get(0);

					for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
						sb.append(address.getAddressLine(i)).append("\n");

					sb.append(address.getLocality()).append("\n");
					sb.append(address.getPostalCode()).append("\n");
					sb.append(address.getCountryName());
				}
				addressString = sb.toString();
			} catch (IOException e) {}
		} else {
			myLati="-6.353370";
			myLongi="106.832349";
			addressString="Lp2m Aray Jkt";
		}

		myPosisi=addressString;
		myLati=String.valueOf(latitude);
		myLongi=String.valueOf(longitude);


		TextView  txtMarquee=(TextView)findViewById(R.id.txtMarquee);
		txtMarquee.setSelected(true);
		String kata="Posisi Anda :"+myLati+"/"+myLongi+" "+myPosisi+"#";
		String kalimat=String.format("%1$s", TextUtils.htmlEncode(kata));
		txtMarquee.setText(Html.fromHtml(kalimat+kalimat+kalimat));
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
