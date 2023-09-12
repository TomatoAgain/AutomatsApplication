package com.example.automatsapp.ui.automatmaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.automatsapp.R;

import java.io.ByteArrayOutputStream;

public class AutomatMakerActivity extends AppCompatActivity {

    private AutomatMakerPresenter presenter;
    private ConstraintLayout mLayout;
    private Dialog dialog;
    private EditText arrowText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automat_maker);
        presenter = new AutomatMakerPresenter(this);
    }

    public void loadCanvas() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.arrows_dialog);
        ImageView addCircle = findViewById(R.id.imageview_automatmaker_circle);
        mLayout = findViewById(R.id.layout_automatmaker_canvas);
        ImageButton save = findViewById(R.id.imagebutton_automatmaker_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              presenter.saveButtonClicked();
            }
        });
        addCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               presenter.circleButtonClicked();
            }
        });
        ImageView addArrow = findViewById(R.id.imageview_automatmaker_arrow);
        addArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.arrowButtonClicked();
            }
        });
        ImageView addRecieving = findViewById(R.id.imageview_automatmaker_recievingcircle);
        addRecieving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               presenter.recievingButtonClicked();
            }
        });

    }
    public void openArrowDialog(){
        dialog.show();
        ImageView arrowToSelf = dialog.findViewById(R.id.imageview_automatmaker_arrowtoself);
        ImageView arrowToSelf2 = dialog.findViewById(R.id.imageview_automatmaker_arrowtoself2);
        ImageView arrowToSelf3 = dialog.findViewById(R.id.imageview_automatmaker_arrowtoself3);
        ImageView arrowToSelf4 = dialog.findViewById(R.id.imageview_automatmaker_arrowtoself4);
        ImageView upperArrow = dialog.findViewById(R.id.imageview_automatmaker_uparrow);
        ImageView downArrow = dialog.findViewById(R.id.imageview_automatmaker_downarrow);
        ImageView rightArrow = dialog.findViewById(R.id.imageview_automatmaker_rightarrow);
        ImageView leftArrow = dialog.findViewById(R.id.imageview_automatmaker_leftarrow);
        ImageView downleftArrow = dialog.findViewById(R.id.imageview_automatmaker_downleftarrow);
        ImageView downrightArrow = dialog.findViewById(R.id.imageview_automatmaker_downrightarrow);
        ImageView upperRightArrow = dialog.findViewById(R.id.imageview_automatmaker_upperrightarrow);
        ImageView upperLeftArrow = dialog.findViewById(R.id.imageview_automatmaker_upperleftarrow);
        arrowText = dialog.findViewById(R.id.edittext_automatmaker_arrowtext);
        arrowToSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.arrowToSelfClicked();
            }
        });
        arrowToSelf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.arrowToSelf2Clicked();
            }
        });
        arrowToSelf3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.arrowToSelf3Clicked();
            }
        });
        arrowToSelf4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.arrowToSelf4Clicked();
            }
        });
        upperArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.upperArrowClicked();
            }
        });
        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.downArrowClicked();
            }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.rightArrowClicked();
            }
        });
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               presenter.leftArrowClicked();
            }
        });
        downleftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.downLeftArrowClicked();
            }
        });
        downrightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.downRightArrowClicked();
            }
        });
        upperRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               presenter.upperRightArrowClicked();
            }
        });
        upperLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             presenter.upperLeftArrowClicked();
            }
        });


    }
    public void deleteView(View view) {
        mLayout = findViewById(R.id.layout_automatmaker_canvas);
        mLayout.removeView(view);
    }
    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    public void savePicture() {
        Bitmap bitmap = getBitmapFromView(mLayout);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show();
    }

    public void addCircle(int circleCount) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.automat_circle);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 250, 250, presenter, 1, 1, circleCount, "");
        mLayout.addView(objectView);
    }

    public void addReceivingCircle(int circleCount) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.recieving);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 250, 250, presenter, 1, 1, circleCount, "");
        mLayout.addView(objectView);
    }

    public void addArrowToSelf() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrowtoself);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 100, 100, presenter, 1, 1, 0, arrowText.getText().toString());
        mLayout.addView(objectView);
        dialog.dismiss();
    }
    public void addArrowToSelf2() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrowtoselfrotated);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 100, 99, presenter, 1, 1, 0, arrowText.getText().toString());
        mLayout.addView(objectView);
        dialog.dismiss();
    }
    public void addArrowToSelf3() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrowtoself);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 100, 100, presenter, -1, -1, 0, arrowText.getText().toString());
        mLayout.addView(objectView);
        dialog.dismiss();
    }
    public void addArrowToSelf4() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrowtoselfrotated);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 100, 99, presenter, -1, -1, 0, arrowText.getText().toString());
        mLayout.addView(objectView);
        dialog.dismiss();
    }
    public void addUpperArrow(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.verticalarrow);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 150, 40, presenter, -1, -1, 0, arrowText.getText().toString());
        mLayout.addView(objectView);
        dialog.dismiss();
    }
    public void addDownArrow(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.verticalarrow);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 150, 40, presenter, 1, 1, 0, arrowText.getText().toString());
        mLayout.addView(objectView);
        dialog.dismiss();
    }
    public void addRightArrow(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 80, 200, presenter,  1, 1, 0, arrowText.getText().toString());
        mLayout.addView(objectView);
        dialog.dismiss();
    }
    public void addLeftArrow(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 80, 200, presenter, -1, -1, 0, arrowText.getText().toString());
        mLayout.addView(objectView);
        dialog.dismiss();
    }
    public void addDownLeftArrow(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.diagonalarrow);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 120, 120, presenter, -1, 1, 0, arrowText.getText().toString());
        mLayout.addView(objectView);
        dialog.dismiss();
    }
    public void addDownRightArrow(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.diagonalarrow);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 120, 120, presenter, 1, 1, 0, arrowText.getText().toString());
        mLayout.addView(objectView);
        dialog.dismiss();
    }
    public void addUpperRightArrow(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.diagonalarrow);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 120, 120, presenter, 1, -1, 0, arrowText.getText().toString());
        mLayout.addView(objectView);
        dialog.dismiss();
    }
    public void addUpperLeftArrow(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.diagonalarrow);
        DraggableObjectView objectView = new DraggableObjectView(AutomatMakerActivity.this, bitmap, 120, 120, presenter, -1, -1, 0, arrowText.getText().toString());
        mLayout.addView(objectView);
        dialog.dismiss();
    }


}