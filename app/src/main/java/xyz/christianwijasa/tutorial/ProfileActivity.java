package xyz.christianwijasa.tutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    JSONParser jsonParser = new JSONParser();

    ArrayList<HashMap<String,String>> memberData;

    EditText first_name, last_name, nip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AccountContract.AccountDbHelper accountDbHelper
                = new AccountContract.AccountDbHelper(this);

        SQLiteDatabase db = accountDbHelper.getReadableDatabase();

//        String[] projection = {
//                AccountContract.AccountEntry.
//        }
        first_name  = (EditText) findViewById(R.id.edit_nama_depan);
        last_name   = (EditText) findViewById(R.id.edit_nama_belakang);
        nip         = (EditText) findViewById(R.id.edit_nip);

        TextView ubah_kata_sandi = (TextView) findViewById(R.id.ubah_kata_sandi);
        ubah_kata_sandi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,ChangePasswordActivity.class));
            }
        });
    }



}
