package org.esn.mobilit.fragments.Satellite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.esn.mobilit.R;
import org.esn.mobilit.adapters.CustomListAdapter;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.parser.RSSFeedParser;


public class ListFragment extends android.support.v4.app.ListFragment
{
    private static final String TAG = ListFragment.class.getSimpleName();
    private RSSFeedParser feed;
    private CustomListAdapter adapter;
    private Activity currentActivity;
    private LayoutInflater layoutInflater;

    ListFragmentItemClickListener itemClickListener;

    public void setFeed(RSSFeedParser feed){
        this.feed = feed;
    }

    /** An interface for defining the callback method */
    public interface ListFragmentItemClickListener {
        /** This method will be invoked when an item in the ListFragment is clicked */
        void onListFragmentItemClick(int position, RSSFeedParser feed);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        layoutInflater = (LayoutInflater) currentActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Set an Adapter to the ListView
        adapter = new CustomListAdapter(feed, layoutInflater);
        this.setListAdapter(adapter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        itemClickListener.onListFragmentItemClick(position, feed);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        currentActivity = activity;

        try{
            /** This statement ensures that the hosting activity implements ListFragmentItemClickListener */
            itemClickListener = (ListFragmentItemClickListener) activity;
        }catch(Exception e){
            Toast.makeText(activity.getBaseContext(), "Exception onAttach ListFragment",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ras_section_settings:
                if (currentActivity != null) {
                    reset_section();
                    Intent returnIntent = new Intent();
                    currentActivity.setResult(ApplicationConstants.RESULT_FIRST_LAUNCH,returnIntent);
                    currentActivity.finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // PREFERENCES
    public void setDefaults(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void reset_section(){
        setDefaults("CODE_SECTION", null);
        setDefaults("CODE_COUNTRY", null);
        setDefaults("SECTION_WEBSITE", null);
        setDefaults("regId", null);
    }
}
