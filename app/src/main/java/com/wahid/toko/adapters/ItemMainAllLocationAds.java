package com.wahid.toko.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wahid.toko.R;

import com.wahid.toko.helper.OnItemClickListener;
import com.wahid.toko.modelsList.homeCatListModel;
import com.wahid.toko.utills.RoundedTransformation;
import com.wahid.toko.utills.SettingsMain;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by taimu on 2/6/2018.
 */

public class ItemMainAllLocationAds extends RecyclerView.Adapter<ItemMainAllLocationAds.MyViewHolder> {
    private ArrayList<homeCatListModel> list;
    private OnItemClickListener onItemClickListener;
    private Context mContext;
    private SettingsMain settingsMain;
    private int noOfCol;

    public ItemMainAllLocationAds(Context context, ArrayList<homeCatListModel> Data, int noOfCol) {
        this.list = Data;
        this.mContext = context;
        this.noOfCol = noOfCol;
        settingsMain = new SettingsMain(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemof_location_ads, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        int dimensionInPixel = 90;
//
//        if (noOfCol == 2) {
//            dimensionInPixel = 110;
//        }
//        if (noOfCol == 3) {
//            dimensionInPixel = 90;
//        }
//        if (noOfCol == 4) {
//            dimensionInPixel = 70;
//        }
//
//        holder.imageView.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, mContext.getResources().getDisplayMetrics());


//    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//        Shader textShader3=new LinearGradient(70, 50, 40, 20, new int[]{mContext.getColor(R.color.gradientthird),
//                mContext.getColor(R.color.gradientsecond),mContext.getColor(R.color.gradientFirst)}, new float[]{0, 1,2}, Shader.TileMode.REPEAT);
//
//        holder.textViewTitle.getPaint().setShader(textShader3);
//    }
        final homeCatListModel feedItem = list.get(position);
        if (!TextUtils.isEmpty(feedItem.getThumbnail())) {
            Picasso.with(mContext).load(feedItem.getThumbnail())
                    .resize(270, 270)
                    .transform(new RoundedTransformation(10, 0))
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
            holder.textViewTitle.setText(feedItem.getTitle());
            holder.textViewAllAds.setText(feedItem.getAdsCount());
        }

        View.OnClickListener listener2 = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(feedItem);
            }
        };

        holder.itemLocation.setOnClickListener(listener2);
        holder.relativeLayout.setOnClickListener(listener2);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewAllAds;
        ImageView imageView, adsLocation;
        FrameLayout itemLocation;
        RelativeLayout relativeLayout;

        MyViewHolder(View v) {
            super(v);

            textViewTitle = v.findViewById(R.id.text_view_name);
            textViewAllAds = v.findViewById(R.id.total_ads);
            adsLocation = v.findViewById(R.id.adsLocation);
            imageView = v.findViewById(R.id.image_view);
            itemLocation = v.findViewById(R.id.itemLocation);
            relativeLayout = v.findViewById(R.id.linear_layout_card_view);


        }
    }
        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

}

