package company.dreambadly.helldesk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sotrudniki extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sotrudniki);

        ListView lv = findViewById(R.id.lvSotrud);

        int[] imgList = {R.drawable.d1,
                R.drawable.d2};
        // массив заполняемых данных !!!
        String[] listName = {"Мои", "сериалы", "иане"};
        String[] listOtdel = {"фильмы", "Моисери", "Миме"};
        // массив имен атрибутов, из которых будут читаться данные
        String[] from = { "img", "name", "otdel"};
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.imgSotrud, R.id.textNameSotrud, R.id.textOtdelSotrud};

        // упаковываем данные в понятную для адаптера структуру
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> m;
        int k = 0;
        for (int i = 0; i < 19; i++) {
            m = new HashMap<>();
            if (k < 1){
                m.put("name", listName[k]);
                m.put("otdel", listOtdel[k]);
                m.put("img", imgList[k]);
                k++;
            } else {
                m.put("name", listName[k]);
                m.put("otdel", listOtdel[k]);
                m.put("img", imgList[k]);
                k = 0;
            }
            data.add(m);
        }

        // создаем адаптер
        SimpleAdapter listAdapter = new SimpleAdapter(this, data, R.layout.list_item_sotrud, from, to);

        lv.setAdapter(listAdapter);

        lv.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(Sotrudniki.this, SatrudnikiDetails.class);
                /*intent.putExtra("name", deskList.get(i).getDetails());
                intent.putExtra("otdel", deskList.get(i).getName());
                intent.putExtra("fio", deskList.get(i).getFio());
                intent.putExtra("data", deskList.get(i).getData());*/
                startActivity(intent);
            }
        });

    }
}
