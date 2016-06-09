package id.semmi.learnrxjava.interfaces;

/**
 * Created by semmi on 20/02/2016.
 */
public interface Observer<T> {

    void onCompleted();
    void onError(Throwable e);
    void onNext(T t);

}
