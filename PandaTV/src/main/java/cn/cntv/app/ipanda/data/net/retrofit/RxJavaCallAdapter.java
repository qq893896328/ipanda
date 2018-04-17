package cn.cntv.app.ipanda.data.net.retrofit;

import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;

/**
 * Created by yuerq on 16/7/8.
 */
public class RxJavaCallAdapter {

    public <R> Observable<R> toBody(Call<R> call) {
        Observable<R> observable = adapt(call)
                .lift(OperatorMapResponseToBodyOrError.<R>instance());

        return observable;
    }

    public <R> Observable<Response<R>> adapt(Call<R> call) {
        Observable<Response<R>> observable = Observable.create(new CallOnSubscribe<>(call));

        return observable;
    }



    static final class CallOnSubscribe<T> implements Observable.OnSubscribe<Response<T>> {
        private final Call<T> originalCall;

        CallOnSubscribe(Call<T> originalCall) {
            this.originalCall = originalCall;
        }

        @Override public void call(final Subscriber<? super Response<T>> subscriber) {
            // Since Call is a one-shot type, clone it for each new subscriber.
            Call<T> call = originalCall.clone();

            // Wrap the call in a helper which handles both unsubscription and backpressure.
            RequestArbiter<T> requestArbiter = new RequestArbiter<>(call, subscriber);
            subscriber.add(requestArbiter);
            subscriber.setProducer(requestArbiter);
        }
    }

    static final class RequestArbiter<T> extends AtomicBoolean implements Subscription, Producer {
        private final Call<T> call;
        private final Subscriber<? super Response<T>> subscriber;

        RequestArbiter(Call<T> call, Subscriber<? super Response<T>> subscriber) {
            this.call = call;
            this.subscriber = subscriber;
        }

        @Override public void request(long n) {
            if (n < 0) throw new IllegalArgumentException("n < 0: " + n);
            if (n == 0) return; // Nothing to do when requesting 0.
            if (!compareAndSet(false, true)) return; // Request was already triggered.

            try {
                Response<T> response = call.execute();
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(response);
                }
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(t);
                }
                return;
            }

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }

        @Override public void unsubscribe() {
            call.cancel();
        }

        @Override public boolean isUnsubscribed() {
            return call.isCanceled();
        }
    }
}
