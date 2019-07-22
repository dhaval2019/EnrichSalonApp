package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.SelectFriendModel;
import com.enrich.salonapp.util.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class SelectFriendAdapter extends RecyclerView.Adapter<SelectFriendAdapter.MyViewHolder> {

    private Context mContext;
    private List<SelectFriendModel> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userMobileNo;
        public ImageView userImage;
        public CheckBox isSelect;

        public MyViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.tvname);
            userMobileNo = (TextView) view.findViewById(R.id.tvmobno);
            userImage = (ImageView) view.findViewById(R.id.ivuser);
            isSelect = (CheckBox) view.findViewById(R.id.cbselect);
        }
    }


    public SelectFriendAdapter(Context mContext, List<SelectFriendModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_friendlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
     final   SelectFriendModel album = albumList.get(position);
        holder.userName.setText(album.getName());
        holder.userMobileNo.setText(album.getMobNo());
       // holder.userImage.setImageURI(album.getPhoto());
        Picasso.with(mContext).load(album.getPhoto()).resize(100, 100)
                .transform(new CircleTransform())
                .placeholder(R.drawable.profile_icon).into(holder.userImage);

        holder.isSelect.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.isSelect.setChecked(album.getIsSelect());

        holder.isSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                album.setIsSelect(isChecked);
            }
        });

    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
