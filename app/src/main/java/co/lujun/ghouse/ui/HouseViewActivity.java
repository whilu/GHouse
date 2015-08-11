package co.lujun.ghouse.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageButton btnAddMoney;
    private LinearLayoutManager mLayoutManager;

    private MemberAdapter mAdapter;

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
        btnAddMoney = (ImageButton) findViewById(R.id.btn_house_add_money);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_house_member);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 3; i++){
            list.add(i + "");
        }
        mAdapter = new MemberAdapter(list);
        mRecyclerView.setAdapter(mAdapter);

        btnAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HouseViewActivity.this, "dsd", Toast.LENGTH_SHORT).show();
            }
        });
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
