package com.example.trashintelai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trashintelai.R;
import com.example.trashintelai.model.Location;
import java.util.List;



public class CollectedGarbage extends RecyclerView.Adapter<CollectedGarbage.ViewHolder>{
    List<Location> list ;

    public CollectedGarbage(List<Location> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new ViewHolder( LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.location.setText(list.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView location;
        public ViewHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.location_location);
        }
    }
}
