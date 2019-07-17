package com.example.sam.chess;



/**
 * @author Laishek Tso, Andrew Ma
 *
 */

/**
 * This is the class for Pawn, which extends Piece
 *
 */
public class Pawn extends Piece {
    /**
     * A boolean value that tells rather an enpassant move is allowed for the piece
     */
    boolean enpassant = false;
    /**
     * Pawn constructor
     * @param x - X coordinate of the pawn
     * @param y - Y coordinate of the pawn
     * @param name - String name should be either wP or bP for white or black pawn
     * @param currentBoard - reference back to the main chess board
     */
    public Pawn(int x, int y, String name, Board currentBoard) {
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
        if(x==6 && currentBoard.board[x][y].piece.white == true) {	//white pawn launch
            for (int row = x-1; row >3; row--) {
                if (currentBoard.board[row][y].piece == null) {
                    if(currentBoard.testMove(this, currentBoard, x, y, row, y) != 1) {
                        potentialMoves.add(row+","+y);
                    }
                }
                else {
                    break;
                }
            }
        }
        else if(x!=6 && x>0 && white == true && currentBoard.board[x-1][y].piece == null) { //white pawn not launch
            if(currentBoard.testMove(this, currentBoard, x, y, x-1, y) != 1) {
                potentialMoves.add((x-1)+","+(y));
            }		}
        //white pawn hit black pieces
        if(x> -1 && x<8 && y> -1 && y<7 && currentBoard.board[x-1][y+1].piece!=null && white == true) {
            if(currentBoard.board[x-1][y+1].piece.white != true) {
                if(currentBoard.testMove(this, currentBoard, x, y, x-1, y+1) != 1) {
                    potentialMoves.add((x-1)+","+(y+1));
                }
            }
        }
        if(x>-1 && x<8 && y>0 && y<8 && currentBoard.board[x-1][y-1].piece!=null && currentBoard.board[x][y].piece.white == true) {
            if(currentBoard.board[x-1][y-1].piece.white != true) {
                if(currentBoard.testMove(this, currentBoard, x, y, x-1, y-1) != 1) {
                    potentialMoves.add((x-1)+","+(y-1));
                }
            }
        }
        if(x> -1 && x<8 && y> -1 && y<7 && currentBoard.board[x][y+1].piece!=null && white == true) { //white pawn enpassant
            if(currentBoard.board[x][y+1].piece instanceof Pawn) {			//right side enpassant
                Pawn temp = (Pawn)currentBoard.board[x][y+1].piece;
                if(temp.enpassant == true && temp.white!=true) {
                    potentialMoves.add((x-1)+","+(y+1));
                }
            }}
        if(x> -1 && x<8 && y> 0 && y<8 && currentBoard.board[x][y-1].piece!=null && white == true) {
            if(currentBoard.board[x][y-1].piece instanceof Pawn) {			//left side enpassant
                Pawn temp = (Pawn)currentBoard.board[x][y-1].piece;
                if(temp.enpassant == true && temp.white!=true) {
                    potentialMoves.add((x-1)+","+(y-1));
                }
            }
        }
        if(x==1 && currentBoard.board[x][y].piece.white == false) { //black pawn launch
            for (int row = x+1; row <4; row++) {
                if (currentBoard.board[row][y].piece == null) {
                    if(currentBoard.testMove(this, currentBoard, x, y, row, y) != 1) {
                        potentialMoves.add((row)+","+(y));
                    }
                }
                else {
                    break;
                }
            }
        }
        else if(x!=1 && x<7 && currentBoard.board[x][y].piece.white == false && currentBoard.board[x+1][y].piece == null) { //black pawn not launch
            if(currentBoard.testMove(this, currentBoard, x, y, x+1, y) != 1) {
                potentialMoves.add((x+1)+","+(y));
            }
        }
        //black pawn hit white pieces
        if(x>-1 && x<8 && y>-1 && y<7 && currentBoard.board[x+1][y+1].piece!=null && currentBoard.board[x][y].piece.white != true ) {
            if(currentBoard.board[x+1][y+1].piece.white == true ) {
                if(currentBoard.testMove(this, currentBoard, x, y, x+1, y+1) != 1) {
                    potentialMoves.add((x+1)+","+(y+1));
                }
            }
        }
        if(x>-1 && x<8 && y>0 && y<8 && currentBoard.board[x+1][y-1].piece!=null && currentBoard.board[x][y].piece.white != true ) {
            if(currentBoard.board[x+1][y-1].piece.white == true ) {
                if(currentBoard.testMove(this, currentBoard, x, y, x+1, y-1) != 1) {
                    potentialMoves.add((x+1)+","+(y-1));
                }
            }
        }

        if(x> -1 && x<8 && y> -1 && y<7 && currentBoard.board[x][y+1].piece!=null && white != true) { //black pawn enpassant
            if(currentBoard.board[x][y+1].piece instanceof Pawn) {			//right side enpassant
                Pawn temp = (Pawn)currentBoard.board[x][y+1].piece;
                if(temp.enpassant == true && temp.white==true) {
                    potentialMoves.add((x+1)+","+(y+1));
                }
            }
        }
        if(x> -1 && x<8 && y> 0 && y<8 && currentBoard.board[x][y-1].piece!=null && white != true) {
            if(currentBoard.board[x][y-1].piece instanceof Pawn) {			//left side enpassant
                Pawn temp = (Pawn)currentBoard.board[x][y-1].piece;
                if(temp.enpassant == true && temp.white==true) {
                    potentialMoves.add((x+1)+","+(y-1));
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
        if(potentialMoves.contains(moveTo)&&newX!=0&&newX!=7) {

            //white pawn enpassant
            if(currentBoard.board[newX+1][newY].piece instanceof Pawn && white == true && currentBoard.board[newX+1][newY].piece.white!=true&&currentBoard.board[newX][newY].piece==null) {
                Pawn temp = (Pawn)currentBoard.board[newX+1][newY].piece;
                if(temp.enpassant==true) {
                    currentBoard.board[newX+1][newY].piece = null;
                }
            }

            //black pawn left side enpassant
            if(currentBoard.board[newX-1][newY].piece instanceof Pawn && white != true && currentBoard.board[newX-1][newY].piece.white==true&&currentBoard.board[newX][newY].piece==null) {
                Pawn temp = (Pawn)currentBoard.board[newX-1][newY].piece;
                if(temp.enpassant==true) {
                    currentBoard.board[newX-1][newY].piece = null;
                }
            }


            if(Math.abs(newX-oldX)==2) {
                enpassant = true;
            }
            else if(Math.abs(newX-oldX)==1) {
                enpassant = false;
            }
            currentBoard.board[newX][newY].piece = currentBoard.board[oldX][oldY].piece;
            x = newX;
            y = newY;
            currentBoard.board[oldX][oldY].piece = null;
            return 1;
        }

        if(potentialMoves.contains(moveTo)&&newX==0) { //white pawn promote
            if(promo == 'Q') {
                currentBoard.board[newX][newY].piece = new Queen(newX, newY, "wQ", currentBoard);
                currentBoard.board[newX][newY].piece.white = true;
                x = newX;
                y = newY;
                currentBoard.board[oldX][oldY].piece = null;
            }
            if(promo == 'R') {
                currentBoard.board[newX][newY].piece = new Rook(newX, newY, "wR", currentBoard,-1);
                currentBoard.board[newX][newY].piece.white = true;
                x = newX;
                y = newY;
                currentBoard.board[oldX][oldY].piece = null;
            }
            if(promo == 'N') {
                currentBoard.board[newX][newY].piece = new Knight(newX, newY, "wN", currentBoard);
                currentBoard.board[newX][newY].piece.white = true;
                x = newX;
                y = newY;
                currentBoard.board[oldX][oldY].piece = null;
            }
            if(promo == 'B') {
                currentBoard.board[newX][newY].piece = new Bishop(newX, newY, "wB", currentBoard);
                currentBoard.board[newX][newY].piece.white = true;
                x = newX;
                y = newY;
                currentBoard.board[oldX][oldY].piece = null;
            }
            return 1;
        }
        if(potentialMoves.contains(moveTo)&&newX==7) { //black pawn promote
            if(promo == 'Q') {
                currentBoard.board[newX][newY].piece = new Queen(newX, newY, "bQ", currentBoard);
                currentBoard.board[newX][newY].piece.white = false;
                x = newX;
                y = newY;
                currentBoard.board[oldX][oldY].piece = null;
            }
            if(promo == 'R') {
                currentBoard.board[newX][newY].piece = new Rook(newX, newY, "bR", currentBoard, -1);
                currentBoard.board[newX][newY].piece.white = false;
                x = newX;
                y = newY;
                currentBoard.board[oldX][oldY].piece = null;
            }
            if(promo == 'N') {
                currentBoard.board[newX][newY].piece = new Knight(newX, newY, "bN", currentBoard);
                currentBoard.board[newX][newY].piece.white = false;
                x = newX;
                y = newY;
                currentBoard.board[oldX][oldY].piece = null;
            }
            if(promo == 'B') {
                currentBoard.board[newX][newY].piece = new Bishop(newX, newY, "bB", currentBoard);
                currentBoard.board[newX][newY].piece.white = false;
                x = newX;
                y = newY;
                currentBoard.board[oldX][oldY].piece = null;
            }
            return 1;
        }
        printError();
        return -1;
    }
}



