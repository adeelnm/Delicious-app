package com.client.delicious.datalayer;

public interface IAsyncTask
{

	public void fail( String message );

	public void success( String message, String serverResponse );

	public void doWait();

}
