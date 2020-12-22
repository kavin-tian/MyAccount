package project.test.com.myapplication;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 服务器请求接口
 */
public interface ServerApi {


    @GET("get")
    Call<String> get();

    /**
     * 测试数据
     */
    @POST("postJson")
    Call<String> postJson(@Body JsonObject jsonObject);

}