package co.lujun.ghouse.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.fragment.HomeFragment;
import co.lujun.ghouse.ui.fragment.TodoListFragment;
import co.lujun.ghouse.util.IntentUtils;


public class MainActivity extends ActionBarActivity {

    private FragmentManager fragmentManager;
    private Fragment[] fragments;
    private Fragment curFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState){
        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        setSupportActionBar(mToolbar);
        fragmentManager = getSupportFragmentManager();
        fragments = new Fragment[]{
            new HomeFragment(),
            new TodoListFragment()
        };
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
                fragmentManager.beginTransaction().hide(from).add(R.id.content_frame, to).commit();
            } else {
                fragmentManager.beginTransaction().hide(from).show(to).commit();
            }
            curFragment = to;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bill) {
            replaceFragment(curFragment, fragments[0]);
            curFragment = fragments[0];
        }else if (id == R.id.action_todo) {
            replaceFragment(curFragment, fragments[1]);
            curFragment = fragments[1];
        }else if (id == R.id.action_add_todo){
            IntentUtils.startPreviewActivity(this, new Intent(this, AddTodoActivity.class));
        }else if (id == R.id.action_center){
            IntentUtils.startPreviewActivity(this, new Intent(this, CenterActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
