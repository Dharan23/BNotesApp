package com.example.android.bnotesapp.util;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.android.bnotesapp.R;
import com.example.android.bnotesapp.adpater.BNotesRecyclerAdapter;


public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.OnSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder != null){
            final View foregroundview = viewHolder.itemView.findViewById(R.id.view_foreground);
            getDefaultUIUtil().onSelected(foregroundview);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        final View foregroundview = viewHolder.itemView.findViewById(R.id.view_foreground);
        getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundview,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundview = viewHolder.itemView.findViewById(R.id.view_foreground);
        getDefaultUIUtil().clearView(foregroundview);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        final View foregroundview = viewHolder.itemView.findViewById(R.id.view_foreground);
        getDefaultUIUtil().onDraw(c,recyclerView,foregroundview,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener{
        void OnSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
