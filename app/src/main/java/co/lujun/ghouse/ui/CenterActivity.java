package co.lujun.ghouse.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.whilu.library.CustomRippleButton;
import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;

import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.widget.SlidingActivity;
import co.lujun.ghouse.ui.widget.roundedimageview.RoundedImageView;
import co.lujun.ghouse.util.IntentUtils;

/**
 * Created by lujun on 2015/7/30.
 */
public class CenterActivity extends SlidingActivity {

    private Toolbar mToolbar;
    private RoundedImageView ivAvatar;
    private TextView tvUName, tvPhone, tvHouseId;
    private RelativeLayout llPhone, llHouseId;
    private CustomRippleButton btnLogout;

    private TextInputLayout tilPhone;
    private View updatePhoneView;

    private static Dialog mUpdatePhoneDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        init();
    }

    private void init(){
        mToolbar = (Toolbar) findViewById(R.id.tb_center);
        setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivAvatar = (RoundedImageView) findViewById(R.id.iv_set_avatar);
        tvUName = (TextView) findViewById(R.id.tv_set_uname);
        tvPhone = (TextView) findViewById(R.id.tv_set_phone);
        tvHouseId = (TextView) findViewById(R.id.tv_set_house_id);
        llPhone = (RelativeLayout) findViewById(R.id.ll_set_phone);
        llHouseId = (RelativeLayout) findViewById(R.id.ll_set_house_id);

        btnLogout = (CustomRippleButton) findViewById(R.id.btn_set_logout);

        updatePhoneView = LayoutInflater.from(this).inflate(R.layout.view_center_change_phone, null, false);
        if (updatePhoneView != null){
            tilPhone = (TextInputLayout) updatePhoneView.findViewById(R.id.til_center_phone);
            tilPhone.setHint(getString(R.string.tv_phone));
            mUpdatePhoneDialog = new SimpleDialog(this);
            mUpdatePhoneDialog.applyStyle(R.style.App_Dialog)
                    .title(getString(R.string.action_update_phone))
                    .positiveAction(R.string.action_update)
                    .negativeAction(R.string.action_back)
                    .contentView(updatePhoneView)
                    .cancelable(false)
                    .positiveActionClickListener(v -> {})
                    .negativeActionClickListener(v -> mUpdatePhoneDialog.dismiss());
        }

        llPhone.setOnClickListener(v -> {
            if (mUpdatePhoneDialog != null){
                mUpdatePhoneDialog.show();
            }
        });
        llHouseId.setOnClickListener(v ->
                IntentUtils.startPreviewActivity(CenterActivity.this,
                        new Intent(CenterActivity.this, HouseViewActivity.class))
        );
        btnLogout.setOnClickListener(v -> {});
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