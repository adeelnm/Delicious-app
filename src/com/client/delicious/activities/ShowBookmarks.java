package com.client.delicious.activities;

import java.util.Collection;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.client.delicious.R;
import com.client.delicious.datalayer.BasePostsList;
import com.client.delicious.datalayer.Posts;

public class ShowBookmarks extends SherlockActivity
{
	TextView		url, hash;
	String			urlAddress, hashAddress;
	ListView		lv;
	PostsAdapter	postsAdapter;

	@Override
	public void onCreate( Bundle savedInstanceState )
	{

		super.onCreate( savedInstanceState );
		setContentView( R.layout.show_bookmark );
		Log.d( "list", BasePostsList.Posts.toString() );
		lv = ( ListView ) findViewById( R.id.listView_bookmarks );
		postsAdapter = new PostsAdapter( getApplicationContext(), BasePostsList.Posts );
		lv.setAdapter( postsAdapter );

		lv.setOnItemClickListener( new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick( AdapterView<?> av, View v, int pos, long id )
			{

				url = ( TextView ) v.findViewById( R.id.textView_href );
				urlAddress = url.getText().toString();

				Intent browserIntent = new Intent( Intent.ACTION_VIEW );
				browserIntent.setData( Uri.parse( urlAddress ) );
				startActivity( browserIntent );
			}
		} );

		lv.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick( AdapterView<?> arg0, View v, int pos, long id )
			{
				final View view = v;
				AlertDialog.Builder builder = new AlertDialog.Builder( ShowBookmarks.this );
				AlertDialog dialog;
				builder.setTitle( "Options" );
				builder.setMessage( "Please select what you want to do" );
				builder.setPositiveButton( "Edit", new OnClickListener()
				{
					@Override
					public void onClick( DialogInterface arg0, int arg1 )
					{
						finish();
					}
				} );
				builder.setNegativeButton( "Delete" , new OnClickListener()
				{
					@Override
					public void onClick( DialogInterface arg0, int arg1 )
					{
						hash = ( TextView ) view.findViewById( R.id.textView_hash );
						hashAddress = hash.getText().toString(); 
						for(int i=0; i<BasePostsList.Posts.size(); i++)
						{
							if(BasePostsList.Posts.get( i ).hash.equals( hashAddress ))
							{
								BasePostsList.Posts.remove( i );
								postsAdapter.notifyDataSetChanged();
								break;
							}
						}
					}
				} );
				
				builder.setNeutralButton( "Cancel", new OnClickListener()
				{
					@Override
					public void onClick( DialogInterface dialog, int which )
					{
						dialog.dismiss();
					}
				} );
				dialog = builder.show();

				return true;
			}
		} );
	}

	public final class PostsAdapter extends ArrayAdapter<Posts>
	{
		private Context		context;
		private List<Posts>	listPosts;

		public PostsAdapter( final Context c, List<Posts> list )
		{

			super( c, R.layout.bookmarks_list );
			listPosts = list;
			context = c;
		}

		@Override
		public void clear()
		{

			listPosts.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount()
		{

			return listPosts.size();
		}

		@Override
		public void addAll( Collection<? extends Posts> collection )
		{

			listPosts.addAll( collection );
			notifyDataSetChanged();
		}

		@Override
		public View getView( int position, View convertView, ViewGroup parent )
		{

			View row = convertView;
			if( row == null )
			{
				LayoutInflater vi = ( LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
				row = vi.inflate( R.layout.bookmarks_list, null );
			}
			Posts item = listPosts.get( position );
			if( item != null )
			{
				TextView textViewDescription = ( TextView ) row.findViewById( R.id.textView_description );
				TextView textViewHref = ( TextView ) row.findViewById( R.id.textView_href );
				TextView textViewExtended = ( TextView ) row.findViewById( R.id.textView_extended );
				TextView textViewTags = ( TextView ) row.findViewById( R.id.textView_tags );
				TextView textViewTime = ( TextView ) row.findViewById( R.id.textView_time );
				TextView textViewHash = (TextView) row.findViewById( R.id.textView_hash );

				textViewDescription.setText( item.description );
				textViewHref.setText( item.href );
				textViewExtended.setText( item.extended );
				textViewTags.setText( item.tag );
				textViewTime.setText( item.time );
				textViewHash.setText( item.hash );
			}
			return row;
		}
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{

		getSupportMenuInflater().inflate( R.menu.main, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{

		switch ( item.getItemId() )
		{
			case R.id.add_bookmark:
				Intent intent = new Intent( ShowBookmarks.this, AddBookmarks.class );
				startActivity( intent );
				return true;
			case R.id.sign_out:
				SharedPreferences preferences = getSharedPreferences( "PREFERENCE", 0 );
				SharedPreferences.Editor editor = preferences.edit();
				editor.clear();
				editor.commit();
				Intent i = new Intent( ShowBookmarks.this, MainActivity.class );
				startActivity( i );
				finish();
				return true;
				/*
				 * case R.id.sync_bookmark:
				 * return true;
				 */
		}
		return super.onOptionsItemSelected( item );
	}
}
