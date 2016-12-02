package xyz.christianwijasa.tutorial;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;

public class LoginActivity extends AppCompatActivity {
    // progress dialog

    private ProgressDialog progressDialog;
    String email, password;

    // creating JSON Parser object
    JSONParser jParser = new JSONParser();

    private static String url_member = "http://192.168.1.3/"+
            "/android/lucid/member.php";

    //TAG untuk passing ke intent lain
    private static final String MEMBER_ID = "member_id";

    //TAG untuk mengambil JSONArray of Object. sesuaikan dengan variable pada file member.php
    private static final String TAG_ARRAY ="member";

    //TAG untuk mengambil kolom json
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MEMBER_ID = "member_id";

    JSONArray members_data = null;

    class CheckLogin extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Checking member");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //building parameter
            List<Pair<String,String>> args = new ArrayList<Pair<String,String>>();
            args.add(new Pair<>("email",email));
            args.add(new Pair<>("password",password));

            JSONObject jsonObject = null;
            try{
                //getting JSON Object
                jsonObject = jParser.makeHttpRequest(url_member,"POST",args);

            }catch(IOException e){
                Log.d("Networking",e.getLocalizedMessage());
            }

            //check for success tag

            try{

                Log.d("Create response", jsonObject.toString());

                int success = jsonObject.getInt(TAG_SUCCESS);

                if(success == 1){
                    //successfully get the member
                    members_data = jsonObject.getJSONArray(TAG_ARRAY);

                    //get the first data of member
                    JSONObject member = members_data.getJSONObject(0);
                    String member_id = member.getString("member_id");

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(MEMBER_ID,member_id);
                    startActivity(intent);
                    finish();
                }
                else{
                    //failed to create product
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //dismiss the dialog once done
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/

        Button btnLogin = (Button) findViewById(R.id.login_button);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the input
                EditText editEmail = (EditText) findViewById(R.id.edit_email);
                EditText editPassword = (EditText) findViewById(R.id.edit_password);

                email = editEmail.getText().toString();
                password = editPassword.getText().toString();

                try {
                    new CheckLogin().execute();
                }
                catch (Exception e) {
                    Toast.makeText(
                            getBaseContext(),
                            "Error: " + e.toString(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }
}
