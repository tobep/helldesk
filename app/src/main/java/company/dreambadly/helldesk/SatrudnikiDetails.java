package company.dreambadly.helldesk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SatrudnikiDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satrudniki_details);

        Intent intent = getIntent();
        TextView ss = findViewById(R.id.oooooo);
        ss.setText(intent.getStringExtra("name"));

    }
}
