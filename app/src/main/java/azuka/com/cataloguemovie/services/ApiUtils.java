package azuka.com.cataloguemovie.services;

import azuka.com.cataloguemovie.BuildConfig;

/**
 * Created by Ivana Situmorang on 1/24/2019.
 */
public class ApiUtils {
    public static ApiService getMovieApi() {
        return RetrofitClient.getClient(BuildConfig.TMDB_BASE_URL).create(ApiService.class);
    }
}
