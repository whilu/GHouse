package co.lujun.ghouse.ui.event;

import android.text.TextUtils;
import android.util.Log;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.Config;
import co.lujun.ghouse.util.PreferencesUtils;
import co.lujun.ghouse.util.SystemUtil;
import rx.Subscriber;

/**
 * Created by lujun on 2015/9/22.
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {
        Log.d(getClass().getSimpleName(), "onCompleted()");
    }

    @Override
    public void onError(Throwable e) {
        Log.d(getClass().getSimpleName(), e.toString());
    }

    @Override
    public void onNext(T t) {
        if (t == null || !(t instanceof BaseJson)){
            SystemUtil.showToast(R.string.msg_request_error);
            return;
        }
        if (((BaseJson)t).getValidate() != null && !TextUtils.isEmpty(((BaseJson)t).getValidate())){
            PreferencesUtils.putString(GlApplication.getContext(),
                    Config.KEY_OF_VALIDATE, ((BaseJson)t).getValidate());
        }
        if (((BaseJson)t).getStatus() != Config.STATUS_CODE_OK){
            SystemUtil.showToast(((BaseJson) t).getMessage());
            return;
        }
    }
}
