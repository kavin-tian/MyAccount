package project.test.com.myapplication;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 子云心 http://blog.csdn.net/lyz_zyx
 */
public class SyncService extends Service {
    public static final String TAG = "SyncService";

    private static final Object sSyncAdapterLock = new Object();
    private static SyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }

    class SyncAdapter extends AbstractThreadedSyncAdapter {
        public SyncAdapter(Context context, boolean autoInitialize) {
            super(context, autoInitialize);
        }

        //系统一般15分钟同步一次
        @Override
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
            // TODO 实现数据同步
            //getContext().getContentResolver().notifyChange(AccountProvider.CONTENT_URI, null, false);

            sendTestData();
        }
    }

    /**
     * 测试用的
     */
    public void sendTestData() {
        String BASE_URL = "http://192.168.101.117:8080/";
        //创建Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                //注意，服务器主机应该以/结束，
                .baseUrl(BASE_URL)//设置服务器主机
                .addConverterFactory(GsonConverterFactory.create())//配置Gson作为json的解析器
                .build();

        ServerApi api = retrofit.create(ServerApi.class);

        postJson(api);

    }

    public void postJson(ServerApi api) {

//        String manufacturer = DeviceUtils.getManufacturer();
//        String model = DeviceUtils.getModel();
//        String sdkVersionName = DeviceUtils.getSDKVersionName();
//        String macAddress = DeviceUtils.getMacAddress();

        //调用业务方法，得到要执行的业务请求对象
        //Call<String> order = heiMaApi.get();
        //Call<String> order = api.post(manufacturer,model,sdkVersionName,macAddress);
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", "redmi 7a");
//        jsonObject.addProperty("id", "honor 9x");
//        jsonObject.addProperty("manufacturer", manufacturer);
//        jsonObject.addProperty("model", model);
//        jsonObject.addProperty("sdkVersionName", sdkVersionName);
//        jsonObject.addProperty("macAddress", macAddress);
        Call<String> call = api.postJson(jsonObject);

        //发起请求
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String stu = response.body();
                Log.e(TAG, "onResponse: " + stu);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

}
