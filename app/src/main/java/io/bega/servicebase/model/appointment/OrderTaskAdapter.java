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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;



import java.util.List;

import io.bega.servicebase.R;

public class OrderTaskAdapter extends BaseAdapter {

    private OrdersTasks data;
    private Context context;
    private int viewId;

    public OrderTaskAdapter(Context context, OrdersTasks data, int viewId) {
        this.context = context;
        this.data = data;
        this.viewId = viewId;
    }

    @Override
    public int getCount() {
        return data.getOrderTask().size();
    }

    @Override
    public OrderTask getItem(int position) {
        return data.getOrderTask().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public boolean isEnabled(int position) {
//        if (position == 2) {
//            return false;
//        } else {
//            return true;
//        }
//    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final OrderTask item = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(viewId, parent, false);
            holder = new ViewHolder();
            // holder.ivImage = (ImageView) convertView.findViewById(R.id.custom_row_imageview);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.custom_row_textview);
            //holder.tvDescription = (TextView) convertView.findViewById(R.id.custom_row_text_description);
            holder.bAction1 = (ImageButton) convertView.findViewById(R.id.custom_row_button_delete);
            holder.bAction2 = (ImageButton) convertView.findViewById(R.id.custom_row_button_photo);
           // holder.bAction3 = (ImageButton) convertView.findViewById(R.id.custom_button_swipe3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

      //  ((com.begasoftware.swipelistview.SwipeListView)parent).recycle(convertView, position);

      //  holder.ivImage.setImageDrawable(item.getIcon());
        holder.tvTitle.setText(item.getOrderID());
        // holder.tvDescription.setText(item.getDescription());

       // return convertView;

        holder.bAction2.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   IOrderTaskCreator creator = (IOrderTaskCreator)context;
                                                   creator.selectTask(item);
                                               }
                                           });

        holder.bAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                IOrderTaskCreator creator = (IOrderTaskCreator)context;
                                creator.deleteTask(item);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Esta seguro que desea borrar la tarea?").setPositiveButton("Si", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();



            }
        });
        
        return convertView;
    }

    static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvDescription;
        ImageButton bAction1;
        ImageButton bAction2;
        ImageButton bAction3;
    }

    private boolean isPlayStoreInstalled() {
        Intent market = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=dummy"));
        PackageManager manager = context.getPackageManager();
        List<ResolveInfo> list = manager.queryIntentActivities(market, 0);

        return list.size() > 0;
    }





}






