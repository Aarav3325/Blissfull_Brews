package com.example.cafeorderingapp.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Cart.class}, version = 5)
public abstract class CartDatabase extends RoomDatabase {
    public abstract CartDAO getCartDAO();

    public static CartDatabase dbInstance;

    public static synchronized CartDatabase getDbInstance(Context context){
        if(dbInstance == null){
            dbInstance = Room.databaseBuilder(context.getApplicationContext(), CartDatabase.class, "cart_dv").fallbackToDestructiveMigration().build();
        }

        return dbInstance;
    }
}
