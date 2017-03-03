package com.cgstate.boxmobile.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/3/3.
 */

public class MyItemDecoration extends RecyclerView.ItemDecoration {
    public MyItemDecoration(int offset) {
        this.offset = offset;
    }

    private int offset;
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        if (childLayoutPosition%3!=0){
            outRect.right=offset/2;
            outRect.bottom=offset/2;
        }else {
            outRect.left=offset/2;
            outRect.right=offset/2;
            outRect.bottom=offset/2;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        Paint paint = new Paint();
//                paint.setColor(Color.parseColor("#B8B8B8"));
        paint.setColor(Color.parseColor("#D8D8D8"));
        paint.setStrokeWidth(offset);

        //获得RecyclerView中条目数量
        int childCount = parent.getChildCount();

        //遍历
        for (int i = 0; i < childCount; i++) {

            //获得子View，也就是一个条目的View，准备给他画上边框
            View childView = parent.getChildAt(i);

            //先获得子View的长宽，以及在屏幕上的位置
            float x = childView.getX();
            float y = childView.getY();
            int width = childView.getWidth();
            int height = childView.getHeight();

            if (i % 3==2){
                //h bottom
                c.drawLine(x, y + height, x + width, y + height, paint);
                continue;
            }else {
                c.drawLine(x + width, y, x + width, y + height, paint);
                //h bottom
                c.drawLine(x, y + height, x + width, y + height, paint);
                continue;
            }
//                    //根据这些点画条目的四周的线 h:水平 v:垂直
//                    //h top
//                    c.drawLine(x, y, x + width, y, paint);
//                    //v left
//                    c.drawLine(x, y, x, y + height, paint);
//                    //v right
//                    c.drawLine(x + width, y, x + width, y + height, paint);
//                    //h bottom
//                    c.drawLine(x, y + height, x + width, y + height, paint);
        }
        super.onDraw(c, parent, state);
    }
}
