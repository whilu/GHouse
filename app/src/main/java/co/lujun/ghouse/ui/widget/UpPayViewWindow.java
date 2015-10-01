package co.lujun.ghouse.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.BaseList;
import co.lujun.ghouse.bean.Config;
import co.lujun.ghouse.bean.SignCarrier;
import co.lujun.ghouse.bean.UpPay;
import co.lujun.ghouse.ui.adapter.UpPayAdapter;
import co.lujun.ghouse.ui.event.BaseSubscriber;
import co.lujun.ghouse.ui.listener.CustomAnimationListener;
import co.lujun.ghouse.util.PreferencesUtils;
import co.lujun.ghouse.util.SignatureUtil;
import co.lujun.ghouse.util.SystemUtil;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by lujun on 2015/9/24.
 */
public class UpPayViewWindow extends PopupWindow {

    private View mView;
    private CardView cvUpd;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Animation showAnim, hideAnim;
    private UpPayAdapter mAdapter;
    private boolean hasMoreData = true;
    private List<UpPay> mList;
    private int current_page;
    private Context mContext;

    public UpPayViewWindow(Context context) {
        mContext = context;
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //关联布局文件
        mView = inflater.inflate(R.layout.view_up_pay_detail, null);
        cvUpd = (CardView) mView.findViewById(R.id.cv_upd);
        mRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.srl_upd);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_up_pay_detail);

        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow弹出窗体动画效果
        showAnim = AnimationUtils.loadAnimation(context, R.anim.anim_in);
        showAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        hideAnim = AnimationUtils.loadAnimation(context, R.anim.anim_out);
        hideAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable bgColor = new ColorDrawable(0x60000000);
        //设置PopupWindow弹出窗体的背景
        this.setBackgroundDrawable(bgColor);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mView.setOnTouchListener(new MenuItemOnTouchListener());

        //
        initExtraView();
    }

    private void initExtraView(){
        mList = new ArrayList<UpPay>();
        mAdapter = new UpPayAdapter(mList);
        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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
                        if (lastVisibleItem - firstVisibleItem < totalItemCount && hasMoreData) {
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
                    hasMoreData = false;
                }
            }
        });
        mRefreshLayout.setOnRefreshListener(() -> onRequestData(true));
    }

    //触摸在popupWindow上方则取消显示
    private class MenuItemOnTouchListener implements View.OnTouchListener {

        @Override public boolean onTouch(View v, MotionEvent event) {
            int y = (int) event.getY();
            int x = (int) event.getX();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < cvUpd.getTop() || y > cvUpd.getBottom()
                        || x < cvUpd.getLeft() || x > cvUpd.getRight()) {
                    hide();
                }
            }
            return true;
        }
    }

    public void show(View parent, int gravity, int x, int y) {
        if (mList.isEmpty()){
            mRefreshLayout.post(() -> {
                mRefreshLayout.setRefreshing(true);
                onRequestData(true);
            });
        }
        this.showAtLocation(parent, gravity, x, y);
        cvUpd.clearAnimation();
        cvUpd.startAnimation(showAnim);
    }

    public void hide() {
        if (mRefreshLayout.isRefreshing()){
            mRefreshLayout.setRefreshing(false);
        }
        cvUpd.clearAnimation();
        cvUpd.startAnimation(hideAnim);
        hideAnim.setAnimationListener(new CustomAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }
        });
    }

    private void onRequestData(boolean isRefresh){
        if (isRefresh) {
            hasMoreData = true;
            current_page = 1;
        }
        String validate = PreferencesUtils.getString(mContext, Config.KEY_OF_VALIDATE);
        String page = current_page + "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", page);
        map.put("validate", validate);
        SignCarrier signCarrier = SignatureUtil.getSignature(map);
        GlApplication.getApiService().onGetUpPayList(
                signCarrier.getAppId(), signCarrier.getNonce(), signCarrier.getTimestamp(),
                signCarrier.getSignature(), validate, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseJson<BaseList<UpPay>>>() {

                    @Override public void onError(Throwable e) {
                        super.onError(e);
                        if (mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override public void onNext(BaseJson<BaseList<UpPay>> uppayListBaseJson) {
                        if (mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(false);
                        }
                        super.onNext(uppayListBaseJson);
                        if (uppayListBaseJson.getData().getLists() == null) {
                            SystemUtil.showToast(R.string.msg_request_error);
                            return;
                        }
                        if(uppayListBaseJson.getData().getCount() == 0){// 没有任何数据
                            if(isRefresh){
                                SystemUtil.showToast(R.string.msg_have_no_data);
                            }
                            return;
                        }
                        if (uppayListBaseJson.getData().getLists().size() == 0) {// 加载更多没有更多数据
                            if (!isRefresh){
                                hasMoreData = false;
                                SystemUtil.showToast(R.string.msg_no_more_data);
                            }
                            return;
                        }
                        current_page = uppayListBaseJson.getData().getCurrent_page() + 1;
                        if (isRefresh){
                            mList.clear();
                        }
                        mList.addAll(uppayListBaseJson.getData().getLists());
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
}
