package talktalkclick.anpr.com.talktalkclick;

/**
 * Created by Jung-Hum Cho on 12/28/18.
 */

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by amagr on 2018-01-01.
 */

public class SettingPreferenceFragment extends PreferenceFragment {

    private SharedPreferences prefs;

    ListPreference soundPreference;
    ListPreference keywordSoundPreference;
    PreferenceScreen keywordScreen;

    AlertDialog.Builder builder ;
    Preference button_login;
    SwitchPreference switchPreference;
    String id;
    String password;
    boolean is_auto_login = false;
    
    
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        addPreferencesFromResource(R.xml.settings_preference);
        
        soundPreference = (ListPreference)findPreference("sound_list");
        keywordSoundPreference = (ListPreference)findPreference("keyword_sound_list");
        keywordScreen = (PreferenceScreen)findPreference("keyword_screen");

        button_login = (Preference)findPreference("button_login");
      
        Preference button_login_pref = (Preference) findPreference("button_login");


        try {
            id = prefs.getString("id", "");
            password = prefs.getString("password", "");

        }catch (NullPointerException e)
        {
            id="";
            password="";
        }
        if(id.isEmpty()&&password.isEmpty()){
            button_login.setTitle("로그인");
            button_login.setSummary("로그인이 필요합니다.");

        }else{
            button_login.setTitle("로그아웃");
            button_login.setSummary("로그인 중 입니다.");
            is_auto_login = true;
        }

       button_login_pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here
                id = prefs.getString("id", "");
                password = prefs.getString("password", "");

                if(id.isEmpty()&&password.isEmpty()){
                    button_login.setTitle("로그인");
                    button_login.setSummary("로그인이 필요합니다.");
                }else{
                    button_login.setTitle("로그아웃");
                    button_login.setSummary("로그인 중 입니다.");
                }


                show(id,password);
                return true;
            }
        });

   
        if(prefs.getString("button_login", "").equals("button_login")){
            show(id,password);
        }
    
    //     prefs.registerOnSharedPreferenceChangeListener(prefListener);

    }// onCreate


    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

/*

            if(key.equals("sound_list")){
                soundPreference.setSummary(prefs.getString("sound_list", "카톡"));
            }

            if(key.equals("keyword_sound_list")){
                keywordSoundPreference.setSummary(prefs.getString("keyword_sound_list", "카톡"));
            }

            if(key.equals("keyword")){

                if(prefs.getBoolean("keyword", false)){
                    keywordScreen.setSummary("사용");

                }else{
                    keywordScreen.setSummary("사용안함");
                }

                //2뎁스 PreferenceScreen 내부에서 발생한 환경설정 내용을 2뎁스 PreferenceScreen에 적용하기 위한 소스
                ((BaseAdapter)getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
            }
*/
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        SwitchPreference preference = (SwitchPreference) findPreference("message");
        preference.setSummaryOff("알림수신을 하지 않습니다. ");
        preference.setSummaryOn("알림수신을 허용 합니다. ");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isChecked = sharedPreferences.getBoolean("message",false);
        Toast.makeText(getActivity(), "isCheck : " + isChecked, Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    void show(String id, String password)
    {

        builder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.activity_login, null);


        final EditText mEmail = (EditText) mView.findViewById(R.id.txtUsername);

        final EditText mPassword = (EditText) mView.findViewById(R.id.txtPassword);

        mEmail.setText(id);

        mPassword.setText(password);

        Button mLogin = (Button) mView.findViewById(R.id.btnLoginLL);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEmail.getText().toString().isEmpty() && !mPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Login success", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor e_id = prefs.edit();
                    e_id.putString("id",mEmail.getText().toString());
                    e_id.commit();

                    SharedPreferences.Editor e_password = prefs.edit();
                    e_password.putString("password",mPassword.getText().toString());
                    e_password.commit();

                } else {

                    Toast.makeText(getActivity(), "Please fill any empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


        builder.setView(mView);
        final AlertDialog dialog = builder.create();
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
    
    
}