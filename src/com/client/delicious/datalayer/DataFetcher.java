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
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.client.delicious.utilities.AppGlobal;

public class DataFetcher extends AsyncTask<String, String, ResponseFetcher> 
{
	IAsyncTask async;
	public DataFetcher(IAsyncTask asyncTask) {
		this.async = asyncTask;
	}

	@Override
	protected void onPreExecute() {
		async.doWait();
	}

	@Override
	protected ResponseFetcher doInBackground(String... params) {
		// TODO Auto-generated method stub
		ResponseFetcher responseFetcher = new ResponseFetcher();
		responseFetcher.status = -1;
		responseFetcher.message = "";
		if(params != null)
		{
			if(params[0].equals(AppGlobal.ACTION_DATAFETCHER_SIGNIN))
			{
				try
				{
					StringBuilder messageFeedBuilder = new StringBuilder();
					// should only be one URL, receives array
						HttpClient httpClient = new DefaultHttpClient();
						try {
							// pass search URL string to fetch
							HttpPost httpPost = new HttpPost(params[1]);
							// execute request
							HttpResponse httpResponse = httpClient
									.execute(httpPost);
							// check status, only proceed if ok
							StatusLine searchStatus = httpResponse.getStatusLine();
							if (searchStatus.getStatusCode() == 200) {
								// get the response
								HttpEntity messageEntity = httpResponse.getEntity();
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
							
							responseFetcher.status = AppGlobal.RESPONSE_SUCCESS;
							responseFetcher.message = "Success";
						} catch (Exception e) {
							responseFetcher.status = AppGlobal.RESPONSE_FAIL;
							responseFetcher.message = e.getMessage();
						}
					
					
						JSONObject jObject = new JSONObject(messageFeedBuilder.toString());
						String accessToken = jObject.getString("access_token");
						Log.d("testing json",accessToken);
						//Log.d("testing json", messageFeedBuilder.toString());
					//return messageFeedBuilder.toString();
				}
				catch(Exception ex)
				{
					responseFetcher.status = AppGlobal.RESPONSE_FAIL;
					responseFetcher.message = ex.getMessage();
				}
			}
		}
		
		return responseFetcher;
	}

	@Override
	protected void onPostExecute(ResponseFetcher result) {
		
		switch (result.status) {
		case AppGlobal.RESPONSE_SUCCESS:
				this.async.success(result.message);
			break;
			
		case AppGlobal.RESPONSE_FAIL:
				this.async.fail(result.message);
			break;
		}
		
	}

}
