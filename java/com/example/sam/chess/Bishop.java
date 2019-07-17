package com.example.sam.chess;


/**
 * @author Laishek Tso, Andrew Ma
 *
 */

/**
 * This is the class for Bishop, which extends Piece
 *
 */
public class Bishop extends Piece{
    /**
     * Bishop constructor
     * @param x - X coordinate of the bishop
     * @param y - Y coordinate of the bishop
     * @param name - String name should be either wB or bB for white or black bishop
     * @param currentBoard - reference back to the main chess board
     */
    public Bishop(int x, int y, String name, Board currentBoard) {
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
        for (int row = x-1, col = y-1;Math.min(row,col)>=0;row--,col-- ) {
            if (currentBoard.board[row][col].piece == null) {
                if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                    potentialMoves.add(row+","+col);
                }
            }
            else if(currentBoard.board[row][col].piece.white != white) {
                if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                    potentialMoves.add(row+","+col);
                }
                break;
            }
            else {
                break;
            }
        }
        for (int row = x+1, col = y+1;Math.max(row,col)<8;row++,col++ ) {
            if (currentBoard.board[row][col].piece == null) {
                if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                    potentialMoves.add(row+","+col);
                }
            }
            else if(currentBoard.board[row][col].piece.white != white) {
                if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                    potentialMoves.add(row+","+col);
                }
                break;
            }
            else {
                break;
            }
        }
        for (int row = x+1, col = y-1;row <8 && col >=0;row++,col-- ) {
            if (currentBoard.board[row][col].piece == null) {
                if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                    potentialMoves.add(row+","+col);
                }
            }
            else if(currentBoard.board[row][col].piece.white != white) {
                if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                    potentialMoves.add(row+","+col);
                }
                break;
            }
            else {
                break;
            }
        }
        for (int row = x-1, col = y+1;row >=0 && col <8;row--,col++) {
            if (currentBoard.board[row][col].piece == null) {
                if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                    potentialMoves.add(row+","+col);
                }
            }
            else if(currentBoard.board[row][col].piece.white != white) {
                if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                    potentialMoves.add(row+","+col);
                }
                break;
            }
            else {
                break;
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
            currentBoard.board[newX][newY].piece = currentBoard.board[oldX][oldY].piece;
            x = newX;
            y = newY;
            currentBoard.board[oldX][oldY].piece = null;
            return 1;
        }
        printError();
        return -1;
    }
}
