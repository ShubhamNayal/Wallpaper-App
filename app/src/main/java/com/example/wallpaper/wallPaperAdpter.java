package com.example.wallpaper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class wallPaperAdpter extends RecyclerView.Adapter<wallpaperViewHolder>{

    private Context context;
    private List<mallpaperModel>wallpaperModelList;

    public wallPaperAdpter(Context context, List<mallpaperModel> wallpaperModelList) {
        this.context = context;
        this.wallpaperModelList = wallpaperModelList;
    }

    @NonNull
    @Override
    public wallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.wallpaper_item,parent,false);
        return new wallpaperViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull wallpaperViewHolder holder, final int position) {

        Glide.with(context).load(wallpaperModelList.get(position).getMediumURL()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,fullScreenWallPaper.class)
                        .putExtra("originalURL",wallpaperModelList.get(position).getOriginalURL()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperModelList.size();
    }
}
 class wallpaperViewHolder extends RecyclerView.ViewHolder{
ImageView imageView;
    public wallpaperViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.imageViewItem);
    }
}
