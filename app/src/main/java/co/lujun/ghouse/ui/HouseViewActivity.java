package co.lujun.ghouse.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.Config;
import co.lujun.ghouse.bean.House;
import co.lujun.ghouse.bean.SignCarrier;
import co.lujun.ghouse.bean.User;
import co.lujun.ghouse.ui.adapter.MemberAdapter;
import co.lujun.ghouse.ui.widget.LoadingWindow;
import co.lujun.ghouse.ui.widget.SlidingActivity;
import co.lujun.ghouse.util.DatabaseHelper;
import co.lujun.ghouse.util.NetWorkUtils;
import co.lujun.ghouse.util.PreferencesUtils;
import co.lujun.ghouse.util.SignatureUtil;
import co.lujun.ghouse.util.SystemUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lujun on 2015/8/10.
 */
public class HouseViewActivity extends SlidingActivity {

    private Toolbar mToolbar;

    private TextView tvHouseId, tvMoneySurplus, tvHouseAddress, tvHouseIntro, tvHouseOwner;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TextInputLayout tilInfo, tilAddMoney, tilAddMoneyExtra, tilHouseAdd, tilHouseIntro, tilUName, tilUPwd;
    private View hvUpdateView, hvAddMoneyView, hvUHouseView, hvAddMemberView;

    private static Dialog mUpdateDialog, mAddMoneyDialog, mUHouseDialog, mAddMemberDialog;

    private SwipeRefreshLayout srlHouse;

    private MemberAdapter mAdapter;

    private List<String> mAvatarList;

    private final static String TAG = "HouseViewActivity";

    private LoadingWindow winLoading;

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

        srlHouse = (SwipeRefreshLayout) findViewById(R.id.srl_house);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        hvUpdateView = LayoutInflater.from(this).inflate(R.layout.view_hv_modify_info, null, false);
        hvAddMoneyView = LayoutInflater.from(this).inflate(R.layout.view_hv_add_money, null, false);
        hvUHouseView = LayoutInflater.from(this).inflate(R.layout.view_hv_change_house, null, false);
        hvAddMemberView = LayoutInflater.from(this).inflate(R.layout.view_hv_add_member, null, false);

        mAvatarList = new ArrayList<String>();
        mAdapter = new MemberAdapter(mAvatarList);
        mRecyclerView.setAdapter(mAdapter);

        initDialog();

        winLoading = new LoadingWindow(LayoutInflater.from(this).inflate(R.layout.view_loading, null, false));

        srlHouse.setOnRefreshListener(() -> onRequestData());
        // read cache
        try {
            List<House> houses = DatabaseHelper.getDatabaseHelper(this).getDao(House.class).queryForAll();
            if (houses != null && houses.size() > 0){
                onShowData(houses.get(0));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        // request data
        onRequestData();
    }

    /**
     * init dialogs
     */
    private void initDialog(){
        if (hvUpdateView != null && hvAddMoneyView != null
                && hvUHouseView != null && hvAddMemberView != null){
            tilInfo = (TextInputLayout) hvUpdateView.findViewById(R.id.til_hv_modify_info);
            tilAddMoney = (TextInputLayout) hvAddMoneyView.findViewById(R.id.til_hv_add_money);
            tilAddMoneyExtra = (TextInputLayout) hvAddMoneyView.findViewById(R.id.til_hv_add_money_extra);
            tilHouseAdd = (TextInputLayout) hvUHouseView.findViewById(R.id.til_hv_house_address);
            tilHouseIntro = (TextInputLayout) hvUHouseView.findViewById(R.id.til_hv_house_info);
            tilUName = (TextInputLayout) hvAddMemberView.findViewById(R.id.til_hv_uname);
            tilUPwd = (TextInputLayout) hvAddMemberView.findViewById(R.id.til_hv_pwd);
            tilHouseAdd.setHint(getString(R.string.til_hint_rhouse_address));
            tilHouseIntro.setHint(getString(R.string.til_hint_rhouse_intro));
            tilUName.setHint(getString(R.string.til_hint_rhouse_name));
            tilUPwd.setHint(getString(R.string.til_hint_rhouse_pwd));
            mUpdateDialog = new SimpleDialog(this);
            mAddMoneyDialog = new SimpleDialog(this);
            mUHouseDialog = new SimpleDialog(this);
            mAddMemberDialog = new SimpleDialog(this);
            mUpdateDialog.applyStyle(R.style.App_Dialog)
                    .positiveAction(R.string.action_update)
                    .negativeAction(R.string.action_back)
                    .contentView(hvUpdateView)
                    .cancelable(false)
                    .positiveActionClickListener(v -> {
                        if ((Integer) tilInfo.getTag() == R.id.tv_house_address){
                            onEditHouse(1);
                        }else if ((Integer) tilInfo.getTag() == R.id.tv_house_intro){
                            onEditHouse(2);
                        }
                    })
                    .negativeActionClickListener(v -> mUpdateDialog.dismiss());
            //
            mAddMoneyDialog.applyStyle(R.style.App_Dialog)
                    .positiveAction(R.string.action_add)
                    .negativeAction(R.string.action_back)
                    .contentView(hvAddMoneyView)
                    .cancelable(false)
                    .positiveActionClickListener(v -> onEditHouse(0))
                    .negativeActionClickListener(v -> mAddMoneyDialog.dismiss());
            //
            mUHouseDialog.applyStyle(R.style.App_Dialog)
                    .title(getString(R.string.action_uhouse))
                    .positiveAction(R.string.action_uhouse)
                    .negativeAction(R.string.action_back)
                    .contentView(hvUHouseView)
                    .cancelable(false)
                    .positiveActionClickListener(v -> {})
                    .negativeActionClickListener(v -> mUHouseDialog.dismiss());
            //
            mAddMemberDialog.applyStyle(R.style.App_Dialog)
                    .title(getString(R.string.action_add_member))
                    .positiveAction(R.string.action_add)
                    .negativeAction(R.string.action_back)
                    .contentView(hvAddMemberView)
                    .cancelable(false)
                    .positiveActionClickListener(v -> onAddMember())
                    .negativeActionClickListener(v -> mAddMemberDialog.dismiss());
        }
    }

    /**
     * add action method
     * @param v
     */
    public void addAction(View v) {
        if (hvAddMoneyView == null || mAddMoneyDialog == null){
            return;
        }
        int vid = v.getId();
        if (vid == R.id.ll_hv_money_surplus){
            mAddMoneyDialog.title(R.string.tv_house_add_money);
            tilAddMoney.getEditText().setText("");
            tilAddMoney.getEditText().setHint(getResources().getString(R.string.tv_house_money_surplus)
                    + tvMoneySurplus.getText());
            tilAddMoneyExtra.setHint(getString(R.string.tv_house_intro));
            mAddMoneyDialog.show();
        }else if (vid == R.id.ll_hv_add_member){
            if (hvAddMemberView != null && mAddMemberDialog != null){
                tilUName.getEditText().setText("");
                tilUPwd.getEditText().setText("");
                mAddMemberDialog.show();
            }
        }else if (vid == R.id.ll_hv_house_moving){
            if (hvUHouseView != null && mUHouseDialog != null){
                tilHouseAdd.getEditText().setText("");
                tilHouseIntro.getEditText().setText("");
                mUHouseDialog.show();
            }
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

    /**
     * 请求房子信息
     */
    private void onRequestData(){
        if (NetWorkUtils.getNetWorkType(this) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            SystemUtil.showToast(R.string.msg_network_disconnect);
            return;
        }
        String validate = PreferencesUtils.getString(this, Config.KEY_OF_VALIDATE);
        Map<String, String> map = new HashMap<String, String>();
        map.put("validate", validate);
        SignCarrier signCarrier = SignatureUtil.getSignature(map);
        GlApplication.getApiService()
            .onGetHouseData(
                signCarrier.getAppId(),
                signCarrier.getNonce(),
                signCarrier.getTimestamp(),
                signCarrier.getSignature(),
                validate
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<BaseJson<House>>() {
                @Override
                public void onCompleted() {
                    Log.d(TAG, "onCompleted()");
                }

                @Override
                public void onError(Throwable e) {
                    if (winLoading.isShowing()) {
                        winLoading.dismiss();
                    }
                    if (srlHouse.isRefreshing()){
                        srlHouse.setRefreshing(false);
                    }
                    Log.d(TAG, e.toString());
                }

                @Override
                public void onNext(BaseJson<House> houseBaseJson) {
                    House house;
                    if (null == houseBaseJson || (house = houseBaseJson.getData()) == null) {
                        SystemUtil.showToast(R.string.msg_nullpointer_error);
                        return;
                    }
                    // not Correct status
                    if (houseBaseJson.getStatus() != Config.STATUS_CODE_OK) {
                        SystemUtil.showToast(houseBaseJson.getMessage());
                        return;
                    }
                    PreferencesUtils.putString(HouseViewActivity.this, Config.KEY_OF_VALIDATE, houseBaseJson.getValidate());
                    onCacheData(house);
                    if (winLoading.isShowing()) {
                        winLoading.dismiss();
                    }
                    if (srlHouse.isRefreshing()){
                        srlHouse.setRefreshing(false);
                    }
                }
            });
    }

    /**
     * 缓存房子信息
     * @param house
     */
    private void onCacheData(House house){
        if (house == null){
            return;
        }
        try{
            Dao houseDao = DatabaseHelper.getDatabaseHelper(this).getDao(House.class);
            Dao userDao = DatabaseHelper.getDatabaseHelper(this).getDao(User.class);
            List<House> tmpHouses = houseDao.queryForAll();
            if (tmpHouses.isEmpty()){
                userDao.delete(userDao.queryForAll());
            }else {
                for (House tmpHouse : tmpHouses) {
                    userDao.deleteBuilder().where().eq("houseid", tmpHouse.getHid());
                    userDao.deleteBuilder().delete();
                }
            }
            houseDao.delete(tmpHouses);
            houseDao.create(house);
            for (User user : house.getMember()) {
                user.setHouse(house);
                userDao.create(user);
            }
            onShowData(house);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * 展示数据
     * @param house
     */
    private void onShowData(House house){
        if (house == null){
            SystemUtil.showToast(R.string.msg_user_null);
            return;
        }
        tvHouseId.setText(Long.toString(house.getHid()));
        tvMoneySurplus.setText(Float.toString(house.getMoney()));
        tvHouseAddress.setText(house.getHouseaddress());
        tvHouseIntro.setText(house.getHouseinfo());
        tvHouseOwner.setText(house.getReg_user());
        mAvatarList.clear();
        for (User user : house.getMember()) {
            mAvatarList.add(user.getAvatar());
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * add member
     */
    private void onAddMember(){
        if (NetWorkUtils.getNetWorkType(this) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            SystemUtil.showToast(R.string.msg_network_disconnect);
            return;
        }
        String uname = tilUName.getEditText().getText().toString();
        String upwd = tilUPwd.getEditText().getText().toString();
        if (TextUtils.isEmpty(uname) || TextUtils.isEmpty(upwd)){
            SystemUtil.showToast(R.string.msg_login_info_not_null);
            return;
        }
        SystemUtil.showOrHideInputMethodManager(this);
        if (mAddMemberDialog.isShowing()){
            mAddMemberDialog.dismiss();
        }
        if (!winLoading.isShowing()) {
            winLoading.showAsDropDown(mToolbar, 0, 0);
        }
        String validate = PreferencesUtils.getString(this, Config.KEY_OF_VALIDATE);
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", uname);
        map.put("password", upwd);
        map.put("validate", validate);
        SignCarrier signCarrier = SignatureUtil.getSignature(map);
        GlApplication.getApiService()
            .onAddMember(
                signCarrier.getAppId(),
                signCarrier.getNonce(),
                signCarrier.getTimestamp(),
                signCarrier.getSignature(),
                uname,
                upwd,
                validate
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
                        if (winLoading.isShowing()) {
                            winLoading.dismiss();
                        }
                        if (!mAddMemberDialog.isShowing()) {
                            mAddMemberDialog.show();
                        }
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onNext(BaseJson<User> userBaseJson) {
                        if (null == userBaseJson) {
                            SystemUtil.showToast(R.string.msg_nullpointer_error);
                            return;
                        }
                        // not Correct status
                        if (userBaseJson.getStatus() != Config.STATUS_CODE_OK) {
                            SystemUtil.showToast(userBaseJson.getMessage());
                            return;
                        }
                        PreferencesUtils.putString(HouseViewActivity.this, Config.KEY_OF_VALIDATE, userBaseJson.getValidate());
                        onRequestData();
                    }
                });
    }

    /**
     * update house information
     */
    private void onEditHouse(int type){
        if (NetWorkUtils.getNetWorkType(this) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            SystemUtil.showToast(R.string.msg_network_disconnect);
            return;
        }
        String value = "";
        String remark = "";
        if (type == 0){
            value = tilAddMoney.getEditText().getText().toString();
            remark = tilAddMoneyExtra.getEditText().getText().toString();
        }else {
            value = tilInfo.getEditText().getText().toString();
        }
        if (TextUtils.isEmpty(value)){
            SystemUtil.showToast(R.string.msg_all_not_empty);
            return;
        }
        SystemUtil.showOrHideInputMethodManager(this);
        Dialog dialog = null;
        if (type == 0){
            dialog = mAddMoneyDialog;
        }else {
            dialog = mUpdateDialog;
        }
        if (dialog.isShowing()){
            dialog.dismiss();
        }
        if (!winLoading.isShowing()) {
            winLoading.showAsDropDown(mToolbar, 0, 0);
        }
        String validate = PreferencesUtils.getString(this, Config.KEY_OF_VALIDATE);
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", Integer.toString(type));
        map.put("value", value);
        map.put("remark", remark);
        map.put("validate", validate);
        SignCarrier signCarrier = SignatureUtil.getSignature(map);
        GlApplication.getApiService()
            .onEditHouse(
                signCarrier.getAppId(),
                signCarrier.getNonce(),
                signCarrier.getTimestamp(),
                signCarrier.getSignature(),
                Integer.toString(type),
                value,
                remark,
                validate
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<BaseJson<House>>() {
                @Override
                public void onCompleted() {
                    Log.d(TAG, "onCompleted()");
                }

                @Override
                public void onError(Throwable e) {
                    if (winLoading.isShowing()) {
                        winLoading.dismiss();
                    }
                    Log.d(TAG, e.toString());
                    SystemUtil.showToast(R.string.msg_update_error);
                }

                @Override
                public void onNext(BaseJson<House> houseBaseJson) {
                    if (null == houseBaseJson) {
                        SystemUtil.showToast(R.string.msg_nullpointer_error);
                        return;
                    }
                    // not Correct status
                    if (houseBaseJson.getStatus() != Config.STATUS_CODE_OK) {
                        SystemUtil.showToast(houseBaseJson.getMessage());
                        return;
                    }
                    PreferencesUtils.putString(HouseViewActivity.this, Config.KEY_OF_VALIDATE, houseBaseJson.getValidate());
                    onRequestData();
                }
            });
    }
}