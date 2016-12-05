package xyz.christianwijasa.tutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChapterQActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> chapterList;

    private static String url_all_chapters = "http://192.168.1.4/"+
            "android/lucid/all_chapter.php";

    //JSON node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ARRAY ="materi";

    //JSON "questions" array data
    private static final String TAG_CHAPTER_ID = "materi_id";
    private static final String TAG_TITLE = "judul";
    private static final String TAG_CHAPTER_DESCRIPTION = "deskripsi_materi";



    JSONArray chapters = null;


    class LoadAllChapters extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChapterQActivity.this);
            pDialog.setMessage("Loading Chapters. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            List<Pair<String,String >> args =
                    new ArrayList<Pair<String, String>>();
            JSONObject jsonObject = null;

            try{
                jsonObject = jParser.makeHttpRequest(url_all_chapters,"POST",args);

            }catch(IOException e){
                Log.d("Networking",e.getLocalizedMessage());
            }

//            check yout logcat for JSON Response
            Log.d("All products: ", jsonObject.toString());

            try{
                //checking for SUCCESS TAG
                int success = jsonObject.getInt(TAG_SUCCESS);
//                Toast.makeText(AssignmentActivity.this,success,Toast.LENGTH_SHORT).show();
                if(success == 1){

                    chapters = jsonObject.getJSONArray(TAG_ARRAY);

                    for(int i = 0;i< chapters.length(); i++){
                        JSONObject c = chapters.getJSONObject(i);
                        String id = c.getString(TAG_CHAPTER_ID);
                        String title = c.getString(TAG_TITLE );
                        String description = c.getString(TAG_CHAPTER_DESCRIPTION);

                        //creating new hashmap
                        HashMap<String, String> map =
                                new HashMap<String, String>();

                        //adding each child node to hashmap key => value
                        map.put(TAG_CHAPTER_ID, id);
                        map.put(TAG_TITLE, title);
                        map.put(TAG_CHAPTER_DESCRIPTION, description);

                        chapterList.add(map);
                    }
                }else{
                    //no questions found
                    //launch add new question
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            final ListView chapterListView =(ListView) findViewById(R.id.chapter_list);
            pDialog.dismiss();
            //updating UI from background thread

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //updating parsed JSOn data into ListView
                    ListAdapter adapter = new SimpleAdapter(
                            ChapterQActivity.this,
                            chapterList,
                            R.layout.chapter_list,
                            new String[]{TAG_CHAPTER_ID, TAG_TITLE},
                            new int[]{R.id.chapter_id, R.id.chapter_title});

                    chapterListView.setAdapter(adapter);
                }
            });
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(pDialog!=null){
            pDialog.dismiss();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_q2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //hashmap for ListView
        chapterList = new ArrayList<HashMap<String, String>>();
        //loading chapters in background thread
        new LoadAllChapters().execute();

        ListView listView = (ListView)findViewById(R.id.chapter_list);

        //on selecting single product
        //launch the AssignmentActivtiy
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting values from selected ListItem
                String chapter_id = ((TextView) view.findViewById(R.id.chapter_id))
                        .getText().toString();
                //starting new Intent
                Intent intent = new Intent(getApplicationContext(),
                        AssignmentActivity.class);

                //sending chapter id to the next activity
                intent.putExtra(TAG_CHAPTER_ID, chapter_id);
                //starting new activity and expecting some responsess back
                startActivityForResult(intent, 100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if result code 100

        if(resultCode == 100){
            //if result code 100 is received means user answered the questions
            //reload this screen again
            Intent i = getIntent();
            finish();
            startActivity(i);
        }
    }
}
