package xyz.christianwijasa.tutorial;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.util.Pair;
import java.util.Set;

public class AssignmentActivity extends AppCompatActivity {


    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> questionList;

    private static String url_all_questions = "http://192.168.1.3/"+
            "android/lucid/get_all_questions.php";

    public String materi_id;// untuk mengambil getPutExtra("materi_id"); pada onCreate

    //JSON node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ARRAY ="questions";

    //JSON "questions" array data
    private static final String TAG_MATERI = "materi_id";
    private static final String TAG_QUESTION_ID = "soal_id";
    private static final String TAG_QUESTION = "deskripsi_soal";
    private static final String TAG_ANSWER_1 = "jawaban_1";
    private static final String TAG_ANSWER_2 = "jawaban_2";
    private static final String TAG_ANSWER_3 = "jawaban_3";
    private static final String TAG_REAL_ANSWER = "jawaban_benar";

    //global variable for checking answer
    ListView simpleList;
    Integer jumlah_soal ;

    JSONArray questions = null;


    class LoadAllQuestions extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AssignmentActivity.this);
            pDialog.setMessage("Loading Questions. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            List<Pair<String,String >> args =
                    new ArrayList<Pair<String, String>>();
            args.add(new Pair<>("materi_id",materi_id));
            JSONObject jsonObject = null;

            try{
                jsonObject = jParser.makeHttpRequest(url_all_questions,"POST",args);

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

                    questions = jsonObject.getJSONArray(TAG_ARRAY);
                    jumlah_soal = questions.length();
                    for(int i = 0;i< questions.length(); i++){
                        JSONObject c = questions.getJSONObject(i);
                        String id = c.getString(TAG_QUESTION_ID);
                        String question = c.getString(TAG_QUESTION); // deskripsi_soal
                        String materi_id = c.getString(TAG_MATERI);
                        String jawaban_1 = c.getString(TAG_ANSWER_1);
                        String jawaban_2 = c.getString(TAG_ANSWER_2);
                        String jawaban_3 = c.getString(TAG_ANSWER_3);
                        String jawaban_real = c.getString(TAG_REAL_ANSWER);

                        //creating new hashmap
                        HashMap<String, String> map =
                                new HashMap<String, String>();

                        //adding each child node to hashmap key => value
                        map.put(TAG_QUESTION_ID, id);
                        map.put(TAG_QUESTION, question);
                        map.put(TAG_ANSWER_1, jawaban_1);
                        map.put(TAG_ANSWER_2, jawaban_2);
                        map.put(TAG_ANSWER_3, jawaban_3);
                        map.put(TAG_REAL_ANSWER, jawaban_real);


                        questionList.add(map);
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

            final  ListView assignmentListView = (ListView) findViewById(R.id.assignment_list);
            pDialog.dismiss();
            //updating UI from background thread

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //updating parsed JSOn data into ListView
                    ListAdapter adapter = new SimpleAdapter(
                            AssignmentActivity.this,
                            questionList,
                            R.layout.list_item,
                            new String[]{TAG_QUESTION_ID, TAG_QUESTION, TAG_ANSWER_1, TAG_ANSWER_2, TAG_ANSWER_3,},
                            new int[]{R.id.pid, R.id.soal,R.id.jawaban_1, R.id.jawaban_2, R.id.jawaban_3});

                    assignmentListView.setAdapter(adapter);
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
        setContentView(R.layout.activity_assignment);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //getting materi_id from intent
        Intent i = getIntent();
        materi_id = i.getStringExtra(TAG_MATERI);

        //hasmap for listview
        questionList = new ArrayList<HashMap<String, String>>();
        new LoadAllQuestions().execute();

        Button submitBtn = (Button) findViewById(R.id.quiz_button_submit);
        Button cancelBtn = (Button) findViewById(R.id.quiz_button_cancel);


        simpleList = (ListView) findViewById(R.id.assignment_list);

        Toast.makeText(AssignmentActivity.this,jumlah_soal,Toast.LENGTH_SHORT).show();
    //        QuestionAdapter customAdapter = new QuestionAdapter(getApplicationContext(), jumlah_soal);
    //        simpleList.setAdapter(customAdapter);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message ="";
                for(int i = 0;i< QuestionAdapter.selectedAnswers.size();i++){
                    message = message + "\n" + (i+1) + " " + QuestionAdapter.selectedAnswers.get(i);

                }

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

            }
        });

    }

}
