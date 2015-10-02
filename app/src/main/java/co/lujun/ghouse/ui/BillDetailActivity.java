package co.lujun.ghouse.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.Bill;
import co.lujun.ghouse.bean.Config;
import co.lujun.ghouse.bean.Image;
import co.lujun.ghouse.ui.adapter.InvoiceImgAdapter;
import co.lujun.ghouse.ui.widget.GalleryWindow;
import co.lujun.ghouse.util.DatabaseHelper;

/**
 * Created by lujun on 2015/8/17.
 */
public class BillDetailActivity extends BaseActivity {

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    
    private TextView tvType, tv7securitycode, tvAddTime, tvLatestTime, tvContent, tvTotal,
            tvAddUser, tvConfUser, tvRemark, tvConfYes, tvConfNo, tvCode;

    private InvoiceImgAdapter mAdapter;
    private GalleryWindow mGalleryWindow;
    private List<String> mInvoiceImgList;

    private static final String TAG = "BillDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        init();
    }

    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.tb_add_billdetail);
        setTitle(getString(R.string.action_bill_view));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_bd_invoice_images);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        tvType = (TextView) findViewById(R.id.tv_bd_bi_type);
        tv7securitycode  = (TextView) findViewById(R.id.tv_bd_bi_7securitycode);
        tvAddTime = (TextView) findViewById(R.id.tv_bd_bi_add_time);
        tvLatestTime = (TextView) findViewById(R.id.tv_bd_bi_latesttime);
        tvContent = (TextView) findViewById(R.id.tv_bd_bi_content);
        tvTotal = (TextView) findViewById(R.id.tv_bd_bi_total);
        tvAddUser = (TextView) findViewById(R.id.tv_bd_bi_add_user);
        tvConfUser = (TextView) findViewById(R.id.tv_bd_bi_conf_user);
        tvRemark = (TextView) findViewById(R.id.tv_bd_bi_remark);
        tvConfYes = (TextView) findViewById(R.id.tv_bd_bi_conf_yes);
        tvConfNo = (TextView) findViewById(R.id.tv_bd_bi_conf_no);
        tvCode = (TextView) findViewById(R.id.tv_bd_bi_code);

        mInvoiceImgList = new ArrayList<String>();
        mAdapter = new InvoiceImgAdapter(mInvoiceImgList);
        mAdapter.setImageClickListener(position -> {
            if (mGalleryWindow != null) {
                if (!mGalleryWindow.isShowing()) {
                    mGalleryWindow.show(BillDetailActivity.this.findViewById(R.id.tv_popup_parent),
                            0, 0, position);
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        if (getIntent() != null){
            long bid = getIntent().getLongExtra(Config.KEY_OF_BID, 0);
            try{
                List<Bill> bills = DatabaseHelper.getDatabaseHelper(this)
                        .getDao(Bill.class).queryBuilder().where().eq("bid", bid).query();
                if (bills != null && bills.size() > 0){
                    onUpdateUI(bills.get(0));
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * update UI
     * @param bill
     */
    private void onUpdateUI(Bill bill){
        if (bill == null){
            return;
        }

        String[] types = GlApplication.getContext()
                .getResources().getStringArray(R.array.bill_type);
        String[] bgDrawableTypes = GlApplication.getContext()
                .getResources().getStringArray(R.array.bill_drawable_type);
        String type, bgDrawableType;
        int tmpCostType = (int)(Math.log((double)bill.getType_id()) / Math.log(2d));
        if (tmpCostType >= 0 && tmpCostType < types.length){
            type = types[tmpCostType];
            bgDrawableType = bgDrawableTypes[tmpCostType];
        }else {
            type = types[5];
            bgDrawableType = bgDrawableTypes[5];
        }
        try {
            Field field = R.drawable.class.getField(bgDrawableType);
            int j = field.getInt(new R.drawable());
            tvType.setBackgroundResource(j);
        }catch (Exception e){
            e.printStackTrace();
        }
        tvType.setText(type);

        String[] moneyFlags = GlApplication.getContext()
                .getResources().getStringArray(R.array.money_flag);
        String moneyFlag;
        if (bill.getMoney_flag() >= 0 && bill.getMoney_flag() < moneyFlags.length){
            moneyFlag = moneyFlags[bill.getMoney_flag()];
        }else {
            moneyFlag = moneyFlags[0];
        }
        tvTotal.setText(moneyFlag + (bill.getTotal() == 0 ? 0 : bill.getTotal()));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tvAddTime.setText(format.format(bill.getCreate_time() * 1000));
        tvLatestTime.setText(format.format(bill.getConfirm_time() * 1000));

        if (bill.getSecurity_code() != null
                && bill.getSecurity_code().length() > 7){
            tv7securitycode.setText(bill.getSecurity_code().substring(0, 7));
        }
                
        tvContent.setText(bill.getContent() == "" ? getString(R.string.til_hint_no_text) : bill.getContent());
        tvAddUser.setText(bill.getAdd_user() == "" ? getString(R.string.til_hint_no_text) : bill.getAdd_user());

        if (bill.getConfirm_status() == 1){// 确认
            tvConfYes.setVisibility(View.VISIBLE);
            tvConfUser.setText(bill.getConf_user() == "" ? getString(R.string.til_hint_no_text) : bill.getConf_user());
        }else {
            tvConfNo.setVisibility(View.VISIBLE);
        }
        tvRemark.setText(bill.getRemark() == "" ? getString(R.string.til_hint_no_text) : bill.getRemark());
        tvCode.setText(bill.getQcode() == "" ? getString(R.string.til_hint_no_text) : bill.getQcode());
        
        mGalleryWindow = new GalleryWindow(this);
        Collection<Image> images = bill.getPhotos();
        Iterator<Image> iterator = images.iterator();
        while (iterator.hasNext()){
            mInvoiceImgList.add(iterator.next().getLarge());
        }
        mGalleryWindow.setImages(mInvoiceImgList);
        mAdapter.notifyDataSetChanged();
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
