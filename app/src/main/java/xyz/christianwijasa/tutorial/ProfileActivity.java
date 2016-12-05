package xyz.christianwijasa.tutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    JSONParser jsonParser = new JSONParser();

    private static String url_member_details = "http://192.168.1.4"+
            "/android/lucid/view_member_details.php";

    private static String url_edit_member = "http://192.168.1.4"+
            "/android/lucid/edit_member.php";

    EditText text_first_name, text_last_name, text_nip;

    //variable untuk mengambil i.getExtra("member_id");
    String member_id;

    //variable untuk edit data
    String edit_first_name, edit_last_name, edit_nip;

    private static final String TAG_SUCCESS = "success";

    //Tag untuk menunjuk array JSON
    private static final String TAG_ARRAY = "member_data";

    //Tag untuk mengambil data kolom JSON
    private static final String TAG_MEMBER_ID="member_id";
    private static final String TAG_FIRST_NAME = "nama_depan";
    private static final String TAG_LAST_NAME = "nama_belakang";
    private static final String TAG_NIP = "nip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //tombol ubah dan keluar
        Button btnUbah = (Button) findViewById(R.id.btn_ubah_profile);
        Button btnKeluar = (Button) findViewById(R.id.btn_keluar_profile);

        //getting member details from intent
        Intent i = getIntent();
        member_id = i.getStringExtra(TAG_MEMBER_ID);

        new GetMemberDetails().execute();



        btnUbah.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                edit_first_name = text_first_name.getText().toString();
                edit_last_name  = text_last_name.getText().toString();
                edit_nip = text_nip.getText().toString();

              new SaveMemberDetails().execute();
            }
        });


        TextView ubah_kata_sandi = (TextView) findViewById(R.id.ubah_kata_sandi);
        ubah_kata_sandi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,ChangePasswordActivity.class);
                intent.putExtra(TAG_MEMBER_ID,member_id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    //AsyncTask untuk mengambil data member untuk ditampilkan dalam PROFILE
    class GetMemberDetails extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Loading member data. Please wait...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //updatin UI from background thread
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    //check for success tag
                    int success;
                    try{
                        //building parameters
                        List<Pair<String, String>> args =
                                new ArrayList<Pair<String,String>>();
                        args.add(new Pair<>(TAG_MEMBER_ID, member_id));
                        JSONObject jsonObject = null;
                        try{
                            //getting member details by making HTTP Request
                            jsonObject = jsonParser.makeHttpRequest(url_member_details,"POST",args);

                        }catch(IOException e){
                            Log.d("Networking", e.getLocalizedMessage());
                        }

                        Log.d("Member details", jsonObject.toString());
                        success = jsonObject.getInt(TAG_SUCCESS);
                        if(success == 1){

                            JSONArray memberObj = jsonObject
                                    .getJSONArray(TAG_ARRAY);

                            JSONObject member = memberObj.getJSONObject(0);

                            //EditText
                            //get the input for update data
                            text_first_name  = (EditText) findViewById(R.id.edit_nama_depan);
                            text_last_name   = (EditText) findViewById(R.id.edit_nama_belakang);
                            text_nip         = (EditText) findViewById(R.id.edit_nip);

                            text_first_name.setText(member.getString(TAG_FIRST_NAME));
                            text_last_name.setText(member.getString(TAG_LAST_NAME));
                            text_nip.setText(member.getString(TAG_NIP));
                        }else{

                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            });
            return null;

        }




        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
    }

    //AsyncTask untuk mengubah data member pada menu PROFILE
    class SaveMemberDetails extends AsyncTask<String, String, String>{
        //before starting background thread, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Saving member data. Please wait...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            List<Pair<String, String>> args =
                    new ArrayList<Pair<String,String>>();

            args.add(new Pair<>(TAG_MEMBER_ID,member_id));
            args.add(new Pair<>(TAG_FIRST_NAME,edit_first_name));
            args.add(new Pair<>(TAG_LAST_NAME,edit_last_name));
            args.add(new Pair<>(TAG_NIP,edit_nip));
            JSONObject jsonObject = null;

            try{
                //sending modified data through HTPP Request

                jsonObject = jsonParser
                        .makeHttpRequest(url_edit_member,"POST",args);
            }catch(IOException e){
                Log.d("Networking", e.getLocalizedMessage());
            }

            //check JSON success tag

            try{
                int success = jsonObject.getInt(TAG_SUCCESS);

                if(success == 1){
                    //sucessfully updated
                    Intent i = getIntent();
                    //send result code 100 to notify about product update

                    setResult(100,i);
                    finish();
                }else{
                    //failed to update member data
                }
            }catch(JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //dismiss the dialog once done
            progressDialog.dismiss();
        }
    }



}
