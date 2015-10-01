package co.lujun.ghouse.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.lujun.ghouse.R;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2015/8/27.
 */
public class GalleryAdapter extends PagerAdapter {

    private List<String> mImages;
    private OnGalleryViewTapListener mListener;

    public GalleryAdapter(List<String> list){
        mImages = list;
    }

    @Override public int getCount() {
        return mImages.size();
    }

    @Override public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        // add tap listener for when tap screen hide the popupWindow
        /*photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v1) {
                if (mListener != null){
                    mListener.onGalleryViewTap();
                }
            }
        });*/
        Picasso.with(container.getContext())
                .load(mImages.get(position))
                .placeholder(R.drawable.ic_image_grey600_48dp)
                .into(photoView);

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setOnGalleryTapListener(OnGalleryViewTapListener listener){
        this.mListener = listener;
    }

    public interface OnGalleryViewTapListener{
        void onGalleryViewTap();
    }
}
