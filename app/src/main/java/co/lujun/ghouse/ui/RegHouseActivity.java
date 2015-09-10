package co.lujun.ghouse.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.BaseJson;
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
        Map<String, String> map = new HashMap<String, String>();
        try{
            map.put("username", tilUName.getEditText().getText().toString());
            map.put("phone", tilPhone.getEditText().getText().toString());
            map.put("houseaddress", tilAddress.getEditText().getText().toString());
            map.put("houseinfo", tilIntro.getEditText().getText().toString());
            map.put("password", MD5.getMD5(tilPwd.getEditText().getText().toString()));
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        SignCarrier signCarrier = SignatureUtil.getSignature(map);
        GlApplication.getApiService()
            .onRegisterHouse(
                    signCarrier.getAppId(),
                    signCarrier.getNonce(),
                    signCarrier.getTimestamp(),
                    signCarrier.getSignature(),
                    map.get("username"),
                    map.get("password"),
                    map.get("phone"),
                    map.get("houseaddress"),
                    map.get("houseinfo")
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
                    Log.d("RegHouseActivity", e.toString());
                }

                @Override
                public void onNext(BaseJson<House> houseBaseJson) {
                    Log.d("RegHouseActivity", "onNext");
                    Log.d("RegHouseActivity", houseBaseJson.getStatus() + "");
                    Log.d("RegHouseActivity", houseBaseJson.getData().toString());
                    Log.d("RegHouseActivity", houseBaseJson.getMessage());
                }
            });
    }
}
