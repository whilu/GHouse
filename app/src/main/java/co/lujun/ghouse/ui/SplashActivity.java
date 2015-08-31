package co.lujun.ghouse.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.github.whilu.library.CustomRippleButton;
import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.CheckBox;

import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.Config;
import co.lujun.ghouse.util.IntentUtils;

/**
 * Created by lujun on 2015/7/30.
 */
public class SplashActivity extends ActionBarActivity implements View.OnClickListener{

    private static Dialog mDialog;
    private static View loginView;
    private TextInputLayout tilUName, tilPwd;
    private CheckBox cbIsOwner;
    private CustomRippleButton btnLogin, btnReg;
    private TextView tvMainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init(){
        btnLogin = (CustomRippleButton) findViewById(R.id.btn_splash_login);
        btnReg = (CustomRippleButton) findViewById(R.id.btn_splash_reg);
        tvMainText = (TextView) findViewById(R.id.tv_splash_main_text);
        loginView = LayoutInflater.from(this).inflate(R.layout.view_login_and_reg, null, false);
        if (loginView != null){
            cbIsOwner = (CheckBox) loginView.findViewById(R.id.cb_splash_is_owner);
            tilUName = (TextInputLayout) loginView.findViewById(R.id.til_splash_uname);
            tilUName.setHint(getString(R.string.tli_hint_uname));
            tilPwd = (TextInputLayout) loginView.findViewById(R.id.til_splash_pwd);
            tilPwd.setHint(getString(R.string.tli_hint_pwd));

            mDialog = new SimpleDialog(this);
            mDialog.applyStyle(R.style.App_Dialog)
                    .title(R.string.dialog_welcome)
                    .positiveAction(R.string.action_login)
                    .negativeAction(R.string.action_back)
                    .contentView(loginView)
                    .cancelable(false)
                    .positiveActionClickListener(view -> {
                            mDialog.dismiss();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    )
                    .negativeActionClickListener(view -> {
                            mDialog.dismiss();
                        }
                    );
        }

        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);

        //delay some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runAnim();
            }
        }, Config.APP_SPLASH_TIME);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_splash_login){
            mDialog.show();
        }else if (view.getId() == R.id.btn_splash_reg){
            IntentUtils.startPreviewActivity(this, new Intent(this, RegHouseActivity.class));
        }
    }

    private void runAnim(){
        AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);
        alphaAnim.setDuration(Config.SPLASH_LAYER_ANIM_TIME);
        alphaAnim.setFillAfter(true);

        TranslateAnimation translateAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1);
        translateAnim.setDuration(Config.SPLASH_LAYER_ANIM_TIME);

        AnimationSet tranAnimSet = new AnimationSet(false);
        tranAnimSet.addAnimation(translateAnim);
        tranAnimSet.setInterpolator(new AccelerateDecelerateInterpolator());
        tranAnimSet.setFillAfter(true);

        btnLogin.setVisibility(View.VISIBLE);
        btnReg.setVisibility(View.VISIBLE);
        btnLogin.setAnimation(alphaAnim);
        btnReg.setAnimation(alphaAnim);

        tvMainText.setAnimation(tranAnimSet);
    }
}
