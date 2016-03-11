package org.esn.mobilit.fragments.Satellite;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.esn.mobilit.R;
import org.esn.mobilit.activities.DetailActivity;
import org.esn.mobilit.activities.FirstLaunchActivity;
import org.esn.mobilit.adapters.ListAdapter;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.feeds.RSSFeedService;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class ListFragment extends android.support.v4.app.ListFragment
{
    private RSSFeedParser feed;
    private RSSFeedService rssFeedService;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListAdapter adapter;

    public void setService(RSSFeedService rssFeedService){
        this.rssFeedService = rssFeedService;
        this.feed = rssFeedService.getFeed();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        adapter = new ListAdapter(feed, inflater);
        this.setListAdapter(adapter);
        if (feed.isEmpty()) {
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_empty);
            SwipeRefreshLayout swipeRefreshListView = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            swipeRefreshListView.setVisibility(View.GONE);
        } else {
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        setHasOptionsMenu(true);



        return view;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);

        Bundle b = new Bundle();
        b.putSerializable("feed", feed.getItem(position));
        intent.putExtras(b);

        startActivity(intent);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ras_section_settings:
                if (getActivity() != null) {
                    PreferencesService.resetSection();
                    Intent intent = new Intent(getActivity(), FirstLaunchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshContent(){
        feed = rssFeedService.getFeed();
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
