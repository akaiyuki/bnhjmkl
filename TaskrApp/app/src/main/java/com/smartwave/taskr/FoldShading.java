package com.smartwave.taskr;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by smartwavedev on 6/15/16.
 */
public interface FoldShading {

    void onPreDraw(Canvas canvas, Rect bounds, float rotation, int gravity);

    void onPostDraw(Canvas canvas, Rect bounds, float rotation, int gravity);

}
