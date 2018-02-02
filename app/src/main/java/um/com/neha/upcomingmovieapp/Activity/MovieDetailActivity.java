package um.com.neha.upcomingmovieapp.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import um.com.neha.upcomingmovieapp.Adapter.MyPagerAdapter;
import um.com.neha.upcomingmovieapp.Model.Backdrop;
import um.com.neha.upcomingmovieapp.Model.Movie;
import um.com.neha.upcomingmovieapp.Model.SliderImage;
import um.com.neha.upcomingmovieapp.R;
import um.com.neha.upcomingmovieapp.rest.RestAPI;

public class MovieDetailActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    String title,overview,base_url;
    int id;
    double popularity;
    TextView screen_title,detail_movie_title,movie_overview;
    List<Backdrop> backdropList;
    CircleIndicator indicator;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getBundle(); //get the information from previous activity from intent
        initView(); // initializing view
        getData(); // get the response data
    }

    // retrieve data coming from intent
    public void getBundle(){
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        title = intent.getStringExtra("movie_title");
        overview = intent.getStringExtra("movie_overview");
        popularity = intent.getDoubleExtra("popularity",0.0);
        base_url = intent.getStringExtra("base_url");
    }

    //initializing views
    public void initView(){
        screen_title = findViewById(R.id.screen_title);
        detail_movie_title = findViewById(R.id.detail_movie_title);
        movie_overview = findViewById(R.id.movie_overview);

        //Overview textView justification only for oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            movie_overview.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }

        mPager = (ViewPager) findViewById(R.id.pager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        ratingBar = findViewById(R.id.ratingBar);

        ratingBar.setRating((float)(popularity*5)/500); //converting popularity into 5 star rating

        ImageView back_button = findViewById(R.id.back_button);

        //back navigation button
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieDetailActivity.this.finish();
            }
        });
    }

    public void getData(){

        screen_title.setText(title);
        detail_movie_title.setText(title);
        movie_overview.setText(overview);

        Call<SliderImage> slideImage= RestAPI.getMovieService().getSlideImage(id); // API call for getting slider images
        slideImage.enqueue(new Callback<SliderImage>() {
            @Override
            public void onResponse(Call<SliderImage> call, Response<SliderImage> response) {
                backdropList = response.body().getBackdrops();
                final List<String> sliderImage=new ArrayList<>();

                // checking condition to show atleast 5 images on slider. If available images are less than 5 thn show them as it is
                if (backdropList.size()>5) {
                    for (int i = 0; i < 5; i++) {
                        sliderImage.add(base_url + backdropList.get(i).getFilePath());
                    }
                }else
                {
                    for (int i = 0; i < backdropList.size(); i++) {
                        sliderImage.add(base_url + backdropList.get(i).getFilePath());
                    }
                }
                mPager.setAdapter(new MyPagerAdapter(MovieDetailActivity.this,sliderImage));
                indicator.setViewPager(mPager);

/*
                // Auto start of viewpager
                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == sliderImage.size()) {
                            currentPage = 0;
                        }
                        mPager.setCurrentItem(currentPage++, true);
                    }
                };
                Timer swipeTimer = new Timer();
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 2500, 2500);
*/
            }

            @Override
            public void onFailure(Call<SliderImage> call, Throwable t) {

            }
        });
    }

}
