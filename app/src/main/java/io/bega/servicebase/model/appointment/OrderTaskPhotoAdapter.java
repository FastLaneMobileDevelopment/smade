/*package com.begasoftware.worktask.datamodel;


import android.widget.ArrayAdapter;

import java.util.List;

import com.begasoftware.worktask.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderTaskAdapter extends BaseSwipeListViewListener {
	List   data;
    Context context;
    int layoutResID;

    public OrderTaskAdapter(Context context, int layoutResourceId,List data) {
	    super(context, layoutResourceId, data);
	
	    this.data=data;
	    this.context=context;
	    this.layoutResID=layoutResourceId;

    // TODO Auto-generated constructor stub
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    NewsHolder holder = null;
	    View row = convertView;
	     holder = null;
	
	   if(row == null)
	   {
	       LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	       row = inflater.inflate(layoutResID, parent, false);
	
	       holder = new NewsHolder();	
	       holder.itemName = (TextView)row.findViewById(R.id.custom_row_textview);
	       holder.icon= (ImageView)row.findViewById(R.id.custom_row_imageview);
	       holder.button1=(Button)row.findViewById(R.id.custom_button_swipe1);
	       holder.button2=(Button)row.findViewById(R.id.custom_button_swipe2);
	       holder.button3=(Button)row.findViewById(R.id.custom_button_swipe3);
	       row.setTag(holder);

	   }
	   else
	   {
	       holder = (NewsHolder)row.getTag();
	   }
	
	   OrderTask itemdata = (OrderTask)data.get(position);
	   holder.itemName.setText(itemdata.getOrderID() + " " + itemdata.getOrderLocator());
	   // holder.icon.setImageDrawable(itemdata.getIcon());
	
	   holder.button1.setOnClickListener(new View.OnClickListener() {
	
	             @Override
	             public void onClick(View v) {
	                   // TODO Auto-generated method stub
	                   Toast.makeText(context, "Button 1 Clicked",Toast.LENGTH_SHORT).show();
	             }
	       });
	
	        holder.button2.setOnClickListener(new View.OnClickListener() {
	
	                         @Override
	                         public void onClick(View v) {
	                               // TODO Auto-generated method stub
	                               Toast.makeText(context, "Button 2 Clicked",Toast.LENGTH_SHORT).show();
	                         }
	                   });
	
	        holder.button3.setOnClickListener(new View.OnClickListener() {
	
	                   @Override
	                   public void onClick(View v) {
	                         // TODO Auto-generated method stub
	                         Toast.makeText(context, "Button 3 Clicked",Toast.LENGTH_SHORT);
	                   }
	             });
	
	   return row;
	}
	
	static class NewsHolder{
		 
	      TextView itemName;
	      ImageView icon;
	      Button button1;
	      Button button2;
	      Button button3;
	      }

}*/

package io.bega.servicebase.model.appointment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import io.bega.servicebase.R;
import io.bega.servicebase.model.photo.PhotoType;

public class OrderTaskPhotoAdapter extends BaseAdapter {

    private OrderTask data;
    private Context context;
    private int viewId;
    private PhotoType type;
    final private IOrderTaskPhotoCreator taskPhotoCreator;


    public OrderTaskPhotoAdapter(Context context, OrderTask data, int viewId, PhotoType type, IOrderTaskPhotoCreator taskPhotoCreator) {
        this.context = context;
        this.data = data;
        this.type = type;
        this.viewId = viewId;
        this.taskPhotoCreator = taskPhotoCreator;
    }

    public void setPhotoFilter(PhotoType type)
    {
        this.type = type;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.getTaskPhotos(type).size();
    }

    @Override
    public TaskPhoto getItem(int position) {
        return null;
        //return data.getTaskPhotos(type).get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TaskPhoto item = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(viewId, parent, false);
            holder = new ViewHolder();
            holder.ivImage = (ImageView) convertView.findViewById(R.id.custom_row_detail_imageview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       // holder.ivImage.setImageBitmap(item.getDrawable());
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                MaterialDialog materialDialog = new
                        MaterialDialog.Builder(context)
                        .title(R.string.dialog_enter_appointment)
                        .content(R.string.dialog_enter_appointment_confirm)
                        .positiveText(R.string.ok_dialog)
                        .negativeText(R.string.cancel_dialog)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                taskPhotoCreator.changeTaskPhoto(item);
                            }
                        })
                        .show();





            }
        });
        holder.ivImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MaterialDialog materialDialog = new
                        MaterialDialog.Builder(context)
                        .title(R.string.dialog_enter_appointment)
                        .content(R.string.dialog_enter_appointment_confirm)
                        .positiveText(R.string.ok_dialog)
                        .negativeText(R.string.cancel_dialog)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();

                return false;
            }
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView ivImage;
    }
}






