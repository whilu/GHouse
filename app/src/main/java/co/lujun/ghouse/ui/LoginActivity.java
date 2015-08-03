package co.lujun.ghouse.ui;

import android.os.Bundle;

import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.widget.SlidingActivity;

/**
 * Created by lujun on 2015/7/30.
 */
public class LoginActivity extends SlidingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
