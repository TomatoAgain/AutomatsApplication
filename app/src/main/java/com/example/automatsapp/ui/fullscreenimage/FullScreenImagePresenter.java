package com.example.automatsapp.ui.fullscreenimage;

public class FullScreenImagePresenter {
    private FullScreenImageActivity view;
    public FullScreenImagePresenter(FullScreenImageActivity view){
        this.view = view;
        if (!view.isUriNull())
        view.loadImage();
        else
        view.loadDefaultImage();
    }
}
