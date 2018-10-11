package com.yuang.library.rx;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * 项目名称: RxManager
 * 类描述: 用于管理RxBus的事件和RxJava相关代码的生命周期处理
 * 创建人: Yuang QQ:274122635
 * 创建时间: 2018/9/10 上午10:52
 */

public class RxManager {

    private RxBus mRxBus = RxBus.$();
    private Map<String, Observable<?>> mObservables = new HashMap<>();// 管理观察源
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();// 管理订阅者者


    public void on(String eventName, Action1<Object> action1) {
        Observable<?> mObservable = mRxBus.register(eventName);
        mObservables.put(eventName, mObservable);
        mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, Throwable::printStackTrace));
    }

    public void add(Subscription m) {
        mCompositeSubscription.add(m);
    }

    public void clear() {
        mCompositeSubscription.unsubscribe();// 取消订阅
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet())
            mRxBus.unregister(entry.getKey(), entry.getValue());// 移除观察
    }

    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }
}