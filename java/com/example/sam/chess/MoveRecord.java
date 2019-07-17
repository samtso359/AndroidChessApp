package com.example.sam.chess;

public class MoveRecord {
    String fromtile;
    String toTile;
    char promotion = 'x';

    int castle = 0;
    int enpassant = 0;
    String endTag = "";
    public MoveRecord(String fromtile, String toTile, char promotion, int castle, int enpassant){
        this.fromtile = fromtile;
        this.toTile = toTile;
        this.promotion = promotion;
        this.castle = castle;
        this.enpassant = enpassant;
        this.endTag = "";

    }
    public MoveRecord(String endTag){
        this.endTag = endTag;

    }


    public static String add(MoveRecord record){
        if(record.endTag.compareTo("") == 0){
            String holder = ""+record.fromtile+record.toTile+record.promotion+record.castle+record.enpassant+"\n";
            return holder;
        }
        else{
            return record.endTag+"\n";
        }
    }
}
