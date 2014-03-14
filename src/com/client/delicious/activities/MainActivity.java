package com.client.delicious.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.client.delicious.R;
import com.client.delicious.utilities.AppGlobal;
import com.client.delicious.utilities.Utils;

public class MainActivity extends SherlockActivity
{
	Button	sign_in, join;
	String 	accessToken;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		SharedPreferences pref = getSharedPreferences( "com.client.delicious.activities", Context.MODE_PRIVATE );
		accessToken = pref.getString( AppGlobal.APP_SHARED_PREF_ACCESS_TOKEN, "" );
		
		/*if(!Utils.isNullOrEmpty( accessToken ))
		{
			Intent intent = new Intent( MainActivity.this, ShowBookmarks.class );
			startActivity( intent );
		}*/
		
		sign_in = ( Button ) findViewById( R.id.btn_signin );
		join = ( Button ) findViewById( R.id.btn_join );

		sign_in.setOnClickListener( new OnClickListener()
		{
			@Override
			public void onClick( View arg0 )
			{
				Intent intent = new Intent( MainActivity.this, SignIn.class );
				startActivity( intent );
			}
		} );

		join.setOnClickListener( new OnClickListener()
		{
			@Override
			public void onClick( View arg0 )
			{
				String url = "http://www.delicious.com";
				Intent browserIntent = new Intent( Intent.ACTION_VIEW );
				browserIntent.setData( Uri.parse( url ) );
				startActivity( browserIntent );
			}
		} );
	}
}
