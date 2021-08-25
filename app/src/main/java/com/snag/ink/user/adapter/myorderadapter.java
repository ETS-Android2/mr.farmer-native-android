package com.snag.ink.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.snag.ink.user.R;
import com.snag.ink.user.model.myorders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class myorderadapter extends FirestorePagingAdapter<myorders, myorderadapter.Itemholder> {

    public myorderadapter(@NonNull FirestorePagingOptions<myorders> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Itemholder holder, int position, @NonNull myorders model) {
        try {
            JSONArray jsonArray = new JSONArray(model.getOrderdetails());
            for(int i=0;i<=jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String itemname = jsonObject.getString("itemname");
                String itemquantity = jsonObject.getString("itemquantity");
                String itemnos = jsonObject.getString("itemcount");
                String itemprice = jsonObject.getString("itemprice");
                holder.orderdetails.append(itemname + " ," + itemquantity + "gms  ," + itemnos + "nos  ,  Rs" + itemprice + "\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.merchantname.setText(model.getMerchantname());
        holder.orderstatus.setText(model.getOrderstatus());
        holder.ordertime.setText(String.valueOf(model.getOrdertime()));
        holder.ordervalue.setText("Rs" +"  "+String.valueOf(model.getOrdervalue()));
    }

    @NonNull
    @Override
    public myorderadapter.Itemholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorders, parent, false);
        return new Itemholder(v);
    }

    class Itemholder extends RecyclerView.ViewHolder {
        TextView merchantname, orderstatus, orderdetails, ordertime, ordervalue;

        public Itemholder(@NonNull View itemView) {
            super(itemView);
            merchantname = itemView.findViewById(R.id.Merchant_name);
            orderstatus = itemView.findViewById(R.id.Order_status);
            orderdetails = itemView.findViewById(R.id.Orderslist);
            ordertime = itemView.findViewById(R.id.Order_time);
            ordervalue = itemView.findViewById(R.id.Order_price);
        }
    }
}
