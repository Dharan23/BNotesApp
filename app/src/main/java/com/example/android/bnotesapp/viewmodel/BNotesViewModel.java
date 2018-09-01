package com.example.android.bnotesapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.bnotesapp.entity.BNotes;
import com.example.android.bnotesapp.repository.BNotesRepository;

import java.util.List;

public class BNotesViewModel extends AndroidViewModel {

    private LiveData<List<BNotes>> bnotesList;
    private BNotesRepository bNotesRepository;

    public BNotesViewModel(@NonNull Application application) {
        super(application);
        bNotesRepository = new BNotesRepository(application);
        bnotesList = bNotesRepository.getBnotesList();
    }

    public LiveData<List<BNotes>> getBnotesList() {
        return bnotesList;
    }

    public LiveData<BNotes> getANote(int bnotesId){
        return bNotesRepository.getANote(bnotesId);
    }

    public void insert(BNotes bNotes){
        bNotesRepository.insert(bNotes);
    }

    public void remove(BNotes bNotes){
        bNotesRepository.remove(bNotes);
    }

    public int checkPIN(int PIN, int bnotesId){
        return bNotesRepository.checkPIN(PIN, bnotesId);
    }

    public void setPIN(BNotes bNotes) {
        bNotesRepository.setPIN(bNotes);
    }

    public void updateNotes(BNotes bNotes) {
        bNotesRepository.updateNotes(bNotes);
    }
}
