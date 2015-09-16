package co.lujun.ghouse.api;

import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.BaseList;
import co.lujun.ghouse.bean.Bill;
import co.lujun.ghouse.bean.House;
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
}
