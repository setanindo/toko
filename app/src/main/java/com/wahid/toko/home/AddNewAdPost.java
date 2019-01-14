package com.wahid.toko.home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import jp.wasabeef.richeditor.RichEditor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.wahid.toko.R;
import com.wahid.toko.SplashScreen;
import com.wahid.toko.ad_detail.Ad_detail_activity;
import com.wahid.toko.adapters.ItemEditImageAdapter;
import com.wahid.toko.adapters.PlaceArrayAdapter;
import com.wahid.toko.adapters.SpinnerAndListAdapter;
import com.wahid.toko.helper.MyAdsOnclicklinstener;
import com.wahid.toko.helper.OnStartDragListener;
import com.wahid.toko.helper.SimpleItemTouchHelperCallback;
import com.wahid.toko.helper.WorkaroundMapFragment;
import com.wahid.toko.modelsList.myAdsModel;
import com.wahid.toko.modelsList.subcatDiloglist;
import com.wahid.toko.utills.Admob;
import com.wahid.toko.utills.AnalyticsTrackers;
import com.wahid.toko.utills.CircularProgressBar;
import com.wahid.toko.utills.GPSTracker;
import com.wahid.toko.utills.Network.RestService;
import com.wahid.toko.utills.SettingsMain;
import com.wahid.toko.utills.UrlController;

import static com.google.android.gms.location.places.AutocompleteFilter.TYPE_FILTER_ADDRESS;
import static com.google.android.gms.location.places.AutocompleteFilter.TYPE_FILTER_REGIONS;


public class AddNewAdPost extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        OnStartDragListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private static final String TAG = "Sample";
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";

    protected GoogleMap mMap;
    AutoCompleteTextView mAutocompleteTextView;
    List<View> allViewInstanceforCustom = new ArrayList<>();
    List<View> requiredFields = new ArrayList<>();
    List<View> requiredFieldsForCustom = new ArrayList<>();
    List<View> page1RequiredFields = new ArrayList<>();
    List<View> page2RequiredFields = new ArrayList<>();

    JSONObject jsonObject, jsonObjectforCustom;
    TextView textViewUserName, textViewUserPhone, textViewLat, textViewLong, textViewLocation, textViewTitle,
            textViewadCountry, bumpAdTV, featureAdTV, btnPostAd, featureAdByPackages;
    Spinner spinnerLocation;
    TextView btnImageChooser, btnSelectPix;
    EditText editTextUserName, editTextUserPhone,
            editTextUserLat, editTextuserLong;
    Activity context;
    FrameLayout frameLayout;
    SettingsMain settingsMain;
    LinearLayout linearLayoutCustom;
    String catID;
    boolean ison = false;
    List<View> allViewInstance = new ArrayList<>();
    LinearLayout page1Layout, page2Layout, linearLayoutMapView;
    CircularProgressBar c3;
    int imageLimit;
    String stringImageLimitText;
    LinearLayout page1, page2, page3, linearLayoutImageSection, showHideLocation;
    ImageView imageViewNext1, imageViewNext2, imageViewBack1, imageViewBack2;
    CardView cardViewPriceInput, cardViewtBidingTimer;
    EditText editTextPrice, editTextTitle, editTexttBidingTimer;
    boolean edittextShowHide = true, HasPrice = true;
    List<File> allFile = new ArrayList<>();
    int addId;
    HorizontalScrollView horizontalScrollView;
    ItemEditImageAdapter itemEditImageAdapter;
    ArrayList<myAdsModel> myImages;
    RecyclerView recyclerView;
    RestService restService;
    RelativeLayout bumAdLayout, featureAdLayout;
    CheckBox featureAdChkBox, chkBxBumpAd;
    private NestedScrollView mScrollView;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private Boolean spinnerTouched = false;
    private ArrayList<Image> images = new ArrayList<>();
    private TextView textViewInfoforDrag;
    private CardView cardViewPriceType;
    private Spinner spinnershow;
    SwitchDateTimeDialogFragment dateTimeFragment;


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("addfadsfa", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);

            if (mMap != null) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

                editTextuserLong.setText(String.format("%s", place.getLatLng().longitude));
                editTextUserLat.setText(String.format("%s", place.getLatLng().latitude));
            }
            Log.e("addfadsfa", "Place query did not complete. Error: " +
                    place.getLatLng().toString());
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            String placeId = null;
            if (item != null) {
                placeId = String.valueOf(item.placeId);
            }
            if (item != null) {
                Log.i("sdfsdf", "Selected: " + item.description);
            }
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_ad_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settingsMain = new SettingsMain(this);
        context = AddNewAdPost.this;
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(settingsMain.getMainColor()));
        }

        toolbar.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));

        mScrollView = (NestedScrollView) findViewById(R.id.scrollView);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.editorToolbar);

        WorkaroundMapFragment mapFragment = ((WorkaroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map));
        mapFragment.setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {

                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });

        mapFragment.getMapAsync(this);

        editTextUserName = (EditText) findViewById(R.id.yourNameET);
        editTextUserPhone = (EditText) findViewById(R.id.phoneNumberET);
        frameLayout = (FrameLayout) findViewById(R.id.frame);

        btnPostAd = (TextView) findViewById(R.id.postAd);
        btnImageChooser = (TextView) findViewById(R.id.Gallary);
        btnSelectPix = (TextView) findViewById(R.id.selectPix);

        btnPostAd.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));

        textViewInfoforDrag = (TextView) findViewById(R.id.textView27);
        textViewInfoforDrag.setVisibility(View.GONE);

        page1Layout = (LinearLayout) findViewById(R.id.customLayout1);
        page2Layout = (LinearLayout) findViewById(R.id.customLayout2);
        linearLayoutCustom = (LinearLayout) findViewById(R.id.customFieldLayout);
        linearLayoutMapView = (LinearLayout) findViewById(R.id.mapViewONOFF);

        editTextUserLat = (EditText) findViewById(R.id.latET);
        editTextuserLong = (EditText) findViewById(R.id.longET);

        page1 = (LinearLayout) findViewById(R.id.line1);
        page2 = (LinearLayout) findViewById(R.id.line2);
        page3 = (LinearLayout) findViewById(R.id.line3);
        showHideLocation = (LinearLayout) findViewById(R.id.line4);

        linearLayoutImageSection = (LinearLayout) findViewById(R.id.ll11);
        restService = UrlController.createService(RestService.class, settingsMain.getUserEmail(), settingsMain.getUserPassword(), this);

        recyclerView = (RecyclerView) findViewById(R.id.cardView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        GridLayoutManager MyLayoutManager = new GridLayoutManager(context, 3);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(MyLayoutManager);


        imageViewBack1 = (ImageView) findViewById(R.id.back1);
        imageViewBack2 = (ImageView) findViewById(R.id.back2);
        imageViewNext1 = (ImageView) findViewById(R.id.next1);
        imageViewNext2 = (ImageView) findViewById(R.id.next2);

        page1.setVisibility(View.VISIBLE);
        page2.setVisibility(View.GONE);
        page3.setVisibility(View.GONE);
        showHideLocation.setVisibility(View.GONE);


        btnPostAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean b = false;

                try {
                    JSONObject jsonObj = jsonObject.getJSONObject("data").getJSONObject("profile");
                    if (jsonObj.getBoolean("ad_country_show")) {
                        if (spinnerLocation.getSelectedItemPosition() == 0) {
                            setSpinnerError(spinnerLocation);
                            spinnerLocation.requestFocus();
                            b = true;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (editTextUserName.getText().toString().isEmpty()) {
                    b = true;

                }
                if (mAutocompleteTextView.getText().toString().isEmpty()) {
                    b = true;
                }

                if (edittextShowHide) {
                    if (editTextPrice != null) {
                        if (editTextPrice.getText().toString().equals("")) {
                            editTextPrice.setError("!");
                            b = true;
                        }
                    }
                }
                if (b) {
                    if (editTextUserName.getText().toString().isEmpty()) {
                        editTextUserName.requestFocus();
                        editTextUserName.setError("!");
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                    if (mAutocompleteTextView.getText().toString().isEmpty()) {
                        mAutocompleteTextView.requestFocus();
                        mAutocompleteTextView.setError("!");
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                    if (spinnerLocation.getSelectedItemPosition() == 0) {
                        setSpinnerError(spinnerLocation);
                        spinnerLocation.requestFocus();
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    adforest_submitQuery(adforest_getDataFromDynamicViews());
                }
            }
        });

        btnSelectPix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageLimit > 0)
                    ImagePicker.with(context)                         //  Initialize ImagePicker with activity or fragment context
                            .setToolbarColor("#212121")         //  Toolbar color
                            .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                            .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                            .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                            .setProgressBarColor("#4CAF50")     //  ProgressBar color
                            .setBackgroundColor("#212121")      //  Background color
                            .setCameraOnly(false)               //  Camera mode
                            .setMultipleMode(true)              //  Select multiple images or single image
                            .setFolderMode(false)               //  Folder mode
                            .setShowCamera(true)                //  Show camera button
                            .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                            .setDoneTitle("Done")               //  Done button title
                            .setLimitMessage("You have reached selection limit")    // Selection limit message
                            .setMaxSize(imageLimit)             //  Max images can be selected
                            .setSavePath("ImagePicker")         //  Image capture folder name
                            .setSelectedImages(images)          //  Selected images
                            .start();
                else {
                    Toast.makeText(context, "You can not upload more images", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageViewNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adforest_page1Validation()) {
                    page1.setVisibility(View.GONE);
                    page3.setVisibility(View.GONE);
                    page2.setVisibility(View.VISIBLE);
                    frameLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_out));
                }
                else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();

                }
//                else {
//                    editTextTitle.setError("");
//                    mScrollView.scrollTo(0, 0);
//                }
            }
        });
        imageViewNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adforest_page2Validation()) {
                    page1.setVisibility(View.GONE);
                    page2.setVisibility(View.GONE);
                    page3.setVisibility(View.VISIBLE);
                    frameLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.right_enter));
                }
                else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();

                }
            }
        });
        imageViewBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page3.setVisibility(View.GONE);
                page2.setVisibility(View.GONE);
                page1.setVisibility(View.VISIBLE);
                frameLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_enter));
            }
        });
        imageViewBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page1.setVisibility(View.GONE);
                page3.setVisibility(View.GONE);
                page2.setVisibility(View.VISIBLE);
                frameLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_enter));
            }
        });

        textViewTitle = (TextView) findViewById(R.id.textUserProfileTV);
        textViewUserName = (TextView) findViewById(R.id.yourNameTV);
        textViewUserPhone = (TextView) findViewById(R.id.phoneNumberTV);
        textViewLat = (TextView) findViewById(R.id.latTV);
        textViewLong = (TextView) findViewById(R.id.longTV);
        textViewLocation = (TextView) findViewById(R.id.locationTV);
        textViewadCountry = (TextView) findViewById(R.id.adCountryTV);
        spinnerLocation = (Spinner) findViewById(R.id.spinnerLocation);

        bumAdLayout = (RelativeLayout) findViewById(R.id.bumAdLayout);
        featureAdLayout = (RelativeLayout) findViewById(R.id.featureAdLayout);
        bumpAdTV = (TextView) findViewById(R.id.bumpAdTV);
        chkBxBumpAd = (CheckBox) findViewById(R.id.chkBxBumpAd);
        featureAdTV = (TextView) findViewById(R.id.featureAdTV);
        featureAdChkBox = (CheckBox) findViewById(R.id.featureAdChkBox);
        featureAdByPackages = (TextView) findViewById(R.id.featureAdByPackages);
//        featureAdByPackages.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));

        bumAdLayout.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        featureAdLayout.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        spinnerLocation.setFocusable(true);
        editTextUserName.setFocusable(true);
        mAutocompleteTextView.setFocusable(true);

        AutocompleteFilter.Builder typeFilter = new AutocompleteFilter.Builder();
        if (SplashScreen.gmap_has_countries) {
            typeFilter.setTypeFilter(Place.TYPE_COUNTRY)
                    .setCountry(SplashScreen.gmap_countries);
        }
        if (settingsMain.getAlertDialogMessage("location_type").equals("regions")) {
            typeFilter
                    .setTypeFilter(TYPE_FILTER_ADDRESS);
        } else {
            typeFilter
                    .setTypeFilter(TYPE_FILTER_REGIONS);
        }
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1, typeFilter.build());
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        c3 = (CircularProgressBar) findViewById(R.id.circularprogressbar1);
        c3.setProgress(0);
        c3.setShadow(1);
        c3.setHasShadow(true);
        c3.setVisibility(View.GONE);

        btnImageChooser.setVisibility(View.VISIBLE);

        // get view from server
        adforest_getViews();
        if (settingsMain.getBannerShow()) {
            adforest_bannersAds();
        }
    }

    public boolean adforest_page1Validation() {

        boolean b = true;

        for (int i = 0; i < page1RequiredFields.size(); i++) {
            if (page1RequiredFields.get(i) instanceof Spinner) {
                Spinner spinner = (Spinner) page1RequiredFields.get(i);
                if (spinner.getSelectedItemPosition() == 0) {
                    setSpinnerError(spinner);
                    spinner.requestFocus();
                    b = false;
                    break;
                }
            }
            if (page1RequiredFields.get(i) instanceof EditText) {
                EditText editText = (EditText) page1RequiredFields.get(i);
                if (editText.getText().toString().equals("")) {
                    if (edittextShowHide) {
                        if (editTextPrice != null) {
                            if (editTextPrice.getText().toString().trim().equals("")) {
                                editTextPrice.setError("!");
                                editText.requestFocus();
                                b = false;
                                break;
                            }
                        }
                    }
                    if (editTextPrice != null && !edittextShowHide) {
                        b = true;
                    } else {
                        editText.setError("!");
                        editText.requestFocus();
                        b = false;
                        break;
                    }
                }
            }

        }
        return b;
    }

    public boolean adforest_page2Validation() {
        boolean b = true;

        for (int i = 0; i < requiredFieldsForCustom.size(); i++) {
            if (requiredFieldsForCustom.get(i) instanceof Spinner) {
                Spinner spinner = (Spinner) requiredFieldsForCustom.get(i);
                if (spinner.getSelectedItemPosition() == 0) {
                    setSpinnerError(spinner);
                    b = false;
                    break;
                }
            }
            if (requiredFieldsForCustom.get(i) instanceof EditText) {
                EditText editText = (EditText) requiredFieldsForCustom.get(i);
                if (editText.getText().toString().trim().equals("")) {
                    editText.setError("!");
                    editText.requestFocus();
                    b = false;
                    break;
                }
            }

            if (requiredFieldsForCustom.get(i) instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) requiredFieldsForCustom.get(i);
                boolean b1 = false;
                for (int j = 1; j < linearLayout.getChildCount(); j++) {
                    CheckBox checkBox = (CheckBox) linearLayout.getChildAt(j);
                    if (checkBox.isChecked()) {
                        b1 = true;
                        break;
                    }
                }
                if (!b1) {
                    CheckBox checkBox = (CheckBox) linearLayout.getChildAt(1);
                    checkBox.setError("!");
                    checkBox.requestFocus();
                    b = false;
                    break;
                } else {
                    CheckBox checkBox = (CheckBox) linearLayout.getChildAt(1);
                    checkBox.setError(null);
                }
            }
        }
        if (edittextShowHide) {
            if (editTextPrice != null) {
                if (editTextPrice.getText().toString().equals("")) {
                    editTextPrice.setError("!");
                    editTextPrice.requestFocus();
                    b = false;
                }
            }
        }

        for (int i = 0; i < page2RequiredFields.size(); i++) {
            if (page2RequiredFields.get(i) instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) page2RequiredFields.get(i);
                TextView textView = (TextView) linearLayout.getChildAt(0);
                RichEditor editText = (RichEditor) linearLayout.getChildAt(1);

                if (editText.getHtml() == null || editText.getHtml().equals("")) {
                    textView.setError("!");
                    textView.requestFocus();
                    editText.requestFocus();
                    b = false;
                } else {
                    textView.setError(null);
                }
            }
            if (page2RequiredFields.get(i) instanceof EditText) {
                EditText editText = (EditText) page2RequiredFields.get(i);
                if (editText.getText().toString().equals("")) {
                    if (edittextShowHide) {
                        if (editTextPrice != null) {
                            if (editTextPrice.getText().toString().equals("")) {
                                editTextPrice.setError("!");
                                editText.requestFocus();
                                b = false;
                                break;
                            }
                        }
                    }
                    if (editTextPrice != null && !edittextShowHide) {
                        b = true;
                    } else {
                        editText.setError("!");
                        editText.requestFocus();
                        b = false;
                        break;
                    }
                }
            }

        }
        return b;
    }

    public void adforest_bannersAds() {
        if (settingsMain.getAdsShow() && !settingsMain.getBannerAdsId().equals("")) {
            if (settingsMain.getAdsPostion().equals("top")) {
                LinearLayout frameLayout = (LinearLayout) findViewById(R.id.adVi);
                Admob.adforest_Displaybanners(AddNewAdPost.this, frameLayout);
            } else {
                LinearLayout frameLayout = (LinearLayout) findViewById(R.id.adViBottom);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.bottomMargin = 80;
                mScrollView.setLayoutParams(layoutParams);
                Admob.adforest_Displaybanners(this, frameLayout);
            }
        }
    }

    //APi for submiting post to server
    private void adforest_submitQuery(JsonObject params) {

        if (jsonObjectforCustom != null && adforest_getDataFromDynamicViewsForCustom() != null) {
            params.add("custom_fields", adforest_getDataFromDynamicViewsForCustom());
        }

        JSONObject jsonObj;
        try {

            if (itemEditImageAdapter != null) {
                if (itemEditImageAdapter.getItemCount() > 0)
                    params.addProperty("images_arr", itemEditImageAdapter.getAllTags());
            } else
                params.addProperty("images_arr", "");
            params.addProperty("ad_id", addId);
            jsonObj = jsonObject.getJSONObject("data").getJSONObject("profile");

            params.addProperty(jsonObj.getJSONObject("name").getString("field_type_name"), editTextUserName.getText().toString());
            try {
                String phoneNumber = editTextUserPhone.getText().toString();
                if (jsonObj.getBoolean("is_phone_verification_on")) {
                    if (phoneNumber.contains("+"))
                        params.addProperty(jsonObj.getJSONObject("phone").getString("field_type_name"), phoneNumber);
                    else
                        params.addProperty(jsonObj.getJSONObject("phone").getString("field_type_name"), "+" + phoneNumber);
                } else
                    params.addProperty(jsonObj.getJSONObject("phone").getString("field_type_name"), phoneNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.addProperty(jsonObj.getJSONObject("location").getString("field_type_name"), mAutocompleteTextView.getText().toString());
            params.addProperty(jsonObj.getJSONObject("map").getJSONObject("location_lat").getString("field_type_name"), editTextUserLat.getText().toString());
            params.addProperty(jsonObj.getJSONObject("map").getJSONObject("location_long").getString("field_type_name"), editTextuserLong.getText().toString());
            if (jsonObj.getBoolean("ad_country_show")) {
                subcatDiloglist subDiloglist = (subcatDiloglist) spinnerLocation.getSelectedView().getTag();
                params.addProperty("ad_country", subDiloglist.getId());
            }
            if (jsonObj.getBoolean("featured_ad_is_show")) {
                if (featureAdChkBox.isChecked()) {
                    params.addProperty(jsonObj.getJSONObject("featured_ad").getString("field_type_name"), "true");
                } else
                    params.addProperty(jsonObj.getJSONObject("featured_ad").getString("field_type_name"), "false");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (SettingsMain.isConnectingToInternet(context)) {

            SettingsMain.showDilog(context);
            Log.d("info adPost Data", "" + params.toString());
            Call<ResponseBody> myCall = restService.postAdNewPost(params, UrlController.AddHeaders(this));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info AdPost Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                Log.d("info AdPost object", response.toString());

                                Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(AddNewAdPost.this, Ad_detail_activity.class);
                                intent.putExtra("adId", response.getJSONObject("data").getString("ad_id"));
                                startActivity(intent);

                                AddNewAdPost.this.finish();
                            } else {
                                Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        SettingsMain.hideDilog();
                    } catch (JSONException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    } catch (IOException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    }
                    SettingsMain.hideDilog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    SettingsMain.hideDilog();
                    Log.d("info AdPost error", String.valueOf(t));
                    Log.d("info AdPost error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                }
            });
        } else {
            SettingsMain.hideDilog();
            Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

    // getting images selected from gallery for post and sending them to server
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            int inImage = 0;
            if (images.size() > 0) {
                btnImageChooser.setText(String.valueOf(images.size()));
                List<MultipartBody.Part> parts = new ArrayList<>();

                btnImageChooser.setVisibility(View.GONE);
                btnSelectPix.setEnabled(false);
                c3.setVisibility(View.VISIBLE);

                for (Image image : images) {
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getPath());

                    Uri tempUri = SettingsMain.decodeFile(context, persistImage(bitmap, "abc"));
                    parts.add(adforestst_prepareFilePart("file" + inImage, tempUri));
                    inImage++;
                }
                String ad_id = Integer.toString(addId);
                RequestBody adID =
                        RequestBody.create(
                                okhttp3.MultipartBody.FORM, ad_id);

                if (SettingsMain.isConnectingToInternet(context)) {

                    Log.d("info SendImage", addId + "");
                    Call<ResponseBody> req = restService.postUploadImage(adID, parts, UrlController.UploadImageAddHeaders(context));
                    Log.d("responce", "asdsad");

                    req.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if (response.isSuccessful()) {

                                Log.v("info Upload", response.toString());
                                JSONObject responseobj = null;
                                try {
                                    responseobj = new JSONObject(response.body().string());
                                    Log.d("info UploadImage object", "" + responseobj.getJSONObject("data").toString());
                                    if (responseobj.getBoolean("success")) {
                                        adforest_showProgress();
                                        updateImagesList(responseobj.getJSONObject("data"));
                                        images.clear();
                                    } else {
                                        Toast.makeText(context, responseobj.get("message").toString(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    SettingsMain.hideDilog();
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    SettingsMain.hideDilog();
                                }

                                for (File file : allFile) {
                                    if (file.exists())
                                        file.delete();
                                }

                                btnSelectPix.setEnabled(true);

                            }
                            SettingsMain.hideDilog();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("info Upload Image Err:", t.toString());

                            if (t instanceof TimeoutException) {

                                c3.setVisibility(View.GONE);
                                btnImageChooser.setVisibility(View.VISIBLE);
                                btnImageChooser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp, 0, 0, 0);

                                btnImageChooser.setText("0");
                                Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                btnSelectPix.setEnabled(true);
                                SettingsMain.hideDilog();
                            }
                            if (t instanceof SocketTimeoutException) {
//                                Log.e("info Upload Image Err:", t.getMessage());
                                c3.setVisibility(View.GONE);
                                btnImageChooser.setVisibility(View.VISIBLE);
                                btnImageChooser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp, 0, 0, 0);

                                btnImageChooser.setText("0");
                                Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                btnSelectPix.setEnabled(true);
                                SettingsMain.hideDilog();

                            } else {
                                Log.e("info Upload Image Err:", t.getMessage());
                                SettingsMain.hideDilog();
                            }
                        }
                    });
                } else {
                    SettingsMain.hideDilog();
                    btnSelectPix.setEnabled(true);
                    Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
                }

            } else {
                btnImageChooser.setText(String.valueOf(images.size()));
                btnImageChooser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp, 0, 0, 0);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public File persistImage(Bitmap bitmap, String name) {
        File filesDir = getApplication().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    public void adforest_showProgress() {
        final int[] pStatus = {0};
        final Handler handler = new Handler();
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (pStatus[0] < 100) {
                    pStatus[0] += 1;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            c3.setProgress(pStatus[0]);
                            c3.setTitle(pStatus[0] + "%");

                        }
                    });
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            c3.setVisibility(View.GONE);
                            btnImageChooser.setVisibility(View.VISIBLE);
                            btnImageChooser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_green_24dp, 0, 0, 0);
                        }
                    }, 2000);
                    try {
                        Thread.sleep(16); //thread will take approx 3 seconds to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        c3.setTitle("Done");
    }

    private MultipartBody.Part adforestst_prepareFilePart(String fileName, Uri fileUri) {

        File finalFile = new File(SettingsMain.getRealPathFromURI(context, fileUri));
        allFile.add(finalFile);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        finalFile
                );
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(fileName, finalFile.getName(), requestFile);
    }

    //setting spiner error for validation
    private void setSpinnerError(Spinner spinner) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("Required!");
            selectedTextView.setTextColor(Color.RED);
        }
    }

    //calling APi for geting views from server

    private void adforest_getViews() {

        if (SettingsMain.isConnectingToInternet(context)) {

            SettingsMain.showDilog(context);

            JsonObject params = new JsonObject();
            params.addProperty("is_update", "");
            Log.d("info sendAdNewPost", "" + params.toString());

            Call<ResponseBody> myCall = restService.postGetAdNewPostViews(params, UrlController.AddHeaders(this));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info GetAdnewPost Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                adforest_setViews(response);
                                Log.d("info GetAdnewPost Data", "" + response.getJSONObject("data"));

                            } else {
                                Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        SettingsMain.hideDilog();
                    } catch (JSONException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    } catch (IOException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    }
                    SettingsMain.hideDilog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    SettingsMain.hideDilog();
                    Log.d("info GetAdnewPost error", String.valueOf(t));
                    Log.d("info GetAdnewPost error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                }
            });
        } else {
            SettingsMain.hideDilog();
            Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap Map) {
        mMap = Map;
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            return;
        }
//        if (!gpsTracker.canGetLocation())
//            gpsTracker.showSettingsAlert();
//        else if (gpsTracker.canGetLocation() && gpsTracker.isCheckPermission()) {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
//        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        LatLng sydney = null;
        sydney = new LatLng(40.7127837, -74.0059413);
        // create marker
        MarkerOptions marker = new MarkerOptions()
                .position(sydney)
                .title("New York, NY, United States")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        mMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(40.7127837, -74.0059413)).zoom(16).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                editTextuserLong.setText(String.format("%s", latLng.longitude));
                editTextUserLat.setText(String.format("%s", latLng.latitude));
            }
        });
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        GPSTracker gpsTracker = new GPSTracker(AddNewAdPost.this);
        if (!gpsTracker.canGetLocation())
            gpsTracker.showSettingsAlert();
        else {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
                Log.d("info location", addresses.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex(
                mAutocompleteTextView.setText(address);
                editTextuserLong.setText(gpsTracker.getLongitude() + "");
                editTextUserLat.setText(gpsTracker.getLatitude() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    //generating fields of layout
    void adforest_setViews(final JSONObject jsonObjec) {

        try {
            jsonObject = jsonObjec;
            Log.d("data ===== ", jsonObject.toString());
            JSONArray customOptnList = jsonObject.getJSONObject("data").getJSONArray("fields");

            addId = jsonObject.getJSONObject("data").getInt("ad_id");
            setTitle(jsonObjec.getJSONObject("data").getString("title"));

            textViewInfoforDrag.setText(jsonObject.getJSONObject("extra").getString("sort_image_msg"));
            btnSelectPix.setText(jsonObject.getJSONObject("extra").getString("image_text"));
            textViewTitle.setText(jsonObject.getJSONObject("extra").getString("user_info"));

            updateImagesList(jsonObject.getJSONObject("data"));

            boolean imagesLayoutShowHide = jsonObject.getJSONObject("data").getJSONObject("images").getBoolean("is_show");
            if (!imagesLayoutShowHide) {
                linearLayoutImageSection.setVisibility(View.GONE);
            } else {
                linearLayoutImageSection.setVisibility(View.VISIBLE);
            }
            requiredFields.clear();
            for (int noOfCustomOpt = 0; noOfCustomOpt < customOptnList.length(); noOfCustomOpt++) {

                final CardView cardView = new CardView(AddNewAdPost.this);

                cardView.setCardElevation(2);
                cardView.setUseCompatPadding(true);
                cardView.setRadius(0);
                cardView.setContentPadding(10, 10, 10, 10);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = 10;
                params.bottomMargin = 10;
                cardView.setLayoutParams(params);

                final JSONObject eachData = customOptnList.getJSONObject(noOfCustomOpt);
                TextView customOptionsName = new TextView(context);
                customOptionsName.setTextSize(12);
                customOptionsName.setAllCaps(true);
                customOptionsName.setTextColor(Color.BLACK);
                customOptionsName.setPadding(10, 15, 10, 15);

                customOptionsName.setText(eachData.getString("title"));
                customOptionsName.setFocusable(true);

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setPadding(5, 5, 5, 5);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                linearLayout.addView(customOptionsName);
                if (eachData.getString("field_type_name").equals("ad_price")) {
                    HasPrice = true;
                }
                if (eachData.getString("field_type").equals("select")) {

                    final JSONArray dropDownJSONOpt = eachData.getJSONArray("values");
                    final ArrayList<subcatDiloglist> SpinnerOptions;
                    SpinnerOptions = new ArrayList<>();
                    for (int j = 0; j < dropDownJSONOpt.length(); j++) {
                        subcatDiloglist subDiloglist = new subcatDiloglist();
                        subDiloglist.setId(dropDownJSONOpt.getJSONObject(j).getString("id"));
                        subDiloglist.setName(dropDownJSONOpt.getJSONObject(j).getString("name"));
                        subDiloglist.setHasSub(dropDownJSONOpt.getJSONObject(j).getBoolean("has_sub"));
                        subDiloglist.setHasCustom(dropDownJSONOpt.getJSONObject(j).getBoolean("has_template"));
                        //String optionString = dropDownJSONOpt.getJSONObject(j).getString("name");

                        if (eachData.getString("field_type_name").equals(
                                jsonObject.getJSONObject("data").getJSONArray("hide_price").get(1))) {
                            subDiloglist.setShow(dropDownJSONOpt.getJSONObject(j).getBoolean("is_show"));
                        }
                        SpinnerOptions.add(subDiloglist);
                    }
                    final SpinnerAndListAdapter spinnerAndListAdapter;
                    spinnerAndListAdapter = new SpinnerAndListAdapter(context, SpinnerOptions, true);
                    final Spinner spinner = new Spinner(context);

                    allViewInstance.add(spinner);
                    spinner.setAdapter(spinnerAndListAdapter);
                    spinner.setSelection(0, false);
                    spinner.setFocusable(true);

                    spinner.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            System.out.println("Real touch felt.");
                            spinnerTouched = true;
                            return false;
                        }
                    });

                    if (eachData.getString("field_type_name").equals(
                            jsonObject.getJSONObject("data").getJSONArray("hide_currency").get(1))) {
                        cardViewPriceType = cardView;
                        spinnershow = spinner;
                        HasPrice = false;
                    }
                    if (HasPrice) {
                        cardViewPriceType = cardView;
                    }
                    if (eachData.getBoolean("is_required")) {
                        requiredFields.add(spinner);
                    }


                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            if (spinnerTouched) {
                                //String variant_name = dropDownJSONOpt.getJSONObject(position).getString("name");
                                final subcatDiloglist subcatDiloglistitem = (subcatDiloglist) selectedItemView.getTag();

                                if (position != 0) {
                                    if (subcatDiloglistitem.isHasSub()) {


                                        if (SettingsMain.isConnectingToInternet(context)) {

                                            SettingsMain.showDilog(context);

                                            JsonObject params = new JsonObject();
                                            params.addProperty("subcat", subcatDiloglistitem.getId());
                                            Log.d("info SendSubCat", params.toString());

                                            Call<ResponseBody> myCall = restService.postGetSubCategories(params, UrlController.AddHeaders(context));
                                            myCall.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                                    try {
                                                        if (responseObj.isSuccessful()) {
                                                            Log.d("info SubCat Resp", "" + responseObj.toString());

                                                            JSONObject response = new JSONObject(responseObj.body().string());
                                                            if (response.getBoolean("success")) {
                                                                Log.d("info SubCat object", "" + response.getJSONObject("data"));
                                                                spinnerTouched = false;

                                                                adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, SpinnerOptions
                                                                        , spinnerAndListAdapter, spinner, eachData.getString("field_type_name"));

                                                            } else {
                                                                Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        SettingsMain.hideDilog();
                                                    } catch (JSONException e) {
                                                        SettingsMain.hideDilog();
                                                        e.printStackTrace();
                                                    } catch (IOException e) {
                                                        SettingsMain.hideDilog();
                                                        e.printStackTrace();
                                                    }
                                                    SettingsMain.hideDilog();
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    SettingsMain.hideDilog();
                                                    Log.d("info SubCat error", String.valueOf(t));
                                                    Log.d("info SubCat error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                                }
                                            });

                                        } else {
                                            SettingsMain.hideDilog();
                                            Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
                                        }
                                        spinnerTouched = false;
                                    }

                                    try {
                                        if (eachData.getBoolean("has_cat_template"))
                                            if (subcatDiloglistitem.isHasCustom()) {
                                                linearLayoutCustom.removeAllViews();
                                                allViewInstanceforCustom.clear();
                                                catID = subcatDiloglistitem.getId();
                                                adforest_showCustom();
                                                ison = true;
                                                Log.d("true===== ", "add All=======" + catID);

                                            } else {
                                                if (ison) {
                                                    linearLayoutCustom.removeAllViews();
                                                    allViewInstanceforCustom.clear();
                                                    requiredFieldsForCustom.clear();
                                                    ison = false;
                                                    Log.d("true===== ", "remove All");

                                                }
                                            }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    if (ison) {
                                        linearLayoutCustom.removeAllViews();
                                        allViewInstanceforCustom.clear();
                                        requiredFieldsForCustom.clear();

                                        ison = false;
                                        Log.d("true===== ", "remove All");
                                    }
                                }

                                try {
                                    if (eachData.getString("field_type_name").equals(
                                            jsonObject.getJSONObject("data").getJSONArray("hide_price").get(1))) {
                                        if (subcatDiloglistitem.isShow()) {
                                            Log.d("showwwwww===== ", "showwwwwww All  " + cardViewPriceInput.getChildCount());
                                            cardViewPriceInput.setVisibility(View.VISIBLE);
                                            edittextShowHide = true;

                                            cardViewPriceType.setVisibility(View.VISIBLE);

                                        } else {
                                            cardViewPriceInput.setVisibility(View.GONE);
                                            cardViewPriceType.setVisibility(View.GONE);

                                            edittextShowHide = false;
                                        }
                                    }
                                    if (eachData.getString("field_type_name").equals("ad_bidding")) {
                                        if (cardViewtBidingTimer != null) {
                                            if (subcatDiloglistitem.getId().equals("1")) {
                                                cardViewtBidingTimer.setVisibility(View.VISIBLE);
                                            } else
                                                cardViewtBidingTimer.setVisibility(View.GONE);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                            spinnerTouched = false;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }

                    });
                    linearLayout.addView(spinner, 1);

                    cardView.addView(linearLayout);
                    if (eachData.getString("has_page_number").equals("1")) {
                        page1Layout.addView(cardView);
                        if (eachData.getBoolean("is_required")) {
                            page1RequiredFields.add(spinner);
                        }
                    }
                    if (eachData.getString("has_page_number").equals("2")) {
                        page2Layout.addView(cardView);
                        if (eachData.getBoolean("is_required")) {
                            page2RequiredFields.add(spinner);
                        }
                    }
                }

                if (eachData.getString("field_type").equals("textfield")) {
                    TextInputLayout til = new TextInputLayout(context);
                    til.setHint(eachData.getString("title"));
                    EditText et = new EditText(context);

                    if (eachData.getString("field_type_name").equals("ad_price")) {
                        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    }
                    et.setTextSize(14);
                    et.setFocusable(true);

                    til.addView(et);
                    til.setLayoutParams(params);
                    allViewInstance.add(et);

                    if (jsonObject.getJSONObject("data").getString("title_field_name")
                            .equals(eachData.getString("field_type_name")))
                        editTextTitle = et;
                    if (eachData.getString("field_type_name").equals(
                            jsonObject.getJSONObject("data").getJSONArray("hide_price").get(0))) {
                        cardViewPriceInput = cardView;
                        editTextPrice = et;

                    } else {
                        if (eachData.getBoolean("is_required")) {
                            requiredFields.add(et);
                        }
                    }

                    cardView.addView(til);

                    cardView.setContentPadding(10, 20, 10, 20);
                    if (eachData.getString("field_type_name").equals("ad_bidding_time")) {
                        cardViewtBidingTimer = cardView;
                        cardViewtBidingTimer.setVisibility(View.GONE);
                        et.setClickable(false);
                        et.setFocusable(false);
                        editTexttBidingTimer = et;
                        final EditText editText = et;
                        editTexttBidingTimer.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN)
                                    adforest_showDateTime(editText);
                                return false;
                            }
                        });

                    }
                    if (eachData.getString("has_page_number").equals("1")) {
                        if (eachData.getBoolean("is_required")) {
                            page1RequiredFields.add(et);
                        }
                        page1Layout.addView(cardView);
                    }
                    if (eachData.getString("has_page_number").equals("2")) {
                        if (eachData.getBoolean("is_required")) {
                            page2RequiredFields.add(et);
                        }
                        page2Layout.addView(cardView);
                    }
                }
                if (eachData.getString("field_type").equals("textarea")) {

                    final RichEditor mEditor = new RichEditor(context);

                    mEditor.setEditorHeight(200);
                    mEditor.setPadding(10, 10, 10, 10);
                    mEditor.setPlaceholder(eachData.getString("title"));
                    mEditor.setFocusable(true);

                    mEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            if (b) {
                                horizontalScrollView.setVisibility(View.VISIBLE);
                            } else {
                                horizontalScrollView.setVisibility(View.GONE);
                            }
                        }
                    });

                    findViewById(R.id.action_undo).

                            setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mEditor.undo();
                                }
                            });

                    findViewById(R.id.action_redo).

                            setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mEditor.redo();
                                }
                            });

                    findViewById(R.id.action_bold).

                            setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mEditor.setBold();
                                }
                            });

                    findViewById(R.id.action_italic).

                            setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mEditor.setItalic();
                                }
                            });

                    findViewById(R.id.action_underline).

                            setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mEditor.setUnderline();
                                }
                            });


                    findViewById(R.id.action_insert_bullets).

                            setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mEditor.setBullets();
                                }
                            });

                    findViewById(R.id.action_insert_numbers).

                            setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mEditor.setNumbers();
                                }
                            });
                    linearLayout.addView(mEditor, 1);

                    allViewInstance.add(mEditor);
                    if (eachData.getBoolean("is_required")) {
                        requiredFields.add(linearLayout);
                    }

                    cardView.addView(linearLayout);

                    cardView.setContentPadding(10, 20, 10, 20);

                    if (eachData.getString("has_page_number").equals("1")) {
                        if (eachData.getBoolean("is_required")) {
                            page1RequiredFields.add(linearLayout);
                        }
                        page1Layout.addView(cardView);
                    }
                    if (eachData.getString("has_page_number").equals("2")) {
                        if (eachData.getBoolean("is_required")) {
                            page2RequiredFields.add(linearLayout);
                        }
                        page2Layout.addView(cardView);
                    }
                }

            }

            JSONObject jsonObj = jsonObject.getJSONObject("data").getJSONObject("profile");
            textViewUserName.setText(jsonObj.getJSONObject("name").getString("title"));
            editTextUserName.setText(jsonObj.getJSONObject("name").getString("values"));
            textViewUserPhone.setText(jsonObj.getJSONObject("phone").getString("title"));
            editTextUserPhone.setText(jsonObj.getJSONObject("phone").getString("values"));
            if (!jsonObj.getBoolean("phone_editable")) {
                editTextUserPhone.setEnabled(false);
            }

            if (jsonObj.getBoolean("ad_country_show")) {

                showHideLocation.setVisibility(View.VISIBLE);
                textViewadCountry.setText(jsonObj.getJSONObject("ad_country").getString("title"));

                final JSONArray dropDownJSONOpt = jsonObj.getJSONObject("ad_country").getJSONArray("values");
                final ArrayList<subcatDiloglist> SpinnerOptions;
                SpinnerOptions = new ArrayList<>();
                for (int j = 0; j < dropDownJSONOpt.length(); j++) {
                    subcatDiloglist subDiloglist = new subcatDiloglist();
                    subDiloglist.setId(dropDownJSONOpt.getJSONObject(j).getString("id"));
                    subDiloglist.setName(dropDownJSONOpt.getJSONObject(j).getString("name"));
                    subDiloglist.setHasSub(dropDownJSONOpt.getJSONObject(j).getBoolean("has_sub"));
                    subDiloglist.setHasCustom(dropDownJSONOpt.getJSONObject(j).getBoolean("has_template"));

                    SpinnerOptions.add(subDiloglist);
                }
                final SpinnerAndListAdapter spinnerAndListAdapter;
                spinnerAndListAdapter = new SpinnerAndListAdapter(context, SpinnerOptions, true);
                allViewInstance.add(spinnerLocation);
                spinnerLocation.setAdapter(spinnerAndListAdapter);
                spinnerLocation.setSelection(0, false);

                spinnerLocation.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        System.out.println("Real touch felt.");
                        spinnerTouched = true;
                        return false;
                    }
                });

                //on location clickListener
                spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (spinnerTouched) {
                            //String variant_name = dropDownJSONOpt.getJSONObject(position).getString("name");
                            final subcatDiloglist subcatDiloglistitem = (subcatDiloglist) selectedItemView.getTag();

                            if (position != 0) {
                                if (subcatDiloglistitem.isHasSub()) {

                                    if (SettingsMain.isConnectingToInternet(context)) {

                                        SettingsMain.showDilog(context);


                                        //for serlecting the location if location have sabLocations

                                        JsonObject params1 = new JsonObject();
                                        params1.addProperty("ad_country", subcatDiloglistitem.getId());
                                        Log.d("info SendLocations", params1.toString());

                                        Call<ResponseBody> myCall = restService.postGetSubLocations(params1, UrlController.AddHeaders(context));
                                        myCall.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                                try {
                                                    if (responseObj.isSuccessful()) {
                                                        Log.d("info SubLocation Resp", "" + responseObj.toString());

                                                        JSONObject response = new JSONObject(responseObj.body().string());
                                                        if (response.getBoolean("success")) {
                                                            Log.d("info SubLocation object", "" + response.getJSONObject("data"));
                                                            spinnerTouched = false;

                                                            adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, SpinnerOptions
                                                                    , spinnerAndListAdapter, spinnerLocation, "ad_country");

                                                        } else {
                                                            Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    SettingsMain.hideDilog();
                                                } catch (JSONException e) {
                                                    SettingsMain.hideDilog();
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    SettingsMain.hideDilog();
                                                    e.printStackTrace();
                                                }
                                                SettingsMain.hideDilog();
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                SettingsMain.hideDilog();
                                                Log.d("info SubLocation error", String.valueOf(t));
                                                Log.d("info SubLocation error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                            }
                                        });
                                    } else {
                                        SettingsMain.hideDilog();
                                        Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
                                    }
                                    spinnerTouched = false;
                                }
                            }
                            spinnerTouched = false;
                        }
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            textViewLocation.setText(jsonObj.getJSONObject("location").getString("title"));
            mAutocompleteTextView.setText(jsonObj.getJSONObject("location").getString("values"));

            textViewLat.setText(jsonObj.getJSONObject("map").getJSONObject("location_lat").getString("title"));
            editTextUserLat.setText(jsonObj.getJSONObject("map").getJSONObject("location_lat").getString("values"));
            textViewLong.setText(jsonObj.getJSONObject("map").getJSONObject("location_long").getString("title"));
            editTextuserLong.setText(jsonObj.getJSONObject("map").getJSONObject("location_long").getString("values"));

            if (jsonObj.getJSONObject("map").getBoolean("on_off")) {
                linearLayoutMapView.setVisibility(View.VISIBLE);

                try {
                    if (!jsonObj.getJSONObject("map").getJSONObject("location_lat").getString("values").equals("")) {

                        final LatLng point = new LatLng(Double.parseDouble(jsonObj.getJSONObject("map").getJSONObject("location_lat").getString("values")), Double.parseDouble(jsonObj.getJSONObject("map").getJSONObject("location_long").getString("values")));
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                googleMap.clear();
                                mMap.clear();
                                if (ActivityCompat.checkSelfPermission(AddNewAdPost.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddNewAdPost.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                mMap.setMyLocationEnabled(true);
                                mMap.setOnMyLocationButtonClickListener(AddNewAdPost.this);
                                mMap.setOnMyLocationClickListener(AddNewAdPost.this);
                                googleMap.addMarker(new MarkerOptions().position(point));

                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
                                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

                            }
                        });
                    }
                } catch (NumberFormatException n) {
                    n.printStackTrace();
                }

            } else {
                linearLayoutMapView.setVisibility(View.GONE);
            }
            //
            //Feature Ads
            if (jsonObj.getBoolean("featured_ad_is_show")) {
                JSONObject bumpAd = jsonObj.getJSONObject("featured_ad");
                JSONObject alertObject = jsonObj.getJSONObject("featured_ad_text");
                Log.d("info AdPost feature ad", bumpAd.toString());
                featureAdChkBox.setVisibility(View.VISIBLE);
                adforest_bumpNDFeatureAds(featureAdLayout, featureAdTV, featureAdChkBox, bumpAd, alertObject);
            } else if (jsonObj.getBoolean("featured_ad_buy")) {
                featureAdLayout.setVisibility(View.VISIBLE);
                featureAdByPackages.setVisibility(View.VISIBLE);
                final JSONObject finalAlertText = jsonObj.getJSONObject("featured_ad_notify");
                featureAdTV.setText(finalAlertText.getString("text"));
                featureAdByPackages.setText(finalAlertText.getString("btn"));
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                featureAdByPackages.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Toast.makeText(AddNewAdPost.this, finalAlertText.getString("text"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }


            btnPostAd.setText(jsonObject.getJSONObject("data").getString("btn_submit"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //show bump and feature ads
    private void adforest_bumpNDFeatureAds(RelativeLayout relativeLayout, TextView textView,
                                           final CheckBox checkBox
            , JSONObject adObject, final JSONObject alertObject) {
        String alertTile = null, alertText = null, alertYesBtn = null, alertNoBtn = null;
        relativeLayout.setVisibility(View.VISIBLE);
        try {
            textView.setText(adObject.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("info AdPost CheckBox", adObject.toString());
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        try {
            alertTile = alertObject.getString("title");
            alertText = alertObject.getString("text");
            alertYesBtn = alertObject.getString("btn_ok");
            alertNoBtn = alertObject.getString("btn_no");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String finalAlertTile = alertTile;
        final String finalAlertText = alertText;
        final String finalAlertYesBtn = alertYesBtn;
        final String finalAlertNoBtn = alertNoBtn;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.setTitle(finalAlertTile);
                alert.setCancelable(false);
                alert.setMessage(finalAlertText);
                alert.setPositiveButton(finalAlertYesBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        checkBox.setChecked(true);
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton(finalAlertNoBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        checkBox.setChecked(false);
                    }
                });
                alert.show();
            }
        });
    }

    //showing dilog for catgory selection
    private void adforest_ShowDialog(JSONObject data, final subcatDiloglist main,
                                     final ArrayList<subcatDiloglist> spinnerOptionsout,
                                     final SpinnerAndListAdapter spinnerAndListAdapterout,
                                     final Spinner spinner1, final String field_type_name) {

        Log.d("info Show Dialog ===== ", "adforest_ShowDialog");
        try {
            Log.d("info Show Dialog ===== ", data.getJSONArray("values").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Dialog dialog = new Dialog(context, R.style.PauseDialog);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_sub_cat);

        dialog.setTitle(main.getName());
        ListView listView = dialog.findViewById(R.id.listView);

        final ArrayList<subcatDiloglist> listitems = new ArrayList<>();
        final JSONArray listArray;
        try {
            listArray = data.getJSONArray("values");
            for (int j = 0; j < listArray.length(); j++) {
                subcatDiloglist subDiloglist = new subcatDiloglist();
                subDiloglist.setId(listArray.getJSONObject(j).getString("id"));
                subDiloglist.setName(listArray.getJSONObject(j).getString("name"));
                subDiloglist.setHasSub(listArray.getJSONObject(j).getBoolean("has_sub"));
                subDiloglist.setHasCustom(listArray.getJSONObject(j).getBoolean("has_template"));
                listitems.add(subDiloglist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final SpinnerAndListAdapter spinnerAndListAdapter1 = new SpinnerAndListAdapter(context, listitems, true);
        listView.setAdapter(spinnerAndListAdapter1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final subcatDiloglist subcatDiloglistitem = (subcatDiloglist) view.getTag();

                //Log.d("helllo" , spinnerOptionsout.adforest_get(1).getId() + " === " + spinnerOptionsout.adforest_get(1).getName());

                if (!spinnerOptionsout.get(1).getId().equals(subcatDiloglistitem.getId())) {

                    if (subcatDiloglistitem.isHasSub()) {


                        if (SettingsMain.isConnectingToInternet(context)) {

                            SettingsMain.showDilog(context);

                            //if user select subcategoreis and again select subCategoreis
                            if (field_type_name.equals("ad_cats1")) {
                                JsonObject params = new JsonObject();
                                params.addProperty("subcat", subcatDiloglistitem.getId());
                                Log.d("info SendSubCatAg", params.toString());

                                Call<ResponseBody> myCall = restService.postGetSubCategories(params, UrlController.AddHeaders(context));
                                myCall.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                        try {
                                            if (responseObj.isSuccessful()) {
                                                Log.d("info SubCatAg Resp", "" + responseObj.toString());

                                                JSONObject response = new JSONObject(responseObj.body().string());
                                                if (response.getBoolean("success")) {
                                                    Log.d("info SubCatAg object", "" + response.getJSONObject("data"));

                                                    adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, spinnerOptionsout
                                                            , spinnerAndListAdapterout, spinner1, field_type_name);

                                                } else {
                                                    Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            SettingsMain.hideDilog();
                                        } catch (JSONException e) {
                                            SettingsMain.hideDilog();
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            SettingsMain.hideDilog();
                                            e.printStackTrace();
                                        }
                                        SettingsMain.hideDilog();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        SettingsMain.hideDilog();
                                        Log.d("info SubCatAg error", String.valueOf(t));
                                        Log.d("info SubCatAg error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                    }
                                });

                            }

                            //if user select subLocation and again select subLocation
                            if (field_type_name.equals("ad_country")) {

                                JsonObject params1 = new JsonObject();
                                params1.addProperty("ad_country", subcatDiloglistitem.getId());

                                Log.d("info SendLocationsAg", params1.toString());

                                Call<ResponseBody> myCall = restService.postGetSubLocations(params1, UrlController.AddHeaders(context));
                                myCall.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                        try {
                                            if (responseObj.isSuccessful()) {
                                                Log.d("info SubLocationAg Resp", "" + responseObj.toString());

                                                JSONObject response = new JSONObject(responseObj.body().string());
                                                if (response.getBoolean("success")) {
                                                    Log.d("info SubLocationAg obj", "" + response.getJSONObject("data"));

                                                    adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, spinnerOptionsout
                                                            , spinnerAndListAdapterout, spinner1, field_type_name);

                                                } else {
                                                    Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            SettingsMain.hideDilog();
                                        } catch (JSONException e) {
                                            SettingsMain.hideDilog();
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            SettingsMain.hideDilog();
                                            e.printStackTrace();
                                        }
                                        SettingsMain.hideDilog();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        SettingsMain.hideDilog();
                                        Log.d("info SubLocationAg err", String.valueOf(t));
                                        Log.d("info SubLocationAg err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                    }
                                });

                            }
                        } else {
                            SettingsMain.hideDilog();
                            Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
                        }


                    } else {

                        for (int ii = 0; ii < spinnerOptionsout.size(); ii++) {
                            if (spinnerOptionsout.get(ii).getId().equals(subcatDiloglistitem.getId())) {
                                spinnerOptionsout.remove(ii);
                                Log.d("info ===== ", "else of list inner is 1st button into for loop");
                                break;
                            }
                        }
                        Log.d("info ===== ", "else of list inner is 1st button out of for loop");

                        spinnerOptionsout.add(1, subcatDiloglistitem);
                        spinner1.setSelection(1, false);
                        spinnerAndListAdapterout.notifyDataSetChanged();

                    }

                    if (subcatDiloglistitem.isHasCustom() && field_type_name.equals("ad_cats1")) {
                        linearLayoutCustom.removeAllViews();
                        allViewInstanceforCustom.clear();
                        requiredFieldsForCustom.clear();
                        catID = subcatDiloglistitem.getId();
                        adforest_showCustom();
                        ison = true;
                        Log.d("true===== ", "inter add All");

                    } else {
                        if (ison && field_type_name.equals("ad_cats1")) {
                            linearLayoutCustom.removeAllViews();
                            allViewInstanceforCustom.clear();
                            requiredFieldsForCustom.clear();
                            ison = false;
                            Log.d("true===== ", "inter remove All");
                        }
                    }
                } else {
                    spinner1.setSelection(1, false);
                    Log.d("info ===== ", "else of chk is 1st button out");

                }
                dialog.dismiss();
            }
        });

        Button Send = dialog.findViewById(R.id.send_button);
        Button Cancel = dialog.findViewById(R.id.cancel_button);

        try {
            Send.setText(jsonObject.getJSONObject("extra").getString("dialog_send"));
            Cancel.setText(jsonObject.getJSONObject("extra").getString("dialg_cancel"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Send.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        Cancel.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < spinnerOptionsout.size(); i++) {
                    if (spinnerOptionsout.get(i).getId().equals(main.getId())) {
                        spinnerOptionsout.remove(i);
                        Log.d("info ===== ", "send button in");
                        break;
                    }
                }

                spinnerOptionsout.add(1, main);
                spinnerAndListAdapterout.notifyDataSetChanged();
                spinner1.setSelection(1, false);
                Log.d("info ===== ", "send button out");

                dialog.dismiss();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    //api calling for getting catgory type custom fields...
    private void adforest_showCustom() {

        if (linearLayoutCustom != null) {

            if (SettingsMain.isConnectingToInternet(context)) {

                JsonObject params = new JsonObject();
                params.addProperty("cat_id", catID);
                Log.d("info Send DynamicFields", catID);

                Call<ResponseBody> myCall = restService.postGetDynamicFields(params, UrlController.AddHeaders(this));
                myCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                        try {
                            if (responseObj.isSuccessful()) {
                                Log.d("info DynamicFields Resp", "" + responseObj.toString());

                                JSONObject response = new JSONObject(responseObj.body().string());
                                if (response.getBoolean("success")) {

                                    adforest_setViewsForCustom(response);
                                    Log.d("info data DynamicFields", "" + response.getJSONArray("data"));

                                } else {
                                    Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            SettingsMain.hideDilog();
                        } catch (JSONException e) {
                            SettingsMain.hideDilog();
                            e.printStackTrace();
                        } catch (IOException e) {
                            SettingsMain.hideDilog();
                            e.printStackTrace();
                        }
                        SettingsMain.hideDilog();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        SettingsMain.hideDilog();
                        Log.d("info DynamicFields err", String.valueOf(t));
                        Log.d("info DynamicFields err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                });
            } else {
                Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
            }


        }
    }

    // generating custom fields of catagory related
    void adforest_setViewsForCustom(JSONObject jsonObjec) {
        edittextShowHide = true;
        try {
            HasPrice = false;
            jsonObjectforCustom = jsonObjec;
            Log.d("DynamicFields Data === ", jsonObjectforCustom.toString());
            JSONArray customOptnList = jsonObjectforCustom.getJSONArray("data");

            requiredFieldsForCustom.clear();

            for (int noOfCustomOpt = 0; noOfCustomOpt < customOptnList.length(); noOfCustomOpt++) {

                CardView cardView = new CardView(context);
                cardView.setCardElevation(2);
                cardView.setUseCompatPadding(true);
                cardView.setRadius(0);
                cardView.setContentPadding(10, 10, 10, 10);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = 10;
                params.bottomMargin = 10;
                cardView.setLayoutParams(params);

                final JSONObject eachData = customOptnList.getJSONObject(noOfCustomOpt);
                TextView customOptionsName = new TextView(context);
                customOptionsName.setTextSize(12);
                customOptionsName.setAllCaps(true);
                customOptionsName.setTextColor(Color.BLACK);
                customOptionsName.setPadding(10, 15, 10, 15);
                customOptionsName.setText(eachData.getString("title"));
                customOptionsName.setFocusable(true);

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                linearLayout.addView(customOptionsName);
                if (eachData.getString("field_type_name").equals("ad_price")) {
                    HasPrice = true;
                }
                if (eachData.getString("field_type").equals("select")) {

                    final JSONArray dropDownJSONOpt = eachData.getJSONArray("values");
                    final ArrayList<subcatDiloglist> SpinnerOptions;
                    SpinnerOptions = new ArrayList<>();
                    for (int j = 0; j < dropDownJSONOpt.length(); j++) {
                        subcatDiloglist subDiloglist = new subcatDiloglist();
                        subDiloglist.setId(dropDownJSONOpt.getJSONObject(j).getString("id"));
                        subDiloglist.setName(dropDownJSONOpt.getJSONObject(j).getString("name"));
                        subDiloglist.setHasSub(dropDownJSONOpt.getJSONObject(j).getBoolean("has_sub"));
                        //String optionString = dropDownJSONOpt.getJSONObject(j).getString("name");

                        if (eachData.getString("field_type_name").equals(
                                jsonObject.getJSONObject("data").getJSONArray("hide_price").get(1))) {
                            subDiloglist.setShow(dropDownJSONOpt.getJSONObject(j).getBoolean("is_show"));
                        }

                        SpinnerOptions.add(subDiloglist);
                    }
                    final SpinnerAndListAdapter spinnerAndListAdapter;

                    spinnerAndListAdapter = new SpinnerAndListAdapter(context, SpinnerOptions, true);

                    final Spinner spinner = new Spinner(context);

                    allViewInstanceforCustom.add(spinner);
                    spinner.setAdapter(spinnerAndListAdapter);
                    spinner.setSelection(0, false);
                    spinner.setFocusable(true);

                    spinner.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            System.out.println("Real touch felt.");
                            spinnerTouched = true;
                            return false;
                        }
                    });

                    if (eachData.getString("field_type_name").equals(
                            jsonObject.getJSONObject("data").getJSONArray("hide_currency").get(1))) {
                        cardViewPriceType = cardView;
                        spinnershow = spinner;
                        HasPrice = false;
                    }
                    if (HasPrice) {
                        cardViewPriceType = cardView;
                    }
                    if (eachData.getBoolean("is_required")) {
                        requiredFieldsForCustom.add(spinner);
                    }
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            if (spinnerTouched) {
                                //String variant_name = dropDownJSONOpt.getJSONObject(position).getString("name");
                                final subcatDiloglist subcatDiloglistitem = (subcatDiloglist) selectedItemView.getTag();


                                try {
                                    if (eachData.getString("field_type_name").equals(
                                            jsonObject.getJSONObject("data").getJSONArray("hide_price").get(1))) {
                                        if (subcatDiloglistitem.isShow()) {
                                            Log.d("showwwwww===== ", "showwwwwww All  " + cardViewPriceInput.getChildCount());
                                            cardViewPriceInput.setVisibility(View.VISIBLE);
                                            edittextShowHide = true;

                                            cardViewPriceType.setVisibility(View.VISIBLE);

                                        } else {
                                            cardViewPriceInput.setVisibility(View.GONE);
                                            cardViewPriceType.setVisibility(View.GONE);
//                                            Toast.makeText(getApplication(),"false ho gya",Toast.LENGTH_LONG).show();
                                            edittextShowHide = false;
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                            spinnerTouched = false;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }

                    });

                    linearLayout.addView(spinner, 1);
                    cardView.addView(linearLayout);
                    linearLayoutCustom.addView(cardView);

                }
                if (eachData.getString("field_type").equals("textfield")) {
                    TextInputLayout til = new TextInputLayout(context);
                    til.setHint(eachData.getString("title"));
                    EditText et = new EditText(context);
                    if (eachData.getString("field_type_name").equals("ad_price")) {
                        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    }
                    et.setTextSize(14);

                    et.getBackground().clearColorFilter();
                    et.setFocusable(true);

                    til.addView(et);
                    allViewInstanceforCustom.add(et);
                    cardView.addView(til);
                    if (jsonObject.getJSONObject("data").getString("title_field_name")
                            .equals(eachData.getString("field_type_name")))
                        editTextTitle = et;
                    if (eachData.getString("field_type_name").equals(
                            jsonObject.getJSONObject("data").getJSONArray("hide_price").get(0))) {
                        cardViewPriceInput = cardView;
                        editTextPrice = et;

                    } else if (eachData.getBoolean("is_required")) {
                        requiredFieldsForCustom.add(et);
                        edittextShowHide = true;
                    }
                    linearLayoutCustom.addView(cardView);
                }

                if (eachData.getString("field_type").equals("radio")) {
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params2.topMargin = 3;
                    params2.bottomMargin = 3;

                    final JSONArray radioButtonJSONOpt = eachData.getJSONArray("values");
                    RadioGroup rg = new RadioGroup(context); //create the RadioGroup
                    allViewInstanceforCustom.add(rg);
                    for (int j = 0; j < radioButtonJSONOpt.length(); j++) {

                        RadioButton rb = new RadioButton(context);
                        rg.addView(rb, params2);
                        if (j == 0)
                            rb.setChecked(true);
                        rb.setLayoutParams(params2);
                        rb.setFocusable(true);
                        rb.setTag(radioButtonJSONOpt.getJSONObject(j).getString("id"));
                        rb.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        String optionString = radioButtonJSONOpt.getJSONObject(j).getString("name");
                        rb.setText(optionString);
                        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                View radioButton = group.findViewById(checkedId);
                                String variant_name = radioButton.getTag().toString();
                                Toast.makeText(context, variant_name + "", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    if (eachData.getBoolean("is_required")) {
                        requiredFieldsForCustom.add(rg);
                    }
                    linearLayout.addView(rg, params2);
                    cardView.addView(linearLayout);
                    linearLayoutCustom.addView(cardView);
                }

                if (eachData.getString("field_type").equals("checkbox")) {
                    JSONArray checkBoxJSONOpt = eachData.getJSONArray("values");

                    for (int j = 0; j < checkBoxJSONOpt.length(); j++) {

                        CheckBox chk = new CheckBox(context);
                        chk.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        chk.setTag(checkBoxJSONOpt.getJSONObject(j).getString("id"));
                        chk.setFocusable(true);
                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params2.topMargin = 3;
                        params2.bottomMargin = 3;
                        String optionString = checkBoxJSONOpt.getJSONObject(j).getString("name");
                        chk.setText(optionString);
                        linearLayout.addView(chk, params2);
                    }
                    if (eachData.getBoolean("is_required")) {
                        requiredFieldsForCustom.add(linearLayout);
                    }
                    allViewInstanceforCustom.add(linearLayout);

                    cardView.addView(linearLayout);
                    linearLayoutCustom.addView(cardView);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //getting data from created fields
    public JsonObject adforest_getDataFromDynamicViews() {
        JsonObject optionsObj = null;

        try {
            JSONArray customOptnList = jsonObject.getJSONObject("data").getJSONArray("fields");
            optionsObj = new JsonObject();
            for (int noOfViews = 0; noOfViews < customOptnList.length(); noOfViews++) {
                JSONObject eachData = customOptnList.getJSONObject(noOfViews);

                if (eachData.getString("field_type").equals("select")) {
                    Spinner spinner = (Spinner) allViewInstance.get(noOfViews);

                    subcatDiloglist subcatDiloglist1 = (subcatDiloglist) spinner.getSelectedView().getTag();
                    JSONArray dropDownJSONOpt = eachData.getJSONArray("values");
                    String variant_name = dropDownJSONOpt.getJSONObject(spinner.getSelectedItemPosition()).getString("id");
                    Log.d("value id", variant_name + "");
                    Log.d("zia", "zia = " + subcatDiloglist1.getId());


                    optionsObj.addProperty(eachData.getString("field_type_name"),
                            "" + subcatDiloglist1.getId());
                }
                if (eachData.getString("field_type").equals("textfield")) {
                    TextView textView = (TextView) allViewInstance.get(noOfViews);
                    if (!textView.getText().toString().equalsIgnoreCase(""))
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    else
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    Log.d("variant_name", textView.getText().toString() + "");
                }

                if (eachData.getString("field_type").equals("textarea")) {

                    RichEditor textView = (RichEditor) allViewInstance.get(noOfViews);
                    optionsObj.addProperty(eachData.getString("field_type_name"), textView.getHtml());

                }

                if (eachData.getString("field_type").equals("glocation_textfield")) {
                    TextView textView = (TextView) allViewInstance.get(noOfViews);
                    if (!textView.getText().toString().equalsIgnoreCase(""))
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    else
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    Log.d("variant_name", textView.getText().toString() + "");
                }
                if (eachData.getString("field_type").equals("range_textfield")) {
                    LinearLayout linearLayout = (LinearLayout) allViewInstance.get(noOfViews);

                    TextInputLayout textView = (TextInputLayout) linearLayout.getChildAt(0);
                    TextInputLayout textView2 = (TextInputLayout) linearLayout.getChildAt(1);

                    if (textView.getEditText() != null && textView2.getEditText() != null)
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getEditText().getText().toString() + "-" +
                                textView2.getEditText().getText().toString());
                }
            }

            hideSoftKeyboard();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("array us", (optionsObj != null ? optionsObj.toString() : null) + " ==== size====  " + allViewInstance.size());

        return optionsObj;
    }

    //getting data from custom created fields generated by catgory selection...
    public JsonObject adforest_getDataFromDynamicViewsForCustom() {
        JsonObject optionsObj = null;

        try {
            JSONArray customOptnList = jsonObjectforCustom.getJSONArray("data");
            optionsObj = new JsonObject();
            for (int noOfViews = 0; noOfViews < customOptnList.length(); noOfViews++) {
                JSONObject eachData = customOptnList.getJSONObject(noOfViews);

                if (eachData.getString("field_type").equals("select")) {
                    Spinner spinner = (Spinner) allViewInstanceforCustom.get(noOfViews);

                    subcatDiloglist subcatDiloglist1 = (subcatDiloglist) spinner.getSelectedView().getTag();
                    JSONArray dropDownJSONOpt = eachData.getJSONArray("values");
                    String variant_name = dropDownJSONOpt.getJSONObject(spinner.getSelectedItemPosition()).getString("id");
                    Log.d("value id", variant_name + "");
                    Log.d("zia", "zia = " + subcatDiloglist1.getId());

                    optionsObj.addProperty(eachData.getString("field_type_name"),
                            "" + subcatDiloglist1.getId());
                }
                if (eachData.getString("field_type").equals("textfield")) {
                    TextView textView = (TextView) allViewInstanceforCustom.get(noOfViews);
                    if (!textView.getText().toString().equalsIgnoreCase(""))
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    else
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    Log.d("variant_name", textView.getText().toString() + "");
                }

                if (eachData.getString("field_type").equals("radio")) {
                    RadioGroup radioGroup = (RadioGroup) allViewInstanceforCustom.get(noOfViews);
                    RadioButton selectedRadioBtn = context.findViewById(radioGroup.getCheckedRadioButtonId());
                    Log.d("variant_name", selectedRadioBtn.getTag().toString() + "");
                    optionsObj.addProperty(eachData.getString("field_type_name"),
                            "" + selectedRadioBtn.getTag().toString());
                }

                if (eachData.getString("field_type").equals("checkbox")) {
                    LinearLayout linearLayout = (LinearLayout) allViewInstanceforCustom.get(noOfViews);
                    JSONArray checkBoxJSONOpt = eachData.getJSONArray("values");
                    String values = "";
                    for (int j = 0; j < checkBoxJSONOpt.length(); j++) {
                        CheckBox chk = (CheckBox) linearLayout.getChildAt(j + 1);
                        if (chk.isChecked()) {
                            values = values.concat("," + chk.getTag());
                        }
                    }
                    optionsObj.addProperty(eachData.getString("field_type_name"), values);
                }
            }

            hideSoftKeyboard();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("array us custom", (optionsObj != null ? optionsObj.toString() : null) + " ==== size====  " + allViewInstanceforCustom.size());

        return optionsObj;
    }

    public void hideSoftKeyboard() {
        if (context.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        if (page2.getVisibility() == View.VISIBLE) {
            imageViewBack1.performClick();
        } else if (page3.getVisibility() == View.VISIBLE) {
            imageViewBack2.performClick();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
        }
        return true;
    }

    private void delImage(String tag) {

        if (SettingsMain.isConnectingToInternet(context)) {

            SettingsMain.showDilog(context);

            JsonObject params = new JsonObject();
            params.addProperty("img_id", tag);
            params.addProperty("ad_id", addId);
            Log.d("info send DeleteImage", params.toString());
            Call<ResponseBody> myCall = restService.postDeleteImages(params, UrlController.AddHeaders(this));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info DeleteImage Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                Log.d("info DeleteImage object", "" + response.toString());

                                updateImagesList(response.getJSONObject("data"));

                                Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        SettingsMain.hideDilog();
                    } catch (JSONException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    } catch (IOException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    }
                    SettingsMain.hideDilog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    SettingsMain.hideDilog();
                    Log.d("info DeleteImage error", t.getStackTrace().toString());
                    Log.d("info DeleteImage error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                }
            });

        } else {
            SettingsMain.hideDilog();
            Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

    void updateImagesList(JSONObject jsonObject) {

        try {
            imageLimit = jsonObject.getJSONObject("images").getInt("numbers");

            JSONArray jsonArrayImages = jsonObject.getJSONArray("ad_images");

            myImages = new ArrayList<>();

            Log.d("info Images Data", "" + jsonArrayImages.toString());

            if (jsonArrayImages.length() > 0) {
                recyclerView.setVisibility(View.VISIBLE);

                for (int i = 0; i < jsonArrayImages.length(); i++) {
                    myAdsModel item = new myAdsModel();
                    JSONObject object = null;
                    try {
                        object = jsonArrayImages.getJSONObject(i);

                        item.setAdId(object.getString("img_id"));
                        item.setImage((object.getString("thumb")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    myImages.add(item);
                }
                itemEditImageAdapter = new ItemEditImageAdapter(context, myImages, this);
                recyclerView.setAdapter(itemEditImageAdapter);

                ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(itemEditImageAdapter);
                mItemTouchHelper = new ItemTouchHelper(callback);
                mItemTouchHelper.attachToRecyclerView(recyclerView);

                itemEditImageAdapter.setOnItemClickListener(new MyAdsOnclicklinstener() {
                    @Override
                    public void onItemClick(myAdsModel item) {

                    }

                    @Override
                    public void delViewOnClick(View v, int position) {
                        delImage(v.getTag().toString());
                    }

                    @Override
                    public void editViewOnClick(View v, int position) {

                    }
                });

                textViewInfoforDrag.setVisibility(View.VISIBLE);

            } else {
                recyclerView.setVisibility(View.GONE);
                recyclerView.setAdapter(null);
                c3.setVisibility(View.GONE);
                btnImageChooser.setVisibility(View.VISIBLE);
                btnImageChooser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp, 0, 0, 0);

                btnImageChooser.setText("0");

                textViewInfoforDrag.setVisibility(View.GONE);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    protected void onResume() {
        try {
            if (settingsMain.getAnalyticsShow() && !settingsMain.getAnalyticsId().equals(""))
                AnalyticsTrackers.getInstance().trackScreenView("Add Post");
            super.onResume();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void adforest_showDateTime(final EditText editText) {

        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if (dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel)
            );
        }
//        // Init format
//        Date date = new Date();
//        String strDateFormat = "M";
//        final SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Log.d("info calender", year + "" + month + "" + day);
//        2018-04-11 11:09:00
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
        // Assign unmodifiable values
        dateTimeFragment.set24HoursMode(false);
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(year, month, day).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());

        // Define new day and month format
        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        // Set listener for date
        // Or use dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                editText.setText(myDateFormat.format(date));
//                Toast.makeText(context, myDateFormat.format(date) + "", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Do nothing
            }

            @Override
            public void onNeutralButtonClick(Date date) {
                // Optional if neutral button does'nt exists
//                myDateFormat.format(date)            }
            }
        });
        dateTimeFragment.startAtCalendarView();

        dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);

    }
}
