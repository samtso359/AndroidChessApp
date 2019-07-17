package com.example.sam.chess;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TileView extends android.support.v7.widget.AppCompatImageView {
    public String currentPiece;
    /*The label will describe the location of this tile. For example: a6 or f2.*/
    public String label;

    public TileView(Context givenContext) {
        super(givenContext);
        this.currentPiece = "empty";
        this.label = "nowhere";
    }

    public TileView(Context givenContext, AttributeSet givenAttrs) {
        super(givenContext, givenAttrs);
        this.currentPiece = "empty";
        this.label = "nowhere";
    }

    public TileView(Context givenContext, AttributeSet givenAttrs, int givenStyleAttr) {
        super(givenContext, givenAttrs, givenStyleAttr);
        this.currentPiece = "empty";
        this.label = "nowhere";
    }



}
