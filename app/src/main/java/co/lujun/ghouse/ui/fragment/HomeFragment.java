package co.lujun.ghouse.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.Bill;
import co.lujun.ghouse.ui.adapter.BillAdapter;
import co.lujun.ghouse.util.MD5;

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

        //test
        for (int i = 0; i < 10; i++){
            Bill bill = new Bill();
            bill.setType((int) (Math.random() * 10 / 2));
            bill.setSummary("晚上合伙请客吃饭+唱歌+打桌球家吃烧烤");
            bill.setTotal((float) ((Math.random() * 10000)));
            bill.setTime(1438832421);
            try {
                bill.setSecurityCode(MD5.getMD5(String.valueOf(Math.random() * 10)));
            }catch (NoSuchAlgorithmException e){
                e.printStackTrace();
            }
            if (i % 3 == 0){
                bill.setInvoice(new String[]{});
            }else if (i % 3 == 1){
                bill.setInvoice(new String[]{""});
            }else {
                bill.setInvoice(new String[]{"", ""});
            }

            mBills.add(bill);
        }
        //
        mAdapter = new BillAdapter(mBills);
        mAdapter.setItemClickListener(new BillAdapter.BillItemViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), mBills.get(position).getTotal() + "", Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(mAdapter);
    }
}
