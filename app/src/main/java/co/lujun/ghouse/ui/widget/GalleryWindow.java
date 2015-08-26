package co.lujun.ghouse.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.lujun.ghouse.R;

/**
 * Created by lujun on 2015/8/26.
 */
public class GalleryWindow extends PopupWindow {

    private GalleryViewPager mViewPager;
    private TextView tvIndicator;
    private View mView;
    private RelativeLayout rlGptv;
    private Animation showAnim, hideAnim;

    public GalleryWindow(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.view_gallery, null);
        mViewPager = (GalleryViewPager) mView.findViewById(R.id.gvp_vg_gallery);
        tvIndicator = (TextView) mView.findViewById(R.id.tv_vg_indicator);
        rlGptv = (RelativeLayout) mView.findViewById(R.id.rl_gptv);

        this.setContentView(mView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        showAnim = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in);
        hideAnim = AnimationUtils.loadAnimation(context, R.anim.push_bottom_out);
        ColorDrawable bgColor = new ColorDrawable(0x02feeeee);
        this.setBackgroundDrawable(bgColor);
        mView.setOnTouchListener(new MenuItemOnTouchListener());
    }

    private class MenuItemOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int height = mView.findViewById(R.id.rl_gptv).getTop();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height) {
                    hide();
                }
            }
            return true;
        }
    }

    public void show(View parent, int x, int y){
        this.showAsDropDown(parent, x, y);
        rlGptv.clearAnimation();
        rlGptv.startAnimation(showAnim);
    }

    public void hide(){
        rlGptv.clearAnimation();
        rlGptv.startAnimation(hideAnim);
        hideAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }
        });
    }
}
