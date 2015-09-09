package co.lujun.ghouse.api;

import co.lujun.ghouse.bean.BaseJson;
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
    @POST("/add_record")
    Observable<BaseJson<Bill>> onAddRecord(
            @Query("nonce") String nonce,
            @Query("timestamp") String timestamp,
            @Query("signature") String signature
    );

    @FormUrlEncoded
    @POST("/get_house_info")
    Observable<BaseJson<House>> onGetHouseInfo(
            @Query("nonce") String nonce,
            @Query("timestamp") String timestamp,
            @Query("signature") String signature
    );

    @FormUrlEncoded
    @POST("/get_list")
    Observable<BaseJson<Bill>> onGetRecord(
            @Query("nonce") String nonce,
            @Query("timestamp") String timestamp,
            @Query("signature") String signature
    );

    @FormUrlEncoded
    @POST("/get_todo_list")
    Observable<BaseJson<Bill>> onGetTodoRecord(
            @Query("nonce") String nonce,
            @Query("timestamp") String timestamp,
            @Query("signature") String signature
    );

    @FormUrlEncoded
    @POST("/login")
    Observable<BaseJson<User>> onLogin(
            @Query("nonce") String nonce,
            @Query("timestamp") String timestamp,
            @Query("signature") String signature
    );

    @FormUrlEncoded
    @POST("/register")
    Observable<BaseJson<House>> onRegisterHouse(
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
    @POST("/edit_house")
    Observable<BaseJson<House>> onUpdateHouse(
            @Query("nonce") String nonce,
            @Query("timestamp") String timestamp,
            @Query("signature") String signature
    );
}
