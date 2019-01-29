package azuka.com.cataloguemovie.services;

import java.util.List;

import azuka.com.cataloguemovie.models.ApiResponse;
import azuka.com.cataloguemovie.models.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ivana Situmorang on 1/24/2019.
 */
public interface ApiService {
    @GET("search/movie")
    Call<ApiResponse<List<Movie>>> search(@Query("api_key") String apiKey, @Query("language") String language, @Query("query") String query);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetail(@Path("movie_id") String movieId, @Query("api_key") String apiKey, @Query("language") String language);

    @GET("movie/now_playing")
    Call<ApiResponse<List<Movie>>> getNowPlaying(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("movie/upcoming")
    Call<ApiResponse<List<Movie>>> getUpComing(@Query("api_key") String apiKey, @Query("language") String language);
}
