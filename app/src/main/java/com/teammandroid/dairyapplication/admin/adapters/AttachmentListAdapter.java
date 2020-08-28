package com.teammandroid.dairyapplication.admin.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.admin.model.CategoryModel;
import com.teammandroid.dairyapplication.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class AttachmentListAdapter extends RecyclerView.Adapter<AttachmentListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<CategoryModel> attachmentModels;
    private  ItemClickListener itemClickListener;
    private Activity activity;


    public AttachmentListAdapter(Activity activity, ArrayList<CategoryModel>   attachmentModels, ItemClickListener itemClickListener){

        this.activity = activity;
        this.attachmentModels =   attachmentModels;
        this.itemClickListener=itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.list_view_layout_xml, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

              return new MyViewHolder(view);
        
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoryModel item=attachmentModels.get(position);
        holder.tv_homeworkAttachment.setText(item.getTitle());

        /*if (item.getFilename().equals("")){
            holder.img_title.setImageResource(R.drawable.ic_attachment_black_24dp);
         }
        else{*/
            String profile_path = Constants.URL_CATEGORY_IMG + item.getImagename();
            Picasso.with(activity)
                    .load(profile_path).into(holder.img_title);

            holder.img_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Intent intent= new Intent(activity, ViewImageActivity.class);
                    intent.putExtra("image_url",profile_path);
                    activity.startActivity(intent);
                */
                   /* Bundle bundle=new Bundle();
                    bundle.putString("image_url",profile_path);
                    Log.e("image_url", String.valueOf(item));*/

                }
            });

            /*holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Log.e("Attachment", " "+schoolModel.getSchoolid());
                    //Change parameter
                   // showCustomDialogForDelete(position,schoolModel.getSchoolid(),item.getAttachmentid());
                }
            });*/


    }

    @Override
    public int getItemCount() {
        return   attachmentModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_homeworkAttachment;
        ImageView img_title,iv_delete;

        public MyViewHolder(View itemView) {
            super(itemView);

            img_title =  itemView.findViewById(R.id.image_view);
            tv_homeworkAttachment =  itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());
        }
    }

    //region Search Filter (setFilter Code)
    public void setFilter(ArrayList<CategoryModel> newList) {
          attachmentModels = new ArrayList<>();
          attachmentModels.addAll(newList);
        notifyDataSetChanged();
    }

/*
    private void showCustomDialogForDelete(int i, int Userid, int Associateid) {
        // this.correct = correct;
        final Dialog resultbox = new Dialog(activity);
        resultbox.setContentView(R.layout.delete_subject_dialog);
        // resultbox.setCanceledOnTouchOutside(false);
        Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
        Button btn_cancel = (Button) resultbox.findViewById(R.id.btn_resume);
        TextView text_title =  resultbox.findViewById(R.id.text_title);
        text_title.setText(" Are you sure you want to delete this Attachment ? ");
        btn_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Utility.isNetworkAvailable(activity)) {

                    */
/**********Delete Code****************//*


                    try {
                        JSONObject jsonObject = new JSONObject();
                        try {

                            //  {"type":1,"Action":4, "Userid":22,"Associateid":2,"Associatetypeid":0}- Class
                            jsonObject.put("type", 1);
                            jsonObject.put("Action", 9);
                            jsonObject.put("Userid", Userid);
                            jsonObject.put("Associateid", Associateid);
                            jsonObject.put("Associatetypeid", 0);

                        } catch (Exception e) {
                            Log.e("JSONOBJECTerr", "" + e);
                            Utility.showErrorMessage(activity, "Something went wrong", Snackbar.LENGTH_SHORT);
                        }


                        AndroidNetworking.post(Constants.URL_DELETE)
                                .addJSONObjectBody(jsonObject)
                                .setTag("test")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        try {
                                            TypeToken<ArrayList<DeleteModel> > token = new TypeToken<ArrayList<DeleteModel>>()
                                            {
                                            };

                                            ArrayList<DeleteModel> userModels = new Gson().fromJson
                                                    (response.toString(), token.getType());

                                            // DeleteModel userModels = new Gson().fromJson(response.toString(),
                                            //      token.getType());
                                            Log.e("UserModel", "" + userModels);

                                            attachmentModels.remove(i);
                                            notifyDataSetChanged();
                                            notifyItemRemoved(i);
                                            notifyItemRangeChanged(i, attachmentModels.size());
                                            Utility.showErrorMessage(activity, "Deleted Successfully", Snackbar.LENGTH_SHORT);
                                            //  viewPager.setCurrentItem(viewPager.getCurrentItem());
                                            Log.e("1111", "2222");

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.e("error", e.getMessage());
                                        }

                                        resultbox.cancel();
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        Log.e("NotesPkgs:anError", "" + anError);
                                        Log.e("NotesPkgs:anError", "" + anError.getErrorBody());
                                        Utility.showErrorMessage(activity, "Something went wrong", Snackbar.LENGTH_SHORT);

                                    }
                                });

                    } catch (Exception ex) {
                        Log.e("onUnknownError", "" + ex);
                        Utility.showErrorMessage(activity, "Something went wrong", Snackbar.LENGTH_SHORT);
                    }

                } else {
                    Utility.showErrorMessage(activity, "No Internet Connection", Snackbar.LENGTH_SHORT);
                }
            }

            ;

            */
/**************************************//*

        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                resultbox.cancel();
            }
        });

        resultbox.show();
    }
*/

}
