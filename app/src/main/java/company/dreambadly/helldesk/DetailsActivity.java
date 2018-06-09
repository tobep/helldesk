package company.dreambadly.helldesk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {

    private TextView fioText, detailsText, nameText, dataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();

        detailsText = findViewById(R.id.detailsDetails);
        fioText = findViewById(R.id.detailsFio);
        nameText = findViewById(R.id.detailsName);
        dataText = findViewById(R.id.detailsDate);

        detailsText.setText(intent.getStringExtra("details"));
        nameText.setText(intent.getStringExtra("name"));
        fioText.setText(intent.getStringExtra("fio"));
        dataText.setText(createDate(intent.getLongExtra("data", 0)));

    }

    private String createDate(Long unixSeconds){

        Date date = new Date(unixSeconds);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        int day = Integer.parseInt(sdf.format(date));
        sdf = new SimpleDateFormat("MM");
        int month = Integer.parseInt(sdf.format(date)) - 1;
        sdf = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(sdf.format(date));

        if (day == calendar.get(Calendar.DAY_OF_MONTH) && month == calendar.get(Calendar.MONTH)
                && year == calendar.get(Calendar.YEAR)){
            sdf = new SimpleDateFormat("сегодня, HH:" + "mm");
        } else if (day == calendar.get(Calendar.DAY_OF_MONTH + 1) && month == calendar.get(Calendar.MONTH)
                && year == calendar.get(Calendar.YEAR)){
            sdf = new SimpleDateFormat("завтра, HH:" + "mm");
        } else if (day == calendar.get(Calendar.DAY_OF_MONTH - 1) && month == calendar.get(Calendar.MONTH)
                && year == calendar.get(Calendar.YEAR)){
            sdf = new SimpleDateFormat("вчера, HH:" + "mm");
        } else {
            sdf = new SimpleDateFormat("dd.MM.yyyy, HH:" + "mm");
        }

        return sdf.format(date);
    }

}
