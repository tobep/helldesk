package company.dreambadly.helldesk;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.view.View.GONE;

public class CreateDeskActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    private EditText editDetails, editFio, editName;
    private RadioGroup rg;
    private int status;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_desk);

        Intent intent = getIntent();

        editDetails = findViewById(R.id.editDetails);
        editFio = findViewById(R.id.editFio);
        editName = findViewById(R.id.editName);

        progressBar = findViewById(R.id.progressBar11);

        rg = findViewById(R.id.radioStatus);

        //lastId = intent.getIntExtra("id", 0);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio0:
                if (checked)
                    status = 0;
                    break;
            case R.id.radio1:
                if (checked)
                    status = 1;
                    break;
            case R.id.radio2:
                if (checked)
                    status = 2;
                    break;
        }
    }

    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.btnOtmena:
                onBackPressed();
                break;
            case R.id.btnOtpravit:
                createDesk();

                Intent intent = new Intent(CreateDeskActivity.this, MainActivity.class);
                //intent.putExtra("id", deskList.get(deskList.size() - 1).getId());
                startActivity(intent);

                break;
            default:
                break;
        }

    }

    private void createDesk() {

        String name = editName.getText().toString();
        String fio = editFio.getText().toString().trim();
        String details = editDetails.getText().toString().trim();
        Date date = new Date();
        String img = "juju";
        int rating = 777;

        HashMap<String, String> contact = new HashMap<>();

        contact.put("id", "3");
        contact.put("name", name);
        contact.put("fio", fio);
        contact.put("rating", String.valueOf(rating));
        contact.put("details", details);
        contact.put("data", String.valueOf(date.getTime()));
        contact.put("image", img);
        contact.put("status", String.valueOf(status));


        GetContacts request = new GetContacts(Api.URL_CREATE_HERO, contact);
        request.execute();


    }

    private class GetContacts extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> contact;

        GetContacts(String url, HashMap<String, String> contact) {
            this.url = url;
            this.contact = contact;
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
                    //refreshDeskList(object.getJSONArray("heroes"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            return requestHandler.sendPostRequest(url, contact);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
