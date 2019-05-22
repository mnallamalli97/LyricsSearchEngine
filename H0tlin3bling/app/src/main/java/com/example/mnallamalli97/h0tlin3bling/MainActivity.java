package com.example.mnallamalli97.h0tlin3bling;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private String TAG = "TAG";


    Button btnSubmit;
    EditText etSearch;
    RadioButton orBtn;
    RadioButton andBtn;
    RadioGroup rbGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btnSubmit = findViewById(R.id.btnSubmit);
        etSearch = findViewById(R.id.etSearch);
        orBtn = findViewById(R.id.rbOr);
        andBtn = findViewById(R.id.rbAnd);
        rbGroup = findViewById(R.id.rbGroup);



        String search_query = etSearch.getText().toString();


        Log.d(TAG,"doInBackground: starting parsetask class");


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //once submit button is clicked
                new ParseTask().execute();

            }
        });






    }

    private HashMap<String, String> createSongItem(String name, String aname){
        HashMap<String, String> songObject = new HashMap<String, String>();
        songObject.put(name, aname);
        return songObject;
    }


    private class ParseTask extends AsyncTask<Void, Void, String>{

        //connect to the backend with the url
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String $url_json;
        URL url;

        ArrayList<HashMap<String,String>> employeeList = new ArrayList<HashMap<String,String>>();

        String result_json = "{\"searchResults\":";

        protected String andQuery(String query){
            String andLink = "https://lit-dusk-32149.herokuapp.com/search/and/" + query;
            return andLink;
        }

        protected String orQuery(String query){
            String orLink = "https://lit-dusk-32149.herokuapp.com/search/or/" + query;
            return orLink;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Log.d(TAG, "doInBackground: starting doinbackground");

                if(rbGroup.getCheckedRadioButtonId() == andBtn.getId()){
                    String testQuery = "i%20need%20a%20hero";
                    $url_json = andQuery(testQuery);
                }

                if(rbGroup.getCheckedRadioButtonId() == orBtn.getId()){
                    String testQuery = "blood%20on%20the%20leaves";
                    $url_json = orQuery(testQuery);
                }

                url = new URL($url_json);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                Log.d(TAG, "doInBackground: connecting to heroku");

                //convert the JSON into a string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                result_json = result_json.concat(buffer.toString());
                result_json = result_json.concat("}");
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result_json;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            //parse the string and put it into the list view
            JSONObject jsonResponse = null;
            try {

                Log.d(TAG, result_json);
               // String jsonString = "{\"searchResults\":[{\"sname\":\"I Wanna Be Your Hero\",\"aname\":\"Def Leppard\",\"url\":\"/d/def+leppard/i+wanna+be+your+hero_20038946.html\"},{\"sname\":\"Hero\",\"aname\":\"David Guetta\",\"url\":\"/d/david+guetta/hero_20991447.html\"},{\"sname\":\"Nobody's Hero\",\"aname\":\"Bon Jovi\",\"url\":\"/b/bon+jovi/nobodys+hero_20811564.html\"},{\"sname\":\"We Don't Need Another Hero\",\"aname\":\"Tina Turner\",\"url\":\"/t/tina+turner/we+dont+need+another+hero_10232469.html\"},{\"sname\":\"Hero For A Day\",\"aname\":\"Conway Twitty\",\"url\":\"/c/conway+twitty/hero+for+a+day_20870543.html\"}]}";
                jsonResponse = new JSONObject(result_json);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("searchResults");

                for(int i = 0; i<jsonMainNode.length();i++){
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String sname = jsonChildNode.optString("sname");
                    String aname = jsonChildNode.optString("aname");
                    String url = jsonChildNode.optString("url");
                    String output = sname + "-" + aname;
                    employeeList.add(createSongItem("result", output));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListView lvResults = findViewById(R.id.lvResults);

            SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, employeeList, android.R.layout.simple_list_item_1, new String[] {"result"}, new int[] {android.R.id.text1});
            Log.d(TAG, "onPostExecute:" + employeeList);
            lvResults.setAdapter(simpleAdapter);

            result_json = "";

        }
    }

}
