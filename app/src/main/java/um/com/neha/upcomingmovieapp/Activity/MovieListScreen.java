package um.com.neha.upcomingmovieapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import um.com.neha.upcomingmovieapp.Adapter.MovieAdapter;
import um.com.neha.upcomingmovieapp.Model.Configuration;
import um.com.neha.upcomingmovieapp.Model.Images;
import um.com.neha.upcomingmovieapp.Model.Movie;
import um.com.neha.upcomingmovieapp.Model.Result;
import um.com.neha.upcomingmovieapp.R;
import um.com.neha.upcomingmovieapp.rest.RestAPI;

public class MovieListScreen extends AppCompatActivity {

    RecyclerView movies_recyclerView;
    MovieAdapter movieAdapter;
    String base_url,back_drop_url;
    List<Result> resultList;
    ProgressBar simpleProgressBar;
    ImageView info_imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list_screen);
        initView();
        getData();
        listener();
    }

    public void initView(){

        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        movies_recyclerView = findViewById(R.id.movies_recyclerView);
        movies_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        info_imageview = findViewById(R.id.info_imageview);

    }

    public void listener(){
        info_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieListScreen.this, InformationScreen.class);
                startActivity(intent);
            }
        });
    }

    public void getData(){

        Call<Movie> movie_list= RestAPI.getMovieService().getMoviesList();
        Call<Configuration> configurationCall=RestAPI.getMovieService().getConfigurationImage();
        simpleProgressBar.setVisibility(View.VISIBLE);
        configurationCall.enqueue(new Callback<Configuration>() {
            @Override
            public void onResponse(Call<Configuration> call, Response<Configuration> response) {
                Configuration configuration=response.body();
                Images images = configuration.getImages();
                List<String> poster_size=images.getPosterSizes();
                base_url=images.getBaseUrl()+poster_size.get(1);
                back_drop_url=images.getBaseUrl()+images.getBackdropSizes().get(1);
            }

            @Override
            public void onFailure(Call<Configuration> call, Throwable t) {

            }
        });

        movie_list.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Log.e("response",""+response.body());
                resultList=response.body().getResults();
                simpleProgressBar.setVisibility(View.INVISIBLE);
                movieAdapter=new MovieAdapter(MovieListScreen.this,resultList,base_url);
                movies_recyclerView.setAdapter(movieAdapter);
                Toast.makeText(MovieListScreen.this,"Success",Toast.LENGTH_LONG).show();
                movieAdapter.SetOnItemClickListener(new MovieAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Result result = resultList.get(position);
                        Intent intent = new Intent(MovieListScreen.this,MovieDetailActivity.class);
                        intent.putExtra("id",result.getId());
                        intent.putExtra("movie_title",result.getTitle());
                        intent.putExtra("movie_overview",result.getOverview());
                        intent.putExtra("popularity",result.getPopularity());
                        intent.putExtra("base_url",back_drop_url);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                simpleProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MovieListScreen.this,"Fail",Toast.LENGTH_LONG).show();
            }
        });
    }

}
