package co.lujun.ghouse.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.adapter.MemberAdapter;
import co.lujun.ghouse.ui.widget.SlidingActivity;

/**
 * Created by lujun on 2015/8/10.
 */
public class HouseViewActivity extends SlidingActivity {

    private Toolbar mToolbar;

    private TextView tvHouseId, tvMoneySurplus, tvHouseAddress, tvHouseIntro, tvHouseOwner;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TextInputLayout tilInfo, tilAddMoney;
    private View hvUpdateView, hvAddMoneyView;

    private static Dialog mUpdateDialog, mAddMoneyDialog;

    private MemberAdapter mAdapter;

    private float moneySurplus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        init();
    }

    private void init(){
        mToolbar = (Toolbar) findViewById(R.id.tb_add_house);
        setTitle(getString(R.string.action_house));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvHouseId = (TextView) findViewById(R.id.tv_house_id);
        tvMoneySurplus = (TextView) findViewById(R.id.tv_house_money_surplus);
        tvHouseAddress = (TextView) findViewById(R.id.tv_house_address);
        tvHouseIntro = (TextView) findViewById(R.id.tv_house_intro);
        tvHouseOwner = (TextView) findViewById(R.id.tv_house_owner);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_house_member);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        hvUpdateView = LayoutInflater.from(this).inflate(R.layout.view_hv_modify_info, null, false);
        hvAddMoneyView = LayoutInflater.from(this).inflate(R.layout.view_hv_add_money, null, false);
        //
        if (hvUpdateView != null && hvAddMoneyView != null){
            tilInfo = (TextInputLayout) hvUpdateView.findViewById(R.id.til_hv_modify_info);
            tilAddMoney = (TextInputLayout) hvAddMoneyView.findViewById(R.id.til_hv_add_money);
            mUpdateDialog = new SimpleDialog(this);
            mAddMoneyDialog = new SimpleDialog(this);
            mUpdateDialog.applyStyle(R.style.App_Dialog)
                    .positiveAction(R.string.action_update)
                    .negativeAction(R.string.action_back)
                    .contentView(hvUpdateView)
                    .cancelable(false)
                    .positiveActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if ((Integer) tilInfo.getTag() == R.id.tv_house_address){
                                Toast.makeText(HouseViewActivity.this,
                                        "tv_house_address" + tilInfo.getEditText().getText(), Toast.LENGTH_SHORT).show();
                            }else if ((Integer) tilInfo.getTag() == R.id.tv_house_intro){
                                Toast.makeText(HouseViewActivity.this,
                                        "tv_house_intro" + tilInfo.getEditText().getText(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .negativeActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mUpdateDialog.dismiss();
                        }
                    });
            //
            mAddMoneyDialog.applyStyle(R.style.App_Dialog)
                    .positiveAction(R.string.action_add)
                    .negativeAction(R.string.action_back)
                    .contentView(hvAddMoneyView)
                    .cancelable(false)
                    .positiveActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(HouseViewActivity.this,
                                    "tv_house_money_surplus" + (moneySurplus +
                                            Float.valueOf(tilAddMoney.getEditText().getText().toString())), Toast.LENGTH_SHORT).show();
                            tvMoneySurplus.setText((moneySurplus
                                    + Float.valueOf(tilAddMoney.getEditText().getText().toString())) + "");
                        }
                    })
                    .negativeActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mAddMoneyDialog.dismiss();
                        }
                    });
        }
        //
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 3; i++){
            list.add(i + "");
        }
        mAdapter = new MemberAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * add action method
     * @param v
     */
    public void addAction(View v){
        if (hvAddMoneyView == null || mAddMoneyDialog == null){
            return;
        }
        int vid = v.getId();
        if (vid == R.id.ll_hv_money_surplus){
            mAddMoneyDialog.title(R.string.tv_house_add_money);
            tilAddMoney.getEditText().setText("");
            tilAddMoney.getEditText().setHint(getResources().getString(R.string.tv_house_money_surplus)
                    + tvMoneySurplus.getText());
            moneySurplus = Float.valueOf(tvMoneySurplus.getText().toString());
            mAddMoneyDialog.show();
        }else if (vid == R.id.ll_hv_add_member){
            Toast.makeText(HouseViewActivity.this, "add member", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * update house information
     * @param v
     */
    public void updateInfo(View v){
        if (hvUpdateView == null || mUpdateDialog == null){
            return;
        }
        int vid = v.getId();
        if (vid == R.id.ll_hv_address){
            mUpdateDialog.title(R.string.tv_house_address);
            tilInfo.getEditText().setText(tvHouseAddress.getText());
            tilInfo.setTag(R.id.tv_house_address);
        }else if (vid == R.id.ll_hv_intro){
            mUpdateDialog.title(R.string.tv_house_intro);
            tilInfo.getEditText().setText(tvHouseIntro.getText());
            tilInfo.setTag(R.id.tv_house_intro);
        }
        mUpdateDialog.show();
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
