package com.client.delicious.utilities;

public class Utils
{

	public static boolean isNullOrEmpty( String str )
	{

		if( str != null && !str.equals( "" ) )
			return false;
		return true;
	}
}
