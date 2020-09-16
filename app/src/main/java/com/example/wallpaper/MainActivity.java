package com.example.wallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
private RecyclerView recyclerView;
wallPaperAdpter wallPaperAdpter;
List<mallpaperModel>wallpaperModels;
int pageNum=1;
boolean isScrolling=false;
int currentItem,totalItems,scrollOutItems;
    String url="https://api.pexels.com/v1/curated/?page="+pageNum+"&per_page=80";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView);
        wallpaperModels=new ArrayList<>();

        wallPaperAdpter =new wallPaperAdpter(this,wallpaperModels);

        recyclerView.setAdapter(wallPaperAdpter);

        final GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);





        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
            isScrolling=true;
        }

    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        currentItem=gridLayoutManager.getChildCount();
        totalItems=gridLayoutManager.getItemCount();
        scrollOutItems=gridLayoutManager.findFirstVisibleItemPosition();
        if(isScrolling&&(currentItem+scrollOutItems==totalItems)){
            isScrolling=false;
            fetchWallPaper();
        }
    }
});
        fetchWallPaper();


    }
    public void fetchWallPaper()
    {

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("photos");

                    int len=jsonArray.length();
                    for (int i=0;i<len;i++){
                    JSONObject obj=jsonArray.getJSONObject(i);

                    int id=obj.getInt("id");

                    JSONObject objectImj=obj.getJSONObject("src");
                    String originalUrl=objectImj.getString("original");
                        String mediumUrl=objectImj.getString("medium");

                        mallpaperModel mallpaperModel=new mallpaperModel(id,originalUrl,mediumUrl);
                        wallpaperModels.add(mallpaperModel);
                    }

                }catch (JSONException e){

                }

                wallPaperAdpter.notifyDataSetChanged();

                pageNum++;



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
            //
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("Authorization","563492ad6f91700001000001f13affaa4ca7441380756a187881f479");

                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId()==R.id.nav_search){
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            final EditText editText=new EditText(this);
            editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            alert.setMessage("Enter Category eg. Nature");
            alert.setTitle("Search WallPaper");

            alert.setView(editText);



            alert.setPositiveButton("GO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    String query=editText.getText().toString().toLowerCase();
                    url=  "https://api.pexels.com/v1/search/?page="+pageNum+"&per_page=80&query="+query;

                    wallpaperModels.clear();
                    fetchWallPaper();
                }
            });


            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }
}