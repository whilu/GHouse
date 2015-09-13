package co.lujun.ghouse.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.Config;
import co.lujun.ghouse.bean.House;
import co.lujun.ghouse.bean.SignCarrier;
import co.lujun.ghouse.ui.widget.SlidingActivity;
import co.lujun.ghouse.util.MD5;
import co.lujun.ghouse.util.SignatureUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lujun on 2015/8/12.
 */
public class RegHouseActivity extends SlidingActivity {

    private Toolbar mToolbar;
    private TextInputLayout tilUName, tilPwd, tilPhone, tilAddress, tilIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_house);
        init();
    }

    private void init(){
        mToolbar = (Toolbar) findViewById(R.id.tb_reg_house);
        setTitle(getString(R.string.action_reg));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tilUName = (TextInputLayout) findViewById(R.id.til_rhouse_uname);
        tilUName.setHint(getString(R.string.til_hint_rhouse_name));
        tilPwd = (TextInputLayout) findViewById(R.id.til_rhouse_pwd);
        tilPwd.setHint(getString(R.string.til_hint_rhouse_pwd));
        tilPhone = (TextInputLayout) findViewById(R.id.til_rhouse_phone);
        tilPhone.setHint(getString(R.string.til_hint_rhouse_phone));
        tilAddress = (TextInputLayout) findViewById(R.id.til_rhouse_address);
        tilAddress.setHint(getString(R.string.til_hint_rhouse_address));
        tilIntro = (TextInputLayout) findViewById(R.id.til_rhouse_intro);
        tilIntro.setHint(getString(R.string.til_hint_rhouse_intro));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reg_house, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }else if (item.getItemId() == R.id.action_confirm_reg_house){
            onRegHouse();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 注册房子
     */
    private void onRegHouse(){
        String username = "";
        String phone = "";
        String houseaddress = "";
        String houseinfo = "";
        String password = "";
        try{
            username = tilUName.getEditText().getText().toString();
            phone = tilPhone.getEditText().getText().toString();
            houseaddress = tilAddress.getEditText().getText().toString();
            houseinfo = tilIntro.getEditText().getText().toString();
            password = MD5.getMD5(tilPwd.getEditText().getText().toString());
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(houseaddress) || TextUtils.isEmpty(houseinfo)
                || TextUtils.isEmpty(password)){
            Toast.makeText(this, R.string.msg_all_not_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO show loading dialog
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("phone", phone);
        map.put("houseaddress", houseaddress);
        map.put("houseinfo", houseinfo);
        map.put("password", password);
        SignCarrier signCarrier = SignatureUtil.getSignature(map);
        GlApplication.getApiService()
            .onRegisterHouse(
                    signCarrier.getAppId(),
                    signCarrier.getNonce(),
                    signCarrier.getTimestamp(),
                    signCarrier.getSignature(),
                    username,
                    password,
                    phone,
                    houseaddress,
                    houseinfo
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<BaseJson<House>>() {
                @Override
                public void onCompleted() {
                    Log.d("RegHouseActivity", "onCompleted");
                }

                @Override
                public void onError(Throwable e) {
                    //TODO Hide app debug Toast
                    Log.d("RegHouseActivity", e.toString());
                    Toast.makeText(GlApplication.getContext(),
                            e.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(BaseJson<House> houseBaseJson) {
                    //TODO update output infomation
                    if (null == houseBaseJson){
                        Toast.makeText(GlApplication.getContext(),
                                "reg houseBaseJson == null", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //Not Correct status
                    if (houseBaseJson.getStatus() != Config.STATUS_CODE_OK){
                        Toast.makeText(GlApplication.getContext(),
                                "reg return code != 200", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(GlApplication.getContext(),
                            R.string.msg_register_success, Toast.LENGTH_SHORT).show();
                    //TODO hide loading dialog
                    finish();
                }
            });
    }
}
