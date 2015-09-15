package co.lujun.ghouse.ui.widget;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Created by lujun on 2015/9/15.
 */
public class LoadingWindow extends PopupWindow {
    private View mView;

    public LoadingWindow(View view){
        super(view);
        this.mView = view;
        this.setContentView(mView);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable());
    }
}
