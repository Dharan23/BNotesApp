package com.example.android.bnotesapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.android.bnotesapp.dao.BNotesDao;
import com.example.android.bnotesapp.db.BNotesDataBase;
import com.example.android.bnotesapp.entity.BNotes;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by gowri on 01-09-2018.
 */

public class BNotesRepository {

    private LiveData<List<BNotes>> bnotesList;
    private BNotesDao bNotesDao;

    public BNotesRepository(Application application) {
        BNotesDataBase bNotesDataBase = BNotesDataBase.getInstance(application);
        bNotesDao = bNotesDataBase.getBNotesDao();
        bnotesList = bNotesDao.getAllNotes();
    }

    public LiveData<List<BNotes>> getBnotesList() {
        return bnotesList;
    }

    public void insert(BNotes bNotes){
        new InsertAsyncTask(bNotesDao).execute(bNotes);
    }

    public void remove(BNotes bNotes) {
        new deleteAsycnTask(bNotesDao).execute(bNotes);
    }

    public int checkPIN(int pin, int bnotesId) {
        int[] pinandid = new int[2];

        pinandid[0] = pin;
        pinandid[1] = bnotesId;
        try {
            return new checkPinAsync(bNotesDao).execute(pinandid).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public LiveData<BNotes> getANote(int bnotesId) {
        try {
            return new getABNoteAsyncTask(bNotesDao).execute(bnotesId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPIN(BNotes bNotes) {
        new updateAsyncTask(bNotesDao).execute(bNotes);
    }

    public void updateNotes(BNotes bNotes) {
        new updateAsyncTask(bNotesDao).execute(bNotes);
    }

    private static class getABNoteAsyncTask extends AsyncTask<Integer, Void, LiveData<BNotes>> {
        BNotesDao bNotesDao;
        public getABNoteAsyncTask(BNotesDao bNotesDao) {
            this.bNotesDao = bNotesDao;
        }

        @Override
        protected LiveData<BNotes> doInBackground(Integer... integers) {
            LiveData<BNotes> bNotes = bNotesDao.getANote(integers[0]);
            return bNotes;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<BNotes,Void,Void>{
        BNotesDao bNotesDao;
        public InsertAsyncTask(BNotesDao bNotesDao) {
            this.bNotesDao = bNotesDao;
        }
        @Override
        protected Void doInBackground(BNotes... bNotes) {
            bNotesDao.inserNotes(bNotes);
            return null;
        }
    }

    private static class deleteAsycnTask extends AsyncTask<BNotes,Void,Void>{
        BNotesDao bNotesDao;
        public deleteAsycnTask(BNotesDao bNotesDao) {
            this.bNotesDao = bNotesDao;
        }
        @Override
        protected Void doInBackground(BNotes... bNotes) {
            bNotesDao.deleteANote(bNotes);
            return null;
        }
    }

    private static class checkPinAsync extends AsyncTask<int[],Void,Integer>{
        BNotesDao bNotesDao;
        public checkPinAsync(BNotesDao bNotesDao) {
            this.bNotesDao = bNotesDao;
        }
        @Override
        protected Integer doInBackground(int[]... ints) {
            return bNotesDao.checkPassword(ints[0][0],ints[0][1]);
        }
    }

    private static class updateAsyncTask extends AsyncTask<BNotes,Void,Void>{
        BNotesDao bNotesDao;
        public updateAsyncTask(BNotesDao bNotesDao) {
            this.bNotesDao = bNotesDao;
        }
        @Override
        protected Void doInBackground(BNotes... bNotes) {
            bNotesDao.updateNotes(bNotes);
            return null;
        }
    }
}
