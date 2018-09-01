package com.example.android.bnotesapp.adpater;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.bnotesapp.NotesDetailActivity;
import com.example.android.bnotesapp.R;
import com.example.android.bnotesapp.databinding.BnotesListBinding;
import com.example.android.bnotesapp.db.BNotesDataBase;
import com.example.android.bnotesapp.entity.BNotes;
import com.example.android.bnotesapp.util.RecyclerClickHandler;

import java.util.List;

public class BNotesRecyclerAdapter extends RecyclerView.Adapter<BNotesRecyclerAdapter.MyViewHolder>{

    private List<BNotes> bNotes;
    private BnotesListBinding binding;
    private RecyclerClickHandler clickHandler;

    public BNotesRecyclerAdapter(List<BNotes> bNotes, RecyclerClickHandler clickHandler) {
        this.bNotes = bNotes;
        this.clickHandler = clickHandler;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BnotesListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.bnotes_list,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        binding = holder.binding;
        holder.bind(bNotes.get(position));
        binding.setBnotes(bNotes.get(position));
        holder.binding.cardBnotes.setTag(bNotes.get(position).getbNotesId());
    }

    @Override
    public int getItemCount() {
        if(null != bNotes && !bNotes.isEmpty()){
            return bNotes.size();
        }
        return 0;
    }

    public void updateAdapter(List<BNotes> bNotes){
        if(!bNotes.isEmpty()){
            this.bNotes = bNotes;
            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private BnotesListBinding binding;
        public MyViewHolder(BnotesListBinding itemView) {
            super(itemView.cardBnotes);
            this.binding = itemView;
        }

        public void bind(final BNotes bNotes) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.OnClick(view,bNotes);
                }
            });
        }
    }

    public void removeNotes(int position){
        bNotes.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreNotes(BNotes bNote, int position){
        bNotes.add(position, bNote);
        notifyItemInserted(position);
    }
}
