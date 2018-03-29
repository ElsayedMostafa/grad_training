package com.example.madara.training.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.madara.training.R;
import com.example.madara.training.models.Rfid;

import java.util.List;

/**
 * Created by madara on 3/28/18.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {
    private static final String TAG = CardAdapter.class.getSimpleName();
    List<Rfid> cardslist ;
    Context context;
    public CardAdapter(List<Rfid> cardslist, Context ctx){
        this.cardslist = cardslist;
        this.context = ctx;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_card, parent, false);
        CardHolder holder = new CardHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        Rfid card = cardslist.get(position);
        holder._CardID.setText(card.getId());
    }
    @Override
    public int getItemCount() {
        return cardslist.size();
    }
    class CardHolder extends RecyclerView.ViewHolder {
        TextView _CardID;
        public CardHolder(View itemView) {
            super(itemView);
            _CardID = (TextView) itemView.findViewById(R.id.tv_card_num);
        }
    }
}
