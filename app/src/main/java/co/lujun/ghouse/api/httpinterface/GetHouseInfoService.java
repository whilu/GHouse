package co.lujun.ghouse.api.httpinterface;

import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.House;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by lujun on 2015/9/6.
 */
public interface GetHouseInfoService {
    @FormUrlEncoded
    @POST("/get_house_info")
    Observable<BaseJson<House>> onGetHouseInfo(@Field("data") String data);
}
