package com.client.delicious.activities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.client.delicious.R;
import com.client.delicious.datalayer.DataFetcher;
import com.client.delicious.datalayer.IAsyncTask;
import com.client.delicious.utilities.AppGlobal;
import com.client.delicious.utilities.Utils;

public class SignIn extends Activity {

	EditText eUsername, ePassword;
	Button login;
	String username, password, url;
	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);

		login = (Button) findViewById(R.id.btn_login);
		eUsername = (EditText) findViewById(R.id.txt_username);
		ePassword = (EditText) findViewById(R.id.txt_password);

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				username = eUsername.getText().toString();
				password = ePassword.getText().toString();
				if (!Utils.isNullOrEmpty(username)
						&& !Utils.isNullOrEmpty(password)) {
					url = AppGlobal.DELICIOUS_API_OAUTH_URL
							+ AppGlobal.CLIENT_ID + "&client_secret="
							+ AppGlobal.CLIENT_SECRET
							+ "&grant_type=credentials&username=" + username
							+ "&password=" + password;
					//Log.d("URL", url);

					new DataFetcher(new IAsyncTask() {
						@Override
						public void success(String message) {
							// TODO Auto-generated method stub
							pd.dismiss();
						}

						@Override
						public void fail(String message) {
							// TODO Auto-generated method stub
							pd.dismiss();
							AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
			                builder.setTitle("Exception");
			                builder.setMessage(message);
			                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			                    @Override
			                    public void onClick(DialogInterface arg0, int arg1) {
			                        // TODO Auto-generated method stub
			                        //Toast.makeText(getApplicationContext(), "Ok is clicked", Toast.LENGTH_LONG).show();
			                    }
			                });
			                builder.show();
							//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
						}

						@Override
						public void doWait() {
							// TODO Auto-generated method stub
							// Show Loading Dialog here
							pd = ProgressDialog.show(SignIn.this, "Loading",
									"Fetching Data");
						}
					}).execute(new String[] {
							AppGlobal.ACTION_DATAFETCHER_SIGNIN, url });
				} else {
					Toast.makeText(SignIn.this,
							"Please enter Username or Password",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private class Async extends AsyncTask<String, Void, String> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(SignIn.this, "Loading", "Fetching Data");
		}

		@Override
		protected String doInBackground(String... messageURL) {
			// start building result which will be json string
			StringBuilder messageFeedBuilder = new StringBuilder();
			// should only be one URL, receives array
			for (String searchURL : messageURL) {
				HttpClient messageClient = new DefaultHttpClient();
				try {
					// pass search URL string to fetch
					HttpGet messageGet = new HttpGet(searchURL);
					// execute request
					HttpResponse messageResponse = messageClient
							.execute(messageGet);
					// check status, only proceed if ok
					StatusLine searchStatus = messageResponse.getStatusLine();
					if (searchStatus.getStatusCode() == 200) {
						// get the response
						HttpEntity messageEntity = messageResponse.getEntity();
						InputStream messageContent = messageEntity.getContent();
						// process the results
						InputStreamReader messageInput = new InputStreamReader(
								messageContent);
						BufferedReader messageReader = new BufferedReader(
								messageInput);
						String lineIn;
						while ((lineIn = messageReader.readLine()) != null) {
							messageFeedBuilder.append(lineIn);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// return result string
			// Log.d("testing json", messageFeedBuilder.toString());
			return messageFeedBuilder.toString();
		}

		@Override
		protected void onPostExecute(String result) {

			Bundle b = new Bundle();
			Intent intent = new Intent();
			intent.putExtras(b);
			startActivity(intent);

			pd.dismiss();
		}
	}
}
