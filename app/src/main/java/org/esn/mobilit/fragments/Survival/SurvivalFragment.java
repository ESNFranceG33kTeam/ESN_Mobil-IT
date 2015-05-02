package org.esn.mobilit.fragments.Survival;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.esn.mobilit.R;
import org.esn.mobilit.models.Category;
import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.utils.ApplicationConstants;

/**
 * Created by aymeric on 06/01/15.
 */
public class SurvivalFragment extends Fragment{
    private static final String TAG = SurvivalFragment.class.getSimpleName();
    private SurvivalGuide survivalGuide;
    private Activity currentActivity;

    public void setSurvivalGuide(SurvivalGuide survivalGuide){
        this.survivalGuide = survivalGuide;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.survival, container,false);

        // Set the Text to try this out
        TextView t = (TextView) myInflatedView.findViewById(R.id.survivalContent);


        String survivalContent = "", title, content;

        for (Category category : survivalGuide.getCategories()){
            title = "<h" + (category.getLevel()+1) + "><font color='" + getColorByCategoryLevel(category.getLevel()) + "'>" + category.getName() + "</font><h" + (category.getLevel()+1) + "><br/>";
            content = "<p><font color='" +getColorByCategoryLevel(3)+ "'>" + category.getContent() + "</font></<br/>";

            survivalContent += title + content;
        }

        t.setText(Html.fromHtml(survivalContent), TextView.BufferType.SPANNABLE);

        return myInflatedView;
    }

    public String getColorByCategoryLevel(int level){
        switch (level){
            case 0 : return String.valueOf(getResources().getColor(R.color.esngreen)); //VERT ESN
            case 1 : return String.valueOf(getResources().getColor(R.color.esnpink)); //ROSE ESN
            case 2 : return String.valueOf(getResources().getColor(R.color.esnorange)); //ORANGE ESN
            default : return String.valueOf(getResources().getColor(R.color.esngrey));//BLEU ESN
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        currentActivity = activity;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ras_section_settings:
                if (currentActivity != null) {
                    Log.d(TAG, "RAS SECTION SETTINGS");
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