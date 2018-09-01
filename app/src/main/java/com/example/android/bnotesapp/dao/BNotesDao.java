package com.example.android.bnotesapp.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.bnotesapp.entity.BNotes;

import java.util.List;

/**
 * Created by gowri on 25-08-2018.
 */

@Dao
public interface BNotesDao{

    @Query("SELECT * FROM BNotes ORDER BY bNotesId DESC")
    LiveData<List<BNotes>> getAllNotes();

    @Query("SELECT * FROM BNotes WHERE bNotesId = :id ")
    LiveData<BNotes> getANote(int id);

    @Insert
    void inserNotes(BNotes... bNotes);

    @Update
    void updateNotes(BNotes... bNotes);

    @Delete
    void  deleteANote(BNotes... bNotes);

    @Query("SELECT COUNT(*) FROM BNotes WHERE bnotesPassword = :password AND bNotesId = :id")
    int checkPassword(int password, int id);
}
