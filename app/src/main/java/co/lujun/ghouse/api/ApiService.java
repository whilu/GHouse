package co.lujun.ghouse.api;

import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.Bill;
import co.lujun.ghouse.bean.House;
import co.lujun.ghouse.bean.User;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by lujun on 2015/9/8.
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("/add_record")
    Observable<BaseJson<Bill>> onAddRecord(@Field("data") String data);

    @FormUrlEncoded
    @POST("/get_house_info")
    Observable<BaseJson<House>> onGetHouseInfo(@Field("data") String data);

    @FormUrlEncoded
    @POST("/get_list")
    Observable<BaseJson<Bill>> onGetRecord(@Field("data") String data);

    @FormUrlEncoded
    @POST("/get_todo_list")
    Observable<BaseJson<Bill>> onGetTodoRecord(@Field("data") String data);

    @FormUrlEncoded
    @POST("/login")
    Observable<BaseJson<User>> onLogin(@Field("data") String data);

    @FormUrlEncoded
    @POST("/register")
    Observable<BaseJson<House>> onRegisterHouse(@Field("data") String data);

    @FormUrlEncoded
    @POST("/edit_house")
    Observable<BaseJson<House>> onUpdateHouse(@Field("data") String data);
}
