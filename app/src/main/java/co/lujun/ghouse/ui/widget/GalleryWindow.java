package co.lujun.ghouse.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.adapter.GalleryAdapter;

/**
 * Created by lujun on 2015/8/26.
 */
public class GalleryWindow extends PopupWindow {

    private GalleryViewPager mViewPager;
    private TextView tvIndicator;
    private View mView;
    private RelativeLayout rlGptv;
    private Animation showAnim, hideAnim;
    private GalleryAdapter mImagesAdapter;

    private int imagesCount;// 账单图片数量

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
        showAnim = AnimationUtils.loadAnimation(context, R.anim.gallery_in);
        hideAnim = AnimationUtils.loadAnimation(context, R.anim.gallery_out);
        ColorDrawable bgColor = new ColorDrawable(0x02feeeee);
        this.setBackgroundDrawable(bgColor);
    }

    /**
     * show popupWindow
     * @param parent
     * @param x
     * @param y
     * @param position 点击的图片的位置
     */
    public void show(View parent, int x, int y, int position){
        if (position >=0 && position < imagesCount){
            //根据点击的位置，显示对应的大图
            mViewPager.setCurrentItem(position, true);
        }
        this.showAsDropDown(parent, x, y);
        rlGptv.clearAnimation();
        rlGptv.startAnimation(showAnim);
    }

    /**
     * hide popupWindow
     */
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

    /**
     * 设置images url
     * @param images
     */
    public void setImages(List<String> images){
        images = images != null ? images : new ArrayList<String>();
        imagesCount = images.size();
        tvIndicator.setText("1/" + imagesCount);
        mImagesAdapter = new GalleryAdapter(images);
        mImagesAdapter.setOnGalleryTapListener(new GalleryAdapter.OnGalleryViewTapListener() {
            @Override
            public void onGalleryViewTap() {
                hide();
            }
        });
        mViewPager.setAdapter(mImagesAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                tvIndicator.setText((position + 1) + "/" + imagesCount);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
}
