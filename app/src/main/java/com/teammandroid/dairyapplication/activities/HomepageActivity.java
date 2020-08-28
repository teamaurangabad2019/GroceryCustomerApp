package com.teammandroid.dairyapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.teammandroid.dairyapplication.Network.CategoryServices;
import com.teammandroid.dairyapplication.Network.SliderService;
import com.teammandroid.dairyapplication.R;

import com.teammandroid.dairyapplication.admin.activities.CategoryListActivity;
import com.teammandroid.dairyapplication.admin.adapters.CategoryHomeAdapter;
import com.teammandroid.dairyapplication.admin.adapters.RecyclerViewAdapter;
import com.teammandroid.dairyapplication.admin.model.CategoryModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.model.SliderModel;
import com.teammandroid.dairyapplication.model.UserModel;

import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.SessionHelper;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomepageActivity extends AppCompatActivity implements View.OnClickListener,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener
  , PaymentResultListener {

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
    private CardView cv_slider;
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
    private RelativeLayout rl_profile;
    private ImageView iv_daily_service_request;
    /**
     * Monthly Service Record
     */
    private TextView tv_daily_service_request;
    private RelativeLayout rl_enrolledstudent;
    private ImageView iv_payment;
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
    private TextView tv_poweredBy;
    private ScrollView Scrll_Drawer;
    DrawerLayout drl_Opener;

    ImageView iv_add_cust;
    ProgressDialog progressDialog;
    RelativeLayout rl_video;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_homepage);
        activity=HomepageActivity.this;

        bindView();
        listener();
        getSlider();

        Checkout.preload(getApplicationContext());

        requestPermission();
        getCategory();
        getImages();
    }

    private void listener() {
        img_openDrawer.setOnClickListener(this);
        rl_headerprofile.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        rl_profile.setOnClickListener(this);
        rl_enrolledstudent.setOnClickListener(this);
        rl_paymentHist.setOnClickListener(this);
        rl_paidelecture.setOnClickListener(this);
        rl_insert_video_link.setOnClickListener(this);
        rl_demo_lecture.setOnClickListener(this);
        rl_delete_count.setOnClickListener(this);
        iv_add_cust.setOnClickListener(this);
        txt_contact.setOnClickListener(this);
        rl_video.setOnClickListener(this);
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
    }

    private void bindView() {
        img_openDrawer = (ImageView) findViewById(R.id.img_openDrawer);
        img_whatsapp = (ImageView) findViewById(R.id.img_whatsapp);
        img_call = (ImageView) findViewById(R.id.img_call);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        custom_indicator = (PagerIndicator) findViewById(R.id.custom_indicator);
        sliderlayout = (LinearLayout) findViewById(R.id.sliderlayout);
        cv_slider = (CardView) findViewById(R.id.cv_slider);
        rv_home_video_pkg = (RecyclerView) findViewById(R.id.rv_home_video_pkg);
        recyclerView = findViewById(R.id.rv_products1);
        rl_headerprofile = (RelativeLayout) findViewById(R.id.rl_headerprofile);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        drl_Opener =  findViewById(R.id.drl_Opener);

        rl_enrolledstudent = (RelativeLayout) findViewById(R.id.rl_enrolledstudent);

        rl_paymentHist = (RelativeLayout) findViewById(R.id.rl_paymentHist);
        rl_video = (RelativeLayout) findViewById(R.id.rl_video);

        rl_paidelecture = (RelativeLayout) findViewById(R.id.rl_paidelecture);

        rl_insert_video_link = (RelativeLayout) findViewById(R.id.rl_insert_video_link);

        rl_demo_lecture = (RelativeLayout) findViewById(R.id.rl_demo_lecture);

        rl_delete_count = (RelativeLayout) findViewById(R.id.rl_delete_count);
        txt_aboutus = (TextView) findViewById(R.id.txt_aboutus);
        txt_rateus = (TextView) findViewById(R.id.txt_rateus);
        txt_contact = (TextView) findViewById(R.id.txt_contact);
        txt_logout = (TextView) findViewById(R.id.txt_logout);
        txt_feedback = (TextView) findViewById(R.id.txt_feedback);
        tv_poweredBy = (TextView) findViewById(R.id.tv_poweredBy);
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
            case R.id.rl_profile:
                drl_Opener.closeDrawer(Gravity.LEFT);
                Utility.launchActivity(HomepageActivity.this,NavProfileActivity.class,false);
                break;
            case R.id.rl_enrolledstudent:
                drl_Opener.closeDrawer(Gravity.LEFT);
                Toast.makeText(getApplicationContext(),"CLick here",Toast.LENGTH_SHORT).show();
                break;

            case R.id.rl_paymentHist:
                drl_Opener.closeDrawer(Gravity.LEFT);
                //Utility.launchActivity(HomepageActivity.this,PaymenyHistoryActivity.class,false);
                //Toast.makeText(getApplicationContext(),"CLick here",Toast.LENGTH_SHORT).show();

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
                Utility.launchActivity(HomepageActivity.this, CategoryListActivity.class,false);
                break;

             case R.id.txt_rateus:
                 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.rate_us_link)));
                 startActivity(intent);
                 break;

            case R.id.tv_share_app:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, (getResources().getString(R.string.rate_us_link)));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
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

        }
    }

        public static void writeReviewMail(Context context) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", context.getResources().getString(R.string.feedback_email), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.app_name)));
        }

        private void getSlider() {

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
                    prefManager.setROLE_ID(5);
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

/*
        private void checkAlreadyPurchased(int packageId,int userId, int total_days) {

            try {
                if (Utility.isNetworkAvailable(activity)){
                    progressDialog.show();
                    Log.e("CheckPackage", "Called");
                    PaymentServices.getInstance(activity).
                            CheckPakageExist(packageId,userId,new ApiStatusCallBack<ArrayList<PaymentCheckModel>>() {
                                @Override
                                public void onSuccess(ArrayList<PaymentCheckModel> arrayList) {
                                    //  lyt_progress_employees.setVisibility(View.GONE);
                                    progressDialog.dismiss();

                                    PaymentCheckModel paymentItem = arrayList.get(0);
                                    Log.e(TAG, "onSuccess: payment"+paymentItem +" "+packageId );
                                    Log.e(TAG, "onSuccess: already exist");
                                    Bundle bundle = new Bundle();
                                    Toast.makeText(getApplicationContext(),"Exist",Toast.LENGTH_LONG).show();
                                    bundle.putInt("Videopackageid", packageId);
                                    bundle.putInt("total_days", total_days);
                                    bundle.putString("Paiddate", paymentItem.getPaiddate());
                                    // bundle.putParcelable("VideoPackagesModel", item);
                                  //  checkDateExtendedFunction(total_days);
                                    Log.e("Videopackageid", String.valueOf(packageId));
                                    Utility.launchActivity(activity, VideoActivity.class, false, bundle);

                                    //showCustomDialogForPaid(packageId,prefManager.getUSER_ID());
                                   // Utility.showErrorMessage(activity, "You Already Have Purchased This program ");
                                }


                                @Override
                                public void onError(ANError anError) {
                                    //  lyt_progress_employees.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    Log.e(TAG, "ANError: chkPayment "+anError.getErrorBody() );
                                    //No Record Found
                                    checkDateExtendedFunctionForNewPayment(total_days,  pamount);
                                   // showCustomDialogForUnpaid(packageId,prefManager.getUSER_ID(),amount);
                                    //Utility.showErrorMessage(activity, "Network:" + anError.getMessage(), Snackbar.LENGTH_LONG);
                                }
                                @Override
                                public void onUnknownError(Exception e) {
                                    //lyt_progress_employees.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Exc e"+e.getMessage(),Toast.LENGTH_LONG).show();
                                    Utility.showErrorMessage(activity, e.getMessage(), Snackbar.LENGTH_LONG);
                                }
                            });
                } else {
                    Utility.showErrorMessage(activity, "Could not connect to the internet", Snackbar.LENGTH_LONG);
                }
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(),"ex "+ex.getMessage(),Toast.LENGTH_LONG).show();
                Utility.showErrorMessage(activity, ex.getMessage(), Snackbar.LENGTH_LONG);
            }

        }
*/

        private void showCustomDialogForUnpaid(double amount) {
            // this.correct = correct;
            resultbox = new Dialog(activity);
            resultbox.setContentView(R.layout.custom_dialog);
            // resultbox.setCanceledOnTouchOutside(false);
            Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
            Button btn_cancel = (Button) resultbox.findViewById(R.id.btn_resume);
            TextView text_assign = resultbox.findViewById(R.id.text_title);

            text_assign.setText("Are you want to purchase Package ?");

            btn_finish.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    resultbox.cancel();
                    Toast.makeText(getApplicationContext(),"DialogunPaidFinishBtn",Toast.LENGTH_LONG).show();
                    Log.e("Amount ", String.valueOf(amount));
                    startPayment("",amount);
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(),"DialogunPaidCancelBtn",Toast.LENGTH_LONG).show();
                    resultbox.cancel();
                }
            });

            resultbox.show();
        }

        private void checkDateExtendedFunction(int total_days) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date today = Calendar.getInstance().getTime();
            String startDate = sdf.format(today);
            Log.e(TAG, "onPaymentSuccess: today "+today );
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(startDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
           // Log.e(TAG, "onPaymentSuccess: Duration "+programModel.getDuration() );
            c.add(Calendar.DATE, total_days);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            String endDate = sdf.format(c.getTime());
            expirydate = sdf.format(c.getTime());
            Log.e(TAG, "onPaymentSuccess: exp "+endDate );

            //AddPayment(0,prefManager.getUSER_ID(),packageId,Utility.getCurrentDate());

        }


        private void checkDateExtendedFunctionForNewPayment(int total_days, double pamount) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date today = Calendar.getInstance().getTime();
            String startDate = sdf.format(today);
            Log.e(TAG, "onPaymentSuccess: today "+today );
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(startDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
           // Log.e(TAG, "onPaymentSuccess: Duration "+programModel.getDuration() );
            c.add(Calendar.DATE, total_days);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            String endDate = sdf.format(c.getTime());
            expirydate = sdf.format(c.getTime());
            Log.e(TAG, "onPaymentSuccess: exp "+expirydate );

            showCustomDialogForUnpaid(pamount);

            //AddPayment(0,prefManager.getUSER_ID(),packageId,Utility.getCurrentDate());

        }
        private void showCustomDialogForPaid(int packageId,int userId) {
            // this.correct = correct;
            resultbox = new Dialog(activity);
            resultbox.setContentView(R.layout.custom_dialog);
            // resultbox.setCanceledOnTouchOutside(false);
            Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
            Button btn_cancel = (Button) resultbox.findViewById(R.id.btn_resume);
            TextView text_assign = resultbox.findViewById(R.id.text_title);

            text_assign.setText("You already have this package,Do you want to watch Videos ??");

            btn_finish.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("Videopackageid", packageId);
                    // bundle.putParcelable("VideoPackagesModel", item);
                    Log.e("Videopackageid", String.valueOf(packageId));
                   //18-08
                    // Utility.launchActivity(activity, VideoActivity.class, false, bundle);
                    resultbox.cancel();

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

        public void startPayment(String orderId,double price) {

            /**   * Instantiate Checkout   */
            Checkout checkout = new Checkout();
            checkout.setKeyID(Constants.RAZOR_ID);
            /*** Set your logo here   */
            //checkout.setImage(R.drawable.logo);
            /**   * Reference to current activity   */
            final Activity activity = this;
            /**   * Pass your payment options to the Razorpay Checkout as a JSONObject   */
            try {
                JSONObject options = new JSONObject();
                /**      * Merchant Name      * eg: ACME Corp || HasGeek etc.      */
                options.put("name", "SuccessAcademy");
                /**      * Description can be anything      * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.      *     Invoice Payment      *     etc.      */
                options.put("description", "Reference No. #123456");
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                // options.put("order_id", orderId);
                options.put("currency", "INR");
                /**      * Amount is always passed in currency subunits      * Eg: "500" = INR 5.00      */

                double finalPrice = price*100;
                options.put("amount", String.valueOf(finalPrice));
                checkout.open(activity, options);

            } catch(Exception e) {
                Toast.makeText(getApplicationContext(),"Error in starting Razorpay Checkout "+e.getMessage(),Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error in starting Razorpay Checkout", e);
            }
        }

        @Override
        public void onPaymentSuccess(String s) {
            Log.e(TAG, "onPaymentSuccess: " + s +"pid "+packageId+"uid "+prefManager.getUSER_ID());
           // AddPayment(0,prefManager.getUSER_ID(),packageId,Utility.getCurrentDate(), expirydate );
        }

        @Override
        public void onPaymentError(int i, String s) {
            Log.e(TAG, "onPaymentError: " + s);
        }

       /* private void AddPayment(int Paymentid,int Userid,int Packageid,String Paiddate, String Expirydate)
        {
            try{
                if (Utility.isNetworkAvailable(activity)) {
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    PaymentServices.getInstance(activity).addPayment(Paymentid,
                            Userid, Packageid, Paiddate, Expirydate, new ApiStatusCallBack<Response>() {
                                @Override
                                public void onSuccess(Response response) {
                                    progressDialog.dismiss();
                                    Log.e("PaymentModel", response.getMessage());
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("Videopackageid", packageId);
                                    // bundle.putParcelable("VideoPackagesModel", item);
                                    Log.e("packageIdSuccess", String.valueOf(packageId));
                                    Utility.launchActivity(activity, VideoActivity.class, false, bundle);
                                    Toast.makeText(activity, "Added Successfully !!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(ANError anError) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"ANErrorAdd "+anError.getMessage(),Toast.LENGTH_LONG).show();
                                    Log.e("PaymentModel", anError.getMessage());

                                }

                                @Override
                                public void onUnknownError(Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"exc e "+e.getMessage(),Toast.LENGTH_LONG).show();
                                    Log.e("PaymentModel", e.getMessage());

                                }
                            });
                }else {
                    Utility.showErrorMessage(activity, "Could not connect to the internet", Snackbar.LENGTH_LONG);
                }
            }catch (Exception ex) {
                Toast.makeText(getApplicationContext(),"ex "+ex.getMessage(),Toast.LENGTH_LONG).show();
                Log.e("CheckReponseOther","InsideExtra"+ex);
                progressDialog.dismiss();
                // Utility.showErrorMessage(PaymenyHistoryActivity.this, "No record found", Snackbar.LENGTH_LONG);
            }

        }
*/

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

    /**RECYCLERVIEW PRODUCT LIST **/
    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        String offerprice="30%OFF";
        String rs="Rs.";


        mOffersprice.add(offerprice);
        mImage.add("https://us.123rf.com/450wm/atoss/atoss1510/atoss151000013/46938944-ripe-mango-isolated-on-white-clipping-path.jpg?ver=6");
        mLabel.add("Mongo 1 Kg/Dozen sell");
        mWeight.add("240g");
        mPrice.add(rs+"994/-");
        mCutoff.add(rs+"1000/-");

        mOffersprice.add(offerprice);
        mImage.add("https://media.istockphoto.com/photos/pineapple-isolated-picture-id90965948?k=6&m=90965948&s=612x612&w=0&h=hbUfhxwZe3-yJ20Xkeo8pns9nMU0iHARNV0yIGu86WY=");
        mLabel.add("Pineapple 1 Kg/Dozen sell");
        mWeight.add("270g");
        mPrice.add(rs+"704/-");
        mCutoff.add(rs+"1400/-");

        mOffersprice.add(offerprice);
        mImage.add("https://media.istockphoto.com/photos/red-apple-with-leaf-picture-id683494078?k=6&m=683494078&s=612x612&w=0&h=aVyDhOiTwUZI0NeF_ysdLZkSvDD4JxaJMdWSx2p3pp4=");
        mLabel.add("Apple 1 Kg/Dozen sell");
        mWeight.add("440g");
        mPrice.add(rs+"304/-");
        mCutoff.add(rs+"500/-");




        initRecyclerView();

    }
    // recycler view adapters
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");



        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
       /* GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);*/


        /*recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));*/
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mOffersprice, mImage, mLabel,mWeight,mPrice,mCutoff);
        recyclerView.setAdapter(adapter);
    }
    }
