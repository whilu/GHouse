package co.lujun.ghouse.api.httpinterface;

import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.Bill;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by lujun on 2015/9/6.
 */
public interface GetRecordService {
    @FormUrlEncoded
    @POST("/get_list")
    Observable<BaseJson<Bill>> onGetRecord(@Field("data") String data);
}
