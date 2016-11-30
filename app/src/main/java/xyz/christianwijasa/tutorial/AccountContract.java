package xyz.christianwijasa.tutorial;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Richard Firdaus on 27/11/2016.
 */

public class AccountContract {

    //mencegah constructor dipanggil
    private AccountContract(){}

    public static class AccountEntry implements BaseColumns{
        public static final String TABLE_NAME  = "member";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_NIP = "nip";

    }

    public static final String SQL_CREATE_ACCOUNTS = String.format(
            "CREATE TABLE %s(%s, %s, %s, %s, %s)",
            AccountEntry.TABLE_NAME,
            String.format("%s BIGINT PRIMARY KEY AUTOINCREMENT", AccountEntry._ID),
            String.format("%s VARCHAR(120)", AccountEntry.COLUMN_EMAIL),
            String.format("%s VARCHAR(120)", AccountEntry.COLUMN_FIRST_NAME),
            String.format("%s VARCHAR(120)", AccountEntry.COLUMN_LAST_NAME),
            String.format("%s VARCHAR(120", AccountEntry.COLUMN_NIP)
    );

    public static final String SQL_DELETE_ACCOUNTS = String.format(
            "DROP TABLE IF EXISTS %s",
            AccountEntry.TABLE_NAME
    );




}
