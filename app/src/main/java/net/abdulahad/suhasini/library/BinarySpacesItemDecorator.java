package net.abdulahad.suhasini.library;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class BinarySpacesItemDecorator extends RecyclerView.ItemDecoration {

        private final int space;

        public BinarySpacesItemDecorator(Context context, int dimenId) {
            this.space = context.getResources().getDimensionPixelSize(dimenId);
        }

        @Override
        public void getItemOffsets(Rect outRect, @NotNull View view, RecyclerView parent, @NotNull RecyclerView.State state) {

            outRect.top = space;
            outRect.bottom = 0;


            boolean evenPos = parent.getChildLayoutPosition(view) % 2 == 0;
            // Add top margin only for the first item to avoid double space between items
            if (evenPos) {
                outRect.right = space / 2;
                outRect.left = space;
            } else {
                outRect.left = space / 2;
                outRect.right = space;
            }
        }
    }