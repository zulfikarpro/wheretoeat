package com.example.appwheretoeat;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class List_restoranBudget extends ListActivity {
String ip="",myPosisi,myLati,myLongi;

	private ProgressDialog pDialog;
	JSONParser jParser = new JSONParser();
	JSONArray myJSON = null;
	
	ArrayList<HashMap<String, String>> arrayList;
	private static final String TAG_SUKSES = "sukses";
	private static final String TAG_record = "record";
	
	private static final String TAG_id_restoran = "id_restoran";
	private static final String TAG_nama_restoran = "atas";
	private static final String TAG_budget = "bawah";

	int jd=0;
	String[]arID;
	String[]arAtas;
	String[]arBawah;
	double[]arJarak;
	String id_pelanggan;

	int sukses;
	EditText	txtcari;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_listcaribudget);
		arrayList = new ArrayList<HashMap<String, String>>();
		ip=jParser.getIP();

		Intent i = getIntent();
		myLati = i.getStringExtra("myLati");
		myLongi= i.getStringExtra("myLongi");
		myPosisi = i.getStringExtra("myPosisi");




		txtcari= (EditText) findViewById(R.id.txtcari);

		Button	btncari= (Button) findViewById(R.id.btncari);btncari.setText("Cari Max Budget");
		btncari.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String cari= txtcari.getText().toString();

				if(cari.length()<1){lengkapi("Item dicari");}
				else{
					try{
						arrayList.clear();
					}
					catch(Exception ee){}
					new load2().execute();
				}//else

			}});

		try{
			arrayList.clear();
		}
		catch(Exception ee){}

		//new load().execute();

		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				String pk = ((TextView) view.findViewById(R.id.kode_k)).getText().toString();
				Intent i = new Intent(getApplicationContext(), Detail_restoran.class);
				i.putExtra("pk", pk);
				i.putExtra("myLati", myLati);
				i.putExtra("myLongi", myLongi);
				i.putExtra("myPosisi", myPosisi);
				startActivityForResult(i, 100);
			}});
		BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
		bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
		bottomNavigationView.setSelectedItemId(R.id.caribudget);
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
						Intent z = new Intent(getApplicationContext(), List_restoranNama.class);
						z.putExtra("myLati", myLati);
						z.putExtra("myLongi", myLongi);
						z.putExtra("myPosisi", myPosisi);
						startActivityForResult(z, 100);
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
	}

	public void lengkapi(String item){
		new AlertDialog.Builder(this)
				.setTitle("Lengkapi Data")
				.setMessage("Silakan lengkapi data "+item +" !")
				.setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {

						//finish();
					}}).show();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {// jika result code 100
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
	}

	class load extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(List_restoranBudget.this);
			pDialog.setMessage("Load data. Silahkan Tunggu...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.makeHttpRequest(ip+"restoran/restoran_show.php", "GET", params);
			Log.d("show: ", json.toString());
			try {
				sukses = json.getInt(TAG_SUKSES);
				if (sukses == 1) {
					myJSON = json.getJSONArray(TAG_record);
					jd=myJSON.length();
					arID=new String[jd];
					arAtas=new String[jd];
					arBawah=new String[jd];
					arJarak=new double[jd];
					double lat1=Double.parseDouble(myLati);
					double longi=Double.parseDouble(myLongi);

					for (int i = 0; i < jd; i++) {
						JSONObject c = myJSON.getJSONObject(i);
						String id_restoran = c.getString(TAG_id_restoran);
						String atas = c.getString("atas");
						String bawah = c.getString("bawah2");

						String latitude= c.getString("latitude");
						String longitude = c.getString("longitude");

						double lat2=Double.parseDouble(latitude);
						double long2=Double.parseDouble(longitude);

						double jarak = 0;
							try{
									jarak=getJarak(lat1,longi,lat2,long2);
							}
								catch(Exception ees){}

						arID[i] = id_restoran;
						arAtas[i] = atas;
						arBawah[i] = bawah;
						arJarak[i] = jarak;
					}


					boolean flag = true;
					double temp=0.0;
					String stemp="";
					while ( flag ){
						flag= false;
						for( int j=0;  j < jd -1;  j++ ){
							if ( arJarak[ j ] > arJarak[j+1] ) {
								temp = arJarak[ j ];
								arJarak[ j ] = arJarak[ j+1 ];
								arJarak[ j+1 ] = temp;

								stemp=arID[j];
								arID[ j ] = arID[ j+1 ];
								arID[ j+1 ] = stemp;

								stemp=arAtas[j];
								arAtas[ j ] = arAtas[ j+1 ];
								arAtas[ j+1 ] = stemp;

								stemp=arBawah[j];
								arBawah[ j ] = arBawah[ j+1 ];
								arBawah[ j+1 ] = stemp;

								flag = true;
							}
						}
					}

					for (int i = 0; i < jd; i++) {
						HashMap<String, String> map = new HashMap<String, String>();
							map.put(TAG_id_restoran, arID[i]);
							map.put(TAG_nama_restoran, arAtas[i]+" ("+arJarak[i]+" km)");
							map.put(TAG_budget, arBawah[i]);

						arrayList.add(map);
					}
				} else {

				}
			}
			catch (JSONException e) {e.printStackTrace();}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (sukses == 0) {
				Toast.makeText(getBaseContext(), "Data belum ada...", Toast.LENGTH_LONG).show();
			}
			runOnUiThread(new Runnable() {
				public void run() {
					ListAdapter adapter = new SimpleAdapter(List_restoranBudget.this, arrayList,R.layout.desain_list, new String[] { TAG_id_restoran,TAG_nama_restoran, TAG_budget,},new int[] { R.id.kode_k, R.id.txtNamalkp ,R.id.txtDeskripsilkp});
					setListAdapter(adapter);
				}
			});}
	}


	class load2 extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(List_restoranBudget.this);
			pDialog.setMessage("Load data. Silahkan Tunggu...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			String cari= txtcari.getText().toString();
			params.add(new BasicNameValuePair("cari", cari));
			JSONObject json = jParser.makeHttpRequest(ip+"restoran/restoran_showbudget.php", "GET", params);
			Log.d("show: ", json.toString());
			try {
				sukses = json.getInt(TAG_SUKSES);
				if (sukses == 1) {
					myJSON = json.getJSONArray(TAG_record);
					jd=myJSON.length();
					arID=new String[jd];
					arAtas=new String[jd];
					arBawah=new String[jd];
					arJarak=new double[jd];
					double lat1=Double.parseDouble(myLati);
					double longi=Double.parseDouble(myLongi);

					for (int i = 0; i < jd; i++) {
						JSONObject c = myJSON.getJSONObject(i);
						String id_restoran = c.getString(TAG_id_restoran);
						String atas = c.getString("atas");
						String bawah = c.getString("bawah2");

						String latitude= c.getString("latitude");
						String longitude = c.getString("longitude");

						double lat2=Double.parseDouble(latitude);
						double long2=Double.parseDouble(longitude);

						double jarak = 0;
						try{
							jarak=getJarak(lat1,longi,lat2,long2);
						}
						catch(Exception ees){}

						arID[i] = id_restoran;
						arAtas[i] = atas;
						arBawah[i] = bawah;
						arJarak[i] = jarak;
					}


					boolean flag = true;
					double temp=0.0;
					String stemp="";
					while ( flag ){
						flag= false;
						for( int j=0;  j < jd -1;  j++ ){
							if ( arJarak[ j ] > arJarak[j+1] ) {
								temp = arJarak[ j ];
								arJarak[ j ] = arJarak[ j+1 ];
								arJarak[ j+1 ] = temp;

								stemp=arID[j];
								arID[ j ] = arID[ j+1 ];
								arID[ j+1 ] = stemp;

								stemp=arAtas[j];
								arAtas[ j ] = arAtas[ j+1 ];
								arAtas[ j+1 ] = stemp;

								stemp=arBawah[j];
								arBawah[ j ] = arBawah[ j+1 ];
								arBawah[ j+1 ] = stemp;

								flag = true;
							}
						}
					}

					for (int i = 0; i < jd; i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put(TAG_id_restoran, arID[i]);
						map.put(TAG_nama_restoran, arAtas[i]+" ("+arJarak[i]+" km)");
						map.put(TAG_budget, arBawah[i]);

						arrayList.add(map);
					}
				} else {

				}
			}
			catch (JSONException e) {e.printStackTrace();}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (sukses == 0) {
				Toast.makeText(getBaseContext(), "Data belum ada...", Toast.LENGTH_LONG).show();
			}
			runOnUiThread(new Runnable() {
				public void run() {
					ListAdapter adapter = new SimpleAdapter(List_restoranBudget.this, arrayList,R.layout.desain_list, new String[] { TAG_id_restoran,TAG_nama_restoran, TAG_budget,},new int[] { R.id.kode_k, R.id.txtNamalkp ,R.id.txtDeskripsilkp});
					setListAdapter(adapter);
				}
			});}
	}


    
public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		finish();
		return true;
		}
		return super.onKeyDown(keyCode, event);
		}

//	public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add(0, 1, 0, "Add New").setIcon(R.drawable.add);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//        case 1:
//        	Intent i = new Intent(getApplicationContext(), Detail_restoran.class);
//			i.putExtra("pk", "");
//			startActivityForResult(i, 100);
//            return true;
//        }
//        return false;
//    }

	public static double getJarak(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2-lat1);
		double dLng = Math.toRadians(lng2-lng1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
						Math.sin(dLng/2) * Math.sin(dLng/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double dist = earthRadius * c;

		int meterConversion = 1609;
		double myjr=dist * meterConversion;
		return Math.floor(myjr);

	}

}
