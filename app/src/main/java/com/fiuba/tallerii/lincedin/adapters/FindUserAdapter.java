package com.fiuba.tallerii.lincedin.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.fiuba.tallerii.lincedin.model.user.UserFriends;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.utils.ImageUtils;
import com.google.gson.Gson;
import com.google.gson.internal.ObjectConstructor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FindUserAdapter extends ArrayAdapter<String> {

    private static final String TAG = "FindUserAdapter";
    private UserFriends userFriendsObject;
    private ArrayList<String> userFriends;
    Context mContext;




    //View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtJob;
        ImageView friendPicture;
        String friendID;
        String imgURL;
    }

    public FindUserAdapter(UserFriends friends, Context ctx) {
        super(ctx, R.layout.friend_row_item);
        this.userFriendsObject = friends;
        this.userFriends = friends.getUserFriends();
        this.mContext = ctx;
    }

    @Override
    public int getCount() {
        return userFriendsObject.getUserFriends().size();
    }

    @Override
    public String getItem(int i)
    {
        return userFriendsObject.getUserFriends().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View result;
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.friend_row_item,viewGroup,false);
            viewHolder.txtName = (TextView) view.findViewById(R.id.friend_name);
            viewHolder.txtJob = (TextView) view.findViewById(R.id.friend_job);
            viewHolder.friendPicture = (ImageView) view.findViewById(R.id.friend_image);
            result = view;
            LincedInRequester.getUserProfile(userFriends.get(i).toString(),mContext, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Gson parser = new Gson();
                            User userData = parser.fromJson(response.toString(), User.class);
                            viewHolder.txtName.setText(userData.fullName);
                            viewHolder.txtJob.setText(" ");
                            if(userData.getCurrentWork() != null){
                                viewHolder.txtJob.setText(userData.getCurrentWork().company);
                            }

                            viewHolder.friendID = userData.id;

                            viewHolder.imgURL = userData.profilePicture;
                            LincedInRequester.getUserProfileImage(mContext, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.w("FINDADAPTER","Successfuly retrieved stranger info");
                                            try {
                                                String b64str = response.getString("content");
                                                Log.w("STRANGERPICTURE",b64str);
                                                ImageUtils.setBase64ImageFromString(mContext, b64str, viewHolder.friendPicture);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            error.printStackTrace();
                                        }

                                    },viewHolder.imgURL);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG,error.toString());
                            error.printStackTrace();
                            Toast.makeText(mContext,"Ha ocurrido un error en la transferencia de datos.",Toast.LENGTH_LONG).show();
                        }
                    });


            view.setTag(viewHolder);



        }else {
            viewHolder = (FindUserAdapter.ViewHolder) view.getTag();
            result = view;

        }


        //ImageUtils.setBase64ImageFromString(mContext,mContext.getResources().getString(R.string.literal_riquelme),viewHolder.friendPicture);
        return view;
    }

    private void setUserData(String tempName, String tempJob, String tempB64img, User userData) {
        tempName = userData.fullName;
        tempJob = userData.getCurrentWork().company;
        tempB64img = userData.profilePicture;
    }


}
