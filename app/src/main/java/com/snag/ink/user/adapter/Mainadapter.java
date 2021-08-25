package com.snag.ink.user.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snag.ink.user.R;
import com.snag.ink.user.roomdb.MainData;
import com.snag.ink.user.roomdb.RoomDB;

import java.util.List;

public class Mainadapter extends RecyclerView.Adapter<Mainadapter.ViewHolder> {
    //Intialize variable
    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;

    public Mainadapter(Activity context, List<MainData> dataList) {
        this.dataList = dataList;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Intialize view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Intialize main data
        MainData data = dataList.get(position);
        //Intialize database
        database = RoomDB.getInstance(context);
        String total_cost = String.valueOf(database.mainDao().gettotalcost());
        String name_quantity = data.getItemname() + "     " + data.getItemquantity()+"gms";
        String count_price = "Rs "+" " + String.valueOf(data.getItemprice())+ "  x  " +String.valueOf(data.getItemcount());

        holder.itemname.setText(name_quantity);
        holder.itemprice.setText(count_price);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainData d = dataList.get(holder.getAdapterPosition());
                database.mainDao().delete(d);
                int position = holder.getAdapterPosition();
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Intialize variable
        TextView itemname,itemprice;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Assign variable
            itemname = itemView.findViewById(R.id.cartitemname);
            itemprice = itemView.findViewById(R.id.cartitemprice);
            imageView = itemView.findViewById(R.id.cartdlt);
        }
    }
}
