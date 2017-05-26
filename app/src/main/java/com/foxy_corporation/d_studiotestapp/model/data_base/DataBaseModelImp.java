package com.foxy_corporation.d_studiotestapp.model.data_base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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

                Message msg = handler.obtainMessage();

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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();

            ArrayList<UserData> arrayList = bundle.getParcelableArrayList(LIST_KEY);

            if (arrayList != null) {
                /*Log.d("DataBaseModelImp...handleMessage...getting!", "!");*/

                /*for (int i = 0; i < arrayList.size(); i++) {
                    Log.d(i + " bebebe," + arrayList.get(i).getUsername(), arrayList.get(i).getUsername());
                }*/

                mInsidePresenter.setDataFromDataBase(arrayList);
            }
            else {
                /*Log.d("DataBaseModelImp...handleMessage...saving!", "!");*/

                mInsidePresenter.setDataBaseUpdateFinished();
            }
        }
    };


    @Override
    public void getUserDatas() {
        Runnable runnable = new Runnable() {
            public void run() {
                DBHelper dbHelper = new DBHelper(MyApplication.getAppContext());

                SQLiteDatabase db = dbHelper.getReadableDatabase();

                Message msg = handler.obtainMessage();

                ArrayList<UserData> usersList = new ArrayList<>();

                Cursor c = db.query(USER_DATAS_TABLE_NAME, null, null, null, null, null, null);

                if (c.moveToFirst()) {

                    int nameColIndex = c.getColumnIndex(USERNAME_KEY);
                    int lastLoginColIndex = c.getColumnIndex(LAST_LOGIN_KEY);

                    do {
                        /*Log.d("Value in DB: ", ", name == " + c.getString(nameColIndex) + ", last_login == " + c.getString(lastLoginColIndex));*/

                        usersList.add(new UserData(c.getString(nameColIndex), c.getString(lastLoginColIndex)));

                    } while (c.moveToNext());
                } else
                    Log.d("TAG", "0 rows");

                c.close();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(LIST_KEY, usersList);
                msg.setData(bundle);

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
