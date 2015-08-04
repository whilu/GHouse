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

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.List;

import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.adapter.BillAdapter;

/**
 * Created by lujun on 2015/7/30.
 */
public class HomeFragment extends Fragment {

    private View mView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private BillAdapter mAdapter;

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

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 10; i++){
            list.add(i + "");
        }
        mAdapter = new BillAdapter(getActivity(), list);
        /*mAdapter.setItemClickListener(new BillAdapter.BillItemViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });*/
        mAdapter.setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(mAdapter);
    }
}
