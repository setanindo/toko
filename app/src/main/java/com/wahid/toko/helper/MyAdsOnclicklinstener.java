package com.wahid.toko.helper;

import android.view.View;

import com.wahid.toko.modelsList.myAdsModel;

public interface MyAdsOnclicklinstener {

    void onItemClick(myAdsModel item);
    void delViewOnClick(View v, int position);
    void editViewOnClick(View v, int position);

}
