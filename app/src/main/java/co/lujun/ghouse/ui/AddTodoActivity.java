package co.lujun.ghouse.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.widget.SlidingActivity;

/**
 * Created by lujun on 2015/7/30.
 */
public class AddTodoActivity extends SlidingActivity {

    private Toolbar mToolbar;

    private TextInputLayout tilBillContent, tilBillTotal, tilBillCode, tilBillExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        init();
    }

    private void init(){
        mToolbar = (Toolbar) findViewById(R.id.tb_add_todo);
        setTitle(getString(R.string.action_add_todo));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tilBillContent = (TextInputLayout) findViewById(R.id.til_bill_content);
        tilBillTotal = (TextInputLayout) findViewById(R.id.til_bill_total);
        tilBillCode = (TextInputLayout) findViewById(R.id.til_bill_code);
        tilBillExtra = (TextInputLayout) findViewById(R.id.til_bill_extra);

        tilBillContent.setHint(getString(R.string.til_hint_bill_content));
        tilBillTotal.setHint(getString(R.string.til_hint_bill_total));
        tilBillCode.setHint(getString(R.string.til_hint_bill_code));
        tilBillExtra.setHint(getString(R.string.til_hint_bill_extra));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }else if (item.getItemId() == R.id.action_confirm_add_todo){
            Toast.makeText(GlApplication.getContext(), "action_confirm_add_todo", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
