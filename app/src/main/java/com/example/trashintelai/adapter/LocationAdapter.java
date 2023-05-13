package com.example.trashintelai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trashintelai.R;
import com.example.trashintelai.model.Location;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{
      List<Location> list ;

    public LocationAdapter(List<Location> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new ViewHolder( LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.location.setText(list.get(position).getLocation());
        holder.dry.setOnClickListener(view -> {
            if (  holder.dry.isChecked()){
                list.get(holder.getAdapterPosition()).setDry(true);
            }else {
                list.get(holder.getAdapterPosition()).setDry(false);
            }
           checkeditem(holder.getAdapterPosition());

        });
        holder.wet.setOnClickListener(view -> {
            if (  holder.wet.isChecked()){
                list.get(holder.getAdapterPosition()).setWet(true);
            }else {
                list.get(holder.getAdapterPosition()).setWet(false);
            }
            checkeditem(holder.getAdapterPosition());
        });

    }
    private void checkeditem(int adapterPosition) {
        Location location = list.get(adapterPosition);
        if (location.isDry() && location.isWet()){
            FirebaseDatabase.getInstance().getReference().child("collected").child(location.getKey()).child("location").setValue(location.getLocation());
            FirebaseDatabase.getInstance().getReference().child("location").child(location.getKey()).removeValue();
            list.remove(adapterPosition);
            notifyDataSetChanged();
        }

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView   location;
        private CheckBox dry , wet;
        public ViewHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.location_location);
            dry = itemView.findViewById(R.id.location_dry);
            wet =itemView.findViewById(R.id.location_wet);


        }
    }
}
