package com.example.trashintelai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trashintelai.R;
import com.example.trashintelai.model.Points;

import java.util.List;



public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    List<Points> list;

    public ItemAdapter(List<Points> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Points points = list.get(position);
        holder.imageView.setImageResource(points.getImage());
        holder.name.setText(points.getName());
        holder.points.setText(points.getPoints());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView imageView;
        private AppCompatTextView name , points ;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.point_image_id);
            name = itemView.findViewById(R.id.point_name_id);
            points = itemView.findViewById(R.id.point_points_id);

        }
    }
}
