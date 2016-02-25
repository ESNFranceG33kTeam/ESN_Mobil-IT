package org.esn.mobilit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.renderers.HomepageRenderer;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.R;
import org.esn.mobilit.models.Country;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.CountriesService;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.adapters.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import retrofit.RetrofitError;

public class FirstLaunchActivity extends Activity {

    private static final String TAG = FirstLaunchActivity.class.getSimpleName();

    @Bind(R.id.spinnersLayout) public LinearLayout spinnersLayout;
    @Bind(R.id.startButton)    public Button startButton;
    @Bind(R.id.chooseCountry)  public TextView textView;
    @Bind(R.id.progressBar)    public ProgressBar progressBar;

    private Country currentCountry;
    private Section currentSection;

    private Spinner spinnerCountries;
    private Spinner spinnerSections;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_firstlaunch);

        Fabric.with(this, new Crashlytics());

        String sectionWebsite = PreferencesService.getDefaults("section_website");

        if (!(sectionWebsite == null || sectionWebsite.equalsIgnoreCase(""))) {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        initContent();

        if (Utils.isConnected()){
            CountriesService.getCountries(new NetworkCallback<List<Country>>() {
                @Override
                public void onSuccess(List<Country> result) {
                    initCountriesSpinner(result);
                }

                @Override
                public void onNoAvailableData() {

                }

                @Override
                public void onFailure(RetrofitError error) {

                }
            });
        }
    }

    private void initContent(){
        // Load Butterknife
        ButterKnife.bind(this);

        startButton.setEnabled(false);
        startButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        spinnerCountries = new Spinner(this);

        HomepageRenderer homepageRenderer = new HomepageRenderer();
        SpannableStringBuilder text = homepageRenderer.renderHomepageText();
        textView.setText(text, TextView.BufferType.SPANNABLE);
    }

    private void initCountriesSpinner(final List<Country> countries){
        ArrayList<String> datas = new ArrayList<String>();

        datas.add(getResources().getString(R.string.selectyourcountry));
        for(Country country : countries){
            datas.add(country.getName());
        }

        spinnerCountries.setSelection(0);
        spinnerCountries.setAdapter(new SpinnerAdapter(FirstLaunchActivity.this, datas));
        spinnerCountries.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        if (position != 0) {
                            currentCountry = countries.get(position - 1);
                            initSectionsSpinner();
                        }
                    }
                    public void onNothingSelected(AdapterView<?> arg0) {}
                }
        );
        progressBar.setVisibility(View.INVISIBLE);
        spinnersLayout.addView(spinnerCountries);
    }

    private void initSectionsSpinner(){
        progressBar.setVisibility(View.VISIBLE);
        if (spinnerSections != null) {
            spinnersLayout.removeView(spinnerSections);
        }

        spinnerSections = new Spinner(this);

        ArrayList<String> datas = new ArrayList<String>();

        datas.add(getResources().getString(R.string.selectyoursection));
        for(Section section : currentCountry.getSections()){
            datas.add(section.getName());
        }

        spinnerSections.setSelection(0);
        spinnerSections.setAdapter(new SpinnerAdapter(FirstLaunchActivity.this,datas));
        spinnerSections.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        if (position != 0) {
                            currentSection = currentCountry.getSections().get(position-1);
                            startButton.setEnabled(true);
                            startButton.setVisibility(View.VISIBLE);
                        }
                    }
                    public void onNothingSelected(AdapterView<?> arg0) {}
                }
        );
        progressBar.setVisibility(View.INVISIBLE);
        spinnersLayout.addView(spinnerSections);
    }

    public void launchSplashActivity(View view){
        PreferencesService.setDefaults("code_country",currentCountry.getCodeCountry());
        PreferencesService.setDefaults("code_section", currentSection.getCode_section());
        PreferencesService.setDefaults("section_website", currentSection.getWebsite());

        CacheService.saveObjectToCache("country", currentCountry);
        CacheService.saveObjectToCache("section", currentSection);

        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
