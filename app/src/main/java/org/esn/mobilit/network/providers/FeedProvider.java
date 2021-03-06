package org.esn.mobilit.network.providers;

import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;
import retrofit.http.GET;

public class FeedProvider {

    public static final String TAG = "FeedProvider";

    private interface FeedProviderInterface{
        @GET(ApplicationConstants.EVENTS_PATH + ApplicationConstants.FEED_PATH)
        void getEvents(Callback<RSS> callback);

        @GET(ApplicationConstants.NEWS_PATH + ApplicationConstants.FEED_PATH)
        void getNews(Callback<RSS> callback);

        @GET(ApplicationConstants.PARTNERS_PATH + ApplicationConstants.FEED_PATH)
        void getPartners(Callback<RSS> callback);
    }

    public static void makeEventRequest(String sectionWebsite, final NetworkCallback<RSS> callback) {
        FeedProviderInterface feedProvider = createBuilder(sectionWebsite);
        feedProvider.getEvents(createCallback(callback));
    }

    public static void makeNewsRequest(String sectionWebsite, final NetworkCallback<RSS> callback) {
        FeedProviderInterface feedProvider = createBuilder(sectionWebsite);
        feedProvider.getNews(createCallback(callback));
    }

    public static void makePartnersRequest(String sectionWebsite, final NetworkCallback<RSS> callback) {
        FeedProviderInterface feedProvider = createBuilder(sectionWebsite);
        feedProvider.getPartners(createCallback(callback));
    }

    private static FeedProviderInterface createBuilder(String sectionWebsite) {
        return new RestAdapter.Builder()
                .setEndpoint(sectionWebsite)
                .setConverter(new SimpleXMLConverter())
                .build()
                .create(FeedProviderInterface.class);
    }

    private static Callback<RSS> createCallback(final NetworkCallback<RSS> callback) {
        return new Callback<RSS>() {
            @Override
            public void success(RSS result, Response response) {
                if (result.getListSize() == 0) {
                    callback.onNoAvailableData();
                } else {
                    callback.onSuccess(result);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Crashlytics.logException(error);
                callback.onFailure(error.getMessage());
            }
        };
    }
}
