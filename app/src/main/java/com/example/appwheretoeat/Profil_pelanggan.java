package com.example.appwheretoeat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Profil_pelanggan extends Activity {
	String ip="",myPosisi,myLati,myLongi;
	String id_pelanggan;
	String id_pelanggan0="";

	EditText txtnama_pelanggan;
	EditText txttelepon;
	EditText txtusername;
	EditText txtemail;
	EditText txtpassword;
	EditText txtjenis_kelamin;
	EditText txtstatuss;

	Button btnProses;
	Button btnHapus;

	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();

	private static final String TAG_SUKSES = "sukses";
	private static final String TAG_record = "record";

	private static final String TAG_nama_pelanggan = "nama_pelanggan";
	private static final String TAG_telepon = "telepon";
	private static final String TAG_username = "username";
	private static final String TAG_password = "password";
	private static final String TAG_email = "email";
	private static final String TAG_jenis_kelamin = "jenis_kelamin";
	private static final String TAG_statuss = "statuss";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profil_pelanggan);

		ip=jsonParser.getIP();
		callMarquee();

		txtnama_pelanggan= (EditText) findViewById(R.id.txtnama_pelanggan);
		txttelepon= (EditText) findViewById(R.id.txttelepon);
		txtusername = (EditText) findViewById(R.id.txtusername);
		txtpassword= (EditText) findViewById(R.id.txtpassword);
		txtemail= (EditText) findViewById(R.id.txtemail);
		txtjenis_kelamin= (EditText) findViewById(R.id.txtjenis_kelamin);
		txtstatuss= (EditText) findViewById(R.id.txtstatuss);txtstatuss.setEnabled(false);

		btnProses= (Button) findViewById(R.id.btnproses);
		btnHapus = (Button) findViewById(R.id.btnhapus);

		Intent i = getIntent();
		id_pelanggan0 = i.getStringExtra("pk");

		new get().execute();

		btnProses.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String lnama_pelanggan= txtnama_pelanggan.getText().toString();
				String ltelepon= txttelepon.getText().toString();
				String lusername= txtusername.getText().toString();
				String lpassword= txtpassword.getText().toString();
				String lemail= txtemail.getText().toString();
				String ljenis_kelamin= txtjenis_kelamin.getText().toString();
				String lstatuss= txtstatuss.getText().toString();

				if(lnama_pelanggan.length()<1){lengkapi("nama_pelanggan");}
				else if(ltelepon.length()<1){lengkapi("telepon");}
				else if(lusername.length()<1){lengkapi("username");}
				else if(lpassword.length()<1){lengkapi("password");}
				else if(lemail.length()<1){lengkapi("email");}
				else if(ljenis_kelamin.length()<1){lengkapi("jenis_kelamin");}
				else if(lstatuss.length()<1){lengkapi("statuss");}
				else{

						new update().execute();
					}

			}});

		btnHapus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}});
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

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	class get extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Profil_pelanggan.this);
			pDialog.setMessage("Load data detail. Silahkan tunggu...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		protected String doInBackground(String... params) {
					int sukses;
					try {
						List<NameValuePair> params1 = new ArrayList<NameValuePair>();
						params1.add(new BasicNameValuePair("id_pelanggan", id_pelanggan0));

						String url=ip+"pelanggan/pelanggan_detail.php";
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
										txtnama_pelanggan.setText(myJSON.getString(TAG_nama_pelanggan));
										txtjenis_kelamin.setText(myJSON.getString(TAG_jenis_kelamin));
										txttelepon.setText(myJSON.getString(TAG_telepon));
										txtusername.setText(myJSON.getString(TAG_username));
										txtpassword.setText(myJSON.getString(TAG_password));
										txtemail.setText(myJSON.getString(TAG_email));
										txtstatuss.setText(myJSON.getString(TAG_statuss));
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
		protected void onPostExecute(String file_url) {pDialog.dismiss();}
	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



	class update extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Profil_pelanggan.this);
			pDialog.setMessage("Mengubah data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		protected String doInBackground(String... args) {
			String lnama_pelanggan= txtnama_pelanggan.getText().toString();
			String ltelepon= txttelepon.getText().toString();
			String ljenis_kelamin= txtjenis_kelamin.getText().toString();
			String lusername= txtusername.getText().toString();
			String lpassword= txtpassword.getText().toString();
			String lemail= txtemail.getText().toString();
			String lstatuss= txtstatuss.getText().toString();

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id_pelanggan", id_pelanggan0));
				params.add(new BasicNameValuePair("nama_pelanggan", lnama_pelanggan));
				params.add(new BasicNameValuePair("telepon", ltelepon));
				params.add(new BasicNameValuePair("jenis_kelamin", ljenis_kelamin));
				params.add(new BasicNameValuePair("username", lusername));
				params.add(new BasicNameValuePair("password", lpassword));
				params.add(new BasicNameValuePair("email", lemail));
				params.add(new BasicNameValuePair("statuss", lstatuss));
	
				String url=ip+"pelanggan/pelanggan_update.php";
				Log.v("update",url);
				JSONObject json = jsonParser.makeHttpRequest(url,"POST", params);
				Log.d("add", json.toString());
				try {
					int sukses = json.getInt(TAG_SUKSES);
					if (sukses == 1) {
						Intent i = getIntent();
						setResult(100, i);
						finish();
					} else {
						// gagal update data
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			return null;
		}

		protected void onPostExecute(String file_url) {pDialog.dismiss();}
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
         int bln= cal.get(Calendar.MONTH)+1;
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
