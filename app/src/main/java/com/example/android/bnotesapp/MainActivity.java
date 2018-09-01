package com.example.android.bnotesapp;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.bnotesapp.adpater.BNotesRecyclerAdapter;
import com.example.android.bnotesapp.databinding.ActivityMainBinding;
import com.example.android.bnotesapp.entity.BNotes;
import com.example.android.bnotesapp.util.RecyclerClickHandler;
import com.example.android.bnotesapp.util.RecyclerItemTouchHelper;
import com.example.android.bnotesapp.viewmodel.BNotesViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private List<BNotes> mbNotes = new ArrayList<>();
    private BNotesRecyclerAdapter adapter;
    private RelativeLayout relative_main;
    private BNotes deletedNotes;
    private BNotesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        relative_main = findViewById(R.id.relative_main);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerBnotes.setLayoutManager(layoutManager);

        viewModel = ViewModelProviders.of(this).get(BNotesViewModel.class);

        viewModel.getBnotesList().observe(this, new Observer<List<BNotes>>() {
            @Override
            public void onChanged(@Nullable List<BNotes> bNotes) {
                mbNotes = bNotes;
                adapter.updateAdapter(bNotes);
            }
        });

        adapter = new BNotesRecyclerAdapter(mbNotes, new RecyclerClickHandler() {
            @Override
            public void OnClick(final View view, final BNotes bNotes) {
                if(bNotes.locked){
                    @SuppressLint("InflateParams") final View passworddialog = LayoutInflater.from(view.getContext()).inflate(R.layout.secure_note_dialog,null,false);
                    android.app.AlertDialog.Builder builder= new android.app.AlertDialog.Builder(view.getContext())
                            .setTitle("PIN required")
                            .setView(passworddialog)
                            .setCancelable(false)
                            .setPositiveButton("CHECK", new DialogInterface.OnClickListener() {
                                @SuppressLint("StaticFieldLeak")
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final EditText editTextPassword = passworddialog.findViewById(R.id.bnotes_password);
                                    if(!editTextPassword.getText().toString().isEmpty()){
                                        final int password = Integer.parseInt(editTextPassword.getText().toString());
                                        final int bnotesId = bNotes.bNotesId;
                                        int iscorrect = viewModel.checkPIN(password,bnotesId);
                                        if(iscorrect == 1){
                                            goToNextActivity(view);
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),"PIN Incorrect",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.show();
                }
                else {
                    goToNextActivity(view);
                }
            }
        });
        binding.recyclerBnotes.setAdapter(adapter);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_add_bnotes);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("InflateParams") final View dialogview = getLayoutInflater().inflate(R.layout.new_notes_dialog, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("New Notes")
                        .setCancelable(false)
                        .setView(dialogview)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText bnotesTitile = dialogview.findViewById(R.id.bnotes_title_edittext);
                                EditText bnotesBody = dialogview.findViewById(R.id.bnotes_body_edittext);

                                if (!bnotesBody.getText().toString().isEmpty() && !bnotesTitile.getText().toString().isEmpty()) {
                                    viewModel.insert(new BNotes(bnotesTitile.getText().toString(), bnotesBody.getText().toString(),false,0));
                                    binding.recyclerBnotes.smoothScrollToPosition(0);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerBnotes);
    }


    private void goToNextActivity(View view){
        Intent detailedIntent = new Intent(this, NotesDetailActivity.class);
        detailedIntent.putExtra("notes_id", view.getTag().toString());
        startActivity(detailedIntent);
    }

    @Override
    public void OnSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        final int notesId = mbNotes.get(viewHolder.getAdapterPosition()).getbNotesId();

        deletedNotes = mbNotes.get(viewHolder.getAdapterPosition());
        final int deletedIndex = viewHolder.getAdapterPosition();

        if(deletedNotes.locked){
            @SuppressLint("InflateParams") final View passworddialog = LayoutInflater.from(this).inflate(R.layout.secure_note_dialog,null,false);
            android.app.AlertDialog.Builder builder= new android.app.AlertDialog.Builder(this)
                    .setTitle("PIN required")
                    .setView(passworddialog)
                    .setCancelable(false)
                    .setPositiveButton("CHECK", new DialogInterface.OnClickListener() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final EditText editTextPassword = passworddialog.findViewById(R.id.bnotes_password);
                            if(!editTextPassword.getText().toString().isEmpty()){
                                final int password = Integer.parseInt(editTextPassword.getText().toString());
                                int iscorrect = viewModel.checkPIN(password, notesId);
                                if(iscorrect == 1){
                                    adapter.removeNotes(position);
                                    viewModel.remove(deletedNotes);
                                    showSnackBar(deletedIndex,notesId);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"PIN Incorrect",Toast.LENGTH_SHORT).show();
                                    adapter.notifyItemChanged(position);
                                }
                            }
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            adapter.notifyItemChanged(position);
                        }
                    });
            builder.show();
        }
        else {
            adapter.removeNotes(position);
            viewModel.remove(deletedNotes);
            showSnackBar(deletedIndex,notesId);
        }
    }

    private void showSnackBar(final int deletedIndex, final int notesId) {
        Snackbar snackbar = Snackbar.make(relative_main, "Deleted a Note", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.restoreNotes(deletedNotes, deletedIndex);
                        deletedNotes.bNotesId = notesId;
                        viewModel.insert(deletedNotes);
                    }
                });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }
}
