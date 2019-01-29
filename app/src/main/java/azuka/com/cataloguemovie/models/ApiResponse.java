package azuka.com.cataloguemovie.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ivana Situmorang on 1/24/2019.
 */
public class ApiResponse<T> {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    @Expose
    private T results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public T getResults() {
        return results;
    }
}
