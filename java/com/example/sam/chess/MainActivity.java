package com.example.sam.chess;

import android.content.Intent;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.PopupWindow;
import android.widget.AdapterView;

import java.io.FileOutputStream;
import java.util.ArrayList;
import android.graphics.drawable.Drawable;
import java.io.File;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ArrayList<MoveRecord> Recordingdata = new ArrayList<MoveRecord>();
    boolean invalidmove = false;
    boolean undoed = true;
    TileView firstSelectedCell = null;
    boolean selectingFirst = true;
    Board chessBoard = null;
    int turn_num = 1;
    boolean whiteTurn = true;
    boolean draw = false;
    String winner = "none";
    boolean gameOver = false;
    TextView message1 = null;
    TextView message2 = null;
    TextView check = null;
    Spinner spinner = null;
    boolean promoting = false;
    boolean waspawn = false;
    char promo = 'x';
    boolean blackenpassant = false;
    boolean whiteenpassant = false;
    boolean blackkingleftcastle = false;
    boolean blackkingrightcastle = false;
    boolean whitekingleftcastle = false;
    boolean whitekingrightcastle = false;

    Piece fromPiece;
    Piece toPiece;
    String tolocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message1 = (TextView)findViewById(R.id.message1);
        message2 = (TextView)findViewById(R.id.message3);
        check = (TextView)findViewById(R.id.message4);
        chessBoard = new Board();
        chessBoard.init();
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        message2.setText("white's turn");
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        String item = parent.getItemAtPosition(position).toString();
        promo = item.charAt(0);
        if(item.charAt(0)=='K'){
            promo = 'N';
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void handleInput (View temp){
        char recordpromo = 'x';

        if(gameOver){
            return;
        }
        message1.setText("");
        TileView castTemp = (TileView) temp;
        String currentLabel = temp.getResources().getResourceEntryName(temp.getId());

        if(selectingFirst){

            String input = currentLabel;
            int xLocation = convert(input.charAt(1));
            int yLocation = toInt(input.charAt(0));
            if (chessBoard.board[xLocation][yLocation].piece == null){
                message1.setText("invalid select - empty tile");
                return;
            }
            else if (chessBoard.board[xLocation][yLocation].piece.white != whiteTurn){
                message1.setText("invalid select - opponent piece");
                return;
            }



            castTemp.setBackgroundColor(getResources().getColor(R.color.green));
            selectingFirst = false;
            firstSelectedCell = castTemp;


            for(int row =0; row <8; row++) {
                for (int col =0; col < 8; col++) {
                    if(chessBoard.board[row][col].piece!= null) {
                        chessBoard.board[row][col].piece.getPotentialMoves(whiteTurn);
                    }
                }
            }


            waspawn = (chessBoard.board[xLocation][yLocation].piece instanceof Pawn);

        }
        //Selecting the second cell
        else {
            firstSelectedCell.setBackgroundColor(getOriginalColor(firstSelectedCell.getResources().getResourceEntryName(firstSelectedCell.getId())));

            String input = currentLabel;
            int toX = convert(input.charAt(1));
            int toY = toInt(input.charAt(0));
            String oldInput = firstSelectedCell.getResources().getResourceEntryName(firstSelectedCell.getId());
            int fromX = convert(oldInput.charAt(1));
            int fromY = toInt(oldInput.charAt(0));

            //read in a promo if needed
            if (chessBoard.board[fromX][fromY].piece == null) {
                message1.setText("Illegal move, try again");
                selectingFirst = true;
                waspawn = false;
                return;
            }



            //make a move if possible

            if(toX!=0&&toX!=7) {
                if (chessBoard.board[toX + 1][toY].piece instanceof Pawn && whiteTurn == true && chessBoard.board[toX + 1][toY].piece.white != true && chessBoard.board[toX][toY].piece == null) {
                    Pawn temppawn = (Pawn) chessBoard.board[toX + 1][toY].piece;
                    if (temppawn.enpassant == true) {
                        whiteenpassant = true;
                    }
                }
                if(chessBoard.board[toX-1][toY].piece instanceof Pawn && whiteTurn != true && chessBoard.board[toX-1][toY].piece.white==true&&chessBoard.board[toX][toY].piece==null) {
                    Pawn temppawn = (Pawn)chessBoard.board[toX-1][toY].piece;
                    if(temppawn.enpassant==true) {
                        blackenpassant = true;
                    }
                }
            }
            if(chessBoard.board[fromX][fromY].piece instanceof King && whiteTurn==true && Math.abs(fromY - toY)==2 && toY == 6) {
                whitekingrightcastle=true;
            }
            if(chessBoard.board[fromX][fromY].piece instanceof King && whiteTurn==true && Math.abs(fromY - toY)==2 && toY == 2) {
                whitekingleftcastle=true;
            }
            if(chessBoard.board[fromX][fromY].piece instanceof King && whiteTurn!=true && Math.abs(fromY-toY)==2 && toY == 6){
                blackkingrightcastle = true;
            }
            if(chessBoard.board[fromX][fromY].piece instanceof King && whiteTurn!=true && Math.abs(fromY-toY)==2 && toY == 2){
                blackkingleftcastle = true;
            }


            //copying toPiece for undo


            if(chessBoard.board[fromX][fromY].getPiece().potentialMoves.contains((toX+","+toY))){
                fromPiece = copyPiece(chessBoard,fromX,fromY);
                System.out.println("contains move");
                if (chessBoard.board[toX][toY].piece == null) {
                    tolocation = "" + toX + "" + toY;
                    toPiece = null;
                } else {
                    toPiece = copyPiece(chessBoard,toX,toY);

                }
            }

            if (chessBoard.board[fromX][fromY].getPiece().move(fromX,fromY,toX, toY, whiteTurn, promo ) != 1) {
                message1.setText("invalid move");
                selectingFirst = true;
                waspawn = false;
                whiteenpassant = false;
                blackenpassant = false;
                whitekingleftcastle = false;
                whitekingrightcastle = false;
                blackkingrightcastle = false;
                blackkingleftcastle = false;

                return;
            }
            else {



                if(waspawn && whiteTurn == true){
                    if(toX==0){
                        promoting = true;
                    }
                }
                else if(waspawn && whiteTurn == false){
                    if(toX==7){
                        promoting = true;
                    }
                }


                System.out.println(""+oldInput+""+input);
                castTemp.setImageDrawable(firstSelectedCell.getDrawable());
                if(promoting == true){
                    if(whiteTurn == true) {
                        if (promo == 'N') {
                            recordpromo = 'N';
                            castTemp.setImageDrawable(getResources().getDrawable(R.drawable.wknight));
                        }
                        else if(promo == 'B'){
                            recordpromo = 'B';
                            castTemp.setImageDrawable(getResources().getDrawable(R.drawable.wbishop));
                        }
                        else if(promo == 'Q'){
                            recordpromo = 'Q';
                            castTemp.setImageDrawable(getResources().getDrawable(R.drawable.wqueen));
                        }
                        else if(promo == 'R'){
                            recordpromo = 'R';
                            castTemp.setImageDrawable(getResources().getDrawable(R.drawable.wrook));
                        }
                    }
                    else{
                        if (promo == 'N') {
                            recordpromo = 'N';
                            castTemp.setImageDrawable(getResources().getDrawable(R.drawable.bknight));
                        }
                        else if(promo == 'B'){
                            recordpromo = 'B';
                            castTemp.setImageDrawable(getResources().getDrawable(R.drawable.bbishop));
                        }
                        else if(promo == 'Q'){
                            recordpromo = 'Q';
                            castTemp.setImageDrawable(getResources().getDrawable(R.drawable.bqueen));
                        }
                        else if(promo == 'R'){
                            recordpromo = 'R';
                            castTemp.setImageDrawable(getResources().getDrawable(R.drawable.brook));
                        }
                    }
                }
                firstSelectedCell.setImageDrawable(null);
                if(whiteenpassant){
                    int temptoy = Character.getNumericValue(input.charAt(1));
                    temptoy = temptoy-1;
                    String tempid = ""+input.charAt(0)+temptoy;
                    int id = getResources().getIdentifier(tempid, "id", getPackageName());
                    ImageView enpassanttile = (ImageView) findViewById(id);
                    enpassanttile.setImageDrawable(null);
                }
                if(blackenpassant){
                    int temptoy = Character.getNumericValue(input.charAt(1));
                    temptoy = temptoy+1;
                    String tempid = ""+input.charAt(0)+temptoy;
                    int id = getResources().getIdentifier(tempid, "id", getPackageName());
                    ImageView enpassanttile = (ImageView) findViewById(id);
                    enpassanttile.setImageDrawable(null);
                }
                if(whitekingrightcastle){
                    String rookid = "h1";
                    int id = getResources().getIdentifier(rookid, "id", getPackageName());
                    ImageView castle = (ImageView) findViewById(id);
                    castle.setImageDrawable(null);
                    rookid = "f1";
                    id = getResources().getIdentifier(rookid, "id", getPackageName());
                    castle = (ImageView) findViewById(id);
                    castle.setImageDrawable(getResources().getDrawable(R.drawable.wrook));

                }
                if(whitekingleftcastle){
                    String rookid = "a1";
                    int id = getResources().getIdentifier(rookid, "id", getPackageName());
                    ImageView castle = (ImageView) findViewById(id);
                    castle.setImageDrawable(null);
                    rookid = "d1";
                    id = getResources().getIdentifier(rookid, "id", getPackageName());
                    castle = (ImageView) findViewById(id);
                    castle.setImageDrawable(getResources().getDrawable(R.drawable.wrook));
                }
                if(blackkingleftcastle){
                    String rookid = "a8";
                    int id = getResources().getIdentifier(rookid, "id", getPackageName());
                    ImageView castle = (ImageView) findViewById(id);
                    castle.setImageDrawable(null);
                    rookid = "d8";
                    id = getResources().getIdentifier(rookid, "id", getPackageName());
                    castle = (ImageView) findViewById(id);
                    castle.setImageDrawable(getResources().getDrawable(R.drawable.brook));
                }
                if(blackkingrightcastle){
                    String rookid = "h8";
                    int id = getResources().getIdentifier(rookid, "id", getPackageName());
                    ImageView castle = (ImageView) findViewById(id);
                    castle.setImageDrawable(null);
                    rookid = "f8";
                    id = getResources().getIdentifier(rookid, "id", getPackageName());
                    castle = (ImageView) findViewById(id);
                    castle.setImageDrawable(getResources().getDrawable(R.drawable.brook));
                }

                int recordEnpass = 0;
                int recordCastle = 0;
                if(whiteenpassant||blackenpassant){
                    recordEnpass = 1;
                }
                if(whitekingleftcastle||whitekingrightcastle||blackkingleftcastle||blackkingrightcastle){
                    recordCastle = 1;
                }

                MoveRecord temprecord = new MoveRecord(oldInput, input, recordpromo, recordCastle, recordEnpass);
                Recordingdata.add(temprecord);



                turn_num++;
                if (turn_num%2 == 1){
                    message2.setText("white's turn");
                    whiteTurn = true;
                }
                else{
                    message2.setText("black's turn");
                    whiteTurn = false;
                }
                undoed = false;
            }

            if(whiteTurn==true) {
                for(int i = 0; i<chessBoard.board.length ; i++) {
                    for(int j = 0; j<chessBoard.board[i].length; j++) {
                        if(chessBoard.board[i][j].piece instanceof Pawn) {
                            Pawn temp3 = (Pawn)chessBoard.board[i][j].piece;
                            if(temp3.white==true) {
                                //System.out.println("white changed");

                                temp3.enpassant = false;
                            }
                        }
                    }
                }
            }
            if(whiteTurn!=true) {
                for(int i = 0; i<chessBoard.board.length ; i++) {
                    for(int j = 0; j<chessBoard.board[i].length; j++) {
                        if(chessBoard.board[i][j].piece instanceof Pawn) {
                            Pawn temp3 = (Pawn)chessBoard.board[i][j].piece;
                            if(temp3.white!=true) {
                                //System.out.println("black changed");

                                temp3.enpassant = false;
                            }
                        }
                    }
                }
            }

            selectingFirst = true;
            waspawn = false;
            promoting = false;
            whiteenpassant = false;
            blackenpassant = false;
            whitekingleftcastle = false;
            whitekingrightcastle = false;
            blackkingrightcastle = false;
            blackkingleftcastle = false;
            invalidmove = false;
            check.setText("");
            if(chessBoard.checkWhite(chessBoard) == 1 || chessBoard.checkBlack(chessBoard) == 1 ) {
                check.setText("Check!");
            }



            for(int row =0; row <8; row++) {
                for (int col =0; col < 8; col++) {
                    if(chessBoard.board[row][col].piece!= null) {
                        chessBoard.board[row][col].piece.getPotentialMoves(whiteTurn);
                    }
                }
            }

            if (turn_num%2==0) {
                if(chessBoard.checkmate(false)) {
                    if (chessBoard.checkBlack(chessBoard) == 1) {
                        message1.setText("CHECKMATE! WHITE WINS - GameOver");
                        MoveRecord temprecord = new MoveRecord("WHITE WINS BY CHECKMATE!");
                        Recordingdata.add(temprecord);
                    }
                    else{
                        message1.setText("STALEMATE!");
                        MoveRecord temprecord = new MoveRecord("STALEMATE!");
                        Recordingdata.add(temprecord);
                    }

                    gameOver = true;
                    startActivityForResult(new Intent(getApplicationContext(), PopUp.class), 999);
                }
            }
            else {
                if(chessBoard.checkmate(true) == true) {
                    if (chessBoard.checkWhite(chessBoard) ==1 ){
                        message1.setText("CHECKMATE! BLACK WINS - GameOver");
                        MoveRecord temprecord = new MoveRecord("BLACK WINS BY CHECKMATE!");
                        Recordingdata.add(temprecord);
                    }
                    else{
                        message1.setText("STALEMATE!");
                        MoveRecord temprecord = new MoveRecord("STALEMATE!");
                        Recordingdata.add(temprecord);


                    }
                    gameOver= true;
                    startActivityForResult(new Intent(getApplicationContext(), PopUp.class), 999);
                }
            }


        }





    }
    public int getOriginalColor(String location){
        char first = location.charAt(0);
        char second = location.charAt(1);
        int firstInt = (int)first;

        if ((firstInt + second)%2 == 0){
            return getResources().getColor(R.color.colorPrimary);
        }
        else{
            return getResources().getColor(R.color.contrast);
        }


    }


    public static int toInt(char x) { //converting the rank input to array index number
        if(x=='a') {
            return 0;
        }
        else if(x=='b') {
            return 1;
        }
        else if(x=='c') {
            return 2;
        }
        else if(x=='d') {
            return 3;
        }
        else if(x=='e') {
            return 4;
        }
        else if(x=='f') {
            return 5;
        }
        else if(x=='g') {
            return 6;
        }
        else if(x=='h') {
            return 7;
        }
        else {
            return 99;
        }
    }

    public static char toChar(char x) { //converting the rank input to array index number
        if(x=='0') {
            return 'a';
        }
        else if(x=='1') {
            return 'b';
        }
        else if(x=='2') {
            return 'c';
        }
        else if(x=='3') {
            return 'd';
        }
        else if(x=='4') {
            return 'e';
        }
        else if(x=='5') {
            return 'f';
        }
        else if(x=='6') {
            return 'g';
        }
        else if(x=='7') {
            return 'h';
        }
        else {
            return 'x';
        }
    }

    public static int convert(char x) { //converting the file input to array index number
        if(x=='8') {
            return 0;
        }
        else if(x=='7') {
            return 1;
        }
        else if(x=='6') {
            return 2;
        }
        else if(x=='5') {
            return 3;
        }
        else if(x=='4') {
            return 4;
        }
        else if(x=='3') {
            return 5;
        }
        else if(x=='2') {
            return 6;
        }
        else if(x=='1') {
            return 7;
        }
        else {
            return -1;
        }
    }

    public static char revert(char x){
        if(x=='0') {
            return '8';
        }
        else if(x=='7') {
            return '1';
        }
        else if(x=='6') {
            return '2';
        }
        else if(x=='5') {
            return '3';
        }
        else if(x=='4') {
            return '4';
        }
        else if(x=='3') {
            return '5';
        }
        else if(x=='2') {
            return '6';
        }
        else if(x=='1') {
            return '7';
        }
        else {
            return 'x';
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        //if users pressed "yes", wanted the recording of the game
        if(requestCode == 999 && resultCode == RESULT_OK){
            System.out.println("yes recordings");

            String filename = data.getStringExtra("message");
            FileOutputStream outputStream;

            System.out.println(filename);
            Context context = getApplicationContext();
            System.out.println(context.getFilesDir().toString());
            File file = new File(context.getFilesDir(), filename);

            ArrayList<String> Recording = MoveRecordToString(Recordingdata);


            try{
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                for (int i = 0; i < Recording.size(); i++){

                    outputStream.write(Recording.get(i).getBytes());
                    System.out.println(Recording.get(i));
                }

                outputStream.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }




            return;
        }

        //if users pressed "no", did not want the recording of the game
        if(requestCode == 999 && resultCode == RESULT_CANCELED){
            System.out.println("no recordings");
            return;
        }


        //if we get a selected item back from recording popup
        if(requestCode == 888 && resultCode == RESULT_OK){
            System.out.println(data.getStringExtra("recording"));
        }

        //if user pressed cancel from recording popup
        if(requestCode == 888 && resultCode == RESULT_CANCELED){
            System.out.println(data.getStringExtra("recording"));
            return;
        }
        //if the user resigns
        if(requestCode == 111 && resultCode == RESULT_OK){
            gameOver =true;
            if(turn_num%2 == 1) {
                message1.setText("White player resigned! Black wins! - GameOver");
                MoveRecord temprecord = new MoveRecord("WHITE RESIGNS, BLACK WINS!");
                System.out.println(MoveRecord.add(temprecord));

                Recordingdata.add(temprecord);
            }
            else{
                message1.setText("Black player resigned! White wins! - GameOver");
                MoveRecord temprecord = new MoveRecord("BLACK RESIGNS, WHITE WINS!");
                System.out.println(MoveRecord.add(temprecord));
                Recordingdata.add(temprecord);
            }
            startActivityForResult(new Intent(getApplicationContext(), PopUp.class), 999);
            return;
        }
        if(requestCode == 222 && resultCode == RESULT_OK){
            gameOver =true;
            message1.setText("Draw! - GameOver");
            MoveRecord temprecord = new MoveRecord("GAMEOVER - DRAW");
            Recordingdata.add(temprecord);
            startActivityForResult(new Intent(getApplicationContext(), PopUp.class), 999);
            return;
        }
    }

    public void drawhandle(View view) {
        if(gameOver){
            return;
        }
        startActivityForResult(new Intent(getApplicationContext(), drawconfirm.class), 222);

        return;
    }

    public void resignhandle(View view) {
        if(gameOver){
            return;
        }
        startActivityForResult(new Intent(getApplicationContext(), resignconfirm.class), 111);

        return;
    }

    public void undo(View view) {
        if(gameOver||undoed){
            return;
        }
        chessBoard.board[fromPiece.x][fromPiece.y].piece = fromPiece;
        String tempid = ""+toChar(Integer.toString(fromPiece.y).charAt(0))+revert(Integer.toString(fromPiece.x).charAt(0));
        int id = getResources().getIdentifier(tempid, "id", getPackageName());
        ImageView fromtile = (ImageView) findViewById(id);
        if(fromPiece.white){
            if(fromPiece instanceof Pawn){
                fromtile.setImageDrawable(getResources().getDrawable(R.drawable.wpawn));
            }
            else if(fromPiece instanceof Knight){
                fromtile.setImageDrawable(getResources().getDrawable(R.drawable.wknight));
            }
            else if(fromPiece instanceof King){
                fromtile.setImageDrawable(getResources().getDrawable(R.drawable.wking));
                chessBoard.whiteKingX = fromPiece.x;
                chessBoard.whiteKingY = fromPiece.y;
            }
            else if(fromPiece instanceof Bishop){
                fromtile.setImageDrawable(getResources().getDrawable(R.drawable.wbishop));
            }
            else if(fromPiece instanceof Rook){
                fromtile.setImageDrawable(getResources().getDrawable(R.drawable.wrook));
            }
            else if(fromPiece instanceof Queen){
                fromtile.setImageDrawable(getResources().getDrawable(R.drawable.wqueen));
            }
        }
        else{
            if(fromPiece instanceof Pawn){
                fromtile.setImageDrawable(getResources().getDrawable(R.drawable.bpawn));
            }
            else if(fromPiece instanceof Knight){
                fromtile.setImageDrawable(getResources().getDrawable(R.drawable.bknight));
            }
            else if(fromPiece instanceof King){
                fromtile.setImageDrawable(getResources().getDrawable(R.drawable.bking));
                chessBoard.blackKingX = fromPiece.x;
                chessBoard.blackKingY = fromPiece.y;
            }
            else if(fromPiece instanceof Bishop){
                fromtile.setImageDrawable(getResources().getDrawable(R.drawable.bbishop));
            }
            else if(fromPiece instanceof Rook){
                fromtile.setImageDrawable(getResources().getDrawable(R.drawable.brook));
            }
            else if(fromPiece instanceof Queen){
                fromtile.setImageDrawable(getResources().getDrawable(R.drawable.bqueen));
            }
        }





        if(toPiece==null){
            System.out.println(tolocation);
            tempid = ""+toChar(tolocation.charAt(1))+revert(tolocation.charAt(0));
            System.out.println("the totile ID is: "+tempid);
            id = getResources().getIdentifier(tempid, "id", getPackageName());
            ImageView toTile = (ImageView) findViewById(id);
            toTile.setImageDrawable(null);
            chessBoard.board[Character.getNumericValue(tolocation.charAt(0))][Character.getNumericValue(tolocation.charAt(1))].piece= null;

        }
        else{
            chessBoard.board[toPiece.x][toPiece.y].piece = toPiece;
            tempid = ""+toChar(Integer.toString(toPiece.y).charAt(0))+revert(Integer.toString(toPiece.x).charAt(0));
            id = getResources().getIdentifier(tempid, "id", getPackageName());
            ImageView toTile = (ImageView) findViewById(id);
            if(toPiece.white){
                if(toPiece instanceof Pawn){
                    toTile.setImageDrawable(getResources().getDrawable(R.drawable.wpawn));
                }
                else if(toPiece instanceof Knight){
                    toTile.setImageDrawable(getResources().getDrawable(R.drawable.wknight));
                }
                else if(toPiece instanceof King){
                    toTile.setImageDrawable(getResources().getDrawable(R.drawable.wking));
                }
                else if(toPiece instanceof Bishop){
                    toTile.setImageDrawable(getResources().getDrawable(R.drawable.wbishop));
                }
                else if(toPiece instanceof Rook){
                    toTile.setImageDrawable(getResources().getDrawable(R.drawable.wrook));
                }
                else if(toPiece instanceof Queen){
                    toTile.setImageDrawable(getResources().getDrawable(R.drawable.wqueen));
                }
            }
            else{
                if(toPiece instanceof Pawn){
                    toTile.setImageDrawable(getResources().getDrawable(R.drawable.bpawn));
                }
                else if(toPiece instanceof Knight){
                    toTile.setImageDrawable(getResources().getDrawable(R.drawable.bknight));
                }
                else if(toPiece instanceof King){
                    toTile.setImageDrawable(getResources().getDrawable(R.drawable.bking));
                }
                else if(toPiece instanceof Bishop){
                    toTile.setImageDrawable(getResources().getDrawable(R.drawable.bbishop));
                }
                else if(toPiece instanceof Rook){
                    toTile.setImageDrawable(getResources().getDrawable(R.drawable.brook));
                }
                else if(toPiece instanceof Queen){
                    toTile.setImageDrawable(getResources().getDrawable(R.drawable.bqueen));
                }
            }
        }
        MoveRecord lastrecord = Recordingdata.get(Recordingdata.size()-1);
        System.out.println(MoveRecord.add(lastrecord));
        System.out.println(lastrecord.fromtile);
        System.out.println(lastrecord.toTile);
        if(lastrecord.enpassant == 1){
            System.out.println("in here");
            if(whiteTurn){

                //System.out.println(tolocation);
                int newxholder = Character.getNumericValue(tolocation.charAt(0))-1;
                int newyholder =  Character.getNumericValue(tolocation.charAt(1));
                //System.out.println("x is: "+newxholder+"   y is: "+newyholder);
                Pawn temppawn = new Pawn(newxholder, newyholder, "wp", chessBoard);
                temppawn.enpassant = true;
                chessBoard.board[newxholder][newyholder].piece = temppawn;
                char holder = revert(Integer.toString(Character.getNumericValue(tolocation.charAt(0))-1).charAt(0));
                tempid = ""+toChar(tolocation.charAt(1))+""+holder;
                id = getResources().getIdentifier(tempid, "id", getPackageName());
                ImageView pawntile = (ImageView) findViewById(id);
                pawntile.setImageDrawable(getResources().getDrawable(R.drawable.wpawn));
            }

            if(!whiteTurn){
                //System.out.println(tolocation);
                int newxholder = Character.getNumericValue(tolocation.charAt(0))+1;
                int newyholder =  Character.getNumericValue(tolocation.charAt(1));
                //System.out.println("x is: "+newxholder+"   y is: "+newyholder);
                Pawn temppawn = new Pawn(newxholder, newyholder, "bp", chessBoard);
                temppawn.enpassant = true;
                chessBoard.board[newxholder][newyholder].piece = temppawn;
                char holder = revert(Integer.toString(Character.getNumericValue(tolocation.charAt(0))+1).charAt(0));
                tempid = ""+toChar(tolocation.charAt(1))+""+holder;
                id = getResources().getIdentifier(tempid, "id", getPackageName());
                ImageView pawntile = (ImageView) findViewById(id);
                pawntile.setImageDrawable(getResources().getDrawable(R.drawable.bpawn));
            }
        }
        if(lastrecord.castle==1){
            System.out.println("hellloooo");
            if(whiteTurn) {
                System.out.println(tolocation);
                int newxholder = Character.getNumericValue(tolocation.charAt(0));
                int newyholder = Character.getNumericValue(tolocation.charAt(1));
                //System.out.println("x is: "+newxholder+"   y is: "+newyholder);
                if (newyholder == 6) {
                    Rook temprook = new Rook(0, 7, "bR", chessBoard);
                    chessBoard.board[0][7].piece = temprook;
                    tempid = "h8";
                    id = getResources().getIdentifier(tempid, "id", getPackageName());
                    ImageView rooktile = (ImageView) findViewById(id);
                    rooktile.setImageDrawable(getResources().getDrawable(R.drawable.brook));
                    tempid = "f8";
                    id = getResources().getIdentifier(tempid, "id", getPackageName());
                    ImageView emptytile = (ImageView) findViewById(id);
                    emptytile.setImageDrawable(null);
                    chessBoard.board[0][5].piece = null;
                    King tempking = new King(0, 4, "bK", chessBoard);
                    tempking.castle = true;
                    chessBoard.board[0][4].piece = tempking;
                    chessBoard.whiteKingX=0;
                    chessBoard.whiteKingY=4;
                } else if (newyholder == 2) {
                    Rook temprook = new Rook(0, 0, "bR", chessBoard);
                    chessBoard.board[0][0].piece = temprook;
                    tempid = "a8";
                    id = getResources().getIdentifier(tempid, "id", getPackageName());
                    ImageView rooktile = (ImageView) findViewById(id);
                    rooktile.setImageDrawable(getResources().getDrawable(R.drawable.brook));
                    tempid = "d8";
                    id = getResources().getIdentifier(tempid, "id", getPackageName());
                    ImageView emptytile = (ImageView) findViewById(id);
                    emptytile.setImageDrawable(null);
                    chessBoard.board[0][3].piece = null;
                    King tempking = new King(0, 4, "bK", chessBoard);
                    tempking.castle = true;
                    chessBoard.board[0][4].piece = tempking;
                    chessBoard.whiteKingX=0;
                    chessBoard.whiteKingY=4;
                }
            }
            if(!whiteTurn){
                System.out.println(tolocation);
                int newxholder = Character.getNumericValue(tolocation.charAt(0));
                int newyholder =  Character.getNumericValue(tolocation.charAt(1));
                System.out.println("x is: "+newxholder+"   y is: "+newyholder);
                if(newyholder==6){
                    System.out.println("iamhere");
                    Rook temprook = new Rook(7, 7, "wR", chessBoard);
                    chessBoard.board[7][7].piece = temprook;
                    tempid = "h1";
                    id = getResources().getIdentifier(tempid, "id", getPackageName());
                    ImageView rooktile = (ImageView) findViewById(id);
                    rooktile.setImageDrawable(getResources().getDrawable(R.drawable.wrook));
                    tempid = "f1";
                    id = getResources().getIdentifier(tempid, "id", getPackageName());
                    ImageView emptytile = (ImageView) findViewById(id);
                    emptytile.setImageDrawable(null);
                    chessBoard.board[7][5].piece = null;
                    King tempking = new King(7,4,"wK",chessBoard);
                    tempking.castle = true;
                    chessBoard.board[7][4].piece = tempking;
                    chessBoard.whiteKingX=7;
                    chessBoard.whiteKingY=4;
                }
                else if(newyholder == 2){
                    Rook temprook = new Rook(7, 0, "wR", chessBoard);
                    chessBoard.board[7][0].piece = temprook;
                    tempid = "a1";
                    id = getResources().getIdentifier(tempid, "id", getPackageName());
                    ImageView rooktile = (ImageView) findViewById(id);
                    rooktile.setImageDrawable(getResources().getDrawable(R.drawable.wrook));
                    tempid = "d1";
                    id = getResources().getIdentifier(tempid, "id", getPackageName());
                    ImageView emptytile = (ImageView) findViewById(id);
                    emptytile.setImageDrawable(null);
                    chessBoard.board[7][3].piece = null;
                    King tempking = new King(7,4,"wK",chessBoard);
                    tempking.castle = true;
                    chessBoard.board[7][4].piece = tempking;
                    chessBoard.whiteKingX=7;
                    chessBoard.whiteKingY=4;
                }

            }
        }
        toPiece = null;
        fromPiece =null;
        undoed = true;
        check.setText("");


        Recordingdata.remove(Recordingdata.size()-1);

        turn_num--;
        if (turn_num%2 == 1){
            message2.setText("white's turn");
            whiteTurn = true;
        }
        else{
            message2.setText("black's turn");
            whiteTurn = false;
        }
        return;
    }

    public void AI(View view) {
        if(gameOver){
            return;
        }
        if (!selectingFirst){
            firstSelectedCell.setBackgroundColor(getOriginalColor(firstSelectedCell.getResources().getResourceEntryName(firstSelectedCell.getId())));
            selectingFirst = true;

        }
        ArrayList<List<String>> wtf = new ArrayList<List<String>>();
        ArrayList<String> origPos = new ArrayList<String>();
        for(int row =0; row <8; row++) {
            for (int col =0; col < 8; col++) {
                if(chessBoard.board[row][col].piece!= null) {
                    chessBoard.board[row][col].piece.getPotentialMoves(whiteTurn);
                    if (chessBoard.board[row][col].piece.potentialMoves.size()==0){
                    }
                    else{
                        origPos.add(""+row+""+col+"");
                        wtf.add(chessBoard.board[row][col].piece.potentialMoves);
                    }
                }
            }
        }

        int randomNum = (int)(Math.random() * (wtf.size() - 1));


        int randomitem = (int) (Math.random() * (wtf.get(randomNum).size() -1));
        String randomMove = wtf.get(randomNum).get(randomitem);
        System.out.println("moving to(Preconverted : " + randomMove);
        System.out.println("moving from(Preconverted : " + origPos.get(randomNum));

        String convertedOrigPos = toChar(origPos.get(randomNum).charAt(1))+""+revert(origPos.get(randomNum).charAt(0));
        String oldInput = convertedOrigPos;
        String convertedRandomMove = toChar(randomMove.charAt(2))+""+revert(randomMove.charAt(0));
        String combinedInput = convertedOrigPos+","+convertedRandomMove;
        System.out.println("converted combined locations : " + combinedInput);
        String input = convertedRandomMove;


        int toX = convert(combinedInput.charAt(4));
        int toY = toInt(combinedInput.charAt(3));
        int fromX = convert(combinedInput.charAt(1));
        int fromY = toInt(combinedInput.charAt(0));

        System.out.println("fromXY: " + fromY+""+fromX);
        System.out.println("ToXY: " + toY+""+toX);

        String tempid1 = convertedOrigPos;
        int id1 = getResources().getIdentifier(tempid1, "id", getPackageName());
        firstSelectedCell= findViewById(id1);

        tempid1 = convertedRandomMove;
        id1= getResources().getIdentifier(tempid1, "id", getPackageName());
        TileView castTemp = findViewById(id1);
        char recordpromo = 'x';
        if (chessBoard.board[Character.getNumericValue(origPos.get(randomNum).charAt(0))][Character.getNumericValue(origPos.get(randomNum).charAt(1))].piece instanceof Pawn){
            waspawn = true;
        }


        //read in a promo if needed
        if(chessBoard.board[fromX][fromY].piece==null) {
            message1.setText("Illegal move, try again");
            selectingFirst = true;
            waspawn = false;
            return;
        }
        //make a move if possible

        if(toX!=0&&toX!=7) {
            if (chessBoard.board[toX + 1][toY].piece instanceof Pawn && whiteTurn == true && chessBoard.board[toX + 1][toY].piece.white != true && chessBoard.board[toX][toY].piece == null) {
                Pawn temppawn = (Pawn) chessBoard.board[toX + 1][toY].piece;
                if (temppawn.enpassant == true) {
                    whiteenpassant = true;
                }
            }
            if(chessBoard.board[toX-1][toY].piece instanceof Pawn && whiteTurn != true && chessBoard.board[toX-1][toY].piece.white==true&&chessBoard.board[toX][toY].piece==null) {
                Pawn temppawn = (Pawn)chessBoard.board[toX-1][toY].piece;
                if(temppawn.enpassant==true) {
                    blackenpassant = true;
                }
            }
        }
        if(chessBoard.board[fromX][fromY].piece instanceof King && whiteTurn==true && Math.abs(fromY - toY)==2 && toY == 6) {
            whitekingrightcastle=true;
        }
        if(chessBoard.board[fromX][fromY].piece instanceof King && whiteTurn==true && Math.abs(fromY - toY)==2 && toY == 2) {
            whitekingleftcastle=true;
        }
        if(chessBoard.board[fromX][fromY].piece instanceof King && whiteTurn!=true && Math.abs(fromY-toY)==2 && toY == 6){
            blackkingrightcastle = true;
        }
        if(chessBoard.board[fromX][fromY].piece instanceof King && whiteTurn!=true && Math.abs(fromY-toY)==2 && toY == 2){
            blackkingleftcastle = true;
        }



        if(chessBoard.board[fromX][fromY].getPiece().potentialMoves.contains((toX+","+toY))){
            fromPiece = copyPiece(chessBoard,fromX,fromY);
            System.out.println("contains move");
            if (chessBoard.board[toX][toY].piece == null) {
                tolocation = "" + toX + "" + toY;
                toPiece = null;
            } else {
                toPiece = copyPiece(chessBoard,toX,toY);
            }
        }

        if (chessBoard.board[fromX][fromY].getPiece().move(fromX,fromY,toX, toY, whiteTurn, promo ) != 1) {
            message1.setText("invalid move");
            selectingFirst = true;
            waspawn = false;
            whiteenpassant = false;
            blackenpassant = false;
            whitekingleftcastle = false;
            whitekingrightcastle = false;
            blackkingrightcastle = false;
            blackkingleftcastle = false;
            return;
        }
        else {
            if(waspawn && whiteTurn == true){
                if(toX==0){
                    promoting = true;
                }
            }
            else if(waspawn && whiteTurn == false){
                if(toX==7){
                    promoting = true;
                }
            }


            castTemp.setImageDrawable(firstSelectedCell.getDrawable());
            if(promoting == true){
                if(whiteTurn == true) {
                    if (promo == 'N') {
                        recordpromo = 'N';
                        castTemp.setImageDrawable(getResources().getDrawable(R.drawable.wknight));
                    }
                    else if(promo == 'B'){
                        recordpromo = 'B';
                        castTemp.setImageDrawable(getResources().getDrawable(R.drawable.wbishop));
                    }
                    else if(promo == 'Q'){
                        recordpromo = 'Q';
                        castTemp.setImageDrawable(getResources().getDrawable(R.drawable.wqueen));
                    }
                    else if(promo == 'R'){
                        recordpromo = 'R';
                        castTemp.setImageDrawable(getResources().getDrawable(R.drawable.wrook));
                    }
                }
                else{
                    if (promo == 'N') {
                        recordpromo = 'N';
                        castTemp.setImageDrawable(getResources().getDrawable(R.drawable.bknight));
                    }
                    else if(promo == 'B'){
                        recordpromo = 'B';
                        castTemp.setImageDrawable(getResources().getDrawable(R.drawable.bbishop));
                    }
                    else if(promo == 'Q'){
                        recordpromo = 'Q';
                        castTemp.setImageDrawable(getResources().getDrawable(R.drawable.bqueen));
                    }
                    else if(promo == 'R'){
                        recordpromo = 'R';
                        castTemp.setImageDrawable(getResources().getDrawable(R.drawable.brook));
                    }
                }
            }
            firstSelectedCell.setImageDrawable(null);
            if(whiteenpassant){
                int temptoy = Character.getNumericValue(input.charAt(1));
                temptoy = temptoy-1;
                String tempid = ""+input.charAt(0)+temptoy;
                int id = getResources().getIdentifier(tempid, "id", getPackageName());
                ImageView enpassanttile = (ImageView) findViewById(id);
                enpassanttile.setImageDrawable(null);
            }
            if(blackenpassant){
                int temptoy = Character.getNumericValue(input.charAt(1));
                temptoy = temptoy+1;
                String tempid = ""+input.charAt(0)+temptoy;
                int id = getResources().getIdentifier(tempid, "id", getPackageName());
                ImageView enpassanttile = (ImageView) findViewById(id);
                enpassanttile.setImageDrawable(null);
            }
            if(whitekingrightcastle){
                String rookid = "h1";
                int id = getResources().getIdentifier(rookid, "id", getPackageName());
                ImageView castle = (ImageView) findViewById(id);
                castle.setImageDrawable(null);
                rookid = "f1";
                id = getResources().getIdentifier(rookid, "id", getPackageName());
                castle = (ImageView) findViewById(id);
                castle.setImageDrawable(getResources().getDrawable(R.drawable.wrook));

            }
            if(whitekingleftcastle){
                String rookid = "a1";
                int id = getResources().getIdentifier(rookid, "id", getPackageName());
                ImageView castle = (ImageView) findViewById(id);
                castle.setImageDrawable(null);
                rookid = "d1";
                id = getResources().getIdentifier(rookid, "id", getPackageName());
                castle = (ImageView) findViewById(id);
                castle.setImageDrawable(getResources().getDrawable(R.drawable.wrook));
            }
            if(blackkingleftcastle){
                String rookid = "a8";
                int id = getResources().getIdentifier(rookid, "id", getPackageName());
                ImageView castle = (ImageView) findViewById(id);
                castle.setImageDrawable(null);
                rookid = "d8";
                id = getResources().getIdentifier(rookid, "id", getPackageName());
                castle = (ImageView) findViewById(id);
                castle.setImageDrawable(getResources().getDrawable(R.drawable.brook));
            }
            if(blackkingrightcastle){
                String rookid = "h8";
                int id = getResources().getIdentifier(rookid, "id", getPackageName());
                ImageView castle = (ImageView) findViewById(id);
                castle.setImageDrawable(null);
                rookid = "f8";
                id = getResources().getIdentifier(rookid, "id", getPackageName());
                castle = (ImageView) findViewById(id);
                castle.setImageDrawable(getResources().getDrawable(R.drawable.brook));
            }

            int recordEnpass = 0;
            int recordCastle = 0;
            if(whiteenpassant||blackenpassant){
                recordEnpass = 1;
            }
            if(whitekingleftcastle||whitekingrightcastle||blackkingleftcastle||blackkingrightcastle){
                recordCastle = 1;
            }

            MoveRecord temprecord = new MoveRecord(oldInput, input, recordpromo, recordCastle, recordEnpass);
            Recordingdata.add(temprecord);



            turn_num++;
            if (turn_num%2 == 1){
                message2.setText("white's turn");
                whiteTurn = true;
            }
            else{
                message2.setText("black's turn");
                whiteTurn = false;
            }
        }

        if(whiteTurn==true) {
            for(int i = 0; i<chessBoard.board.length ; i++) {
                for(int j = 0; j<chessBoard.board[i].length; j++) {
                    if(chessBoard.board[i][j].piece instanceof Pawn) {
                        Pawn temp3 = (Pawn)chessBoard.board[i][j].piece;
                        if(temp3.white==true) {
                            //System.out.println("white changed");

                            temp3.enpassant = false;
                        }
                    }
                }
            }
        }
        if(whiteTurn!=true) {
            for(int i = 0; i<chessBoard.board.length ; i++) {
                for(int j = 0; j<chessBoard.board[i].length; j++) {
                    if(chessBoard.board[i][j].piece instanceof Pawn) {
                        Pawn temp3 = (Pawn)chessBoard.board[i][j].piece;
                        if(temp3.white!=true) {
                            //System.out.println("black changed");

                            temp3.enpassant = false;
                        }
                    }
                }
            }
        }
        selectingFirst = true;
        waspawn = false;
        promoting = false;
        whiteenpassant = false;
        blackenpassant = false;
        whitekingleftcastle = false;
        whitekingrightcastle = false;
        blackkingrightcastle = false;
        blackkingleftcastle = false;
        check.setText("");
        undoed = false;
        if(chessBoard.checkWhite(chessBoard) == 1 || chessBoard.checkBlack(chessBoard) == 1 ) {
            check.setText("Check!");
        }



        for(int row =0; row <8; row++) {
            for (int col =0; col < 8; col++) {
                if(chessBoard.board[row][col].piece!= null) {
                    chessBoard.board[row][col].piece.getPotentialMoves(whiteTurn);
                }
            }
        }

        if (turn_num%2==0) {
            if(chessBoard.checkmate(false)) {
                if (chessBoard.checkBlack(chessBoard) == 1) {
                    message1.setText("CHECKMATE! WHITE WINS - GameOver");
                    MoveRecord temprecord = new MoveRecord("WHITE WINS BY CHECKMATE!");
                    Recordingdata.add(temprecord);
                }
                else{
                    message1.setText("STALEMATE!");
                    MoveRecord temprecord = new MoveRecord("STALEMATE!");
                    Recordingdata.add(temprecord);
                }

                gameOver = true;
                startActivityForResult(new Intent(getApplicationContext(), PopUp.class), 999);
            }
        }
        else {
            if(chessBoard.checkmate(true) == true) {
                if (chessBoard.checkWhite(chessBoard) ==1 ){
                    message1.setText("CHECKMATE! BLACK WINS - GameOver");
                    MoveRecord temprecord = new MoveRecord("BLACK WINS BY CHECKMATE!");
                    Recordingdata.add(temprecord);
                }
                else{
                    message1.setText("STALEMATE!");
                    MoveRecord temprecord = new MoveRecord("STALEMATE!");
                    Recordingdata.add(temprecord);


                }
                gameOver= true;
                startActivityForResult(new Intent(getApplicationContext(), PopUp.class), 999);
            }
        }






        return;

    }

    public ArrayList<String> MoveRecordToString(ArrayList<MoveRecord> input){
        ArrayList<String> result= new ArrayList<String>();
        for(int i =0; i<input.size();i++){
            result.add(MoveRecord.add(input.get(i)));
        }
        return result;
    }

    public Piece copyPiece (Board currentBoard, int fromX, int fromY){
        Piece copiedPiece = null;
        if (currentBoard.board[fromX][fromY].piece == null){
            return copiedPiece;
        }
        if (currentBoard.board[fromX][fromY].piece instanceof Rook) {
            copiedPiece = new Rook(fromX, fromY, currentBoard.board[fromX][fromY].piece.name, currentBoard);
        } else if (currentBoard.board[fromX][fromY].piece instanceof Bishop) {
            copiedPiece = new Bishop(fromX, fromY, currentBoard.board[fromX][fromY].piece.name, currentBoard);
        } else if (currentBoard.board[fromX][fromY].piece instanceof Knight) {
            copiedPiece = new Knight(fromX, fromY, currentBoard.board[fromX][fromY].piece.name, currentBoard);
        } else if (currentBoard.board[fromX][fromY].piece instanceof Pawn) {
            copiedPiece = new Pawn(fromX, fromY, currentBoard.board[fromX][fromY].piece.name, currentBoard);
        } else if (currentBoard.board[fromX][fromY].piece instanceof King) {
            copiedPiece = new King(fromX, fromY, currentBoard.board[fromX][fromY].piece.name, currentBoard);
        } else if (currentBoard.board[fromX][fromY].piece instanceof Queen) {
            copiedPiece = new Queen(fromX, fromY, currentBoard.board[fromX][fromY].piece.name, currentBoard);
        }
        return copiedPiece;
    }
}
