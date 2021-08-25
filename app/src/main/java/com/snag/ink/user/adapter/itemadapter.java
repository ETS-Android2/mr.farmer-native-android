package com.snag.ink.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.snag.ink.user.R;
import com.snag.ink.user.model.Item;
import com.snag.ink.user.model.merchants;
import com.squareup.picasso.Picasso;

public class itemadapter extends FirestorePagingAdapter<Item, itemadapter.Itemholder> {

    private final onlistitemclick onlistitemclick;

    public itemadapter(@NonNull FirestorePagingOptions<Item> options, onlistitemclick onlistitemclick) {
        super(options);
        this.onlistitemclick = onlistitemclick;
    }

    @Override
    protected void onBindViewHolder(@NonNull itemadapter.Itemholder holder, int position, @NonNull Item model) {

        holder.itemname.setText(model.getItemname());
        holder.itemquantity.setText(String.valueOf(model.getItemquantity()+"gms"));
        holder.itemprice.setText(String.valueOf(model.getItemprice()+"/-"));
        Picasso.get()
                .load(model.getItemurl())
                .fit()
                .into(holder.itemimage);

    }

    @NonNull
    @Override
    public Itemholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_item, parent, false);
        return new Itemholder(v);
    }

    class Itemholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView itemimage;
        TextView itemname;
        TextView itemquantity;
        TextView itemprice;

        public Itemholder(@NonNull View itemView) {
            super(itemView);

            itemimage = itemView.findViewById(R.id.itemimage);
            itemname = itemView.findViewById(R.id.itemname);
            itemquantity = itemView.findViewById(R.id.itemquantity);
            itemprice = itemView.findViewById(R.id.itemprice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onlistitemclick.onitemclick(getItem(getAbsoluteAdapterPosition()), getAbsoluteAdapterPosition());
        }
    }

    public interface onlistitemclick {
        void onitemclick(DocumentSnapshot snapshot, int position);
    }
}
