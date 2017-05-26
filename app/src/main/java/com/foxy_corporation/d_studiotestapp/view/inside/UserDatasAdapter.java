package com.foxy_corporation.d_studiotestapp.view.inside;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foxy_corporation.d_studiotestapp.R;
import com.foxy_corporation.d_studiotestapp.model.data.UserData;
import com.foxy_corporation.d_studiotestapp.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vlad on 26.05.2017.
 */

class UserDatasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String NULL_LAST_LOGIN = "null";

    private static final String NO_LAST_LOGIN = "No last login";

    private ArrayList<UserData> mUserDatasList = new ArrayList<>();

    UserDatasAdapter(ArrayList<UserData> userDatasList) {
        mUserDatasList.addAll(userDatasList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_datas, parent, false);

        return new UserDatasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserDatasViewHolder myUserDatasViewHolder = (UserDatasViewHolder) holder;

        UserData currentUserData = mUserDatasList.get(position);

        myUserDatasViewHolder.mUsername.setText(currentUserData.getUsername());

        String lastLogin = currentUserData.getLast_login();

        if (lastLogin == null)
            lastLogin = NO_LAST_LOGIN;

        myUserDatasViewHolder.mLastLogin.setText(lastLogin);

    }


    @Override
    public int getItemCount() {
        return mUserDatasList.size();
    }

    class UserDatasViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtUsername_IUD) TextView mUsername;

        @BindView(R.id.txtLastLogin_IUD) TextView mLastLogin;

        private UserDatasViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
