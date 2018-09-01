package com.example.android.bnotesapp.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


@Entity
public class BNotes {
    @PrimaryKey(autoGenerate = true)
    public int bNotesId;
    public final String bNotesTitle;
    public String bNotesBody;
    public boolean locked;
    public int bnotesPassword;

    @Ignore
    public BNotes(int bNotesId, String bNotesTitle, String bNotesBody, boolean locked, int bnotesPassword) {
        this.bNotesId = bNotesId;
        this.bNotesTitle = bNotesTitle;
        this.bNotesBody = bNotesBody;
        this.locked = locked;
        this.bnotesPassword = bnotesPassword;
    }

    public BNotes(String bNotesTitle, String bNotesBody, boolean locked, int bnotesPassword) {
        this.bNotesTitle = bNotesTitle;
        this.bNotesBody = bNotesBody;
        this.locked = locked;
        this.bnotesPassword = bnotesPassword;
    }

    public int getbNotesId() {
        return bNotesId;
    }
    public void setbNotesId(int bNotesId) {
        this.bNotesId = bNotesId;
    }
}
