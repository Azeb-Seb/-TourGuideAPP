package com.azeb.android.newsfeed;



import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsFeed>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    //URL for newsfeed data from the gardian database
    private TextView mEmptyStateTextView;

    private NewsFeedAdapter mAdapter;
    private static final int NEWSFEED_LOADER_ID = 1;

    private static final String GARDIAN_REQUEST_URL = " http://content.guardianapis.com/search?show-tags=contributor&q=debates&api-key=test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find a reference to the {@link ListView} in layout

        ListView newsfeedListView = (ListView)findViewById(R.id.list);

         mEmptyStateTextView =(TextView)findViewById(R.id.empty_view);
        newsfeedListView.setEmptyView(mEmptyStateTextView);

        //create a new adapter that takes an empty list of newsfeeds as input
        mAdapter = new NewsFeedAdapter(this, new ArrayList<NewsFeed>());

        //set the adapter on the {@link ListView}
        //so the list can be populated in the user interface

        newsfeedListView.setAdapter(mAdapter);

        //set onitem click listener on the listview hich sends on intent to a web browse
        //to open a website with more information about the selected newsfeed

        newsfeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //find the current earthquake that was clicked on
                NewsFeed currentNewsFeed = mAdapter.getItem(position);

                //Convert the String URL onject (to pass into the Intent constructor)
                Uri newsFeedUri = Uri.parse(currentNewsFeed.getmUrl());

                //Creat a new intent to view the Newsfeed URI
                Intent websitIntent = new Intent(Intent.ACTION_VIEW, newsFeedUri);

                //Send the intent to launch a new activity
                startActivity(websitIntent);
            }
        });

        //get reffernce to the connectivity to check state of the newtork connectivity

        View loadingIndicator = findViewById(R.id.loading_indicator);
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //get details on the currently activity default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //If there is a network connection fetch data
        if (networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();

            //Initialize the Loader pass in the ID constant difined above and pass in null for
            //the builde pass in the activity for the LoaderCallbacks parameter(which is valid
            // because this activity implements the loadercallbacks interface)
            loaderManager.initLoader(NEWSFEED_LOADER_ID, null,this);

        }else {

            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<NewsFeed>> onCreateLoader(int id, Bundle args) {
        //Create a new Loader for the given URL
        return new NewsFeedLoader(this , GARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsFeed>> loader, List<NewsFeed> newsFeeds) {

        //Hide loading indicator whne the data lodeded
        View loadingindicator = findViewById(R.id.loading_indicator);
        loadingindicator.setVisibility(View.GONE);

        //set empty text
        mEmptyStateTextView.setText(R.string.no_internet);

        //Clear the adapter of previous newsfeed data
        mAdapter.clear();
        //If there is a valid list of {@link NewsFeed}then add them to the adapter's
        //data set. This will trigger the ListView to update.

        if (newsFeeds !=null && !newsFeeds.isEmpty()){

            mAdapter.addAll(newsFeeds);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<NewsFeed>> loader) {

        //Loader reset so we can clear out our existing data.
        mAdapter.clear();

    }


}
