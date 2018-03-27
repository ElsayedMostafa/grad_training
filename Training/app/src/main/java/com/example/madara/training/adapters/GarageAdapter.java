package com.example.madara.training.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.madara.training.R;
import com.example.madara.training.models.Garage;

import java.util.List;

/**
 * Created by madara on 3/12/18.
 */

public class GarageAdapter extends RecyclerView.Adapter<GarageAdapter.GarageHolder> {
    List<Garage> garageList;

    public GarageAdapter(List<Garage> garageList) {
        this.garageList = garageList;
    }

    @NonNull
    @Override
    public GarageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.garage_row, parent, false);
        GarageHolder holder = new GarageHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GarageHolder holder, int position) {
        Garage garage = garageList.get(position);
        holder._garageName.setText(garage.getGarageName());
        holder._garageDistance.setText(garage.getGarageDistance());
    }

    @Override
    public int getItemCount() {

        return garageList.size();
    }

    class GarageHolder extends RecyclerView.ViewHolder {
        TextView _garageName, _garageDistance;

        public GarageHolder(View itemView) {
            super(itemView);
            _garageName = (TextView) itemView.findViewById(R.id.garage_name);
            _garageDistance = (TextView) itemView.findViewById(R.id.garage_distance);

        }
    }
}
