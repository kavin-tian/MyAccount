package project.test.com.myapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 子云心 http://blog.csdn.net/lyz_zyx
 */
public class AuthenticatorActivity extends AppCompatActivity {

    public static final String ACCOUNT_TYPE = "project.test.com.myapplication.account.type";        // TYPE必须与account_preferences.xml中的TYPE保持一致
    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        mAccountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);

        //会检查***Manifest.permission.GET_ACCOUNTS***的权限（Android6.0及以上是运行时权限，需动态申请）
        @SuppressLint("MissingPermission")
        Account[] accounts = mAccountManager.getAccountsByType(ACCOUNT_TYPE);

        // 获取系统帐户列表中是否存在我们的帐户，用TYPE做为标识
        if (accounts.length > 0) {
            Toast.makeText(this, "已添加当前登录的帐户", Toast.LENGTH_SHORT).show();
            finish();
        }

        Button btnAddAccount = (Button) findViewById(R.id.btn_add_account);

        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {

                Account account = new Account(getString(R.string.app_name), ACCOUNT_TYPE);
                mAccountManager.addAccountExplicitly(account, null, null);                          // 帐户密码和信息这里用null演示

                // 自动同步
                Bundle bundle = new Bundle();
                ContentResolver.setIsSyncable(account, AccountProvider.AUTHORITY, 1);
                ContentResolver.setSyncAutomatically(account, AccountProvider.AUTHORITY, true);

                //系统一般15分钟同步一次
                ContentResolver.addPeriodicSync(account, AccountProvider.AUTHORITY, bundle, 30);    // 间隔时间为30秒,参考时间

                // 手动同步
                // ContentResolver.requestSync(account, AccountProvider.AUTHORITY, bundle);

                finish();
            }
        });
    }


}
