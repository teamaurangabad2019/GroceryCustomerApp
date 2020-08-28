package com.teammandroid.dairyapplication.admin.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.steelkiwi.library.view.BadgeHolderLayout;
import com.teammandroid.dairyapplication.Network.CategoryServices;
import com.teammandroid.dairyapplication.Network.ProductServices;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.activities.HomepageActivity;
import com.teammandroid.dairyapplication.admin.adapters.ProductViewListAdapter;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.admin.model.SubcategoryModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.offline.DatabaseHelper;
import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;

import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_4;

public class ProductListActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ProductListActivity.class.getSimpleName();

    LinearLayout lyt_progress;
    RecyclerView rv_productlist;
    EditText toolbarEditText;

    RelativeLayout originalToolbar;
    RelativeLayout replaceToolbar;
    private ArrayList<ProductModel> mList = new ArrayList<>();
    View viewSearch, viewReplaceClear;

    FloatingActionButton floating_create;
    ProgressDialog progressDialog;


    int categoryid;
    String categoryName;

    View viewMenuIconBack;
    Spinner sp_category;

    ProductViewListAdapter categoryListAdapter;
    ProductModel expenseCategoryHolder;
    Activity activity;

    ImageView iv_add;
    Dialog resultbox;
    SubcategoryModel subcategoryModel;
    TextView txtTitleBar;
    DatabaseHelper dbHelper;

    BadgeHolderLayout badgeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        bindView();
        btnlistener();
        Log.e("itemee", String.valueOf(categoryid));
        subcategoryModel = getIntent().getParcelableExtra("SubcategoryModel");

        txtTitleBar.setText("Product List");

        productList(subcategoryModel.getSubcategoryid());
        //productList(22);
        getCount();
        //getExpenseList(expenseCategoryHolder.getCategoryid());

        iv_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putParcelable("SubcategoryModel", subcategoryModel);
                Utility.launchActivity(ProductListActivity.this, AddProductActivity.class, true,bundle);
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
                InputMethodManager imm = (InputMethodManager) ProductListActivity.this.getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.showSoftInput(toolbarEditText, 0);
                //imm.showSoftInputFromWindow(mainActivity.toolbarEditText.getWindowToken(), 0);
                filter(s.toString());
            }
        });
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
                        fetchProduct(Subcategoryid,new ApiStatusCallBack<ArrayList<ProductModel>>() {
                            @Override
                            public void onSuccess(ArrayList<ProductModel> productModels) {
                                progressDialog.dismiss();
                                BindList(productModels);
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e(TAG, "ANError " + anError.getMessage());
                                progressDialog.dismiss();
                                Utility.showErrorMessage(ProductListActivity.this, "No Attachment found", Snackbar.LENGTH_LONG);
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                progressDialog.dismiss();
                                Log.e(TAG, "exc " + e.getMessage());
                                //Utility.showErrorMessage(CategoryListActivity.this, e.getMessage(), Snackbar.LENGTH_LONG);

                            }

                        });
            } else {
                Utility.showErrorMessage(ProductListActivity.this, "Could not connect to the internet");
            }
        } catch (Exception ex) {
            //  lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            Utility.showErrorMessage(ProductListActivity.this, ex.getMessage());
        }
    }

    private void BindList(final ArrayList<ProductModel> mUserList) {
        try {
            progressDialog.dismiss();

            setTitle("NotesPackages (" + mUserList.size() + ")");
            Log.e(TAG, "CAtemUserList: "+mUserList.toString());

             rv_productlist.setLayoutManager(new LinearLayoutManager(ProductListActivity.this,
                    LinearLayoutManager.VERTICAL, false));
            rv_productlist.setItemAnimator(new DefaultItemAnimator());
            rv_productlist.setHasFixedSize(true);

            categoryListAdapter = new ProductViewListAdapter(ProductListActivity.this,
                    mUserList, new ProductViewListAdapter.ItemClickListener() {
                @Override
                public void onClick(View view, int position) {

                }
            });

            rv_productlist.setAdapter(categoryListAdapter);
            categoryListAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            //Utility.showErrorMessage(this, ex.getMessage(), Snackbar.LENGTH_LONG);
        }
    }
    
    private void filter(String text) {
        ArrayList<ProductModel> filterdNames = new ArrayList<>();
        for (ProductModel member : mList) {

            String name = member.getTitle().toLowerCase();
           // String code = member.getInstituteid();

            if (name.contains(text.toLowerCase()) )
                filterdNames.add(member);
        }

      // categoryListAdapter.setFilter(filterdNames);
    }
    
    private void bindView() {

        lyt_progress =  findViewById(R.id.lyt_progress);
        sp_category =  findViewById(R.id.sp_category);
        txtTitleBar =  findViewById(R.id.txtTitleBar);
        rv_productlist =  findViewById(R.id.rv_categorylist);
        toolbarEditText =  findViewById(R.id.toolbarEditText);
        originalToolbar =  findViewById(R.id.originalToolbar);
        replaceToolbar =  findViewById(R.id.replaceToolbar);
        viewSearch =  findViewById(R.id.viewSearch);
        viewReplaceClear =  findViewById(R.id.viewReplaceClear);
        viewMenuIconBack =  findViewById(R.id.viewMenuIconBack);
        floating_create =   findViewById(R.id.floating_create);
        iv_add =   findViewById(R.id.iv_add);
        badgeLayout = findViewById(R.id.badgeLayout);
        dbHelper = new DatabaseHelper(ProductListActivity.this);
        progressDialog=new ProgressDialog(ProductListActivity.this);
        activity= ProductListActivity.this;
    }

    private void btnlistener() {
        viewReplaceClear.setOnClickListener(this);
        viewSearch.setOnClickListener(this);
        viewMenuIconBack.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        badgeLayout.setOnClickListener(this);
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
                onBackPressed();
                break;

               case R.id.badgeLayout:
                  Utility.launchActivity(ProductListActivity.this,CartActivity.class,false);
                   //Toast.makeText(activity,"count " , Toast.LENGTH_LONG).show();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showCustomDialogNoRecord() {
        // this.correct = correct;
        resultbox = new Dialog(ProductListActivity.this);
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

    private  void getCount()
    {

        SQLiteDatabase db=dbHelper.getWritableDatabase();

        //SELECT sum(count) from quantity where productid = 21;
        Cursor cursor = db.rawQuery("SELECT SUM (" + QUANTITY_4 + ") FROM " + dbHelper.TABLE_QUANTITY +" WHERE "+ QUANTITY_2 , null);

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

    public void ReloadActivity() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
