package co.lujun.ghouse.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.github.whilu.library.CustomRippleButton;
import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.CheckBox;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.Config;
import co.lujun.ghouse.bean.House;
import co.lujun.ghouse.bean.SignCarrier;
import co.lujun.ghouse.bean.User;
import co.lujun.ghouse.ui.widget.LoadingWindow;
import co.lujun.ghouse.util.DatabaseHelper;
import co.lujun.ghouse.util.IntentUtils;
import co.lujun.ghouse.util.MD5;
import co.lujun.ghouse.util.PreferencesUtils;
import co.lujun.ghouse.util.SignatureUtil;
import co.lujun.ghouse.util.SystemUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    private LoadingWindow winLoading;

    private final static String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    /**
     * inti views & something
     */
    private void init(){
        btnLogin = (CustomRippleButton) findViewById(R.id.btn_splash_login);
        btnReg = (CustomRippleButton) findViewById(R.id.btn_splash_reg);
        tvMainText = (TextView) findViewById(R.id.tv_splash_main_text);
        loginView = LayoutInflater.from(this).inflate(R.layout.view_login_and_reg, null, false);
        winLoading = new LoadingWindow(LayoutInflater.from(this).inflate(R.layout.view_loading, null, false));
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
                    .positiveActionClickListener(view -> onLogin())
                    .negativeActionClickListener(view -> mDialog.dismiss());
        }

        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);

        //delay some time
        new Handler().postDelayed(() -> runAnim(), Config.APP_SPLASH_TIME);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_splash_login){
            if (!mDialog.isShowing()){
                mDialog.show();
            }
        }else if (view.getId() == R.id.btn_splash_reg){
            IntentUtils.startPreviewActivity(this, new Intent(this, RegHouseActivity.class));
        }
    }

    /**
     * 登录逻辑
     */
    private void onLogin(){
        String username = "";
        String password = "";
        String usertype = "";
        try{
            username = tilUName.getEditText().getText().toString();
            password = MD5.getMD5(tilPwd.getEditText().getText().toString());
            usertype = cbIsOwner.isChecked() ? "1" : "0";
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            Toast.makeText(this, R.string.msg_login_info_not_null, Toast.LENGTH_SHORT).show();
            return;
        }
        // 隐藏输入框
        if (mDialog.isShowing()){
            mDialog.dismiss();
        }
        // 显示Loading
        if (!winLoading.isShowing()){
            winLoading.showAtLocation(btnLogin, 0, 0, Gravity.CENTER);
        }
        // 隐藏软键盘
        SystemUtil.showOrHideInputMethodManager(this);
        // 请求数据
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);
        map.put("usertype", usertype);
        SignCarrier signCarrier = SignatureUtil.getSignature(map);
        GlApplication.getApiService()
                .onLogin(
                        signCarrier.getAppId(),
                        signCarrier.getNonce(),
                        signCarrier.getTimestamp(),
                        signCarrier.getSignature(),
                        username,
                        password,
                        usertype
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseJson<User>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (winLoading.isShowing()){
                            winLoading.dismiss();
                        }
                        if (!mDialog.isShowing()){
                            mDialog.show();
                        }
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onNext(BaseJson<User> userBaseJson) {
                        if (winLoading.isShowing()){
                            winLoading.dismiss();
                        }
                        User user;
                        if (null == userBaseJson || (user = userBaseJson.getData()) == null){
                            Toast.makeText(GlApplication.getContext(),
                                    getString(R.string.msg_nullpointer_error), Toast.LENGTH_SHORT).show();
                            if (!mDialog.isShowing()){
                                mDialog.show();
                            }
                            return;
                        }
                        // not Correct status
                        if (userBaseJson.getStatus() != Config.STATUS_CODE_OK){
                            Toast.makeText(GlApplication.getContext(),
                                    userBaseJson.getMessage(), Toast.LENGTH_SHORT).show();
                            if (!mDialog.isShowing()){
                                mDialog.show();
                            }
                            return;
                        }
                        try{
                            DatabaseHelper.getDatabaseHelper(SplashActivity.this).getDao(User.class).create(user);
                            PreferencesUtils.putBoolean(SplashActivity.this, Config.KEY_OF_LOGIN_FLAG, true);
                            PreferencesUtils.putString(SplashActivity.this, Config.KEY_OF_VALIDATE, userBaseJson.getValidate());
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                        mDialog.dismiss();
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }

    /**
     * run adnimation
     */
    private void runAnim(){
        boolean isLogin = PreferencesUtils.getBoolean(this, Config.KEY_OF_LOGIN_FLAG, false);
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
        tranAnimSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isLogin) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        tvMainText.setAnimation(tranAnimSet);

        if (!isLogin){// 未登录
            AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);
            alphaAnim.setDuration(Config.SPLASH_LAYER_ANIM_TIME);
            alphaAnim.setFillAfter(true);

            btnLogin.setVisibility(View.VISIBLE);
            btnReg.setVisibility(View.VISIBLE);
            btnLogin.setAnimation(alphaAnim);
            btnReg.setAnimation(alphaAnim);
        }else {
            btnLogin.setVisibility(View.INVISIBLE);
            btnReg.setVisibility(View.INVISIBLE);

        }
    }
}
