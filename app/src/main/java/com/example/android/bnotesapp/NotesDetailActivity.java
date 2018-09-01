package com.example.android.bnotesapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bnotesapp.databinding.ActivityNotesDetailBinding;
import com.example.android.bnotesapp.db.BNotesDataBase;
import com.example.android.bnotesapp.entity.BNotes;
import com.example.android.bnotesapp.util.ClickHandler;
import com.example.android.bnotesapp.viewmodel.BNotesViewModel;

public class NotesDetailActivity extends AppCompatActivity implements ClickHandler {

    private BNotes bNotes = new BNotes("", "", false, 0);
    private ActivityNotesDetailBinding binding;
    private BNotesViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        int notesId = Integer.parseInt(intent.getStringExtra("notes_id"));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notes_detail);
        binding.setClick(this);

        viewModel = ViewModelProviders.of(this).get(BNotesViewModel.class);

        viewModel.getANote(notesId).observe(this, new Observer<BNotes>() {
            @Override
            public void onChanged(@Nullable BNotes bNotes) {
                binding.setBnotes(bNotes);
            }
        });

        ImageView lockImage = findViewById(R.id.bnotes_lock_img);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_detail_view_menu, menu);
        return true;
    }

    boolean enableSaveMenu = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.share_notes_menu_item:
                Intent sharing_intent = new Intent(Intent.ACTION_SEND);
                sharing_intent.putExtra(Intent.EXTRA_TEXT, binding.tvNotesBody.getText().toString());
                sharing_intent.putExtra(Intent.EXTRA_SUBJECT, binding.bnotesTitle.getText().toString());
                sharing_intent.setType("text/html");
                startActivity(sharing_intent);
                break;
            case R.id.edit_notes_menu_item:
                enableSaveMenu = !enableSaveMenu;
                invalidateOptionsMenu();
                enableEditTextForNotes(item);
                break;
            case R.id.save_notes_menu_item:
                enableSaveMenu = !enableSaveMenu;
                invalidateOptionsMenu();
                disableEditTextForNotes(item);
                bNotes = binding.getBnotes();
                bNotes.bNotesBody = binding.etNotesBody.getText().toString();
                viewModel.updateNotes(bNotes);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (enableSaveMenu) {
            menu.findItem(R.id.save_notes_menu_item).setVisible(true);
            menu.findItem(R.id.edit_notes_menu_item).setVisible(false);
        } else {
            menu.findItem(R.id.save_notes_menu_item).setVisible(false);
            menu.findItem(R.id.edit_notes_menu_item).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void enableEditTextForNotes(MenuItem menuItem) {
        EditText editTextNotesBody = findViewById(R.id.et_notes_body);
        editTextNotesBody.setVisibility(View.VISIBLE);
        TextView textViewNotesBody = findViewById(R.id.tv_notes_body);
        textViewNotesBody.setVisibility(View.GONE);
    }

    private void disableEditTextForNotes(MenuItem item) {
        EditText editTextNotesBody = findViewById(R.id.et_notes_body);
        editTextNotesBody.setVisibility(View.GONE);
        TextView textViewNotesBody = findViewById(R.id.tv_notes_body);
        textViewNotesBody.setVisibility(View.VISIBLE);
    }

    @Override
    public void lockClick(View view) {
        if (!binding.getBnotes().locked) {
            lockClick("UNLOCK");
        }
    }

    public void lockClick(String flag) {
        final View securitydialogview = getLayoutInflater().inflate(R.layout.secure_note_dialog, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Secure Notes?")
                .setCancelable(false)
                .setView(securitydialogview)
                .setPositiveButton("SET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText password = securitydialogview.findViewById(R.id.bnotes_password);
                        if (!password.getText().toString().isEmpty()) {
                            int pin = Integer.parseInt(password.getText().toString());
                            if (pin != 0 && !(password.getText().toString().length() < 4)) {
                                bNotes = binding.getBnotes();
                                bNotes.locked = true;
                                bNotes.bnotesPassword = pin;
                                viewModel.setPIN(bNotes);
                                showToastMessage("Notes is now Secured",Toast.LENGTH_SHORT);
                            } else {
                                showToastMessage("PIN should be 4 characters length.",Toast.LENGTH_SHORT);
                            }
                        } else {
                            showToastMessage("PIN should be 4 characters length.", Toast.LENGTH_SHORT);
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

    private void showToastMessage(String message, int length){
        Toast.makeText(getApplicationContext(),message,length).show();
    }
}
