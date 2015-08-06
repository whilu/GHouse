package co.lujun.ghouse.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.github.whilu.library.CustomRippleButton;

import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.widget.SlidingActivity;

/**
 * Created by lujun on 2015/7/30.
 */
public class LoginActivity extends SlidingActivity {

    private Toolbar mToolbar;
    private TextInputLayout tilUName, tilPwd;
    private CustomRippleButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){
        mToolbar = (Toolbar) findViewById(R.id.tb_login);
        setTitle(getString(R.string.action_center));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.action_login));

        tilUName = (TextInputLayout) findViewById(R.id.til_uname);
        tilUName.setHint(getString(R.string.tli_hint_uname));
        tilPwd = (TextInputLayout) findViewById(R.id.til_pwd);
        tilPwd.setHint(getString(R.string.tli_hint_pwd));

        btnLogin = (CustomRippleButton) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
