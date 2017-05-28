package com.foxy_corporation.d_studiotestapp.model.data_base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.foxy_corporation.d_studiotestapp.model.data.UserData;
import com.foxy_corporation.d_studiotestapp.presenter.inside.InsidePresenter;
import com.foxy_corporation.d_studiotestapp.view.MyApplication;

import java.util.ArrayList;


/**
 * Created by Vlad on 26.05.2017.
 */

public class DataBaseModelImp implements DataBaseModel {
    private static final String TABLE_CREATION_REQUEST
            = "create table userDatasTable (id integer primary key autoincrement, username text, last_login text);";

    private static final String DATABASE_NAME = "userDatasDB";

    private static final String USER_DATAS_TABLE_NAME = "userDatasTable";

    private static final String USERNAME_KEY = "username";

    private static final String LAST_LOGIN_KEY = "last_login";

    private static final int STATUS_GETTING_DATA_FROM_BASE_SUCCESSFUL = 1;

    private static final int STATUS_GETTING_DATA_FROM_BASE_NO_DATA = 2;

    private static final int STATUS_SAVING_DATA = 3;

    private static final String LIST_KEY = "list";

    private InsidePresenter mInsidePresenter;

    public DataBaseModelImp (InsidePresenter insidePresenter) {
        this.mInsidePresenter = insidePresenter;
    }

    @Override
    public void saveUserDatas(final ArrayList<UserData> userDatasList) {
        Runnable runnable = new Runnable() {
            public void run() {
                ContentValues cv = new ContentValues();

                DBHelper dbHelper = new DBHelper(MyApplication.getAppContext());

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                db.delete(USER_DATAS_TABLE_NAME, null, null);

                Message msg = handler.obtainMessage(STATUS_SAVING_DATA);

                for (int i = 0; i < userDatasList.size(); i++) {
                    cv.put(USERNAME_KEY, userDatasList.get(i).getUsername());
                    cv.put(LAST_LOGIN_KEY, userDatasList.get(i).getLast_login());

                    db.insert(USER_DATAS_TABLE_NAME, null, cv);
                }

                dbHelper.close();

                handler.sendMessage(msg);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case STATUS_SAVING_DATA:
                    mInsidePresenter.setDataBaseUpdateFinished();
                    break;
                case STATUS_GETTING_DATA_FROM_BASE_SUCCESSFUL:
                    Bundle bundle = msg.getData();

                    ArrayList<UserData> arrayList = bundle.getParcelableArrayList(LIST_KEY);

                    mInsidePresenter.setDataFromDataBase(arrayList);

                    mInsidePresenter.setDataBaseUpdateFinished();
                    break;
                case STATUS_GETTING_DATA_FROM_BASE_NO_DATA:
                    mInsidePresenter.setNoDataInDatabase();
                    break;
            }
        }
    };


    @Override
    public void getUserDatas() {
        Runnable runnable = new Runnable() {
            public void run() {
                DBHelper dbHelper = new DBHelper(MyApplication.getAppContext());

                SQLiteDatabase db = dbHelper.getReadableDatabase();

                ArrayList<UserData> usersList = new ArrayList<>();

                Message msg;

                Cursor c = db.query(USER_DATAS_TABLE_NAME, null, null, null, null, null, null);

                if (c.moveToFirst()) {

                    int nameColIndex = c.getColumnIndex(USERNAME_KEY);
                    int lastLoginColIndex = c.getColumnIndex(LAST_LOGIN_KEY);

                    do {
                        /*Log.d("Value in DB: ", ", name == " + c.getString(nameColIndex) + ", last_login == " + c.getString(lastLoginColIndex));*/

                        usersList.add(new UserData(c.getString(nameColIndex), c.getString(lastLoginColIndex)));

                    } while (c.moveToNext());

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(LIST_KEY, usersList);

                    msg = handler.obtainMessage(STATUS_GETTING_DATA_FROM_BASE_SUCCESSFUL);
                    msg.setData(bundle);

                } else
                    msg = handler.obtainMessage(STATUS_GETTING_DATA_FROM_BASE_NO_DATA);

                c.close();

                dbHelper.close();

                handler.sendMessage(msg);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    private class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATION_REQUEST);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    }
}
