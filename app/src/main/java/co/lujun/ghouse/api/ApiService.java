package co.lujun.ghouse.api;

import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.BaseList;
import co.lujun.ghouse.bean.Bill;
import co.lujun.ghouse.bean.House;
import co.lujun.ghouse.bean.UploadToken;
import co.lujun.ghouse.bean.User;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by lujun on 2015/9/8.
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("/login")
    Observable<BaseJson<User>> onLogin(
        @Query("appid") String appid,
        @Query("nonce") String nonce,
        @Query("timestamp") String timestamp,
        @Query("signature") String signature,
        @Field("username") String username,
        @Field("password") String password,
        @Field("usertype") String usertype
    );

    @FormUrlEncoded
    @POST("/register")
    Observable<BaseJson<House>> onRegisterHouse(
        @Query("appid") String appid,
        @Query("nonce") String nonce,
        @Query("timestamp") String timestamp,
        @Query("signature") String signature,
        @Field("username") String username,
        @Field("password") String password,
        @Field("phone") String phone,
        @Field("houseaddress") String houseaddress,
        @Field("houseinfo") String houseinfo
    );

    @FormUrlEncoded
    @POST("/get_list")
    Observable<BaseJson<BaseList<Bill>>> onGetBillList(
        @Query("appid") String appid,
        @Query("nonce") String nonce,
        @Query("timestamp") String timestamp,
        @Query("signature") String signature,
        @Field("page") String page,
        @Field("validate") String validate,
        @Field("type") String type
    );

    @FormUrlEncoded
    @POST("/get_house_info")
    Observable<BaseJson<House>> onGetHouseData(
        @Query("appid") String appid,
        @Query("nonce") String nonce,
        @Query("timestamp") String timestamp,
        @Query("signature") String signature,
        @Field("validate") String validate
    );

    @FormUrlEncoded
    @POST("/get_user_info")
    Observable<BaseJson<User>> onGetUserData(
        @Query("appid") String appid,
        @Query("nonce") String nonce,
        @Query("timestamp") String timestamp,
        @Query("signature") String signature,
        @Field("validate") String validate
    );

    @FormUrlEncoded
    @POST("/edit_user_info")
    Observable<BaseJson<User>> onEditUserData(
        @Query("appid") String appid,
        @Query("nonce") String nonce,
        @Query("timestamp") String timestamp,
        @Query("signature") String signature,
        @Field("type") String type,// 0-phone, 1-password
        @Field("value") String value,
        @Field("value1") String value1,// when update pwd need as old pwd
        @Field("validate") String validate
    );

    @FormUrlEncoded
    @POST("/add_member")
    Observable<BaseJson<User>> onAddMember(
        @Query("appid") String appid,
        @Query("nonce") String nonce,
        @Query("timestamp") String timestamp,
        @Query("signature") String signature,
        @Field("username") String username,
        @Field("password") String password,
        @Field("validate") String validate
    );

    @FormUrlEncoded
    @POST("/edit_house_info")
    Observable<BaseJson<House>> onEditHouse(
        @Query("appid") String appid,
        @Query("nonce") String nonce,
        @Query("timestamp") String timestamp,
        @Query("signature") String signature,
        @Field("type") String type,// 0-money, 1- address, 2-intro
        @Field("value") String value,
        @Field("value1") String value1,
        @Field("remark") String remark,
        @Field("validate") String validate
    );

    @FormUrlEncoded
    @POST("/operate_record")
    Observable<BaseJson<Bill>> onOperateBill(
        @Query("appid") String appid,
        @Query("nonce") String nonce,
        @Query("timestamp") String timestamp,
        @Query("signature") String signature,
        @Field("bid") String bid,
        @Field("type") String type,// 0-删除， 1-确认
        @Field("validate") String validate
    );

    @FormUrlEncoded
    @POST("/qiniu_upload_token")
    Observable<BaseJson<UploadToken>> onGetUploadToken(
        @Query("appid") String appid,
        @Query("nonce") String nonce,
        @Query("timestamp") String timestamp,
        @Query("signature") String signature,
        @Field("validate") String validate
    );

    @FormUrlEncoded
    @POST("/edit_record")
    Observable<BaseJson<Bill>> onEditRecord(
        @Query("appid") String appid,
        @Query("nonce") String nonce,
        @Query("timestamp") String timestamp,
        @Query("signature") String signature,
        @Field("validate") String validate,
        @Field("content") String content,
        @Field("total") String total,
        @Field("qcode") String qcode,
        @Field("remark") String remark,
        @Field("photos") String photos,
        @Field("bid") String bid,
        @Field("type") String type,
        @Field("mtype") String mtype
    );
}
