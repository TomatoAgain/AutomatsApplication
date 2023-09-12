package com.example.automatsapp.ui.automatmaker;

import android.view.View;

import java.util.ArrayList;

class AutomatMakerPresenter implements DraggableObjectView.DeleteObjectListener{
    private AutomatMakerActivity view;
    private ArrayList<Integer> circleCountList;
    private int totalCircleNumber;

    public AutomatMakerPresenter(AutomatMakerActivity view){
        this.view = view;
        circleCountList = new ArrayList<>();
        totalCircleNumber = 0;
        view.loadCanvas();
    }

    @Override
    public void onObjectDraggedToDelete(View view, boolean isCircle, int index) {
        if (isCircle) {
            circleCountList.set(index, 1);
        }
        this.view.deleteView(view);
    }

    public void arrowButtonClicked(){
        view.openArrowDialog();
    }
    public void saveButtonClicked(){
        view.savePicture();
    }
    public void circleButtonClicked(){
       addCircle(false);
    }
    public void recievingButtonClicked(){
       addCircle(true);
    }
    public void arrowToSelfClicked(){
        view.addArrowToSelf();
    }
    public void arrowToSelf2Clicked() {
        view.addArrowToSelf2();
    } public void arrowToSelf3Clicked() {
        view.addArrowToSelf3();
    } public void arrowToSelf4Clicked() {
        view.addArrowToSelf4();
    }
    public void upperArrowClicked(){
        view.addUpperArrow();
    }
    public void downArrowClicked(){
        view.addDownArrow();
    }
    public void rightArrowClicked(){
        view.addRightArrow();
    }
    public void leftArrowClicked(){
        view.addLeftArrow();
    }
    public void downLeftArrowClicked(){
        view.addDownLeftArrow();
    }
    public void downRightArrowClicked(){
        view.addDownRightArrow();
    }
    public void upperRightArrowClicked(){
        view.addUpperRightArrow();
    }
    public void upperLeftArrowClicked() {
        view.addUpperLeftArrow();
    }
    public void addCircle(boolean isReceiving){
        int i = 0;
        boolean check = false;
        while(i < totalCircleNumber&&!check){
            if (circleCountList.get(i)==1){
                circleCountList.set(i, 0);
                check = true;
            }
            else
            i++;
        }
        if (!check) {
            circleCountList.add(i, 0);
            totalCircleNumber++;
        }
        if (isReceiving)
            view.addReceivingCircle(i);
        else
            view.addCircle(i);
    }


}
