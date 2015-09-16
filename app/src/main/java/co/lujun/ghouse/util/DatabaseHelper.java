package co.lujun.ghouse.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import co.lujun.ghouse.bean.Bill;
import co.lujun.ghouse.bean.House;
import co.lujun.ghouse.bean.Image;
import co.lujun.ghouse.bean.User;

/**
 * Created by lujun on 2015/9/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private final static String DB_NAME = "ghouse.db";

    private static DatabaseHelper mDatabaseHelper;

    private DatabaseHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            // create tables here
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, House.class);
            TableUtils.createTableIfNotExists(connectionSource, Bill.class);
            TableUtils.createTableIfNotExists(connectionSource, Image.class);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,
                          ConnectionSource connectionSource, int i, int i1) {
        try {
            // drop tables here
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, House.class, true);
            TableUtils.dropTable(connectionSource, Bill.class, true);
            TableUtils.dropTable(connectionSource, Image.class, true);
            // then create tables again
            onCreate(sqLiteDatabase, connectionSource);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * return the singleton instance
     * @param context
     * @return
     */
    public static DatabaseHelper getDatabaseHelper(Context context){
        if (mDatabaseHelper == null){
            synchronized (DatabaseHelper.class){
                if (mDatabaseHelper == null){
                    mDatabaseHelper = new DatabaseHelper(context);
                }
            }
        }
        return mDatabaseHelper;
    }

    /**
     * get DAO
     * @param clazz
     * @return
     * @throws SQLException
     */
    public synchronized Dao getDao(Class clazz) throws SQLException{
        return super.getDao(clazz);
    }

    @Override
    public void close() {
        super.close();
    }
}
