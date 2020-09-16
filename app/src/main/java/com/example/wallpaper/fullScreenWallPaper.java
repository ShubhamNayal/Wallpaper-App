package com.example.wallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class fullScreenWallPaper extends AppCompatActivity {
    PhotoView photoView;
    String originalURL="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_full_screen_wall_paper);
        Intent intent=getIntent();
        originalURL=intent.getStringExtra("originalURL");

        photoView=findViewById(R.id.photoView);
        Glide.with(this).load(originalURL).into(photoView);

    }

    public void setWallPaper(View view)
    {
        WallpaperManager wallpaperManager=WallpaperManager.getInstance(this);
        Bitmap bitmap = ((BitmapDrawable)photoView.getDrawable()).getBitmap();
        try
        {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(getApplicationContext(),"WallPaper Set",Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadImage(View view)
    {
        DownloadManager downloadManager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(originalURL);

        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);
        Toast.makeText(getApplicationContext(),"Downloading...",Toast.LENGTH_LONG).show();
    }
}