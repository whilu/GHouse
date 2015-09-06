package co.lujun.ghouse.api.httpinterface;

import co.lujun.ghouse.bean.BaseJson;
import co.lujun.ghouse.bean.User;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by lujun on 2015/9/6.
 */
public interface LoginService {
    @FormUrlEncoded
    @POST("/login")
    Observable<BaseJson<User>> onLogin(@Field("data") String data);
}
