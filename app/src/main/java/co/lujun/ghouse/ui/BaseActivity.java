package co.lujun.ghouse.ui;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.umeng.analytics.MobclickAgent;

import co.lujun.ghouse.ui.widget.SlidingActivity;

/**
 * Created by lujun on 2015/9/22.
 */
public class BaseActivity extends SlidingActivity {

    @Override public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }
}
