package com.wahid.toko.helper;

import android.view.View;

import com.wahid.toko.modelsList.catSubCatlistModel;

public interface CatSubCatOnclicklinstener {
    void onItemClick(catSubCatlistModel item);
    void onItemTouch(catSubCatlistModel item);
    void addToFavClick(View v, String position);

}
