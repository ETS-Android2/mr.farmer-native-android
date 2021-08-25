package com.snag.ink.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.snag.ink.user.R;
import com.snag.ink.user.model.Item;
import com.squareup.picasso.Picasso;

public class merchantadapter extends FirestorePagingAdapter<Item, merchantadapter.Itemholder> {

    private final onvaluechanged onvaluechanged;


    public merchantadapter(@NonNull FirestorePagingOptions<Item> options,onvaluechanged onvaluechanged) {
        super(options);
        this.onvaluechanged = onvaluechanged;
    }

    @Override
    protected void onBindViewHolder(@NonNull merchantadapter.Itemholder holder, int position, @NonNull Item model) {

        holder.itemname.setText(model.getItemname());
        holder.itemquantity.setText(String.valueOf(model.getItemquantity()+"gms"));
        holder.itemprice.setText(String.valueOf(model.getItemprice()+"/-"));
        Picasso.get()
                .load(model.getItemurl())
                .fit()
                .into(holder.itemimage);
    }

    //Clears content messing up
    @Override
    public int getItemViewType(int position) {
        return position;
    }



    @NonNull
    @Override
    public Itemholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_item, parent, false);
        return new Itemholder(v);
    }

    class Itemholder extends RecyclerView.ViewHolder implements ElegantNumberButton.OnValueChangeListener {
        ImageView itemimage;
        TextView itemname;
        TextView itemquantity;
        TextView itemprice;
        ElegantNumberButton counter;

        public Itemholder(@NonNull View itemView) {
            super(itemView);

            itemimage = itemView.findViewById(R.id.itemimage);
            itemname = itemView.findViewById(R.id.itemname);
            itemquantity = itemView.findViewById(R.id.itemquantity);
            itemprice = itemView.findViewById(R.id.itemprice);
            counter = itemView.findViewById(R.id.item_counter);
            counter.setOnValueChangeListener(this);
        }


        @Override
        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
            onvaluechanged.onvaluechanged(getItem(getAdapterPosition()), getAdapterPosition(),newValue);
        }
    }

    public interface onvaluechanged{
        void onvaluechanged(DocumentSnapshot snapshot, int position, int newvalue);
    }

}
