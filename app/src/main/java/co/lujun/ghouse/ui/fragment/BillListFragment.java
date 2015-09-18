package co.lujun.ghouse.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.util.Attributes;
import com.j256.ormlite.dao.Dao;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.BaseList;
import co.lujun.ghouse.bean.Bill;
import co.lujun.ghouse.bean.Config;
import co.lujun.ghouse.bean.House;
import co.lujun.ghouse.bean.Image;
import co.lujun.ghouse.bean.SignCarrier;
import co.lujun.ghouse.bean.User;
import co.lujun.ghouse.ui.BillDetailActivity;
import co.lujun.ghouse.ui.MainActivity;
import co.lujun.ghouse.ui.adapter.BillAdapter;
import co.lujun.ghouse.util.DatabaseHelper;
import co.lujun.ghouse.util.IntentUtils;
import co.lujun.ghouse.util.MD5;
import co.lujun.ghouse.util.NetWorkUtils;
import co.lujun.ghouse.util.PreferencesUtils;
import co.lujun.ghouse.util.SignatureUtil;
import co.lujun.ghouse.util.SystemUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lujun on 2015/7/30.
 */
public class BillListFragment extends Fragment {

    private View mView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private BillAdapter mAdapter;
    private List<Bill> mBills;

    private int current_page = 1;
    private final static String TAG = "BillListFragment";

    private int flag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, null);
        initView();
        return mView;
    }

    /**
     * init members
     */
    private void init(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBills = new ArrayList<Bill>();
        if (getArguments() == null
                || (flag = getArguments().getInt(Config.KEY_OF_FRAGMENT, Config.BILL_UNKNOW))
                == Config.BILL_UNKNOW){
            return;
        }
    }

    /**
     * init views
     */
    private void initView(){
        if (mView == null){
            return;
        }
        mRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.srl_home);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_home);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRefreshLayout.setOnRefreshListener(() -> {
            if (mRefreshLayout.isRefreshing()) {
                onRequestData(true);
            }
        });
        mAdapter = new BillAdapter(mBills);
        mAdapter.setItemClickListener((View view, int position) -> {
            IntentUtils.startPreviewActivity(getActivity(),
                    new Intent(getActivity(), BillDetailActivity.class));
        });
        mAdapter.setBillOperationListener(new BillAdapter.OnBillOperationListener() {
            @Override
            public void onConfirmBill(int position) {
                onOperateBill(position, 1);
            }

            @Override
            public void onEditBill(int positin) {
                onEditBill(positin);
            }

            @Override
            public void onDeleteBill(int position) {
                onOperateBill(position, 0);
            }
        });
        mAdapter.setMode(Attributes.Mode.Single);
        initRecyclerView();
        // load cache
        try {
            List<Bill> tmpBills = null;
            if (flag == Config.BILL_FRAGMENT){
                tmpBills = DatabaseHelper.getDatabaseHelper(getActivity()).getDao(Bill.class)
                        .queryForEq("confirm_status", 1);
            }else if (flag == Config.TODO_FRAGMENT){
                tmpBills = DatabaseHelper.getDatabaseHelper(getActivity()).getDao(Bill.class)
                        .queryForEq("confirm_status", 0);
            }
            if (tmpBills != null && tmpBills.size() > 0) {
                onShowData(tmpBills, true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // refresh data
        onRequestData(true);
    }

    /**
     * intit recyclerview
     */
    private void initRecyclerView(){
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItem = mLayoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = mLayoutManager.getItemCount();
                    if (lastVisibleItem == totalItemCount - 1) {
                        // TODO cal item num
                        if (lastVisibleItem - firstVisibleItem < totalItemCount) {
                            ((BillAdapter) recyclerView.getAdapter()).showFooter();
                            onRequestData(false);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItem = mLayoutManager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                if (lastVisibleItem - firstVisibleItem == totalItemCount - 1) {
                    ((BillAdapter) recyclerView.getAdapter()).hideFooter();
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * request data
     */
    private void onRequestData(boolean isRefresh){
        if (NetWorkUtils.getNetWorkType(getActivity()) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            SystemUtil.showToast(R.string.msg_network_disconnect);
            return;
        }
        String type = "";// 1-BillList, 2-TodoList
        if (flag == Config.BILL_FRAGMENT){
            type = "1";
        }else if (flag == Config.TODO_FRAGMENT){
            type = "0";
        }
        if (isRefresh){
            current_page = 1;
        }
        String validate = PreferencesUtils.getString(getActivity(), Config.KEY_OF_VALIDATE);
        String page = current_page + "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", page);
        map.put("validate", validate);
        map.put("type", type);
        SignCarrier signCarrier = SignatureUtil.getSignature(map);
        GlApplication.getApiService()
            .onGetBillList(
                signCarrier.getAppId(),
                signCarrier.getNonce(),
                signCarrier.getTimestamp(),
                signCarrier.getSignature(),
                page,
                validate,
                type
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseJson<BaseList<Bill>>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(false);
                        }
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onNext(BaseJson<BaseList<Bill>> billListBaseJson) {
                        if (mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(false);
                        }
                        if (null == billListBaseJson
                                || billListBaseJson.getStatus() != Config.STATUS_CODE_OK
                                || billListBaseJson.getData().getLists() == null) {
                            SystemUtil.showToast(R.string.msg_request_error);
                            return;
                        }
                        current_page = billListBaseJson.getData().getCurrent_page() + 1;
                        PreferencesUtils.putString(getActivity(), Config.KEY_OF_VALIDATE, billListBaseJson.getValidate());
                        onCacheData(billListBaseJson.getData().getLists(), isRefresh);
                    }
                });
    }

    /**
     * show data
     * @param bills
     * @param isRefresh
     */
    private synchronized void onShowData(List<Bill> bills, boolean isRefresh){
        if (bills == null || bills.size() == 0){
            return;
        }
        if (isRefresh){
            mBills.clear();
        }
        mBills.addAll(bills);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * save data
     * @param bills
     * @param isRefresh
     */
    private void onCacheData(List<Bill> bills, boolean isRefresh){
        if (bills == null || bills.size() <= 0){
            return;
        }
        try {
            Dao billDao = DatabaseHelper.getDatabaseHelper(getActivity()).getDao(Bill.class);
            Dao imageDao = DatabaseHelper.getDatabaseHelper(getActivity()).getDao(Image.class);
            if (isRefresh){// 刷新，先删除所有缓存，在写缓存
                List<Bill> tmpBills = null;
                if (flag == Config.BILL_FRAGMENT) {
                    tmpBills = billDao.queryForEq("confirm_status", 1);
                }else if (flag == Config.TODO_FRAGMENT) {
                    tmpBills = billDao.queryForEq("confirm_status", 0);
                }
                for (Bill bill : tmpBills) {
                    imageDao.deleteBuilder().where().eq("bid", bill.getBid());
                    imageDao.deleteBuilder().delete();
                }
                billDao.delete(tmpBills);
            }
            for (Bill bill : bills) {
                long bid = bill.getBid();
                billDao.create(bill);
                for (Image image : bill.getPhotos()) {
                    image.setBid(bid);
                    image.setBill(bill);
                    imageDao.create(image);
                }
            }
            onShowData(bills, isRefresh);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * operate bill
     * @param i
     */
    private void onOperateBill(int i, int type){
        if (!onCheckPermission()){
            SystemUtil.showToast(R.string.msg_have_no_permission);
            return;
        }
        if (NetWorkUtils.getNetWorkType(getActivity()) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            SystemUtil.showToast(R.string.msg_network_disconnect);
            return;
        }
        long billId = mBills.get(i).getBid();
        String bid = Long.toString(billId);
        String validate = PreferencesUtils.getString(getActivity(), Config.KEY_OF_VALIDATE);
        if (TextUtils.isEmpty(bid) || TextUtils.isEmpty(validate)){
            SystemUtil.showToast(R.string.msg_data_null);
            return;
        }
        // 请求数据
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", Integer.toString(type));
        map.put("validate", validate);
        map.put("bid", bid);
        SignCarrier signCarrier = SignatureUtil.getSignature(map);
        GlApplication.getApiService()
            .onOperateBill(
                signCarrier.getAppId(),
                signCarrier.getNonce(),
                signCarrier.getTimestamp(),
                signCarrier.getSignature(),
                bid,
                Integer.toString(type),
                validate
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<BaseJson<Bill>>() {
                @Override
                public void onCompleted() {
                    Log.d(TAG, "onCompleted()");
                }

                @Override
                public void onError(Throwable e) {
                    onRequestData(true);
                    Log.d(TAG, e.toString());
                }

                @Override
                public void onNext(BaseJson<Bill> billBaseJson) {
                    if (null == billBaseJson) {
                        SystemUtil.showToast(R.string.msg_nullpointer_error);
                        onRequestData(true);
                        return;
                    }
                    // not Correct status
                    if (billBaseJson.getStatus() != Config.STATUS_CODE_OK) {
                        SystemUtil.showToast(billBaseJson.getMessage());
                        onRequestData(true);
                        return;
                    }
                    // delete success, delete cache
                    onDeleteCacheById(billId);
                }
            });
    }

    /**
     * update bill
     * @param i
     */
    private void onUpdateBill(int i){

    }

    /**
     * delete bill cache by bill id
     * @param bid
     */
    private void onDeleteCacheById(long bid){
        try {
            Dao billDao = DatabaseHelper.getDatabaseHelper(getActivity()).getDao(Bill.class);
            billDao.deleteById(bid);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * check user permission
     * @return true, user can operate confirm & delete, else have no permission
     */
    private boolean onCheckPermission(){
        if (PreferencesUtils.getInt(getActivity(), Config.KEY_OF_USER_TYPE, 0) == 1){
            return true;
        }else {
            return false;
        }
    }
}
