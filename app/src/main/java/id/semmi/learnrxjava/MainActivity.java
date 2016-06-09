package id.semmi.learnrxjava;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import id.semmi.learnrxjava.interfaces.RetrofitApiInterface;
import id.semmi.learnrxjava.model.Career;
import id.semmi.learnrxjava.utils.EndPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private Subscription subscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        // Install Retrofit with Rx Java
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPoint.endPoint)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        RetrofitApiInterface retrofitApiInterface = retrofit.create(RetrofitApiInterface.class);


        // Retrofit Only without RX
//        Retrofit retrofit2 = new Retrofit.Builder()
//                .baseUrl(EndPoint.endPoint)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        RetrofitApiInterface retrofitApiInterface = retrofit2.create(RetrofitApiInterface.class);


        // Only Retrofit without RX

//        Call<List<Career>> call = retrofitApiInterface.getRetroCareer();
//        call.enqueue(new Callback<List<Career>>() {
//            @Override
//            public void onResponse(Call<List<Career>> call, Response<List<Career>> response) {
//
//                if(response.isSuccess()){
//                    Log.d(EndPoint.TAG, "onResponse: sukses");
//
//                    Log.d(EndPoint.TAG, "onResponse: "+response.body().toString());
//
//                    List<Career> careers  = response.body();
//                    for(Career career : careers){
//                        Log.d(EndPoint.TAG, "onResponse: "+career.getKarirNama());
//                    }
//
//                }
//                Log.d(EndPoint.TAG, "onResponse: Error response code");
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Career>> call, Throwable t) {
//                Log.d(EndPoint.TAG, "onFailure: ");
//            }
//        });




          // RX with Retrofit
        Observable<List<Career>> call = retrofitApiInterface.getCareer();
        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Career>>() {
            @Override
            public void onCompleted() {
                Log.d(EndPoint.TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) {
                    HttpException response = (HttpException) e;
                    int code = response.code();
                    Log.d(EndPoint.TAG, "onError: " + code);
                }
                Log.d(EndPoint.TAG, "onError:gagal" + e.getMessage());
            }

            @Override
            public void onNext(List<Career> careers) {

                for (Career career : careers) {
                    Log.d(EndPoint.TAG, "onNext: " + career.getKarirNama());
                }
            }
        });


//         Simple RX Java
//        Observable.just("1","2","3").subscribe(new Observer<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(EndPoint.TAG, "onError: simple"+e.getMessage());
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.d(EndPoint.TAG, "onNext: "+s);
//            }
//        });




    }


 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        this.subscription.unsubscribe();
        super.onDestroy();
    }
}
