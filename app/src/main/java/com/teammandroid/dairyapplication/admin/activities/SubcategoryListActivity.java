package com.teammandroid.dairyapplication.admin.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.error.ANError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.teammandroid.dairyapplication.Network.CategoryServices;
import com.teammandroid.dairyapplication.Network.SubCategoryServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.activities.HomepageActivity;
import com.teammandroid.dairyapplication.admin.adapters.CategoryViewListAdapter;
import com.teammandroid.dairyapplication.admin.adapters.SubCategoryAdapter;
import com.teammandroid.dairyapplication.admin.model.CategoryModel;
import com.teammandroid.dairyapplication.admin.model.SubcategoryModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;

public class SubcategoryListActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SubcategoryListActivity.class.getSimpleName();

    LinearLayout lyt_progress;
    RecyclerView rv_categorylist;
    CategoryViewListAdapter expenseListAdapter;
    EditText toolbarEditText;

    RelativeLayout originalToolbar;
    RelativeLayout replaceToolbar;
    private ArrayList<CategoryModel> mList = new ArrayList<>();
    View viewSearch, viewReplaceClear;

    FloatingActionButton floating_create;
    ProgressDialog progressDialog;


    int categoryid;
    String categoryName;

    View viewMenuIconBack;
    Spinner sp_category;

    SubCategoryAdapter subCategoryAdapter;
    CategoryModel expenseCategoryHolder;
    Activity activity;

    ImageView iv_add;
    Dialog resultbox;
    CategoryModel categoryModel;
    TextView txtTitleBar;
    SwipeRefreshLayout swipeLayout;
    RelativeLayout childlayout,parentlayout;
    TextView txt_error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        bindView();
        btnlistener();
        Log.e("itemee", String.valueOf(categoryid));

        expenseCategoryHolder=getIntent().getParcelableExtra("expenseCategory");

        categoryModel = getIntent().getParcelableExtra("CategoryModel");

        // Log.e(TAG," chk "+categoryModel.getDetails() +" "+ categoryModel.getImagename());
        txtTitleBar.setText("Subcategory List");

        subcategoryList(categoryModel.getCategoryid());
        //getExpenseList(expenseCategoryHolder.getCategoryid());

        iv_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putParcelable("CategoryModel", categoryModel);
                Utility.launchActivity(SubcategoryListActivity.this, AddSubCategoryActivity.class, false,bundle);
            }
        });

        toolbarEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InputMethodManager imm = (InputMethodManager) SubcategoryListActivity.this.getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.showSoftInput(toolbarEditText, 0);
                //imm.showSoftInputFromWindow(mainActivity.toolbarEditText.getWindowToken(), 0);
                filter(s.toString());
            }
        });

        swipeLayout = findViewById(R.id.swipe_container);
        // Adding Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code here

                subcategoryListWithoutDialog(categoryModel.getCategoryid());
                // Toast.makeText(getApplicationContext(), "Works!", Toast.LENGTH_LONG).show();
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);

                    }
                }, 4000); // Delay in millis
            }
        });

    }

    private void subcategoryList(int Categoryid) {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {

                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                // progressDialog.setProgressStyle(R.id.abbreviationsBar);
                progressDialog.show();

                Log.e(TAG, "LoginUser: ");

                SubCategoryServices.getInstance(getApplicationContext()).
                        fetchSubCategorywise(Categoryid,new ApiStatusCallBack<ArrayList<SubcategoryModel>>() {
                            @Override
                            public void onSuccess(ArrayList<SubcategoryModel> classModels) {
                                progressDialog.dismiss();
                                BindList(classModels);
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e(TAG, "ANError " + anError.getMessage());
                                progressDialog.dismiss();
                                //  Utility.showErrorMessage(SubcategoryListActivity.this, "No Attachment found", Snackbar.LENGTH_LONG);
                                Utility.setError(parentlayout,childlayout,txt_error,getResources().getString(R.string.data_not_available));

                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                progressDialog.dismiss();
                                Log.e(TAG, "exc " + e.getMessage());
                                Utility.setError(parentlayout,childlayout,txt_error,getResources().getString(R.string.data_not_available));

                                //Utility.showErrorMessage(CategoryListActivity.this, e.getMessage(), Snackbar.LENGTH_LONG);

                            }

                        });
            } else {
                //   Utility.showErrorMessage(SubcategoryListActivity.this, "Could not connect to the internet");
                Utility.setError(parentlayout,childlayout,txt_error,getResources().getString(R.string.internet));

            }
        } catch (Exception ex) {
            //  lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            // Utility.showErrorMessage(SubcategoryListActivity.this, ex.getMessage());
            Utility.setError(parentlayout,childlayout,txt_error,getResources().getString(R.string.data_not_available));

        }
    }
    private void subcategoryListWithoutDialog(int Categoryid) {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {

                Log.e(TAG, "LoginUser: ");

                SubCategoryServices.getInstance(getApplicationContext()).
                        fetchSubCategorywise(Categoryid,new ApiStatusCallBack<ArrayList<SubcategoryModel>>() {
                            @Override
                            public void onSuccess(ArrayList<SubcategoryModel> classModels) {
                                BindList(classModels);
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e(TAG, "ANError " + anError.getMessage());
                                //  Utility.showErrorMessage(SubcategoryListActivity.this, "No Attachment found", Snackbar.LENGTH_LONG);
                                Utility.setError(parentlayout,childlayout,txt_error,getResources().getString(R.string.data_not_available));

                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                Log.e(TAG, "exc " + e.getMessage());
                                //Utility.showErrorMessage(CategoryListActivity.this, e.getMessage(), Snackbar.LENGTH_LONG);
                                Utility.setError(parentlayout,childlayout,txt_error,getResources().getString(R.string.data_not_available));


                            }

                        });
            } else {
                // Utility.showErrorMessage(SubcategoryListActivity.this, "Could not connect to the internet");

                Utility.setError(parentlayout,childlayout,txt_error,getResources().getString(R.string.internet));

            }
        } catch (Exception ex) {
            //  lyt_progress_reg.setVisibility(View.GONE);
            //  Utility.showErrorMessage(SubcategoryListActivity.this, ex.getMessage());
            Utility.setError(parentlayout,childlayout,txt_error,getResources().getString(R.string.data_not_available));

        }
    }

    private void BindList(final ArrayList<SubcategoryModel> mUserList) {
        try {
            progressDialog.dismiss();

            setTitle("NotesPackages (" + mUserList.size() + ")");
            Log.e(TAG, "CAtemUserList: "+mUserList.toString());

            rv_categorylist.setLayoutManager(new GridLayoutManager(SubcategoryListActivity.this,2));
            rv_categorylist.setItemAnimator(new DefaultItemAnimator());
            rv_categorylist.setHasFixedSize(true);

            subCategoryAdapter = new SubCategoryAdapter(SubcategoryListActivity.this,
                    mUserList, new SubCategoryAdapter.ItemClickListener() {
                @Override
                public void onClick(View view, int position) {

                }
            });

            rv_categorylist.setAdapter(subCategoryAdapter);
            subCategoryAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            //Utility.showErrorMessage(this, ex.getMessage(), Snackbar.LENGTH_LONG);
        }
    }

    private void filter(String text) {
        ArrayList<CategoryModel> filterdNames = new ArrayList<>();
        for (CategoryModel member : mList) {

            String name = member.getTitle().toLowerCase();
            // String code = member.getInstituteid();

            if (name.contains(text.toLowerCase()) )
                filterdNames.add(member);
        }

        //SubCategoryAdapter.setFilter(filterdNames);
    }

    private void bindView() {

        lyt_progress =  findViewById(R.id.lyt_progress);
        sp_category =  findViewById(R.id.sp_category);
        rv_categorylist =  findViewById(R.id.rv_categorylist);
        toolbarEditText =  findViewById(R.id.toolbarEditText);
        originalToolbar =  findViewById(R.id.originalToolbar);
        replaceToolbar =  findViewById(R.id.replaceToolbar);
        viewSearch =  findViewById(R.id.viewSearch);
        viewReplaceClear =  findViewById(R.id.viewReplaceClear);
        viewMenuIconBack =  findViewById(R.id.viewMenuIconBack);
        floating_create =   findViewById(R.id.floating_create);
        iv_add =   findViewById(R.id.iv_add);
        txtTitleBar =   findViewById(R.id.txtTitleBar);
        progressDialog=new ProgressDialog(SubcategoryListActivity.this);
        activity= SubcategoryListActivity.this;

        childlayout=findViewById(R.id.childlayout);
        parentlayout=findViewById(R.id.parentlayout);
        txt_error=findViewById(R.id.txt_error);

    }

    private void btnlistener() {
        viewReplaceClear.setOnClickListener(this);
        viewSearch.setOnClickListener(this);
        viewMenuIconBack.setOnClickListener(this);
        iv_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.viewSearch:
                originalToolbar.setVisibility(View.GONE);
                replaceToolbar.setVisibility(View.VISIBLE);
                toolbarEditText.requestFocus();
                break;

            case R.id.viewReplaceClear:
                toolbarEditText.setText("");
                originalToolbar.setVisibility(View.VISIBLE);
                replaceToolbar.setVisibility(View.GONE);
                break;

            case R.id.viewMenuIconBack:
                Utility.launchActivity(SubcategoryListActivity.this,CategoryListActivity.class,true);
                break;


        }
    }

    /* @Override
     public void onBackPressed() {
         super.onBackPressed();
         Utility.launchActivity(SubcategoryListActivity.this,CategoryListActivity.class,true);
     }
 */
    private void showCustomDialogNoRecord() {
        // this.correct = correct;
        resultbox = new Dialog(SubcategoryListActivity.this);
        resultbox.setContentView(R.layout.custom_dialog);
        resultbox.setCanceledOnTouchOutside(false);
        Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
        Button btn_cancel = (Button) resultbox.findViewById(R.id.btn_resume);
        TextView text_assign = resultbox.findViewById(R.id.text_title);

        text_assign.setText(R.string.text_dialog);

        btn_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                resultbox.cancel();
                //Utility.launchActivity(HomeworkListActivity.this,HomeworkListActivity.class,true);
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);// Toast.makeText(getApplicationContext(),"DialogunPaidFinishBtn",Toast.LENGTH_LONG).show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resultbox.cancel();
                finish();
                onBackPressed();
                //Utility.launchActivity(HomeworkListActivity.this,HomeworkListActivity.class,true);
            }
        });

        resultbox.show();
    }
}
