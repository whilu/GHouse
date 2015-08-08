package co.lujun.ghouse.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.whilu.library.CustomRippleButton;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.RadioButton;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.widget.SlidingActivity;

/**
 * Created by lujun on 2015/7/30.
 */
public class AddTodoActivity extends SlidingActivity
        implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Toolbar mToolbar;

    private TextInputLayout tilBillContent, tilBillTotal, tilBillCode, tilBillExtra;
    private RadioButton rbBillRmb, rbBillDollar, rbBillOther;
    private CheckBox cbBillEat, cbBillWear, cbBillLive, cbBillTravel, cbBillPlay, cbBillOther;
    private ImageView ivBillImg1, ivBillImg2;
    private CustomRippleButton btnBillCameraCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        init();
    }

    private void init(){
        mToolbar = (Toolbar) findViewById(R.id.tb_add_todo);
        setTitle(getString(R.string.action_add_todo));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tilBillContent = (TextInputLayout) findViewById(R.id.til_bill_content);
        tilBillTotal = (TextInputLayout) findViewById(R.id.til_bill_total);
        tilBillCode = (TextInputLayout) findViewById(R.id.til_bill_code);
        tilBillExtra = (TextInputLayout) findViewById(R.id.til_bill_extra);

        rbBillRmb = (RadioButton) findViewById(R.id.rb_bill_rmb);
        rbBillDollar = (RadioButton) findViewById(R.id.rb_bill_dollar);
        rbBillOther = (RadioButton) findViewById(R.id.rb_bill_other);

        cbBillEat = (CheckBox) findViewById(R.id.cb_bill_eat);
        cbBillWear = (CheckBox) findViewById(R.id.cb_bill_wear);
        cbBillLive = (CheckBox) findViewById(R.id.cb_bill_live);
        cbBillTravel = (CheckBox) findViewById(R.id.cb_bill_travel);
        cbBillPlay = (CheckBox) findViewById(R.id.cb_bill_play);
        cbBillOther = (CheckBox) findViewById(R.id.cb_bill_other);

        ivBillImg1 = (ImageView) findViewById(R.id.iv_bill_image1);
        ivBillImg2 = (ImageView) findViewById(R.id.iv_bill_image2);

        btnBillCameraCode = (CustomRippleButton) findViewById(R.id.btn_bill_camera_code);

        tilBillContent.setHint(getString(R.string.til_hint_bill_content));
        tilBillTotal.setHint(getString(R.string.til_hint_bill_total));
        tilBillCode.setHint(getString(R.string.til_hint_bill_code));
        tilBillExtra.setHint(getString(R.string.til_hint_bill_extra));

        rbBillRmb.setOnCheckedChangeListener(this);
        rbBillDollar.setOnCheckedChangeListener(this);
        rbBillOther.setOnCheckedChangeListener(this);

        cbBillEat.setOnCheckedChangeListener(this);
        cbBillWear.setOnCheckedChangeListener(this);
        cbBillLive.setOnCheckedChangeListener(this);
        cbBillTravel.setOnCheckedChangeListener(this);
        cbBillPlay.setOnCheckedChangeListener(this);
        cbBillOther.setOnCheckedChangeListener(this);

        ivBillImg1.setOnClickListener(this);
        ivBillImg2.setOnClickListener(this);

        btnBillCameraCode.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            rbBillRmb.setChecked(rbBillRmb == compoundButton);
            rbBillDollar.setChecked(rbBillDollar == compoundButton);
            rbBillOther.setChecked(rbBillOther == compoundButton);
        }
        if (compoundButton instanceof CheckBox){

        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof ImageView){

        }else if (view instanceof CustomRippleButton){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }else if (item.getItemId() == R.id.action_confirm_add_todo){
            Toast.makeText(GlApplication.getContext(), "action_confirm_add_todo", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
