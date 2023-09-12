package com.example.automatsapp.ui.automatmaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.automatsapp.R;

class DraggableObjectView extends View implements View.OnTouchListener {

   private float mLastTouchX;
   private float mLastTouchY;
   Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.automat_circle);
   private int currentObjectHeight = 0;
   private int currentObjectWidth = 0;
   private int currentObjectXRotation = 1;
   private int currentObjectYRotation = 1;
   private int circleCount = 0;
   private String currentText = "";
   private DeleteObjectListener listener;

   public interface DeleteObjectListener{
      void onObjectDraggedToDelete(View view, boolean isCircle, int index);
   }
   public DraggableObjectView(Context context) {
      super(context);
      setOnTouchListener(this);
   }

   public DraggableObjectView(Context context, @Nullable AttributeSet attrs) {
      super(context, attrs);
      setOnTouchListener(this);
   }
   public DraggableObjectView(Context context, Bitmap bitmap, int height, int width, DeleteObjectListener listener, int xRotation, int yRotation, int count, String text) {
      super(context);
      this.bitmap = bitmap;
      this.currentObjectHeight = height;
      this.currentObjectWidth = width;
      this.listener = listener;
      this.currentObjectXRotation = xRotation;
      this.currentObjectYRotation = yRotation;
      this.circleCount = count;
      this.currentText = text;
      setOnTouchListener(this);
   }

   public DraggableObjectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      setOnTouchListener(this);
   }

   @Override
   protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      Paint paint = new Paint();
      paint.setTextSize(100);
      if (!(currentObjectWidth==0)) {
         if (currentObjectWidth == 250)
            canvas.drawText("q" + circleCount, 375, 450, paint);
         else{
            paint.setTextSize(50);
            if(currentObjectWidth == 100){
               if (currentObjectXRotation == 1)
               canvas.drawText(currentText, 350-8*currentText.length(), 295, paint);
               else{
                  canvas.drawText(currentText, 350-8*currentText.length(), 430, paint);
               }
            }
            else if(currentObjectWidth == 99){
               if (currentObjectXRotation == 1)
                  canvas.drawText(currentText, 400, 355, paint);
               else{
                  canvas.drawText(currentText, 270-16*currentText.length(), 355, paint);
               }
            }
            else if (currentObjectWidth == 200) {
               canvas.drawText(currentText, 375 - 8 * currentText.length(), 355-currentObjectXRotation*40, paint);
            }
            else if(currentObjectWidth==40){
               if(currentObjectYRotation==1)
               canvas.drawText(currentText, 345, 375, paint);
               else
                  canvas.drawText(currentText, 295-17*currentText.length(), 375, paint);
            }
            else{
               if (currentObjectXRotation*currentObjectYRotation==1)
               canvas.drawText(currentText, 340, 325, paint);
               else{
                  canvas.drawText(currentText, 350-14*currentText.length(), 325, paint);
               }
            }
         }
         canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, currentObjectXRotation * currentObjectWidth, currentObjectYRotation * currentObjectHeight, false), 300, 300, null);
      }
   }

   @Override
   public boolean onTouch(View view, MotionEvent event) {
         switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
               mLastTouchX = event.getX();
               mLastTouchY = event.getY();
               break;
            case MotionEvent.ACTION_MOVE:
                  float dx = event.getX() - mLastTouchX;
                  float dy = event.getY() - mLastTouchY;
                  if (view.getX() > 450 && view.getY() < -450 && listener != null) {
                     listener.onObjectDraggedToDelete(view, currentObjectWidth == 250, circleCount);
                  } else {
                     view.setX(view.getX() + dx);
                     view.setY(view.getY() + dy);
                     mLastTouchX = event.getX();
                     mLastTouchY = event.getY();
                  }
               break;
            default:
               return false;
         }
      return true;
   }
}
