package tsocket.zby.com.tsocket.utils;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * RxBus
 * Created by YoKeyword on 2015/6/17.
 */
public class RxBus {
  private static volatile RxBus defaultInstance;
  // 主题
  private final Subject bus;
  // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
  private RxBus() {
    bus = new SerializedSubject<>(PublishSubject.create());
  }
  // 单例RxBus
  public static RxBus getDefault() {
    RxBus rxBus = defaultInstance;
    if (defaultInstance == null) {
      synchronized (RxBus.class) {
        rxBus = defaultInstance;
        if (defaultInstance == null) {
          rxBus = new RxBus();
          defaultInstance = rxBus;
        }
      }
    }
    return rxBus;
  }
  // 提供了一个新的事件
  public void post (Object o) {
    bus.onNext(o);
  }

  /**
   * 延迟时间发送事件
   * @param o
   * @param delayMillis
   */
  public void post (final Object o,final long delayMillis) {
    new Thread(new Runnable() {
      @Override public void run() {
        try {
          Thread.sleep(delayMillis);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        post(o);
      }
    }).start();
  }
  // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
  public <T> Observable<T> toObserverable (Class<T> eventType) {
    return bus.ofType(eventType);
  }



  /**
   * 订阅事件，接收推送消息
  */
  public static Subscription subscribePushMessage(final SubscrieListener listener) {
    Subscription mRxSubscription = getDefault().toObserverable(Object.class)
        .observeOn(AndroidSchedulers.mainThread())
        .unsubscribeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Action1<Object>() {
                     @Override
                     public void call(Object userEvent) {
                       if(userEvent!=null) {
                         //LogUtils.d("base", "receiver : " + userEvent.getClass());
                         if (listener!=null) {
                           listener.subscribeCall(userEvent);
                         }
                       }
                     }
                   },
            new Action1<Throwable>() {
              @Override
              public void call(Throwable throwable) {
                // TODO: 处理异常
                LogUtils.v("base", " onReceiverError : " + throwable.getMessage());
              }
            });
    return mRxSubscription;
  }

  /**
   * 取消订阅
   */
  public static void unSubscribePushMessage(Subscription subscription) {
    if(subscription!=null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }

  public interface SubscrieListener {
    void subscribeCall(Object object);
  }
}
