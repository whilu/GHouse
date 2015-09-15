package co.lujun.ghouse.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.BaseList;
import co.lujun.ghouse.bean.Bill;
import co.lujun.ghouse.bean.Config;
import co.lujun.ghouse.bean.House;
import co.lujun.ghouse.bean.SignCarrier;
import co.lujun.ghouse.ui.BillDetailActivity;
import co.lujun.ghouse.ui.CenterActivity;
import co.lujun.ghouse.ui.adapter.BillAdapter;
import co.lujun.ghouse.util.DatabaseHelper;
import co.lujun.ghouse.util.IntentUtils;
import co.lujun.ghouse.util.MD5;
import co.lujun.ghouse.util.PreferencesUtils;
import co.lujun.ghouse.util.SignatureUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lujun on 2015/7/30.
 */
public class HomeFragment extends Fragment {

    private View mView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private BillAdapter mAdapter;
    private List<Bill> mBills;

    private int current_page = 1;
    private final static String TAG = "HomeFragment";

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

    private void init(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBills = new ArrayList<Bill>();
    }

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
            if(mRefreshLayout.isRefreshing()){
                onRefreshData();
            }
        });

        try {
            mBills = DatabaseHelper.getDatabaseHelper(getActivity()).getDao(Bill.class).queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mAdapter = new BillAdapter(mBills);
        mAdapter.setItemClickListener((View view, int position) -> {
            IntentUtils.startPreviewActivity(getActivity(),
                    new Intent(getActivity(), BillDetailActivity.class));
        });
        mAdapter.setBillOperationListener(new BillAdapter.OnBillOperationListener() {
            @Override
            public void onConfirmBill(int position) {
            }

            @Override
            public void onEditBill(int positin) {
            }

            @Override
            public void onDeleteBill(int position) {
            }
        });
        mAdapter.setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(mAdapter);
        onRefreshData();
    }

    /**
     * 更新数据
     */
    private void onRefreshData(){
        String validate = PreferencesUtils.getString(getActivity(), Config.KEY_OF_VALIDATE);
        String page = current_page + "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", page);
        map.put("validate", validate);
        SignCarrier signCarrier = SignatureUtil.getSignature(map);
        GlApplication.getApiService()
                .onGetBillList(signCarrier.getAppId(),
                        signCarrier.getNonce(),
                        signCarrier.getTimestamp(),
                        signCarrier.getSignature(),
                        page,
                        validate
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
                            Toast.makeText(GlApplication.getContext(),
                                    getString(R.string.msg_request_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        PreferencesUtils.putString(getActivity(), Config.KEY_OF_VALIDATE, billListBaseJson.getValidate());
                        mBills.addAll(billListBaseJson.getData().getLists());
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
}
