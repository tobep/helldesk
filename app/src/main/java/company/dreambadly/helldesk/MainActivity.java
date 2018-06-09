package company.dreambadly.helldesk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;

import android.text.TextUtils;

import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    private static final String TAG = "TAG";
    private int[] resStatus = {R.drawable.draw1, R.drawable.draw2, R.drawable.draw3};

    private ProgressDialog pDialog;
    private ListView lv;
    private ListAdapter lvAdapter;
    private ArrayList<Map<String, Object>> contactList;
    List<Desk> deskList;
    private int isWhoRun;
    private JSONArray currentHeroes;

    public Drawer drawer;
    ProgressBar progressBar;
    private String[] spinnerList = {"Важные", "По дате", "По имени"};
    TextView text;
    ArrayList<String> detailslsList;
    FloatingActionButton fab;
    boolean aaa = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("spName", MODE_PRIVATE);
        if (sp.getBoolean("isFirstRun", true)) {

            sp.edit().putBoolean("isFirstRun", false).apply();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            //intent.putExtra("id", deskList.get(deskList.size() - 1).getId());
            startActivity(intent);

        } else {

            Log.v(TAG, String.valueOf(sp.getInt("idWhoRun", 1000)));

            text = findViewById(R.id.t1);

            contactList = new ArrayList<>();

            lv = findViewById(R.id.listTask);

            deskList = new ArrayList<>();
            progressBar = findViewById(R.id.progressBar);
            detailslsList = new ArrayList<>();
            fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MainActivity.this, CreateDeskActivity.class);
                    //intent.putExtra("id", deskList.get(deskList.size() - 1).getId());
                    startActivity(intent);

                }
            });


            //Обявление и инициализация View
            Spinner spinner = findViewById(R.id.spinner);

            // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
            // Определяем разметку для использования при выборе элемента
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Применяем адаптер к элементу spinner
            spinner.setAdapter(adapter);

            //Обработчик нажатия
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    /*
    sortType - тип сортировки
    0 - по важности
    1 - по дате
    2 - по имени
     */
                    currentHeroes = sortHeroesJsonBySortType(currentHeroes, position);
                    try {
                        refreshDeskList(currentHeroes);

                        Log.d("sorting", currentHeroes.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            initializeNavigationDrawer();

            readDesk();

        }
    }

    /*
    sortType - тип сортировки
    0 - по важности
    1 - по дате
    2 - по имени
     */
    private JSONArray sortHeroesJsonBySortType(JSONArray inputJsonArr, final int sortType) {
        List<JSONObject> jsonArrAsList = new ArrayList<JSONObject>();

        try {
            for (int i = 0; i < inputJsonArr.length(); i++)
                jsonArrAsList.add(inputJsonArr.getJSONObject(i));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(jsonArrAsList, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
                int compare = 0;
                try {
                    int keyA;
                    int keyB;
                    switch (sortType) {
                        case 0:
                            //сортировка по важности
                            keyA = jsonObjectA.getInt("status");
                            keyB = jsonObjectB.getInt("status");
                            compare = Integer.compare(keyA, keyB);
                            break;
                        case 1:
                            //сортировка по дате
                            keyA = jsonObjectA.getInt("data");
                            keyB = jsonObjectB.getInt("data");
                            compare = Integer.compare(keyA, keyB);
                            break;
                        case 2:
                            //сортировка по имени
                            String stringA = jsonObjectA.getString("name");
                            String stringB = jsonObjectB.getString("name");
                            compare = stringA.compareTo(stringB);
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return compare;
            }
        });

        JSONArray resultJsonArray = new JSONArray();
        for (int i = 0; i < jsonArrAsList.size(); i++) {
            resultJsonArray.put(jsonArrAsList.get(i));
        }

        return resultJsonArray;
    }

    public void initializeNavigationDrawer() {

        IProfile profile = new ProfileDrawerItem()
                .withName("Robbert")
                .withEmail("Robert969696@gmail.com")
                //.withIcon(R.drawable.hell_io)
                ;


        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.hell_io)
                .addProfiles(profile)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withSliderBackgroundColor(getResources().getColor(R.color.bgNav))
                .withAccountHeader(accountHeader)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName("Задачи")
                                .withTextColor(getResources().getColor(R.color.bgNavText))
                                .withSelectedColor(getResources().getColor(R.color.bgNavSelectedIcon))
                                .withIdentifier(1)
                                .withIcon(R.drawable.ic_home_black_18dp),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName("Склад")
                                .withTextColor(getResources().getColor(R.color.bgNavText))
                                .withSelectedColor(getResources().getColor(R.color.bgNavSelectedIcon))
                                .withIcon(R.drawable.ic_info_black_18dp),
                        new SecondaryDrawerItem()
                                .withName("Сотрудники")
                                .withTextColor(getResources().getColor(R.color.bgNavText))
                                .withSelectedColor(getResources().getColor(R.color.bgNavSelectedIcon))
                                .withIcon(R.drawable.ic_pageview_black_18dp),
                        new SecondaryDrawerItem()
                                .withName("F.A.Q.")
                                .withTextColor(getResources().getColor(R.color.bgNavText))
                                .withSelectedColor(getResources().getColor(R.color.bgNavSelectedIcon))
                                .withIcon(R.drawable.ic_help_black_18dp)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (position == 4){
                            Intent intent = new Intent(MainActivity.this, Sotrudniki.class);
                            //intent.putExtra("id", deskList.get(deskList.size() - 1).getId());
                            startActivity(intent);
                        }
                        return false;
                    }
                })
                .build();

    }

    private void readDesk() {
        GetContacts request = new GetContacts(Api.URL_READ_HEROES);
        request.execute();
    }

    private void refreshDeskList(JSONArray heroes) throws JSONException {
        detailslsList.clear();
        deskList.clear();



        for (int i = 0; i < heroes.length(); i++) {

            JSONObject obj = heroes.getJSONObject(i);

            detailslsList.add(obj.getString("details"));

            HashMap<String, Object> contact = new HashMap<>();

            contact.put("textName", obj.getString("name"));
            contact.put("textFio", obj.getString("fio"));

            long unixSeconds = Long.parseLong(obj.getString("data"));
            Date date = new Date(unixSeconds);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            int day = Integer.parseInt(sdf.format(date));
            sdf = new SimpleDateFormat("MM");
            int month = Integer.parseInt(sdf.format(date)) - 1;
            sdf = new SimpleDateFormat("yyyy");
            int year = Integer.parseInt(sdf.format(date));

            if (day == calendar.get(Calendar.DAY_OF_MONTH) && month == calendar.get(Calendar.MONTH)
                    && year == calendar.get(Calendar.YEAR)) {
                sdf = new SimpleDateFormat("сегодня, HH:" + "mm");
            } else if (day == calendar.get(Calendar.DAY_OF_MONTH + 1) && month == calendar.get(Calendar.MONTH)
                    && year == calendar.get(Calendar.YEAR)) {
                sdf = new SimpleDateFormat("завтра, HH:" + "mm");
            } else if (day == calendar.get(Calendar.DAY_OF_MONTH - 1) && month == calendar.get(Calendar.MONTH)
                    && year == calendar.get(Calendar.YEAR)) {
                sdf = new SimpleDateFormat("вчера, HH:" + "mm");
            } else {
                sdf = new SimpleDateFormat("dd.MM.yyyy, HH:" + "mm");
            }

            String formattedDate = sdf.format(date);

            contact.put("textData", formattedDate);
            contact.put("img", obj.getString("image"));
            contact.put("status", resStatus[obj.getInt("status")]);
            contact.put("rating", obj.getInt("rating"));

            deskList.add(new Desk(
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.getString("fio"),
                    obj.getInt("rating"),
                    obj.getString("details"),
                    obj.getLong("data"),
                    obj.getString("image"),
                    obj.getInt("status")
            ));


            contactList.add(contact);
        }

        String[] from = {"textName", "textFio", "textData", "status"};
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = {R.id.textName, R.id.textFio,
                R.id.textData, R.id.imgStop};

        lv.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("details", deskList.get(i).getDetails());
                intent.putExtra("name", deskList.get(i).getName());
                intent.putExtra("fio", deskList.get(i).getFio());
                intent.putExtra("data", deskList.get(i).getData());
                startActivity(intent);
            }
        });

        // создаем адаптер
        lvAdapter = new SimpleAdapter(MainActivity.this, contactList, R.layout.list_item_task, from, to);

        // присваиваем адаптер
        lv.setAdapter(lvAdapter);

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
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshDeskList(object.getJSONArray("heroes"));
                    currentHeroes = object.getJSONArray("heroes");
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
