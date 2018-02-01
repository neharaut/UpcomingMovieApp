package um.com.neha.upcomingmovieapp.rest;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import um.com.neha.upcomingmovieapp.Model.Configuration;
import um.com.neha.upcomingmovieapp.Model.Movie;
import um.com.neha.upcomingmovieapp.Model.Result;
import um.com.neha.upcomingmovieapp.Model.SliderImage;

/**
 * Created by Mangesh on 2/1/2018.
 */

public class RestAPI {

    private static final String api_key="b7cd3340a794e5a2f35e3abb820b497f";
    private static final String url="https://api.themoviedb.org/3/";

    public static MovieService movieService = null;

    public static MovieService getMovieService(){
        if(movieService==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            movieService = retrofit.create(MovieService.class);
        }
        return movieService;
    }

    public interface MovieService{

        @GET("movie/upcoming?api_key="+api_key)
        Call<Movie> getMoviesList();

        @GET("configuration?api_key="+api_key)
        Call<Configuration> getConfigurationImage();

        @GET("movie/{id}/images?api_key="+api_key)
        Call<SliderImage> getSlideImage(@Path("id") int id);

    }

}
