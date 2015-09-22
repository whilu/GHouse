package co.lujun.ghouse.ui;

import android.os.Bundle;
import android.os.PersistableBundle;

import co.lujun.ghouse.ui.widget.SlidingActivity;

/**
 * Created by lujun on 2015/9/22.
 */
public class BaseActivity extends SlidingActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
