package co.lujun.ghouse.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.widget.roundedimageview.RoundedImageView;

/**
 * Created by lujun on 2015/9/24.
 */
public class UserViewWindow extends PopupWindow {

    private static View mView;
    private static CardView cvGptv;
    private static Animation showAnim, hideAnim;
    private static TextView tvUserName, tvPhone;
    private static RoundedImageView ivAvatar;
    private static UserViewWindow mInstance;

    private UserViewWindow(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //关联布局文件
        mView = inflater.inflate(R.layout.view_user_detail, null);
        cvGptv = (CardView) mView.findViewById(R.id.cv_gptv);
        tvUserName = (TextView) mView.findViewById(R.id.tv_vud_uname);
        tvPhone = (TextView) mView.findViewById(R.id.tv_vud_phone);
        ivAvatar = (RoundedImageView) mView.findViewById(R.id.iv_vud_avatar);

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
    }

    //触摸在popupWindow上方则取消显示
    private class MenuItemOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int y = (int) event.getY();
            int x = (int) event.getX();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < cvGptv.getTop() || y > cvGptv.getBottom()
                        || x < cvGptv.getLeft() || x > cvGptv.getRight()) {
                    hide();
                }
            }
            return true;
        }
    }

    public static void show(View parent, int gravity, int x, int y) {
        throwIfNotInit();
        mInstance.showAtLocation(parent, gravity, x, y);
        cvGptv.clearAnimation();
        cvGptv.startAnimation(showAnim);
    }

    public static void hide() {
        throwIfNotInit();
        cvGptv.clearAnimation();
        cvGptv.startAnimation(hideAnim);
        hideAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mInstance.dismiss();
            }
        });
    }

    public static void init(Context context) {
        if (mInstance == null) {
            mInstance = new UserViewWindow(context);
        }
    }

    public static void setUName(CharSequence name) {
        throwIfNotInit();
        tvUserName.setText(name);
    }

    public static void setPhone(CharSequence phone) {
        throwIfNotInit();
        tvPhone.setText(phone);
    }

    public static void setAvatarUrl(CharSequence url) {
        throwIfNotInit();
        Picasso.with(GlApplication.getContext())
                .load(url.toString())
                .placeholder(R.drawable.ic_timer_auto_grey600_48dp)
                .into(ivAvatar);
    }

    private static void throwIfNotInit() {
        if (mInstance == null) {
            throw new IllegalStateException("AnnDetailView was't init, please init before use it!");
        }
    }
}
