package com.client.delicious.datalayer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import com.client.delicious.utilities.AppGlobal;

public class DataFetcher extends AsyncTask<String, String, ResponseFetcher>
{

	IAsyncTask		async;
	ResponseFetcher	responseFetcher;
	String			status, bookMarksFeeds, responseXML;
	JSONObject		jObject;

	public DataFetcher( IAsyncTask asyncTask )
	{
		this.async = asyncTask;
	}

	@Override
	protected void onPreExecute()
	{
		async.doWait();
	}

	@Override
	protected ResponseFetcher doInBackground( String... params )
	{
		// TODO Auto-generated method stub
		responseFetcher = new ResponseFetcher();
		responseFetcher.status = -1;
		responseFetcher.message = "";

		if( params != null )
		{
			if( params[0].equals( AppGlobal.ACTION_DATAFETCHER_SIGN_IN ) )
			{
				bookMarksFeeds = ServerHit( params[1] );
				try
				{
					jObject = new JSONObject( bookMarksFeeds );
					status = jObject.getString( "status" );
					if( status.equals( AppGlobal.STATUS_SUCESS ) )
					{

						responseFetcher.status = AppGlobal.RESPONSE_SUCCESS;
						BasePostsList.accessToken = jObject.getString( "access_token" );

						responseXML = ServerHit( AppGlobal.DELICIOUS_API_URL + "all" );

						responseFetcher.serverResponse = responseXML;
						// Log.d( "checking header", responseXML );
					}
					else
					{
						responseFetcher.status = AppGlobal.RESPONSE_FAIL;
						responseFetcher.message = AppGlobal.STATUS_FAIL;
					}
				}
				catch ( JSONException e )
				{
					// TODO Auto-generated catch block
					responseFetcher.status = AppGlobal.RESPONSE_FAIL;
					responseFetcher.message = e.toString();
				}
			}
			else
			{
				if( params[0].equals( AppGlobal.ACTION_DATAFETCHER_ADD_LINKS ) )
				{
					responseFetcher.status = AppGlobal.RESPONSE_SUCCESS;
					responseFetcher.message = params[2];
					responseXML = ServerHit( params[1] );
					responseFetcher.serverResponse = responseXML;
				}
				else
				{
					responseFetcher.status = AppGlobal.RESPONSE_FAIL;
					responseFetcher.message = AppGlobal.STATUS_FAIL;
				}
			}
		}
		return responseFetcher;
	}

	@Override
	protected void onPostExecute( ResponseFetcher result )
	{

		switch ( result.status )
		{
			case AppGlobal.RESPONSE_SUCCESS:
				this.async.success( result.message, result.serverResponse );
				break;

			case AppGlobal.RESPONSE_FAIL:
				this.async.fail( result.message );
				break;
		}
	}

	private String ServerHit( String url )
	{

		StringBuilder messageFeedBuilder = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		try
		{
			// pass search URL string to fetch
			HttpPost httpPost = new HttpPost( url );
			if( responseFetcher.status == AppGlobal.RESPONSE_SUCCESS )
			{
				String access = "Bearer " + BasePostsList.accessToken; // jObject.getString(
																		// "access_token"
																		// );
				httpPost.addHeader( "Authorization", access );
				// httpPost.addHeader( "host", "http://api.delicious.com" );
				// execute request
			}
			HttpResponse httpResponse = httpClient.execute( httpPost );
			// check status, only proceed if ok
			StatusLine searchStatus = httpResponse.getStatusLine();
			if( searchStatus.getStatusCode() == 200 )
			{
				// get the response
				HttpEntity messageEntity = httpResponse.getEntity();
				InputStream messageContent = messageEntity.getContent();
				// process the results
				InputStreamReader messageInput = new InputStreamReader( messageContent );
				BufferedReader messageReader = new BufferedReader( messageInput );
				String lineIn;
				while ( ( lineIn = messageReader.readLine() ) != null )
				{
					messageFeedBuilder.append( lineIn );
				}
			}
			responseFetcher.status = AppGlobal.RESPONSE_SUCCESS;
			responseFetcher.message = "Success";
		}
		catch ( Exception e )
		{
			responseFetcher.status = AppGlobal.RESPONSE_FAIL;
			responseFetcher.message = e.getMessage();
		}
		// Log.d("testing json",accessToken);
		// Log.d("testing json", messageFeedBuilder.toString());
		return messageFeedBuilder.toString();
	}
}
