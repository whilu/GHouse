package co.lujun.ghouse.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.github.whilu.library.CustomButton;
import com.github.whilu.library.CustomRippleButton;
import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.CheckBox;

import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.Config;

/**
 * Created by lujun on 2015/7/30.
 */
public class SplashActivity extends ActionBarActivity implements View.OnClickListener{

    private static Dialog mDialog;
    private static View loginView;
    private TextInputLayout tilUName, tilPwd;
    private CheckBox cbIsOwner;
    private CustomRippleButton btnLogin, btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init(){
        btnLogin = (CustomRippleButton) findViewById(R.id.btn_splash_login);
        btnReg = (CustomRippleButton) findViewById(R.id.btn_splash_reg);
        loginView = LayoutInflater.from(this).inflate(R.layout.view_login_and_reg, null, false);
        if (loginView != null){
            cbIsOwner = (CheckBox) loginView.findViewById(R.id.cb_splash_is_owner);
            tilUName = (TextInputLayout) loginView.findViewById(R.id.til_splash_uname);
            tilUName.setHint(getString(R.string.tli_hint_uname));
            tilPwd = (TextInputLayout) loginView.findViewById(R.id.til_splash_pwd);
            tilPwd.setHint(getString(R.string.tli_hint_pwd));
        }

        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);

        //delay some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, Config.APP_SPLASH_TIME);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_splash_login){
            showDialog();
        }else if (view.getId() == R.id.btn_splash_reg){
            startActivity(new Intent(this, RegHouseActivity.class));
        }
    }

    private void showDialog(){
        mDialog = new SimpleDialog(this);
        mDialog.applyStyle(R.style.Login_Dialog)
                .title(R.string.dialog_welcome)
                .positiveAction(R.string.action_login)
                .negativeAction(R.string.action_back)
                .contentView(loginView)
                .cancelable(false)
                .positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                })
                .show();
    }
}
