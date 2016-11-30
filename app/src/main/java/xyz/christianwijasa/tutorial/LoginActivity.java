package xyz.christianwijasa.tutorial;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.StrictMode;
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

public class LoginActivity extends AppCompatActivity {
    //progress dialog

    private ProgressDialog progressDialog;
    String email, password;

    //creating JSON Parser object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String,String>> member_map;

    private static String url_member = "http://192.168.1.2/"+
            "/android/lucid/member.php";

    //JSON node names

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MEMBER= "member";       //TAG_MEMBER harus sama dengan array response pada member.php
    private static final String TAG_MEMBER_ID = "member_id";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_FIRST_NAME = "first_name";
    private static final String TAG_LAST_NAME = "last_name";
    private static final String TAG_NIP = "nip";





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

            Log.d("Create response", jsonObject.toString());
            //check for success tag

            try{
                int success = jsonObject.getInt(TAG_SUCCESS);

                if(success == 1){
                    //successfully get the member

                    members_data = jsonObject.getJSONArray(TAG_MEMBER);

                    //get the first data of member
                    JSONObject member = members_data.getJSONObject(0);
                    String member_id = member.getString(TAG_MEMBER_ID);
                    String email     = member.getString(TAG_EMAIL);
                    String first_name= member.getString(TAG_FIRST_NAME);
                    String last_name = member.getString(TAG_LAST_NAME);
                    String nip       = member.getString(TAG_NIP);


                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(TAG_MEMBER_ID,member_id);

                    //save member data to database SQLLite
                    AccountContract.AccountDbHelper accountDbHelper
                            = new AccountContract.AccountDbHelper(LoginActivity.this);
                    SQLiteDatabase db = accountDbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(
                            AccountContract.AccountEntry.COLUMN_EMAIL,
                            email
                    );

                    values.put(
                            AccountContract.AccountEntry.COLUMN_FIRST_NAME,
                            first_name
                    );

                    values.put(
                            AccountContract.AccountEntry.COLUMN_LAST_NAME,
                            last_name
                    );

                    values.put(
                            AccountContract.AccountEntry.COLUMN_NIP,
                            nip
                    );

                    long newRowId = db.insert(
                      AccountContract.AccountEntry.TABLE_NAME,
                            null,
                            values
                    );

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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button btnLogin = (Button) findViewById(R.id.login_button);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get the input
                EditText editEmail = (EditText) findViewById(R.id.edit_email);
                EditText editPassword = (EditText) findViewById(R.id.edit_password);

                email = editEmail.getText().toString();
                password = editPassword.getText().toString();

                new CheckLogin().execute();


            }
        });
    }
}
