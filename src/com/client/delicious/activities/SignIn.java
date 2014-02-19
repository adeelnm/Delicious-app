package com.client.delicious.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.client.delicious.R;
import com.client.delicious.datalayer.AppGlobal;

public class SignIn extends Activity {
	
	EditText eUsername, ePassword;
	Button login;
	String Username,Password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);
		
		login = (Button)findViewById(R.id.btn_login);
		eUsername = (EditText)findViewById(R.id.txt_username);
		ePassword = (EditText)findViewById(R.id.txt_password);
		Username = eUsername.getText().toString();
		Password = ePassword.getText().toString();
		
		String URL = AppGlobal.DELICIOUS_API_OAUTH_URL + AppGlobal.CLIENT_ID + "&client_secret=" + AppGlobal.CLIENT_SECRET 
				+ "&grant_type=credentials&username=" + Username + "&password=" + Password; 
		Log.d("URL", URL);
	}
}
