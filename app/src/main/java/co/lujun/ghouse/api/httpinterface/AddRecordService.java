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
public interface AddRecordService {
    @FormUrlEncoded
    @POST("/add_record")
    Observable<BaseJson<Bill>> onAddRecord(@Field("data") String data);
}
