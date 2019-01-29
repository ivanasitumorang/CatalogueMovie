package azuka.com.cataloguemovie.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ivana Situmorang on 1/24/2019.
 */
public class Genre {
    @SerializedName("id")
    @Expose
    private String genreId;

    @SerializedName("name")
    @Expose
    private String genreName;

    public String getGenreName() {
        return genreName;
    }
}
