package co.lujun.ghouse.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;

import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.Config;
import co.lujun.ghouse.ui.fragment.BillListFragment;
import co.lujun.ghouse.util.IntentUtils;


public class MainActivity extends BaseActivity {

    private FragmentManager fragmentManager;
    private Fragment[] fragments;
    private Fragment curFragment;
    private Toolbar mToolbar;
    private FloatingActionButton fabAddBill;
    private OnLogoutActionReceiver mReceiver;

    private static Dialog mAboutDialog;
    private static View mAboutView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState){
        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        setSupportActionBar(mToolbar);
        fabAddBill = (FloatingActionButton) findViewById(R.id.fab_add_bill);
        fabAddBill.setOnClickListener(
                view -> IntentUtils.startPreviewActivity(MainActivity.this,
                        new Intent(MainActivity.this, AddTodoActivity.class))
        );
        fragmentManager = getSupportFragmentManager();

        // init fragments
        Bundle billBundle = new Bundle();
        billBundle.putInt(Config.KEY_OF_FRAGMENT, Config.BILL_FRAGMENT);
        Fragment billListFragment = new BillListFragment();
        billListFragment.setArguments(billBundle);

        Bundle todoBundle = new Bundle();
        todoBundle.putInt(Config.KEY_OF_FRAGMENT, Config.TODO_FRAGMENT);
        Fragment todoListFragment = new BillListFragment();
        todoListFragment.setArguments(todoBundle);

        mAboutView = LayoutInflater.from(this).inflate(R.layout.view_about, null, false);
        mAboutDialog = new SimpleDialog(this);

        mAboutDialog.applyStyle(R.style.App_Dialog)
                .title(R.string.action_about)
                .positiveAction(R.string.action_confirm)
                .contentView(mAboutView == null ? new View(MainActivity.this) : mAboutView)
                .cancelable(false)
                .positiveActionClickListener(view -> mAboutDialog.dismiss());

        fragments = new Fragment[]{
            billListFragment,
            todoListFragment
        };
        mReceiver = new OnLogoutActionReceiver();
        registerReceiver(mReceiver, new IntentFilter(Config.ACTION_LOGOUT));
        if (savedInstanceState == null){
            fragmentManager.beginTransaction().add(R.id.content_frame, fragments[0]).commit();
            curFragment = fragments[0];
        }
    }

    private void replaceFragment(Fragment from, Fragment to){
        if (from == null || to == null){
            return;
        }
        if (curFragment != to) {
            if (!to.isAdded()) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right)
                        .hide(from).add(R.id.content_frame, to).commit();
            } else {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right)
                        .hide(from).show(to).commit();
            }
            curFragment = to;
        }
    }

    class OnLogoutActionReceiver extends BroadcastReceiver{

        @Override public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bill) {
            replaceFragment(curFragment, fragments[0]);
            setTitle(R.string.app_name);
            curFragment = fragments[0];
        }else if (id == R.id.action_todo) {
            replaceFragment(curFragment, fragments[1]);
            setTitle(R.string.action_todo);
            curFragment = fragments[1];
        }else if (id == R.id.action_center){
            IntentUtils.startPreviewActivity(this, new Intent(this, CenterActivity.class));
        }else if (id == R.id.action_about){

        }

        return super.onOptionsItemSelected(item);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
