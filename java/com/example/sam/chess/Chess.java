package com.example.sam.chess;

import java.util.Scanner;

/**
 * <h1>Chess</h1>
 * This is 2-player chess program
 * @author Laishek Tso, Andrew Ma
 * @since 10/30/2018
 */




/**
 * This class execute the entire chess program, which includes asking user for inputs, and printing the chess board, etc.
 *
 */
public class Chess {

    /**
     * This method is to check rather an user input is valid or not (although is not required for this assignment)
     * @return A boolean which determines rather a user input is valid or not
     * @param x - is a string input
     */
    public static boolean InvalidInput(String x) {
        if(x.length()!=5&&x.length()!=7&&x.length()!=11) {
            //System.out.println("Length has to be 5");
            return true;
        }
        else if(x.charAt(0)!='a'&&x.charAt(0)!='b'&&x.charAt(0)!='c'&&x.charAt(0)!='d'&&x.charAt(0)!='e'&&x.charAt(0)!='f'&&x.charAt(0)!='g'&&x.charAt(0)!='h') {
            //System.out.println("first character has to be a-h");
            return true;
        }
        else if(x.charAt(1)!='1'&&x.charAt(1)!='2'&&x.charAt(1)!='3'&&x.charAt(1)!='4'&&x.charAt(1)!='5'&&x.charAt(1)!='6'&&x.charAt(1)!='7'&&x.charAt(1)!='8') {
            //System.out.println("second character has to be a number");
            return true;
        }
        else if(x.charAt(2)!=' ') {
            //System.out.println("thrid character has to be a space");
            return true;
        }
        else if(x.charAt(3)!='a'&&x.charAt(3)!='b'&&x.charAt(3)!='c'&&x.charAt(3)!='d'&&x.charAt(3)!='e'&&x.charAt(3)!='f'&&x.charAt(3)!='g'&&x.charAt(3)!='h') {
            //System.out.println("third character has to be a-h");
            return true;
        }
        else if(x.charAt(4)!='1'&&x.charAt(4)!='2'&&x.charAt(4)!='3'&&x.charAt(4)!='4'&&x.charAt(4)!='5'&&x.charAt(4)!='6'&&x.charAt(4)!='7'&&x.charAt(4)!='8') {
            //System.out.println("forth character has to be a number");
            return true;
        }
        else if(x.length()==7&&(x.charAt(6)!=('Q')&&x.charAt(6)!=('R')&&x.charAt(6)!=('N')&&x.charAt(6)!=('B'))) {
            //System.out.println("can only choose Q,R,N,B for the pawn to promote into");
            return true;
        }
        return false;
    }

    /**
     * This methods convert the letter of the ranks, a - h, into numbers, 0-7, which translates into array index.
     * @param x - a character, letter
     * @return integer 0 to 7
     */
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

    /**
     * This methods revert and translate the file number on the board, 8 to 0, 7 to 1, so it is in the correct order of array index
     * @param x - a char input of the file number, '8', '7' ... '0'
     * @return integer 0 to 7
     */
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

    /**
     * This is the main which creates the board and execute the chess game
     * @param args Unused.
     *
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Board chessBoard = new Board();
        chessBoard.init();
        int turn_num = 1;
        boolean whiteTurn = true;
        boolean invalidMove = false;
        boolean draw = false;
        String winner = "none";
        boolean stalemate = false;
        while (true) {

            if (invalidMove==false) {
                chessBoard.printBoard();
            }

            if(turn_num%2!=0) {
                whiteTurn = true;
            }
            else {
                whiteTurn = false;
            }

            if(whiteTurn==true&&invalidMove==false) {
                for(int i = 0; i<chessBoard.board.length ; i++) {
                    for(int j = 0; j<chessBoard.board[i].length; j++) {
                        if(chessBoard.board[i][j].piece instanceof Pawn) {
                            Pawn temp = (Pawn)chessBoard.board[i][j].piece;
                            if(temp.white==true) {
                                //System.out.println("white changed");
                                temp.enpassant = false;
                            }
                        }
                    }
                }
            }
            if(whiteTurn!=true&&invalidMove==false) {
                for(int i = 0; i<chessBoard.board.length ; i++) {
                    for(int j = 0; j<chessBoard.board[i].length; j++) {
                        if(chessBoard.board[i][j].piece instanceof Pawn) {
                            Pawn temp = (Pawn)chessBoard.board[i][j].piece;
                            if(temp.white!=true) {
                                //System.out.println("black changed");
                                temp.enpassant = false;
                            }
                        }
                    }
                }
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
                    if (chessBoard.checkWhite(chessBoard) == 1) {
                        stalemate=true;
                        break;
                    }
                    winner = "White";
                    break;
                }
            }

            else {
                if(chessBoard.checkmate(true) == true) {
                    if (chessBoard.checkBlack(chessBoard) ==1 ){
                        stalemate=true;
                        break;
                    }
                    winner = "Black";
                    break;
                }
            }


            if(turn_num%2!=0) {
                if(chessBoard.checkWhite(chessBoard) == 1  && invalidMove==false) {
                    System.out.println("Check");
                }
                System.out.print("White's move: ");
            }
            else {
                if(chessBoard.checkBlack(chessBoard) == 1   && invalidMove==false) {
                    System.out.println("Check");
                }
                System.out.print("Black's move: ");
            }
            Scanner scan = new Scanner(System.in);
            String input = scan.nextLine();

            if(input.compareTo("resign")==0 &&turn_num%2==0) {
                System.out.println("White wins");
                return;
            }
            if(input.compareTo("resign")==0 && turn_num%2!=0) {
                System.out.println("Black wins");
                return;
            }
            if(input.compareTo("draw")==0 && draw==true) {
                return;
            }

            //comment this out when submit, since we don't need to check for illegal input
            if(InvalidInput(input)==true) {
                //System.out.println("Illegal input, try again");
                invalidMove = true;
                continue;
            }
            if(input.length()==11) {
                draw = true;
            }
            int fromX, fromY, toX, toY;
            char promo = 'Q';
            fromX = convert(input.charAt(1));
            fromY = toInt(input.charAt(0));
            toX = convert(input.charAt(4));
            toY = toInt(input.charAt(3));
            if(input.length()==7) {
                promo = input.charAt(6);
            }


            if(chessBoard.board[fromX][fromY].piece==null) {
                //System.out.println("Illegal move, try again");
                invalidMove = true;
                continue;
            }

            if (chessBoard.board[fromX][fromY].getPiece().move(fromX,fromY,toX, toY, whiteTurn, promo ) != 1) {
                invalidMove = true;

            }


            else {
                turn_num++;
                invalidMove = false;
                System.out.println("");
            }

        }
        if (stalemate) {
            System.out.println("Stalemate");
        }
        else {
            System.out.println("Checkmate");
            System.out.print(winner+ " wins");
        }




        //chessBoard.printBoard();

    }

}
