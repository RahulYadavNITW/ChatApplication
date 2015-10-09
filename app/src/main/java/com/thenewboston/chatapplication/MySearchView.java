package com.thenewboston.chatapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.SearchView;
import android.widget.ImageView;

/**
 * Created by rajkiran on 14/09/15.
 */
public class MySearchView {
    public static SearchView getSearchView(Context context, String strHint) {
        SearchView searchView = new SearchView(context);
        searchView.setQueryHint(strHint);
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();
        searchView.requestFocusFromTouch();
        int closeButtonId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchBtnClose = (ImageView) searchView.findViewById(closeButtonId);
        //  searchBtnClose.setImageResource(R.mipmap.ic_search);

        SearchView.SearchAutoComplete searchText = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        Drawable leftDrawable = context.getResources().getDrawable(R.mipmap.ic_search);
        searchText.setCompoundDrawables(leftDrawable, null, null, null);
        searchText.setThreshold(1);
        searchText.setTextColor(context.getResources().getColor(R.color.white));
        searchText.setHintTextColor(context.getResources().getColor(R.color.white));

        return searchView;
    }

    public static SearchView getSearchView(Context context, int strHintRes) {
        return getSearchView(context, context.getString(strHintRes));
    }
}
