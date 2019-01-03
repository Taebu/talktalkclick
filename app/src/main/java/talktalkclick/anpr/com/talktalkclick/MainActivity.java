package talktalkclick.anpr.com.talktalkclick;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    private SharedPreferences prefs    ;
    WebView mWebView;
    String id;
    String password;

    JsonParser1 jp1;
    boolean is_once_run = true;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new

                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

       jp1 = new JsonParser1();
        requestWindowFeature(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        id = prefs.getString("id", "");
        password = prefs.getString("password", "");

        /* 자동로그인 시도 */
        loginWork(id,password);

        //Toast.makeText(MainActivity.this, id, Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, password, Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);


        //출처: http://1666-2498.tistory.com/498?category=789728 [1666-2498 최고집이사]
        mWebView  = new WebView(this);
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(mWebView.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        String cookie = cookieManager.getCookie("https://prq.co.kr");
        Log.d("login session","cookie ===>"+cookie);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
          //  CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mWebView,true);
        }
        mWebView  = (WebView)findViewById(R.id.activity_main_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        // mWebView  = new WebView(this).findViewById(R.id.activity_main_webview);
        //mWebView = (WebView)findViewById(R.id.activity_main_webview);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().getAllowContentAccess();

        final AppCompatActivity activity = this;
        String url = "https://prq.co.kr/ttc/auth/json_login/"+id+"/"+password;

        mWebView.loadUrl(url);
        //mWebView.loadUrl("https://prq.co.kr/ttc/");
//        setContentView(mWebView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        mWebView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                if(is_once_run) {
                    mWebView.loadUrl("https://prq.co.kr/ttc/");
                }
                is_once_run = false;
            }
        });

    }

     //   showDialog();



    private void requestWindowFeature(Bundle savedInstanceState) {
    /*
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    CookieSyncManager.getInstance().sync();
                } else {
                    CookieManager.getInstance().flush();
                }
            }
        });
    */
    }
    @SuppressLint("ResourceType")
    @Override

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id= item.getItemId();



        switch (id) {

            case R.id.action_bar_refresh:

                mWebView.loadUrl("https://prq.co.kr/ttc/");
                //mWebView.reload();

                break;

            case R.id.action_bar_setting:

               // setContentView(R.xml.settings_preference);
                setContentView(R.layout.activity_setting);

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().show();
                break;


        }

        return true;

    }

    public void showDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.activity_login, null);

        final EditText mEmail = (EditText) mView.findViewById(R.id.txtUsername);
        final EditText mPassword = (EditText) mView.findViewById(R.id.txtPassword);
        Button mLogin = (Button) mView.findViewById(R.id.btnLoginLL);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEmail.getText().toString().isEmpty() && !mPassword.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(MainActivity.this, "Please fill any empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        Button mCancel = (Button) mView.findViewById(R.id.btnClearLL);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
                ;
            }
        });
        //출처: http://1666-2498.tistory.com/498?category=789728 [1666-2498 최고집이사]
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            intentMain();
        }
        return super.onOptionsItemSelected(item);
    }

    private void intentMain()
    {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void loginWork(String id,String password) {

        String phoneNum = "01077430009";

        boolean isAutoLogin = true;

        if ("".equals(phoneNum)) {

            Toast.makeText(this, "핸드폰번호를 입력해주세요.", Toast.LENGTH_SHORT).show();

        } else {

            new LoginTask(isAutoLogin,id,password).execute(phoneNum);

        }
    }

    private class LoginTask extends AsyncTask<String, Void, JSONObject> {

        private LoginTask(boolean isAutoLogin,String id, String password) {
            this.isAutoLogin = isAutoLogin;
            this.id=id;
            this.password=password;
        }

        private boolean isAutoLogin;
        String id;
        String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String phoneNum = params[0];

            String url = "https://prq.co.kr/ttc/auth/json_login/"+this.id.toString()+"/"+this.password.toString();
            System.out.println(this.id.toString()+":"+this.password.toString());
            return jp1.getJSONObjectFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {
                if (jsonObject.getBoolean("success")) {

//                    String[] seq = jsonObject.getString("st_seq").split("_");

 //                   Log.e("CallLog", "seq : " + Arrays.toString(seq));

                    String msg = jsonObject.getString("msg");
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    //Util.saveSharedPreferences_boolean(mThis, "calllog_autologin", isAutoLogin);
                  // intentMain();
                } else {

                    String msg = jsonObject.getString("msg");

                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
