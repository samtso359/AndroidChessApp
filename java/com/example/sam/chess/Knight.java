package com.example.sam.chess;

/**
 * @author Laishek Tso, Andrew Ma
 *
 */

/**
 * This is the class for Knight, which extends Piece
 *
 */
public class Knight extends Piece{
    /**
     * Knight constructor
     * @param x - X coordinate of the knight
     * @param y - Y coordinate of the knight
     * @param name - String name should be either wN or bN for white or black knight
     * @param currentBoard - reference back to the main chess board
     */
    public Knight(int x, int y, String name, Board currentBoard) {
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
                if(Math.abs(col-y) == 2 && Math.abs(row-x) == 1) {
                    if(currentBoard.board[row][col].piece == null) {
                        if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                            potentialMoves.add(row+","+col);
                        }
                    }
                    else if(currentBoard.board[row][col].piece.white != white) {
                        if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                            potentialMoves.add(row+","+col);
                        }					}
                }
                if(Math.abs(col-y) == 1 && Math.abs(row-x) == 2) {
                    if(currentBoard.board[row][col].piece == null) {
                        if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                            potentialMoves.add(row+","+col);
                        }					}
                    else if(currentBoard.board[row][col].piece.white != white) {
                        if(currentBoard.testMove(this, currentBoard, x, y, row, col) != 1) {
                            potentialMoves.add(row+","+col);
                        }					}
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
