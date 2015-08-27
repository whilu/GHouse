package co.lujun.ghouse.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.adapter.InvoiceImgAdapter;
import co.lujun.ghouse.ui.widget.GalleryWindow;
import co.lujun.ghouse.ui.widget.SlidingActivity;

/**
 * Created by lujun on 2015/8/17.
 */
public class BillDetailActivity extends SlidingActivity {

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private InvoiceImgAdapter mAdapter;
    private GalleryWindow mGalleryWindow;

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

        mGalleryWindow = new GalleryWindow(this);
        List<String> mlist = new ArrayList<String>();
        mlist.add("http://s.ledandan.com/image/4/f/5/4f562f3169325678cf2e844d29beb999.png");
        mlist.add("http://s.ledandan.com/image/0/2/4/0248805c50e3e76e8f5dffd874f37bea.png");
        mlist.add("http://s.ledandan.com/image/d/6/8/d680d2453443d24d62674e67cf03b4fb.png");
        mGalleryWindow.setImages(mlist);

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 8; i++){
            list.add(i + "");
        }
        mAdapter = new InvoiceImgAdapter(list);
        mAdapter.setImageClickListener(new InvoiceImgAdapter.ImageClickListener() {
            @Override
            public void onClick(int position) {
                if (mGalleryWindow != null){
                    if (!mGalleryWindow.isShowing()){
                        mGalleryWindow.show(
                                BillDetailActivity.this.findViewById(R.id.tv_popup_parent),
                                0,
                                0,
                                position
                        );
                    }
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
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
