package com.example.appwheretoeat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class daftar extends Activity {
	String ip="";

	EditText txtnama_pelanggan;
	EditText txttelepon;
	EditText txtusername;
	EditText txtemail;
	EditText txtpassword;
	EditText txtjenis_kelamin;

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

	@Override  
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daftar);

		ip=jsonParser.getIP();

		txtnama_pelanggan= (EditText) findViewById(R.id.txtnama_pelanggan);
		txttelepon= (EditText) findViewById(R.id.txttelepon);
		txtusername = (EditText) findViewById(R.id.txtusername);
		txtpassword= (EditText) findViewById(R.id.txtpassword);
		txtemail= (EditText) findViewById(R.id.txtemail);
		txtjenis_kelamin= (EditText) findViewById(R.id.txtjenis_kelamin);

		btnProses= (Button) findViewById(R.id.btnproses);



		btnProses.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String lnama_pelanggan= txtnama_pelanggan.getText().toString();
				String ltelepon= txttelepon.getText().toString();
				String lusername= txtusername.getText().toString();
				String lpassword= txtpassword.getText().toString();
				String lemail= txtemail.getText().toString();
				String ljenis_kelamin= txtjenis_kelamin.getText().toString();

				if(lnama_pelanggan.length()<1){lengkapi("nama_pelanggan");}
				else if(ltelepon.length()<1){lengkapi("telepon");}
				else if(lusername.length()<1){lengkapi("username");}
				else if(lpassword.length()<1){lengkapi("password");}
				else if(lemail.length()<1){lengkapi("email");}
				else if(ljenis_kelamin.length()<1){lengkapi("jenis_kelamin");}
				else{

						new save().execute();
					}


			}});


	}


	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	

	class save extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(daftar.this);
			pDialog.setMessage("Menyimpan data ...");
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

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("nama_pelanggan", lnama_pelanggan));
				params.add(new BasicNameValuePair("telepon", ltelepon));
				params.add(new BasicNameValuePair("jenis_kelamin", ljenis_kelamin));
				params.add(new BasicNameValuePair("username", lusername));
				params.add(new BasicNameValuePair("password", lpassword));
				params.add(new BasicNameValuePair("email", lemail));

				String url=ip+"pelanggan/pelanggan_add.php";
				Log.v("add",url);
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
				   	finish();
			   }}).show();
		 } 
	 
	 



	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
			}
			return super.onKeyDown(keyCode, event);
			}   
			 
}
