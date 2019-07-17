package com.example.sam.chess;

import java.lang.Math;

/**
 * @author Laishek Tso, Andrew Ma
 *
 */

/**
 * This is the class for King, which extends Piece
 *
 */
public class King extends Piece{
    /**
     * A boolean value that decides rather castle move is allowed for the king
     */
    boolean castle = true;

    /**
     * King constructor
     * @param x - X coordinate of the king
     * @param y - Y coordinate of the king
     * @param name - String name should be either wK or bK for white or black king
     * @param currentBoard - reference back to the main chess board
     */
    public King(int x, int y, String name, Board currentBoard) {
        super(x, y, name, currentBoard);
    }
    /* (non-Javadoc)
     * @see chess.Piece#getPotentialMoves(boolean)
     */
    @Override
    public void getPotentialMoves(boolean color) {
        potentialMoves.clear();
        if ( currentBoard.board[x][y].piece.white != color) {
            return;
        }
        for(int row =0; row <8; row++) {
            for (int col =0; col < 8; col++) {
                if(Math.abs(col-y) <= 1 && Math.abs(row-x) <= 1) {
                    if (col == y && row == x) {

                    }
                    else if(currentBoard.board[row][col].piece == null) {
                        if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                            potentialMoves.add(row+","+col);
                        }
                    }
                    else if(currentBoard.board[row][col].piece.white != white) {
                        if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                            potentialMoves.add(row+","+col);
                        }
                    }
                }
            }
        }

        //castling for white king
        if(castle==true && white == true) {
            //right side
            if(currentBoard.board[7][7].piece instanceof Rook) {
                Rook temp = (Rook)currentBoard.board[7][7].piece;
                if(currentBoard.board[7][7].piece.white == true && temp.castle && currentBoard.board[7][6].piece == null && currentBoard.board[7][5].piece == null) {
                    if(currentBoard.positionalCheck(currentBoard, 7, 5, true) == -1 && currentBoard.positionalCheck(currentBoard, 7, 6, true) == -1 ) {
                        potentialMoves.add(7+","+6);
                    }
                }
            }
            //left side
            if(currentBoard.board[7][0].piece instanceof Rook) {
                Rook temp = (Rook)currentBoard.board[7][0].piece;
                if(currentBoard.board[7][0].piece.white == true && temp.castle && currentBoard.board[7][1].piece == null && currentBoard.board[7][2].piece == null && currentBoard.board[7][3].piece == null) {
                    if(currentBoard.positionalCheck(currentBoard, 7, 1, true) == -1 && currentBoard.positionalCheck(currentBoard, 7, 2, true) == -1 && currentBoard.positionalCheck(currentBoard, 7, 3, true) == -1 ) {
                        potentialMoves.add(7+","+2);
                    }
                }
            }
        }

        //castling for black king
        if(castle==true && white != true) {
            //right side
            if(currentBoard.board[0][7].piece instanceof Rook) {
                Rook temp = (Rook)currentBoard.board[0][7].piece;
                if(currentBoard.board[0][7].piece.white != true && temp.castle && currentBoard.board[0][6].piece == null && currentBoard.board[0][5].piece == null) {
                    if(currentBoard.positionalCheck(currentBoard, 0, 5, false) == -1 && currentBoard.positionalCheck(currentBoard, 0, 6, false) == -1 ) {
                        potentialMoves.add(0+","+6);
                    }
                }
            }
            //left side
            if(currentBoard.board[0][0].piece instanceof Rook) {
                Rook temp = (Rook)currentBoard.board[0][0].piece;
                if(currentBoard.board[0][0].piece.white != true && temp.castle && currentBoard.board[0][1].piece == null && currentBoard.board[0][2].piece == null && currentBoard.board[0][3].piece == null) {
                    if(currentBoard.positionalCheck(currentBoard, 0, 1, false) == -1 && currentBoard.positionalCheck(currentBoard, 0, 2, false) == -1 && currentBoard.positionalCheck(currentBoard, 0, 3, false) == -1 ) {
                        potentialMoves.add(0+","+2);
                    }
                }
            }
        }
    }
    /* (non-Javadoc)
     * @see chess.Piece#move(int, int, int, int, boolean, char)
     */
    @Override
    public int move(int oldX, int oldY, int newX, int newY, boolean color, char promo) {
        //ignore promo, it is only for pawn
        String moveTo = newX+","+newY;

        if(potentialMoves.contains(moveTo)) {
            if(color==true && Math.abs(oldY - newY)==2 && newY == 6) { //white king RIGHT side castle execute
                currentBoard.board[newX][newY].piece = currentBoard.board[oldX][oldY].piece;
                x = newX;
                y = newY;
                currentBoard.whiteKingX=newX;
                currentBoard.whiteKingX=newY;
                currentBoard.board[oldX][oldY].piece = null;
                castle = false;
                currentBoard.board[7][5].piece = currentBoard.board[7][7].piece;
                currentBoard.board[7][5].piece.y = 5;
                currentBoard.board[7][7].piece = null;
                return 1;
            }
            if(color==true && Math.abs(oldY - newY)==2 && newY == 2) { //white king LEFT side castle execute
                currentBoard.board[newX][newY].piece = currentBoard.board[oldX][oldY].piece;
                x = newX;
                y = newY;
                currentBoard.whiteKingX=newX;
                currentBoard.whiteKingX=newY;
                currentBoard.board[oldX][oldY].piece = null;
                castle = false;
                currentBoard.board[7][3].piece = currentBoard.board[7][0].piece;
                currentBoard.board[7][3].piece.y = 3;
                currentBoard.board[7][0].piece = null;
                return 1;
            }
            if(color!=true && Math.abs(oldY-newY)==2 && newY == 6) { //black king RIGHT side castle execute
                currentBoard.board[newX][newY].piece = currentBoard.board[oldX][oldY].piece;
                x = newX;
                y = newY;
                currentBoard.blackKingX=newX;
                currentBoard.blackKingX=newY;
                currentBoard.board[oldX][oldY].piece = null;
                castle = false;
                currentBoard.board[0][5].piece = currentBoard.board[0][7].piece;
                currentBoard.board[0][5].piece.y = 5;
                currentBoard.board[0][7].piece = null;
                return 1;
            }
            if(color!=true && Math.abs(oldY - newY)==2 && newY == 2) { //black king LEFT side castle execute
                currentBoard.board[newX][newY].piece = currentBoard.board[oldX][oldY].piece;
                x = newX;
                y = newY;
                currentBoard.blackKingX=newX;
                currentBoard.blackKingX=newY;
                currentBoard.board[oldX][oldY].piece = null;
                castle = false;
                currentBoard.board[0][3].piece = currentBoard.board[0][0].piece;
                currentBoard.board[0][3].piece.y = 3;
                currentBoard.board[0][0].piece = null;
                return 1;
            }
            currentBoard.board[newX][newY].piece = currentBoard.board[oldX][oldY].piece;
            x = newX;
            y = newY;
            if(color == true) {
                currentBoard.whiteKingX=newX;
                currentBoard.whiteKingY=newY;

            }
            else {
                currentBoard.blackKingX=newX;
                currentBoard.blackKingY=newY;
            }
            currentBoard.board[oldX][oldY].piece = null;
            castle = false;
            return 1;
        }
        printError();
        return -1;
    }
}