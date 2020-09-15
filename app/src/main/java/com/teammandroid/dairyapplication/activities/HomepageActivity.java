package com.teammandroid.dairyapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.razorpay.Checkout;
import com.steelkiwi.library.view.BadgeHolderLayout;
import com.teammandroid.dairyapplication.Network.CategoryServices;
import com.teammandroid.dairyapplication.Network.ProductServices;
import com.teammandroid.dairyapplication.Network.SliderService;
import com.teammandroid.dairyapplication.R;

import com.teammandroid.dairyapplication.admin.activities.AddHomeProductActivity;
import com.teammandroid.dairyapplication.admin.activities.CartActivity;
import com.teammandroid.dairyapplication.admin.activities.CategoryListActivity;
import com.teammandroid.dairyapplication.admin.activities.DeliveryboyListActivity;
import com.teammandroid.dairyapplication.admin.activities.DeliveryboyOrderListActivity;
import com.teammandroid.dairyapplication.admin.activities.DeliveryboyStatusListActivity;
import com.teammandroid.dairyapplication.admin.activities.OrderHistoryActivity;
import com.teammandroid.dairyapplication.admin.activities.SelectRoleActivity;
import com.teammandroid.dairyapplication.admin.adapters.CategoryHomeAdapter;
import com.teammandroid.dairyapplication.admin.adapters.ProductListHomeAdapter;
import com.teammandroid.dairyapplication.admin.model.CategoryModel;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.model.SliderModel;
import com.teammandroid.dairyapplication.model.UserModel;

import com.teammandroid.dairyapplication.offline.DatabaseHelper;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.SessionHelper;
import com.teammandroid.dairyapplication.utils.Utility;
import com.teammandroid.dairyapplication.wishlist.WishlistActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_3;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_4;

public class HomepageActivity extends AppCompatActivity implements View.OnClickListener,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener
{
////////////////////////////////////////////////////////////
    int a=0;
    RecyclerView recyclerView;
    private final static String TAG=HomepageActivity.class.getSimpleName();
    RecyclerView rv_home_video_pkg;
    RecyclerView rv_home_videoPaid_pkg;
    private ImageView img_openDrawer;
    private ImageView img_whatsapp;
    private ImageView img_call;
    private SliderLayout mDemoSlider;
    private PagerIndicator custom_indicator;
    private LinearLayout sliderlayout;
    private EditText toolbarEditText;
    private View viewReplaceClear;
    private RelativeLayout replaceToolbar;
    private View viewMenuIconBack;
    /**
     * Type name
     */
    private TextView newtxtTitleBar;
    private View viewSearch;
    private RelativeLayout neworiginalToolbar;
    private RelativeLayout newtoolbar;
    private RelativeLayout rl_headerprofile;
    private ImageView iv_feed;
    /**
     * Home
     */
    private TextView txt_home;
    private RelativeLayout rl_home;
    private ImageView iv_profile;
    /**
     * Profile
     */
    private TextView tv_profile;
    private ImageView iv_daily_service_request;
    /**
     * Monthly Service Record
     */

    /**
     * Payment
     */
    private TextView tv_payment;
    private RelativeLayout rl_paymentHist;
    private ImageView iv_drop_order;
    /**
     * Vacations
     */
    private TextView tv_drop_order;
    private RelativeLayout rl_paidelecture;
    private ImageView iv_todays_count;
    /**
     * Todays Count
     */
    private TextView tv_todays_count;
    private RelativeLayout rl_insert_video_link;
    private ImageView iv_tomorrows_count;
    /**
     * Tomorrow's Count
     */
    private TextView tv_tomorrows_count;
    private RelativeLayout rl_demo_lecture;
    private ImageView iv_delete_count;
    /**
     * Delete Entry
     */
    private TextView tv_delete_count;
    private RelativeLayout rl_delete_count;
    /**
     * About Us
     */
    private TextView txt_aboutus;
    /**
     * Setting
     */
    private TextView txt_rateus;
    private TextView txt_contact,tv_share_app;
    /**
     * Logout
     */
    private TextView txt_logout,txt_feedback;
    /**
     * Powered by Mandroid
     */
    UserModel userModel;
    private ScrollView Scrll_Drawer;
    DrawerLayout drl_Opener;

    ImageView iv_add_cust;
    ProgressDialog progressDialog;
    RelativeLayout rl_video;
    RelativeLayout rl_profileChnage, rl_Category, rl_track_order, rl_wishlist;
    PrefManager prefManager;

    Dialog resultbox;

    Activity activity;

    int packageId;
    CircleImageView ib_twitter;
    CircleImageView ib_facebook;
    CircleImageView ib_instagram;
    CircleImageView ib_whatsapp;
    CircleImageView ib_linkedin;
    CircleImageView ib_telegram;

    int total_days;
    double pamount;

    String expirydate, paiddate;
    CategoryHomeAdapter categoryHomeAdapter;

    private ArrayList<String> mOffersprice = new ArrayList<>();
    private ArrayList<String> mImage = new ArrayList<>();
    private ArrayList<String> mLabel = new ArrayList<>();
    private ArrayList<String> mWeight = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();
    private ArrayList<String> mCutoff = new ArrayList<>();

    ProductListHomeAdapter productListHomeAdapter;

    TextView tv_view_all_test;

    DatabaseHelper dbHelper;

    BadgeHolderLayout badgeLayout;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_homepage);
        activity=HomepageActivity.this;

        bindView();
        listener();
        getSlider();
        prefManager.setSelect(0);
        Checkout.preload(getApplicationContext());

        requestPermission();
        getCategory();
        productList(0);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.navigation_dashboard:
                        Intent a = new Intent(HomepageActivity.this, HomepageActivity.class);
                        startActivity(a);
                        break;
                    case R.id.navigation_alloffers:
                        Intent a1 = new Intent(HomepageActivity.this, CategoryListActivity.class);
                        startActivity(a1);
                        break;

                    case R.id.navigation_allorders:
                        Intent b = new Intent(HomepageActivity.this, WishlistActivity.class);
                        startActivity(b);
                        break;

                    case R.id.navigation_settings:
                        Intent b1 = new Intent(HomepageActivity.this, CartActivity.class);
                        startActivity(b1);
                        break;


                }
                return false;
            }
        });

        if(prefManager.getUSER_ID()==0)
        {
            getCountForNotLogin();
        }
        else {

            getCount();

        }

        Log.d(TAG," "+prefManager.getUSER_ID());

        if (prefManager.getUSER_ID() <= 0)
        {
            rl_profileChnage.setVisibility(View.GONE);
        }else
        {
            rl_profileChnage.setVisibility(View.VISIBLE);
        }

        swipeLayout = findViewById(R.id.swipe_container);
        // Adding Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                getCategoryWithoutDialog();
                productListWithoutDialog(0);
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);

                    }
                }, 4000); // Delay in millis
            }
        });


    }

    private void listener() {

        int c=0;
        img_openDrawer.setOnClickListener(this);
        rl_profileChnage.setOnClickListener(this);
        rl_paymentHist.setOnClickListener(this);
        txt_rateus.setOnClickListener(this);
        txt_aboutus.setOnClickListener(this);
        txt_logout.setOnClickListener(this);
        txt_feedback.setOnClickListener(this);
        tv_share_app.setOnClickListener(this);

        ib_twitter.setOnClickListener(this);
        ib_facebook.setOnClickListener(this);
        ib_instagram.setOnClickListener(this);
        ib_whatsapp.setOnClickListener(this);
        ib_linkedin.setOnClickListener(this);
        ib_telegram.setOnClickListener(this);
        tv_view_all_test.setOnClickListener(this);

        badgeLayout.setOnClickListener(this);
        rl_Category.setOnClickListener(this);
        rl_track_order.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        rl_wishlist.setOnClickListener(this);
    }

    private void bindView() {
        int nb=0;
        img_openDrawer = (ImageView) findViewById(R.id.img_openDrawer);
        img_whatsapp = (ImageView) findViewById(R.id.img_whatsapp);
        img_call = (ImageView) findViewById(R.id.img_call);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        custom_indicator = (PagerIndicator) findViewById(R.id.custom_indicator);
        rv_home_video_pkg = (RecyclerView) findViewById(R.id.rv_home_video_pkg);
        recyclerView = findViewById(R.id.rv_products1);
        rl_headerprofile = (RelativeLayout) findViewById(R.id.rl_headerprofile);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_track_order = (RelativeLayout) findViewById(R.id.rl_track_order);
        rl_Category = (RelativeLayout) findViewById(R.id.rl_Category);
        drl_Opener =  findViewById(R.id.drl_Opener);
        tv_view_all_test =  findViewById(R.id.tv_view_all_test);
        rl_wishlist =  findViewById(R.id.rl_wishlist);

        badgeLayout = findViewById(R.id.badgeLayout);
        dbHelper = new DatabaseHelper(HomepageActivity.this);

        rl_paymentHist = (RelativeLayout) findViewById(R.id.rl_paymentHist);
        rl_video = (RelativeLayout) findViewById(R.id.rl_video);
        rl_profileChnage = (RelativeLayout) findViewById(R.id.rl_profileChnage);

        rl_paidelecture = (RelativeLayout) findViewById(R.id.rl_paidelecture);

        rl_insert_video_link = (RelativeLayout) findViewById(R.id.rl_insert_video_link);

        rl_demo_lecture = (RelativeLayout) findViewById(R.id.rl_demo_lecture);

        rl_delete_count = (RelativeLayout) findViewById(R.id.rl_delete_count);
        txt_aboutus = (TextView) findViewById(R.id.txt_aboutus);
        txt_rateus = (TextView) findViewById(R.id.txt_rateus);
        txt_contact = (TextView) findViewById(R.id.txt_contact);
        txt_logout = (TextView) findViewById(R.id.txt_logout);
        txt_feedback = (TextView) findViewById(R.id.txt_feedback);

        iv_add_cust = findViewById(R.id.iv_add_cust);
        tv_share_app = findViewById(R.id.tv_share_app);

        ib_twitter = findViewById(R.id.ib_twitter);
        ib_facebook = findViewById(R.id.ib_facebook);
        ib_instagram = findViewById(R.id.ib_instagram);
        ib_whatsapp =   findViewById(R.id.ib_whatsapp);
        ib_linkedin =findViewById(R.id.ib_linkedin);
        ib_telegram =findViewById(R.id.ib_telegram);

        progressDialog=new ProgressDialog(HomepageActivity.this);
        prefManager=new PrefManager(HomepageActivity.this);
    }

    /*private ArrayList<NewRequestDetailsModel> arrayListProduct() {
        ArrayList<NewRequestDetailsModel> list = new ArrayList<>();
            list.add(new NewRequestDetailsModel(0, 0, 0, 500, 0.0, "", "", 0,0, 0, 0, "", 0, "",0, "dahdh kkk","Ghee", "8649303233", "abad") );
            list.add(new NewRequestDetailsModel(0, 0, 0, 500, 0.0, "", "", 0,0, 0, 0, "", 0, "",0, "dahdh kkk","Ghee", "8649303233", "abad") );
            list.add(new NewRequestDetailsModel(0, 0, 0, 500, 0.0, "", "", 0,0, 0, 0, "", 0, "",0, "dahdh kkk","Ghee", "8649303233", "abad") );
            list.add(new NewRequestDetailsModel(0, 0, 0, 500, 0.0, "", "", 0,0, 0, 0, "", 0, "",0, "dahdh kkk","Ghee", "8649303233", "abad") );

        return list;
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.img_openDrawer:
                drl_Opener.openDrawer(Gravity.LEFT);

                break;
            case R.id.rl_headerprofile:

                break;
            case R.id.rl_home:
                Utility.launchActivity(HomepageActivity.this,HomepageActivity.class,false);
                drl_Opener.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_view_all_test:
                Utility.launchActivity(HomepageActivity.this,CategoryListActivity.class,false);
                drl_Opener.closeDrawer(Gravity.LEFT);
                break;



            case R.id.rl_paymentHist:
                //Utility.launchActivity(HomepageActivity.this, DeliveryboyOrderListActivity.class,false);
                Utility.launchActivity(HomepageActivity.this, DeliveryboyStatusListActivity.class,false);

                break;
            case R.id.rl_paidelecture:
                drl_Opener.closeDrawer(Gravity.LEFT);
                Toast.makeText(getApplicationContext(),"CLick here",Toast.LENGTH_SHORT).show();
                // Utility.launchActivity(HomepageActivity.this, PayementActivity.class,false);
                break;
            case R.id.rl_insert_video_link:
                drl_Opener.closeDrawer(Gravity.LEFT);
                Toast.makeText(getApplicationContext(),"CLick here",Toast.LENGTH_SHORT).show();

                break;
            case R.id.rl_demo_lecture:
                drl_Opener.closeDrawer(Gravity.LEFT);
                Toast.makeText(getApplicationContext(),"CLick here",Toast.LENGTH_SHORT).show();

                break;
            case R.id.rl_delete_count:
                drl_Opener.closeDrawer(Gravity.LEFT);
                Toast.makeText(getApplicationContext(),"CLick here",Toast.LENGTH_SHORT).show();

                break;
            case R.id.rl_video:
                drl_Opener.closeDrawer(Gravity.LEFT);
                Utility.launchActivity(HomepageActivity.this, CategoryListActivity.class,false);
                break;

            case R.id.rl_profileChnage:
                drl_Opener.closeDrawer(Gravity.LEFT);
                Utility.launchActivity(HomepageActivity.this, ShowProfileActivity.class,false);
                break;

            case R.id.txt_rateus:
               /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.rate_us_link)));
                startActivity(intent);*/
                break;

            case R.id.tv_share_app:
              /*  Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, (getResources().getString(R.string.rate_us_link)));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);*/
                break;

            case R.id.txt_contact:
                Utility.launchActivity(HomepageActivity.this, AboutUsActivity.class,false);
                break;

            case R.id.txt_feedback:
                writeReviewMail(HomepageActivity.this);
                break;

            case R.id.txt_aboutus:
                Utility.launchActivity(HomepageActivity.this, AboutUsActivity.class,false);
                break;

            case R.id.rl_track_order:
                Utility.launchActivity(HomepageActivity.this, DeliveryboyStatusListActivity.class,false);
                break;

            case R.id.rl_Category:
                Utility.launchActivity(HomepageActivity.this, CategoryListActivity.class,false);
                break;

            case R.id.txt_logout:
                showCustomDialogLogout();

                break;

            case R.id.ib_twitter:
                Intent intentTwitter = null;
                intentTwitter = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/@teammandroid"));
                this.startActivity(intentTwitter);
                break;

            case R.id.ib_facebook:
                Intent intentFacebook = null;
                try {
                    // intentFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
                    intentFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ipsirssuccessacademy"));
                } catch (Exception e) {
                    // intentFacebook =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
                    intentFacebook =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ipsirssuccessacademy"));
                }
                startActivity(intentFacebook);
                // Toast.makeText(getContext(),"Comming soon ",Toast.LENGTH_SHORT).show();
                break;

            case R.id.ib_instagram:
                // Uri uri2 = Uri.parse("http://instagram.com/_u/teammandroid");
                Uri uri2 = Uri.parse("https://www.instagram.com/balasaheb.bodkhe.7/");
                Intent intentInstagram = new Intent(Intent.ACTION_VIEW, uri2);

                intentInstagram.setPackage("com.instagram.android");

                try {
                    startActivity(intentInstagram);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            // Uri.parse("http://instagram.com/teammandroid")));
                            Uri.parse("https://www.instagram.com/balasaheb.bodkhe.7/")));
                }

                break;

            case R.id.ib_whatsapp:
                String url = "https://api.whatsapp.com/send?phone="+"+91 72765 68803";
                Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                intentWhatsapp.setData(Uri.parse(url));
                startActivity(intentWhatsapp);
                break;

            case R.id.ib_linkedin:
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=you"));
                startActivity(intent1);
                // Toast.makeText(getContext(),"Comming soon",Toast.LENGTH_SHORT).show();
                break;

            case R.id.ib_telegram:
                Uri uri3 = Uri.parse("https://t.me/successacademypune");
                Intent intenttele = new Intent(Intent.ACTION_VIEW, uri3);

                //intenttele.setPackage("com.instagram.android");

                try {
                    startActivity(intenttele);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            // Uri.parse("https://t.me/OLSeducation")));
                            Uri.parse("https://t.me/successacademypune")));
                }
                break;

            case R.id.badgeLayout:
                Utility.launchActivity(HomepageActivity.this, CartActivity.class,
                        false);
                break;

            case R.id.rl_wishlist:
                Utility.launchActivity(HomepageActivity.this, WishlistActivity.class,
                        false);
                break;


        }
    }

    public static void writeReviewMail(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", context.getResources().getString(R.string.feedback_email), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.app_name)));
    }

    private void getSlider() {
     int d=0;
        try {
            if (Utility.isNetworkAvailable(HomepageActivity.this)) {
                progressDialog.setTitle("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                SliderService.getInstance(HomepageActivity.this).
                        GetSlides(1,new ApiStatusCallBack<ArrayList<SliderModel>>() {
                            @Override
                            public void onSuccess(ArrayList<SliderModel> sliderModels) {
                                progressDialog.dismiss();
                                slider(sliderModels);
                                Log.e("checkModel",""+sliderModels);
                            }

                            @Override
                            public void onError(ANError anError) {
                                // lyt_progress_employees.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                Log.e("errorHome",""+anError);
                                Utility.showErrorMessage(HomepageActivity.this, "Network:" + anError.getMessage(), Snackbar.LENGTH_LONG);

                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                // lyt_progress_employees.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                Utility.showErrorMessage(HomepageActivity.this, e.getMessage(), Snackbar.LENGTH_LONG);
                            }
                        });

            } else {
                Utility.showErrorMessage(HomepageActivity.this, "Could not connect to the internet", Snackbar.LENGTH_LONG);
            }
        } catch (Exception ex) {
            Utility.showErrorMessage(HomepageActivity.this, ex.getMessage(), Snackbar.LENGTH_LONG);
        }
    }

    public void slider(ArrayList<SliderModel> arraylist) {
        progressDialog.dismiss();
        HashMap<String, String> url_maps1 = new HashMap<String, String>();
        int id = 1;
        for (SliderModel model : arraylist) {
            Log.e("checkLists",""+arraylist);

            String imgPath = Constants.URL_USER_PIC +model.getImages();
            //String imgPath = model.getImages();
            //Log.e( "slider: ",imgPath);
            url_maps1.put(imgPath,imgPath);
        }
        for (String name : url_maps1.keySet()) {
            //Log.e("checkImg",""+name);
            DefaultSliderView defaultSliderView = new DefaultSliderView(this);
            defaultSliderView.image(url_maps1.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            defaultSliderView.bundle(new Bundle());
            defaultSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(defaultSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);//Fade
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.INTERNET)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            // showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! "+error, Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showCustomDialogLogout() {
        // this.correct = correct;
        resultbox = new Dialog(HomepageActivity.this);
        resultbox.setContentView(R.layout.custom_dialog);
        // resultbox.setCanceledOnTouchOutside(false);
        Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
        Button btn_cancel = (Button) resultbox.findViewById(R.id.btn_resume);
        TextView text_assign = resultbox.findViewById(R.id.text_title);

        text_assign.setText("Are you sure you want to exit ?");

        btn_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resultbox.cancel();
                SessionHelper sessionManager=new SessionHelper(HomepageActivity.this);
                sessionManager.logoutUser();
                prefManager.setUSER_ID(0);
                Utility.launchActivity(HomepageActivity.this,HomepageActivity.class,true);
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultbox.cancel();
            }
        });

        resultbox.show();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void getCategory() {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {
                progressDialog.setTitle("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                //Add porogressDialog
                Log.e("CheckReponseVideostry", "Called");
                CategoryServices.getInstance(HomepageActivity.this).fetchCategory(
                        new ApiStatusCallBack<ArrayList<CategoryModel>>() {

                            @Override
                            public void onSuccess(ArrayList<CategoryModel> arraylist) {
                                Log.e("CheckReponseVideosSucs", "Called");
                                progressDialog.dismiss();
                                bindVideoPackages(arraylist);
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("CheckReponseanError", ""+anError.getMessage());
                                progressDialog.dismiss();
                                Utility.showErrorMessage(HomepageActivity.this, "Network:" + anError.getMessage(), Snackbar.LENGTH_LONG);
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                Log.e("CheckReponseUnknown", "Called");
                                progressDialog.dismiss();
                                //   Utility.showErrorMessage(getActivity(), e.getMessage(), Snackbar.LENGTH_LONG);
                            }
                        });
            } else {
                Utility.showErrorMessage(HomepageActivity.this, "Could not connect to the internet", Snackbar.LENGTH_LONG);
            }
        }catch (Exception ex) {
            Log.e("CheckReponseOther", "Called");
            Log.e("GetVideoPackages","InsideGetVideoPackagesExtra"+ex);
            Utility.showErrorMessage(HomepageActivity.this, "No record found", Snackbar.LENGTH_LONG);
        }
    }
    private void getCategoryWithoutDialog() {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {

                Log.e("CheckReponseVideostry", "Called");
                CategoryServices.getInstance(HomepageActivity.this).fetchCategory(
                        new ApiStatusCallBack<ArrayList<CategoryModel>>() {

                            @Override
                            public void onSuccess(ArrayList<CategoryModel> arraylist) {
                                Log.e("CheckReponseVideosSucs", "Called");
                                bindVideoPackages(arraylist);
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("CheckReponseanError", ""+anError.getMessage());
                                Utility.showErrorMessage(HomepageActivity.this, "Network:" + anError.getMessage(), Snackbar.LENGTH_LONG);
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                Log.e("CheckReponseUnknown", "Called");
                                //   Utility.showErrorMessage(getActivity(), e.getMessage(), Snackbar.LENGTH_LONG);
                            }
                        });
            } else {
                Utility.showErrorMessage(HomepageActivity.this, "Could not connect to the internet", Snackbar.LENGTH_LONG);
            }
        }catch (Exception ex) {
            Log.e("CheckReponseOther", "Called");
            Log.e("GetVideoPackages","InsideGetVideoPackagesExtra"+ex);
            Utility.showErrorMessage(HomepageActivity.this, "No record found", Snackbar.LENGTH_LONG);
        }
    }

    private void bindVideoPackages(final ArrayList<CategoryModel> mVideoPackages) {
        Log.e("adapter called","yes");
        try {

            rv_home_video_pkg.setLayoutManager(new LinearLayoutManager(HomepageActivity.this,
                    LinearLayoutManager.HORIZONTAL, false));
            rv_home_video_pkg.setItemAnimator(new DefaultItemAnimator());
            rv_home_video_pkg.setHasFixedSize(true);

            categoryHomeAdapter = new CategoryHomeAdapter(HomepageActivity.this, mVideoPackages,
                    new CategoryHomeAdapter.ItemClickListener() {
                        @Override
                        public void onClick(View view, int position) {

                        }
                    });
            rv_home_video_pkg.setAdapter(categoryHomeAdapter);
            categoryHomeAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Utility.showErrorMessage(HomepageActivity.this, "No record found", Snackbar.LENGTH_LONG);
        }
    }


    private void productList(int Subcategoryid) {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {

                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                // progressDialog.setProgressStyle(R.id.abbreviationsBar);
                progressDialog.show();

                Log.e(TAG, "LoginUser: ");

                ProductServices.getInstance(getApplicationContext()).
                        fetchHomeProduct(Subcategoryid,new ApiStatusCallBack<ArrayList<ProductModel>>() {
                            @Override
                            public void onSuccess(ArrayList<ProductModel> productModels) {
                                progressDialog.dismiss();
                                BindList(productModels);
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e(TAG, "ANError " + anError.getMessage());
                                progressDialog.dismiss();
                                Utility.showErrorMessage(HomepageActivity.this, "No Attachment found", Snackbar.LENGTH_LONG);
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                progressDialog.dismiss();
                                Log.e(TAG, "exc " + e.getMessage());
                                //Utility.showErrorMessage(CategoryListActivity.this, e.getMessage(), Snackbar.LENGTH_LONG);

                            }

                        });
            } else {
                Utility.showErrorMessage(HomepageActivity.this, "Could not connect to the internet");
            }
        } catch (Exception ex) {
            //  lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            Utility.showErrorMessage(HomepageActivity.this, ex.getMessage());
        }
    }
    private void productListWithoutDialog(int Subcategoryid) {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {

                ProductServices.getInstance(getApplicationContext()).
                        fetchHomeProduct(Subcategoryid,new ApiStatusCallBack<ArrayList<ProductModel>>() {
                            @Override
                            public void onSuccess(ArrayList<ProductModel> productModels) {
                                BindList(productModels);
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e(TAG, "ANError " + anError.getMessage());
                                Utility.showErrorMessage(HomepageActivity.this, "No Attachment found", Snackbar.LENGTH_LONG);
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                Log.e(TAG, "exc " + e.getMessage());
                                //Utility.showErrorMessage(CategoryListActivity.this, e.getMessage(), Snackbar.LENGTH_LONG);

                            }

                        });
            } else {
                Utility.showErrorMessage(HomepageActivity.this, "Could not connect to the internet");
            }
        } catch (Exception ex) {
            //  lyt_progress_reg.setVisibility(View.GONE);
            Utility.showErrorMessage(HomepageActivity.this, ex.getMessage());
        }
    }

    private void BindList(final ArrayList<ProductModel> mUserList) {
        try {

            setTitle("NotesPackages (" + mUserList.size() + ")");
            Log.e(TAG, "CAtemUserList: "+mUserList.toString());
            recyclerView.setLayoutManager(new GridLayoutManager(HomepageActivity.this,2));

            // recyclerView.setLayoutManager(new LinearLayoutManager(HomepageActivity.this,
            //        LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);

            productListHomeAdapter = new ProductListHomeAdapter(HomepageActivity.this,
                    mUserList, new ProductListHomeAdapter.ItemClickListener() {
                @Override
                public void onClick(View view, int position) {

                }
            });

            recyclerView.setAdapter(productListHomeAdapter);
            productListHomeAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            //Utility.showErrorMessage(this, ex.getMessage(), Snackbar.LENGTH_LONG);
        }
    }

    private  void getCount()
    {

        SQLiteDatabase db=dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT SUM (" + QUANTITY_4 + ") FROM " +
                dbHelper.TABLE_QUANTITY +" WHERE userid="+prefManager.getUSER_ID(), null);

        int id = 0;

        if(cursor.moveToNext()){

            id = cursor.getInt(0);//to get id, 0 is the column index
            //productId=cursor.getInt(1);
            //userid=cursor.getInt(1);
            //prefManager.setCOUNT_ID(id);
        }

        // int count = bookingSqliteOperations.GetBookings().size();
        badgeLayout.setCountWithAnimation(id);
        //Toast.makeText(activity,"count "+ id , Toast.LENGTH_LONG).show();

    }

    private  void getCountForNotLogin()
    {

        badgeLayout.setCountWithAnimation(0);

    }
}
