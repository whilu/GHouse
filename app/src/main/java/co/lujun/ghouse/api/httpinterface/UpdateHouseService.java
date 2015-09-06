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
public interface UpdateHouseService {
    @FormUrlEncoded
    @POST("/edit_house")
    Observable<BaseJson<House>> onUpdateHouse(@Field("data") String data);
}
