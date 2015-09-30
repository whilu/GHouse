package co.lujun.ghouse.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;

import com.github.whilu.library.CustomRippleButton;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.RadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.Bill;
import co.lujun.ghouse.bean.Config;
import co.lujun.ghouse.bean.Image;
import co.lujun.ghouse.bean.SignCarrier;
import co.lujun.ghouse.bean.UploadToken;
import co.lujun.ghouse.bean.User;
import co.lujun.ghouse.ui.adapter.GridImageAdapter;
import co.lujun.ghouse.ui.event.BaseSubscriber;
import co.lujun.ghouse.util.DatabaseHelper;
import co.lujun.ghouse.util.ImageUtils;
import co.lujun.ghouse.util.MD5;
import co.lujun.ghouse.util.NetWorkUtils;
import co.lujun.ghouse.util.PreferencesUtils;
import co.lujun.ghouse.util.SignatureUtil;
import co.lujun.ghouse.util.SystemUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lujun on 2015/7/30.
 */
public class AddTodoActivity extends BaseActivity
        implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Toolbar mToolbar;

    private TextInputLayout tilBillContent, tilBillTotal, tilBillCode, tilBillExtra;
    private RadioButton rbBillRmb, rbBillDollar, rbBillOther;
    private CheckBox[] cbCostType;
    private CustomRippleButton btnBillCameraCode;
    private GridView gvPhotos;

    private List<String> mFileNameList;
    private long bid;
    private int costType;
    private int moneyType = 0;
    private int mDoneUploadTotal;
    private String content, total, code, extra;
    private User mUser;
    private GridImageAdapter mPhotosAdapter;
    private List<Uri> mUriList;

    private static UploadManager sUploadMananger = new UploadManager();

    private static final String TAG = "AddTodoActivity";

    @Override protected void onCreate(Bundle savedInstanceState) {
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

        cbCostType = new CheckBox[]{
                (CheckBox) findViewById(R.id.cb_bill_eat),
                (CheckBox) findViewById(R.id.cb_bill_wear),
                (CheckBox) findViewById(R.id.cb_bill_live),
                (CheckBox) findViewById(R.id.cb_bill_travel),
                (CheckBox) findViewById(R.id.cb_bill_play),
                (CheckBox) findViewById(R.id.cb_bill_other)
        };

        gvPhotos = (GridView) findViewById(R.id.gv_activity_add_todo);

        btnBillCameraCode = (CustomRippleButton) findViewById(R.id.btn_bill_camera_code);

        tilBillContent.setHint(getString(R.string.til_hint_bill_content));
        tilBillTotal.setHint(getString(R.string.til_hint_bill_total));
        tilBillCode.setHint(getString(R.string.til_hint_bill_code));
        tilBillExtra.setHint(getString(R.string.til_hint_bill_extra));

        rbBillRmb.setOnCheckedChangeListener(this);
        rbBillDollar.setOnCheckedChangeListener(this);
        rbBillOther.setOnCheckedChangeListener(this);

        for (CheckBox checkBox : cbCostType) {
            checkBox.setOnCheckedChangeListener(this);
        }

        btnBillCameraCode.setOnClickListener(this);
        try{
            List<User> users =
                    DatabaseHelper.getDatabaseHelper(this).getDao(User.class).queryForAll();
            if (users != null && users.size() > 0){
                mUser = users.get(0);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        imagePath = Environment.getExternalStorageDirectory() + Config.APP_IMAGE_PATH;

        mFileNameList = new ArrayList<String>();
        mUriList = new ArrayList<Uri>();
        mPhotosAdapter = new GridImageAdapter(this, mUriList);
        mPhotosAdapter.setOnOperateListener(new GridImageAdapter.OnOperateListener() {
            @Override
            public void onAddImage() {
                onAddPhoto();
            }

            @Override
            public void onDeleteImage(int position) {
                mUriList.remove(position);
                mFileNameList.remove(position);
            }
        });
        gvPhotos.setAdapter(mPhotosAdapter);

        onEditLoadCache();
    }

    @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton instanceof RadioButton && b){
            rbBillRmb.setChecked(rbBillRmb == compoundButton);
            rbBillDollar.setChecked(rbBillDollar == compoundButton);
            rbBillOther.setChecked(rbBillOther == compoundButton);
            if (rbBillRmb == compoundButton){
                moneyType = 0;
            }else if (rbBillDollar == compoundButton){
                moneyType = 1;
            }else if(rbBillOther == compoundButton){
                moneyType = 2;
            }
        }
        if (compoundButton instanceof CheckBox){
            costType = 0;
            for (int i = 0; i < cbCostType.length; i++){
                if (cbCostType[i].isChecked()){
                    costType += Math.pow(2, i);
                }
            }
        }
    }

    private String imagePath;
    private String imageName;
    private Uri photoUri;
    private final static int BITMAP_SCALE = 5;

    @Override public void onClick(View view) {
        if (view instanceof CustomRippleButton){
            startActivityForResult(
                    new Intent(this, CaptureActivity.class), Config.ACTIVITY_REQ_SCAN);
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.ACTIVITY_REQ_SCAN){
            if (data != null){
                tilBillCode.getEditText().setText(data.getStringExtra(Config.KEY_SCAN_CODE_RESULT));
            }
        }else if (requestCode == Config.ACTIVITY_REQ_CAMERA){
            if (Activity.RESULT_OK == resultCode){
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath + imageName);
                if (bitmap == null){
                    return;
                }
                ImageUtils.savePhotoToSDCard(ImageUtils.zoomBitmap(
                        bitmap, bitmap.getWidth() / BITMAP_SCALE,
                        bitmap.getHeight() / BITMAP_SCALE), imagePath, imageName);
                // 刷新图库
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(photoUri);
                this.sendBroadcast(intent);
                bitmap.recycle();

                mUriList.add(photoUri);
                mFileNameList.add(imageName);
                mPhotosAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * when eedit bill then load cache
     */
    private void onEditLoadCache(){
        if (getIntent() != null){
            bid = getIntent().getLongExtra(Config.KEY_OF_BID, 0);
            try{
                List<Bill> bills = DatabaseHelper.getDatabaseHelper(this)
                        .getDao(Bill.class).queryBuilder().where().eq("bid", bid).query();
                if (bills != null && bills.size() > 0){
                    List<Image> images = DatabaseHelper.getDatabaseHelper(this)
                            .getDao(Image.class).queryBuilder().where().eq("bid", bid).query();
                    // bills.get(0)
                    for (int i = 0; i < images.size(); i++) {
                        // TODO check the array length
                        mFileNameList.add(images.get(i).getLarge().split("//")[2]);
                        // TODO check dir exist
                        mUriList.add(Uri.fromFile(new File(new File(imagePath), imageName)));
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * add one photo from camera
     */
    private void onAddPhoto(){
        if (mUser == null || !ImageUtils.checkSDCardAvailable()){
            return;
        }
        try {
            imageName = MD5.getMD5(
                    Long.toString(mUser.getUid()) + mUser.getHouseid()
                            + System.currentTimeMillis()) + ".png";
            File file = new File(imagePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            photoUri = Uri.fromFile(new File(file, imageName));
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, Config.ACTIVITY_REQ_CAMERA);
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    /**
     * add record
     */
    private void onAddRecord(){
        content = tilBillContent.getEditText().getText().toString();
        total = tilBillTotal.getEditText().getText().toString();
        code = tilBillCode.getEditText().getText().toString();
        extra = tilBillExtra.getEditText().getText().toString();

        if (NetWorkUtils.getNetWorkType(this) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            SystemUtil.showToast(R.string.msg_network_disconnect);
            return;
        }
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(total)){
            SystemUtil.showToast(R.string.msg_content_bill_not_null);
            return;
        }
        if (costType <= 0){
            SystemUtil.showToast(R.string.msg_cost_type_not_null);
            return;
        }
        if (!mFileNameList.isEmpty()){// 有图片，先获取token，再上传图片，再发表记录
            onGetUploadToken();
        }else {// 否则，直接发表记录
            onPublishRecord("");
        }
    }

    /**
     * get upload token from server
     */
    private void onGetUploadToken(){
        String validate = PreferencesUtils.getString(this, Config.KEY_OF_VALIDATE);
        Map<String, String> map = new HashMap<String, String>();
        map.put("validate", validate);
        SignCarrier signCarrier = SignatureUtil.getSignature(map);
        GlApplication.getApiService().onGetUploadToken(
                    signCarrier.getAppId(), signCarrier.getNonce(), signCarrier.getTimestamp(),
                    signCarrier.getSignature(), validate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseJson<UploadToken>>() {
                    @Override public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override public void onNext(BaseJson<UploadToken> tokenBaseJson) {
                        super.onNext(tokenBaseJson);
                        UploadToken token;
                        if ((token = tokenBaseJson.getData()) == null) {
                            SystemUtil.showToast(R.string.msg_nullpointer_error);
                            return;
                        }
                        onUploadImages(token.getToken());
                    }
                });
    }

    /**
     * upload images to QiNiu
     */
    private void onUploadImages(String token){
        mDoneUploadTotal = 0;
        StringBuilder builder = new StringBuilder();
        for (String name : mFileNameList) {
            String data = imagePath + name;
            String key = name;
            sUploadMananger.put(data, key, token, (s, responseInfo, jsonObject) -> {
                try{
                    if (jsonObject != null && jsonObject.getInt("status") == Config.STATUS_CODE_OK){
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        if (jsonData != null){
                            builder.append(jsonData.get("file_name") + ",");
                            mDoneUploadTotal++;
                        }
                    }
                    if (mDoneUploadTotal == mFileNameList.size()){
                        onPublishRecord(builder.substring(0, builder.length() - 1).toString());
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }, new UploadOptions(null, null, false, (s, percent) -> {
                Log.d(TAG, s + ":" + percent);
            }, () -> false));
        }
    }

    /**
     * publish record
     */
    private void onPublishRecord(String photos){
        String validate = PreferencesUtils.getString(this, Config.KEY_OF_VALIDATE);
        String content = tilBillContent.getEditText().toString();
        String total = tilBillTotal.getEditText().toString();
        String qcode = tilBillCode.getEditText().toString();
        String remark = tilBillExtra.getEditText().toString();
        Map<String, String> map = new HashMap<String, String>();
        map.put("content", content);
        map.put("total", total);
        map.put("qcode", qcode);
        map.put("remark", remark);
        map.put("photos", photos);
        map.put("bid", Long.toString(bid));
        map.put("type", Integer.toString(costType));
        map.put("mtype", Integer.toString(moneyType));

        SignCarrier signCarrier = SignatureUtil.getSignature(map);
        GlApplication.getApiService().onEditRecord(
                    signCarrier.getAppId(), signCarrier.getNonce(), signCarrier.getTimestamp(),
                    signCarrier.getSignature(), validate, content, total, qcode, remark, photos,
                    Long.toString(bid), Integer.toString(costType), Integer.toString(moneyType))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseJson<Bill>>() {
                    @Override public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override public void onNext(BaseJson<Bill> billBaseJson) {
                        super.onNext(billBaseJson);
                        // TODO publish success
                    }
                });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_todo, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }else if (item.getItemId() == R.id.action_confirm_add_todo){
            onAddRecord();
        }
        return super.onOptionsItemSelected(item);
    }
}
