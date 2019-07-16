package com.example.appwheretoeat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_LONG;

public class Login extends Activity {
	
     EditText txtusername,txtpassword;
     String ip="";
     int sukses;
     private LoginButton loginButton;
     private ProgressDialog pDialog;
     private TextView txtnama_pelanggan,txtemail;
     private CallbackManager callbackManager;
	    JSONParser jsonParser = new JSONParser();

	    private static final String TAG_SUKSES = "sukses";
	    private static final String TAG_record = "record";

	    String id_pelanggan="",nama_pelanggan;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ip=jsonParser.getIP();

        txtnama_pelanggan=(EditText)findViewById(R.id.txtnama_pelanggan);
        txtemail=(EditText)findViewById(R.id.txtemail);
        txtusername=(EditText)findViewById(R.id.txtusername);
        txtpassword=(EditText)findViewById(R.id.txtpassword);
        callbackManager = CallbackManager.Factory.create();


        Button btnLogin= (Button) findViewById(R.id.btnlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=txtusername.getText().toString();
                String pass=txtpassword.getText().toString();
                if(user.length()<1){lengkapi("Username");}
                else if(pass.length()<1){lengkapi("Password");}
                else{
                	new ceklogin().execute();
                         }

                  }

        });


        Button btnDaftar= (Button) findViewById(R.id.btnDaftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent i = new Intent(getApplicationContext(), daftar.class);
                    startActivityForResult(i, 100);
                }});



//		TextView txtBuat=(TextView)findViewById(R.id.btnbantuan);
//		txtBuat.setOnClickListener(new View.OnClickListener() {
//		public void onClick(View arg0) {
//			Intent i = new Intent(Login.this,Bantuan.class);
//			startActivity(i);
//		}});

//		TextView txtLupa=(TextView)findViewById(R.id.btnkeluar);
//		txtLupa.setOnClickListener(new View.OnClickListener() {
//		public void onClick(View arg0) {
//				finish();
//		}});

    }
    public void gagal(){
        new AlertDialog.Builder(this)
                .setTitle("Gagal Login")
                .setMessage("Silakan Cek Account Anda Kembali")
                .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {

                    }})
                .show();
    }

    public void sukses(String item,String ex){
        new AlertDialog.Builder(this)
                .setTitle("Sukses "+ex)
                .setMessage(ex+" data "+item+" Berhasil")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {
//                        Intent i = new Intent(login.this,MainActivity.class);
//                        i.putExtra("xmlbio", xmlbio);
//                        startActivity(i);
                        // finish();
                    }})
                .show();
    }

    class ceklogin extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Proses Login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... params) {

            try {
            	String	username=txtusername.getText().toString().trim();
           		String	password=txtpassword.getText().toString().trim();

                List<NameValuePair> myparams = new ArrayList<NameValuePair>();
                myparams.add(new BasicNameValuePair("username", username));
                myparams.add(new BasicNameValuePair("password", password));

                String url=ip+"pelanggan/pelanggan_login.php";
                Log.v("detail",url);
                JSONObject json = jsonParser.makeHttpRequest(url, "GET", myparams);
                Log.d("detail", json.toString());
                sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    JSONArray myObj = json.getJSONArray(TAG_record); // JSON Array
                    final JSONObject myJSON = myObj.getJSONObject(0);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {

                            	id_pelanggan=myJSON.getString("id_pelanggan");
                                nama_pelanggan=myJSON.getString("nama_pelanggan");
                                    }
                            catch (JSONException e) {e.printStackTrace();}
                        }});
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @SuppressLint("NewApi")
		protected void onPostExecute(String file_url) {

        	pDialog.dismiss();
	        Log.v("SUKSES",id_pelanggan);

        	if(sukses==1){
		        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Login.this);
		        SharedPreferences.Editor editor = sharedPref.edit();
		        editor.putBoolean("Registered", true);
		        editor.putString("id_pelanggan", id_pelanggan);
		        editor.putString("nama_pelanggan", nama_pelanggan);
		        editor.apply();
		        Intent i = new Intent(getApplicationContext(), menuutama2.class);
		        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(i);
		        finish();
        	}
        	else{
        		gagal("Login");
        	}
        }
    }

        public void lengkapi(String item){
	    	new AlertDialog.Builder(this)
			.setTitle("Lengkapi Data")
			.setMessage("Silakan lengkapi data "+item)
			.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dlg, int sumthin) {
				}})
			.show();
	    }

	  public void gagal(String item){
	    	new AlertDialog.Builder(this)
			.setTitle("Gagal Login")
			.setMessage("Login "+item+" ,, Gagal")
			.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dlg, int sumthin) {
				}})
			.show();
	    }

	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
	        	finish();
	                return true;
	        }
	    return super.onKeyDown(keyCode, event);
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode,resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
        {
            if(currentAccessToken==null)
            {
                txtnama_pelanggan.setText("");
                txtemail.setText("");
                Toast.makeText(Login.this,"User Logged Out", LENGTH_LONG).show();
            }
            else
                    loadUserProfile(currentAccessToken );
        }
    };
        private void loadUserProfile(AccessToken newAccessToken)
        {
            GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response)
                {
                    try {
                        String first_name = object.getString("first_name");
                        String last_name = object.getString("last_name");
                        String email = object.getString("email");
                        String id = object.getString("id");

                        txtemail.setText(email);
                        txtnama_pelanggan.setText(first_name + " " +last_name);
                     //   RequestOptions requestOptions = new RequestOptions();
                    //    returnOptions.dontAnimate();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields","first_name,last_name,email,id");
            request.setParameters(parameters);
            request.executeAsync();
        }

}
