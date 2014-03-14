package com.client.delicious.activities;

import java.util.ArrayList;
import java.util.List;

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
import com.client.delicious.datalayer.BasePostsList;
import com.client.delicious.datalayer.DataFetcher;
import com.client.delicious.datalayer.IAsyncTask;
import com.client.delicious.datalayer.PostXMLParser;
import com.client.delicious.datalayer.Posts;
import com.client.delicious.utilities.AppGlobal;
import com.client.delicious.utilities.Utils;

public class SignIn extends Activity
{
	EditText										eUsername, ePassword;
	com.client.delicious.utilities.ButtonBgChange	login;
	String											username, password, url;
	ProgressDialog									pd;
	List<Posts>										Posts	= new ArrayList<Posts>();

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.sign_in );

		login = ( com.client.delicious.utilities.ButtonBgChange ) findViewById( R.id.btn_login );
		eUsername = ( EditText ) findViewById( R.id.txt_username );
		ePassword = ( EditText ) findViewById( R.id.txt_password );

		login.setOnClickListener( new OnClickListener()
		{
			@Override
			public void onClick( View arg0 )
			{
				username = eUsername.getText().toString();
				password = ePassword.getText().toString();
				if( !Utils.isNullOrEmpty( username ) && !Utils.isNullOrEmpty( password ) )
				{
					url = AppGlobal.DELICIOUS_API_OAUTH_URL + AppGlobal.CLIENT_ID + "&client_secret=" + AppGlobal.CLIENT_SECRET + "&grant_type=credentials&username=" + username + "&password=" + password;

					new DataFetcher( new IAsyncTask()
					{
						@Override
						public void success( String message, String serverResponse )
						{
							pd.dismiss();
							SharedPreferences pref = getSharedPreferences( "com.client.delicious.activities", Context.MODE_PRIVATE );
							pref.edit().putString( AppGlobal.APP_SHARED_PREF_ACCESS_TOKEN, BasePostsList.accessToken ).commit();
							if( PostXMLParser.parse( serverResponse ) )
							{
								Intent intent = new Intent( SignIn.this, ShowBookmarks.class );
								startActivity( intent );
								finish();
							}
						}

						@Override
						public void fail( String message )
						{
							pd.dismiss();
							AlertDialog.Builder builder = new AlertDialog.Builder( SignIn.this );
							builder.setTitle( "Exception" );
							builder.setMessage( message );
							builder.setPositiveButton( "OK", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick( DialogInterface arg0, int arg1 )
								{
								}
							} );
							builder.show();
						}

						@Override
						public void doWait()
						{
							pd = ProgressDialog.show( SignIn.this, "Loading", "Fetching Data" );
						}
					} ).execute( new String[] { AppGlobal.ACTION_DATAFETCHER_SIGN_IN, url } );
				}
				else
				{
					Toast.makeText( SignIn.this, "Please enter Username or Password", Toast.LENGTH_SHORT ).show();
				}
			}
		} );
	}
}
