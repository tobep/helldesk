package company.dreambadly.helldesk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText editPhone, editPass;
    private boolean isOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.progressBar11);
        editPhone = findViewById(R.id.editPhone);
        editPass = findViewById(R.id.editPass);

    }


    public void loginOnClick(View view) {

        Intent intent;

        switch (view.getId()) {

            case R.id.loginBtn:

                readUsers();

                if (isOk){
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("aaa", false);
                    startActivity(intent);
                }
                break;
            case R.id.registrBtn:
                intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                //intent.putExtra("id", deskList.get(deskList.size() - 1).getId());
                startActivity(intent);
                break;
        }

    }

    private void readUsers() {
        GetContacts request = new GetContacts(Api.URL_READ_USERS);
        request.execute();
    }

    private void doUsersList(JSONArray heroes) throws JSONException {

        for (int i = 0; i < heroes.length(); i++) {

            JSONObject obj = heroes.getJSONObject(i);

            Log.v("TAG", String.valueOf(obj.getInt("phone")));
            Log.v("TAG", editPhone.getText().toString());

            if (obj.getInt("phone") == Integer.parseInt(editPhone.getText().toString())){
                if (obj.getString("pass").equals(editPass.getText().toString())){

                    SharedPreferences sp = getSharedPreferences("spName", MODE_PRIVATE);
                    sp.edit().putInt("idWhoRun", obj.getInt("id")).apply();

                    isOk = true;
                    break;

                } else {
                    Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                }
            }

            Toast.makeText(this, "Нет пользователя с таким телефоном", Toast.LENGTH_SHORT).show();
        }

    }

    private class GetContacts extends AsyncTask<Void, Void, String> {
        String url;

        GetContacts(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    doUsersList(object.getJSONArray("users"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            return requestHandler.sendGetRequest(url);
        }
    }
}
