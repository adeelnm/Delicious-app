package com.client.delicious.utilities;

public class AppGlobal
{

	public static final String	CLIENT_ID						= "4ee6eefb6e7b7fdd0c2fc43bbabd6109";
	public static final String	CLIENT_SECRET					= "94237dc31e9bc196647fa48368ba6bcf";
	public static final String	APP_SHARED_PREF_ACCESS_TOKEN	= "access token";
	public static final String	DELICIOUS_API_OAUTH_URL			= "https://avosapi.delicious.com/api/v1/oauth/token?client_id=";
	public static final String	DELICIOUS_API_URL				= "https://api.delicious.com/v1/posts/";
	public static final String	STATUS_SUCESS					= "success";
	public static final String	STATUS_FAIL						= "invalid_credentials";

	// //Response Status Codes
	public static final int		RESPONSE_SUCCESS				= 1;
	public static final int		RESPONSE_FAIL					= 2;

	// /DataFetcher Actions Constants
	public static final String	ACTION_DATAFETCHER_SIGNIN		= "signIn";
	public static final String	ACTION_DATAFETCHER_GET_LINKS	= "fetch";
}
