package com.jacob.pchat;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Task.class},version = 2)
public abstract class DatabaseClass extends RoomDatabase {
    public abstract TaskDao getTask();
    static final Migration migration_1_2=new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    public static DatabaseClass databaseClass;

    public static synchronized DatabaseClass getInstance(Context x){
        if(databaseClass==null){
            databaseClass= Room.databaseBuilder(x,DatabaseClass.class,"taskManager").addMigrations(migration_1_2).allowMainThreadQueries().build();
        }
        return databaseClass;
    }


}
