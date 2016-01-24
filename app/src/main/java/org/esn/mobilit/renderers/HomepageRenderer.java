package org.esn.mobilit.renderers;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.utils.ApplicationConstants;

public class HomepageRenderer {

    public SpannableStringBuilder renderHomepageText(){

        SpannableStringBuilder text = new SpannableStringBuilder();
        text.append(MobilITApplication.getContext().getResources().getString(R.string.chooseyour) + " "); // Choose your

        SpannableString countrySpan = new SpannableString(MobilITApplication.getContext().getResources().getString(R.string.country));
        countrySpan.setSpan(new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB), 0, countrySpan.length(), 0);
        text.append(countrySpan); // Choose your country
        text.append(", ");// Choose your country ,

        SpannableString citySpan = new SpannableString(MobilITApplication.getContext().getResources().getString(R.string.city));
        citySpan.setSpan(new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB), 0, citySpan.length(), 0);
        text.append(citySpan); // Choose your country, city
        text.append(" "); // Choose your country, city
        text.append(MobilITApplication.getContext().getResources().getString(R.string.and) + " ");// Choose your country ,city and

        SpannableString esnSectionSpan = new SpannableString(MobilITApplication.getContext().getResources().getString(R.string.esnsection));
        esnSectionSpan.setSpan(new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB), 0, esnSectionSpan.length(), 0);
        text.append(esnSectionSpan); // Choose your country, city and ESN Section
        text.append('.');// Choose your country ,city and ESN Section.

        return text;
    }
}