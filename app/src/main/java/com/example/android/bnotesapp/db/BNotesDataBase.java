package com.example.android.bnotesapp.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.bnotesapp.dao.BNotesDao;
import com.example.android.bnotesapp.entity.BNotes;

/**
 * Created by gowri on 25-08-2018.
 */

@Database(entities = {BNotes.class},version = 2,exportSchema = false)
public abstract class BNotesDataBase extends RoomDatabase{
    private static final String DB_NAME = "bnotesdatabase.db";
    private static volatile BNotesDataBase instance;

    public static synchronized BNotesDataBase getInstance(Context context){
        if(instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static BNotesDataBase create(Context context) {
        return Room.databaseBuilder(context, BNotesDataBase.class,DB_NAME).addMigrations(MIGRATE_1_2).build();
    }

    static final Migration MIGRATE_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'bnotes' ADD COLUMN 'locked' INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE 'bnotes' ADD COLUMN 'bnotesPassword' INTEGER NOT NULL DEFAULT 0");
            Log.d("", "migrate: " + "called migration");
        }
    };

    public abstract BNotesDao getBNotesDao();
}
