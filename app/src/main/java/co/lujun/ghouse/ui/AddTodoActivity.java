package co.lujun.ghouse.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.whilu.library.CustomRippleButton;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.RadioButton;

import java.io.File;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.Config;
import co.lujun.ghouse.ui.widget.SlidingActivity;
import co.lujun.ghouse.util.ImageUtils;
import co.lujun.ghouse.util.IntentUtils;

/**
 * Created by lujun on 2015/7/30.
 */
public class AddTodoActivity extends SlidingActivity
        implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Toolbar mToolbar;

    private TextInputLayout tilBillContent, tilBillTotal, tilBillCode, tilBillExtra;
    private RadioButton rbBillRmb, rbBillDollar, rbBillOther;
    private CheckBox cbBillEat, cbBillWear, cbBillLive, cbBillTravel, cbBillPlay, cbBillOther;
    private ImageView[] ivBillImages;
    private CustomRippleButton btnBillCameraCode;

    private int billImageViewId;
    private final static int BITMAP_SCALE = 5;

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

        rbBillRmb = (RadioButton) findViewById(R.id.rb_bill_rmb);
        rbBillDollar = (RadioButton) findViewById(R.id.rb_bill_dollar);
        rbBillOther = (RadioButton) findViewById(R.id.rb_bill_other);

        cbBillEat = (CheckBox) findViewById(R.id.cb_bill_eat);
        cbBillWear = (CheckBox) findViewById(R.id.cb_bill_wear);
        cbBillLive = (CheckBox) findViewById(R.id.cb_bill_live);
        cbBillTravel = (CheckBox) findViewById(R.id.cb_bill_travel);
        cbBillPlay = (CheckBox) findViewById(R.id.cb_bill_play);
        cbBillOther = (CheckBox) findViewById(R.id.cb_bill_other);

        ivBillImages = new ImageView[]{
            (ImageView) findViewById(R.id.iv_bill_image1),
            (ImageView) findViewById(R.id.iv_bill_image2),
            (ImageView) findViewById(R.id.iv_bill_image3),
            (ImageView) findViewById(R.id.iv_bill_image4),
            (ImageView) findViewById(R.id.iv_bill_image5),
            (ImageView) findViewById(R.id.iv_bill_image6)
        };

        btnBillCameraCode = (CustomRippleButton) findViewById(R.id.btn_bill_camera_code);

        tilBillContent.setHint(getString(R.string.til_hint_bill_content));
        tilBillTotal.setHint(getString(R.string.til_hint_bill_total));
        tilBillCode.setHint(getString(R.string.til_hint_bill_code));
        tilBillExtra.setHint(getString(R.string.til_hint_bill_extra));

        rbBillRmb.setOnCheckedChangeListener(this);
        rbBillDollar.setOnCheckedChangeListener(this);
        rbBillOther.setOnCheckedChangeListener(this);

        cbBillEat.setOnCheckedChangeListener(this);
        cbBillWear.setOnCheckedChangeListener(this);
        cbBillLive.setOnCheckedChangeListener(this);
        cbBillTravel.setOnCheckedChangeListener(this);
        cbBillPlay.setOnCheckedChangeListener(this);
        cbBillOther.setOnCheckedChangeListener(this);

        for (ImageView imageView: ivBillImages) {
            imageView.setOnClickListener(this);
        }
        btnBillCameraCode.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton instanceof RadioButton && b){
            rbBillRmb.setChecked(rbBillRmb == compoundButton);
            rbBillDollar.setChecked(rbBillDollar == compoundButton);
            rbBillOther.setChecked(rbBillOther == compoundButton);
        }
        if (compoundButton instanceof CheckBox){

        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof ImageView){
            billImageViewId = view.getId();
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), Config.ACTIVITY_REQ_CAMERA);
        }else if (view instanceof CustomRippleButton){
            startActivityForResult(new Intent(this, CaptureActivity.class), Config.ACTIVITY_REQ_SCAN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null){
            return;
        }
        if (requestCode == Config.ACTIVITY_REQ_SCAN){
            tilBillCode.getEditText().setText(data.getStringExtra(Config.KEY_SCAN_CODE_RESULT));
        }else if (requestCode == Config.ACTIVITY_REQ_CAMERA){
            if (Activity.RESULT_OK == resultCode){
                Bitmap bitmap = (Bitmap)(data.getExtras().get("data"));
                if (bitmap == null){
                    return;
                }
                StringBuilder tmpPhotoName = new StringBuilder();
                String dotpng = ".png";
                switch (billImageViewId){
                    case R.id.iv_bill_image1:
                        ivBillImages[0].setImageBitmap(bitmap);
                        ivBillImages[1].setVisibility(View.VISIBLE);
                        tmpPhotoName.append(R.id.iv_bill_image1);
                        break;
                    case R.id.iv_bill_image2:
                        ivBillImages[1].setImageBitmap(bitmap);
                        ivBillImages[2].setVisibility(View.VISIBLE);
                        tmpPhotoName.append(R.id.iv_bill_image2);
                        break;
                    case R.id.iv_bill_image3:
                        ivBillImages[2].setImageBitmap(bitmap);
                        ivBillImages[3].setVisibility(View.VISIBLE);
                        tmpPhotoName.append(R.id.iv_bill_image3);
                        break;
                    case R.id.iv_bill_image4:
                        ivBillImages[3].setImageBitmap(bitmap);
                        ivBillImages[4].setVisibility(View.VISIBLE);
                        tmpPhotoName.append(R.id.iv_bill_image4);
                        break;
                    case R.id.iv_bill_image5:
                        ivBillImages[4].setImageBitmap(bitmap);
                        ivBillImages[5].setVisibility(View.VISIBLE);
                        tmpPhotoName.append(R.id.iv_bill_image5);
                        break;
                    case R.id.iv_bill_image6:
                        ivBillImages[5].setImageBitmap(bitmap);
                        tmpPhotoName.append(R.id.iv_bill_image6);
                        break;
                    default:
                        break;
                }
                tmpPhotoName.append(dotpng);
                ImageUtils.savePhotoToSDCard(
                        ImageUtils.zoomBitmap(
                                bitmap,
                                bitmap.getWidth() / BITMAP_SCALE,
                                bitmap.getHeight() / BITMAP_SCALE
                        ),
                        Environment.getExternalStorageDirectory() + Config.APP_IMAGE_PATH,
                        tmpPhotoName.toString());
                Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                        + Config.APP_IMAGE_PATH + "/" + tmpPhotoName));
                // 刷新图库
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(uri);
                this.sendBroadcast(intent);
                bitmap.recycle();
            }
        }
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
