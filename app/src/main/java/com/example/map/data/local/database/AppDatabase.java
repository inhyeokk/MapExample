package com.example.map.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.map.data.local.dao.FavoriteDocumentDao;
import com.example.map.data.local.model.DocumentEntity;

@Database(entities = {DocumentEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoriteDocumentDao favoriteDocumentDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, AppDatabase.class, "app-database").build();
                }
            }
        }
        return instance;
    }
}
