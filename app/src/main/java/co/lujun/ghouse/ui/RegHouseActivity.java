package co.lujun.ghouse.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.widget.SlidingActivity;

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

    }
}
