package com.example.appwheretoeat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Detail_restoran extends Activity {
	String ip="";
	String id_restoran;
	String id_restoran0="";

	EditText txtid_restoran;
	EditText txtnama_restoran;
	EditText txtlokasi;
	EditText txtlatitude;
	EditText txtlongitude;
	EditText txtbudget;

	Button btnMaps;

	String myLati = "-6.353370";
	String myLongi = "106.832349";
	String myPosisi = "Lp2m aray Jkt";

	String latitude, longitude;


	String mn1, mn2, mn3, mn4, mn5, mn6, mn7, mn8, mn9, mn10;
	private SliderLayout sliderLayout;


	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();

	private static final String TAG_SUKSES = "sukses";
	private static final String TAG_record = "record";

	private static final String TAG_nama_restoran = "nama_restoran";
	private static final String TAG_lokasi = "lokasi";
	private static final String TAG_latitude = "latitude";
	private static final String TAG_longitude = "longitude";
	private static final String TAG_budget = "budget";
	private static final String TAG_menu1 = "menu1";
	private static final String TAG_menu2 = "menu2";
	private static final String TAG_menu3 = "menu3";
	private static final String TAG_menu4 = "menu4";
	private static final String TAG_menu5 = "menu5";
	private static final String TAG_menu6 = "menu6";
	private static final String TAG_menu7 = "menu7";
	private static final String TAG_menu8 = "menu8";
	private static final String TAG_menu9 = "menu9";
	private static final String TAG_menu01 = "menu01";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_restoran);

		ip=jsonParser.getIP();
		callMarquee();

		txtid_restoran = (EditText) findViewById(R.id.txtid_restoran);txtid_restoran.setEnabled(false);
		txtnama_restoran= (EditText) findViewById(R.id.txtnama_restoran);txtnama_restoran.setEnabled(false);
		txtlokasi= (EditText) findViewById(R.id.txtlokasi);txtlokasi.setEnabled(false);
		txtlatitude= (EditText) findViewById(R.id.txtlatitude);txtlatitude.setEnabled(false);
		txtlongitude= (EditText) findViewById(R.id.txtlongitude);txtlongitude.setEnabled(false);
		txtbudget = (EditText) findViewById(R.id.txtbudget);txtbudget.setEnabled(false);

		btnMaps= (Button) findViewById(R.id.btnMaps);




		Intent i = getIntent();
		id_restoran0 = i.getStringExtra("pk");
		myLati = i.getStringExtra("myLati");
		myLongi= i.getStringExtra("myLongi");
		myPosisi = i.getStringExtra("myPosisi");

			new get().execute();



		btnMaps.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String modeDirection = "driving";
				String url = "http://maps.google.com/maps?saddr="+myLati+","+myLongi+"&daddr="+latitude+","+longitude+"&directionsmode=" + modeDirection;
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
				intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
				startActivity(intent);
			}
		});
}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	class get extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Detail_restoran.this);
			pDialog.setMessage("Load data detail. Silahkan tunggu...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		protected String doInBackground(String... params) {
					int sukses;
					try {
						List<NameValuePair> params1 = new ArrayList<NameValuePair>();
						params1.add(new BasicNameValuePair("id_restoran", id_restoran0));

						String url=ip+"restoran/restoran_detail.php";
						Log.v("detail",url);
						JSONObject json = jsonParser.makeHttpRequest(url, "GET", params1);
						Log.d("detail", json.toString());
						sukses = json.getInt(TAG_SUKSES);
						if (sukses == 1) {
							JSONArray myObj = json.getJSONArray(TAG_record); // JSON Array
							final JSONObject myJSON = myObj.getJSONObject(0);
							runOnUiThread(new Runnable() {
							public void run() {
								try {
										txtid_restoran.setText(id_restoran0);
										txtnama_restoran.setText(myJSON.getString(TAG_nama_restoran));
										txtlokasi.setText(myJSON.getString(TAG_lokasi));
										txtlatitude.setText(myJSON.getString(TAG_latitude));
										txtlongitude.setText(myJSON.getString(TAG_longitude));
										txtbudget.setText(myJSON.getString(TAG_budget));




									mn1=myJSON.getString(TAG_menu1);
									mn2=myJSON.getString(TAG_menu2);
									mn3=myJSON.getString(TAG_menu3);
									mn4=myJSON.getString(TAG_menu4);
									mn5=myJSON.getString(TAG_menu5);
									mn6=myJSON.getString(TAG_menu6);
									mn7=myJSON.getString(TAG_menu7);
									mn8=myJSON.getString(TAG_menu8);
									mn9=myJSON.getString(TAG_menu9);
									mn10=myJSON.getString(TAG_menu01);

									latitude=myJSON.getString(TAG_latitude);
									longitude=myJSON.getString(TAG_longitude);

								}
								catch (JSONException e) {e.printStackTrace();}
							}});
						}
						else{
							// jika id tidak ditemukan
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
			return null;
		}
		protected void onPostExecute(String file_url) {pDialog.dismiss();

			sliderLayout = (SliderLayout) findViewById(R.id.slider);
			// Load image dari URL
			HashMap<String,String> f_url= new HashMap<String, String>();
			f_url.put("Menu 1", mn1);
			f_url.put("Menu 2", mn2);
			f_url.put("Menu 3", mn3);
			f_url.put("Menu 4", mn4);
			f_url.put("Menu 5", mn5);
			f_url.put("Menu 6", mn6);
			f_url.put("Menu 7", mn7);
			f_url.put("Menu 8", mn8);
			f_url.put("Menu 9", mn9);
			f_url.put("Menu 10", mn10);

// Load Image Dari res/drawable
			for(String name : f_url .keySet()){
				TextSliderView textSliderView = new TextSliderView(Detail_restoran.this);
				// initialize a SliderLayout
				textSliderView
						.description(name)
						.image(f_url .get(name))
						.setScaleType(BaseSliderView.ScaleType.Fit);

				//add your extra information
				textSliderView.bundle(new Bundle());
				textSliderView.getBundle()
						.putString("extra",name);
				sliderLayout.addSlider(textSliderView);
			}

			sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
			sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
			sliderLayout.setCustomAnimation(new DescriptionAnimation());
			sliderLayout.setDuration(5000);
		}
	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	
	 public void lengkapi(String item){
		  new AlertDialog.Builder(this)
		  .setTitle("Lengkapi Data")
		  .setMessage("Silakan lengkapi data "+item +" !")
		  .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dlg, int sumthin) {

			   }}).show();
		 } 
	 
	 
	 void callMarquee(){
         Calendar cal = Calendar.getInstance();          
         int jam = cal.get(Calendar.HOUR);
         int menit= cal.get(Calendar.MINUTE);
         int detik= cal.get(Calendar.SECOND);

         int tgl= cal.get(Calendar.DATE);
         int bln= cal.get(Calendar.MONTH);
         int thn= cal.get(Calendar.YEAR);

         String stgl=String.valueOf(tgl)+"-"+String.valueOf(bln)+"-"+String.valueOf(thn);
         String sjam=String.valueOf(jam)+":"+String.valueOf(menit)+":"+String.valueOf(detik);
        
         TextView  txtMarquee=(TextView)findViewById(R.id.txtMarquee);
         txtMarquee.setSelected(true);
         String kata="Selamat Datang @lp2maray.com Aplikasi Android  "+stgl+"/"+sjam+" #";
         String kalimat=String.format("%1$s",TextUtils.htmlEncode(kata));
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
