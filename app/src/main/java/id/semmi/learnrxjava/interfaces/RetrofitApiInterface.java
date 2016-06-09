package id.semmi.learnrxjava.interfaces;

import java.util.List;

import id.semmi.learnrxjava.model.Career;
import retrofit2.Call;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by semmi on 20/02/2016.
 */
public interface RetrofitApiInterface {


    @GET("retrieveCareer.php")
    Observable<List<Career>> getCareer();


    @GET("retrieveCareer.php")
    Call<List<Career>> getRetroCareer();
}
