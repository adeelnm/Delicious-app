package com.client.delicious.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class SignIn extends Activity
{

	EditText		eUsername, ePassword;
	Button			login;
	String			username, password, url;
	ProgressDialog	pd;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.sign_in );

		login = ( Button ) findViewById( R.id.btn_login );
		eUsername = ( EditText ) findViewById( R.id.txt_username );
		ePassword = ( EditText ) findViewById( R.id.txt_password );

		login.setOnClickListener( new OnClickListener()
		{

			@Override
			public void onClick( View arg0 )
			{

				// TODO Auto-generated method stub
				username = eUsername.getText().toString();
				password = ePassword.getText().toString();
				if( !Utils.isNullOrEmpty( username ) && !Utils.isNullOrEmpty( password ) )
				{
					url = AppGlobal.DELICIOUS_API_OAUTH_URL
							+ AppGlobal.CLIENT_ID + "&client_secret="
							+ AppGlobal.CLIENT_SECRET
							+ "&grant_type=credentials&username=" + username
							+ "&password=" + password;
					// Log.d("URL", url);

					new DataFetcher( new IAsyncTask()
					{
						@Override
						public void success( String message )
						{
							// TODO Auto-generated method stub
							pd.dismiss();
							SharedPreferences pref = getSharedPreferences("com.client.delicious.activities",
									Context.MODE_PRIVATE );
							pref.edit().putString( AppGlobal.APP_SHARED_PREF_ACCESS_TOKEN, message ).commit();
							Intent intent = new Intent( SignIn.this,
									ShowBookmarks.class );
							startActivity( intent );
						}

						@Override
						public void fail( String message )
						{

							// TODO Auto-generated method stub
							pd.dismiss();
							AlertDialog.Builder builder = new AlertDialog.Builder( SignIn.this );
							builder.setTitle( "Exception" );
							builder.setMessage( message );
							builder.setPositiveButton( "OK",
									new DialogInterface.OnClickListener()
									{
										@Override
										public void onClick( DialogInterface arg0, int arg1 )
										{
											// TODO Auto-generated method stub
										}
									} );
							builder.show();
							// Toast.makeText(getApplicationContext(), message,
							// Toast.LENGTH_LONG).show();
						}

						@Override
						public void doWait()
						{
							pd = ProgressDialog.show( SignIn.this, "Loading","Fetching Data" );
						}
					} ).execute( new String[] {
							AppGlobal.ACTION_DATAFETCHER_SIGNIN, url } );
				}
				else
				{
					Toast.makeText( SignIn.this,
							"Please enter Username or Password",
							Toast.LENGTH_SHORT ).show();
				}
			}
		} );
	}
}
