package com.example.sam.chess;

/**
 * @author Laishek Tso, Andrew Ma
 *
 */

/**
 * This is the class for Rook, which extends Piece
 *
 */
public class Rook extends Piece{
    /**
     A boolean value that decides rather castle move is allowed for the king
     **/
    boolean castle = true;
    /**
     * Rook constructor
     * @param x - X coordinate of the rook
     * @param y - Y coordinate of the rook
     * @param name - String name should be either wR or bR for white or black rook
     * @param currentBoard - reference back to the main chess board
     */
    public Rook(int x, int y, String name, Board currentBoard) {
        super(x, y, name, currentBoard);
    }

    /**
     * Another rook constructor for rook's that are created from pawn promotion, which disallows castling
     * @param x - X coordinate of the rook
     * @param y - Y coordinate of the rook
     * @param name - String name should be either wR or bR for white or black rook
     * @param currentBoard - reference back to the main chess board
     * @param temp - just to distinguish from the original constructor, can be any integer, the integer no other practical use
     */
    public Rook(int x, int y, String name, Board currentBoard, int temp) {
        super(x, y, name, currentBoard);
        castle = false;
    }

    /* (non-Javadoc)
     * @see chess.Piece#getPotentialMoves(boolean)
     */
    public void getPotentialMoves(boolean color) {
        potentialMoves.clear();
        if ( currentBoard.board[x][y].piece.white != color) {
            return;
        }
        for (int col = y+1; col <8; col++) {
            if (currentBoard.board[x][col].piece == null) {
                if(currentBoard.testMove(this, currentBoard, x, y, x, col) != 1) {
                    potentialMoves.add(x+","+col);
                }			}
            else if(currentBoard.board[x][col].piece.white != white) {
                if(currentBoard.testMove(this, currentBoard, x, y, x, col) != 1) {
                    potentialMoves.add(x+","+col);
                }				break;
            }
            else {
                break;
            }
        }
        for (int col = y- 1; col >=0; col--) {
            if (currentBoard.board[x][col].piece == null) {
                if(currentBoard.testMove(this, currentBoard, x, y, x, col) != 1) {
                    potentialMoves.add(x+","+col);
                }			}
            else if(currentBoard.board[x][col].piece.white != white) {
                if(currentBoard.testMove(this, currentBoard, x, y, x, col) != 1) {
                    potentialMoves.add(x+","+col);
                }				break;
            }
            else {
                break;
            }
        }
        for (int row = x+ 1; row <8; row++) {
            if (currentBoard.board[row][y].piece == null) {
                if(currentBoard.testMove(this, currentBoard, x, y, row, y) != 1) {
                    potentialMoves.add(row+","+y);
                }			}
            else if(currentBoard.board[row][y].piece.white != white) {
                if(currentBoard.testMove(this, currentBoard, x, y, row, y) != 1) {
                    potentialMoves.add(row+","+y);
                }				break;
            }
            else {
                break;
            }
        }
        for (int row = x-  1; row >=0; row--) {
            if (currentBoard.board[row][y].piece == null) {
                if(currentBoard.testMove(this, currentBoard, x, y, row, y) != 1) {
                    potentialMoves.add(row+","+y);
                }			}
            else if(currentBoard.board[row][y].piece.white != white) {
                if(currentBoard.testMove(this, currentBoard, x, y, row, y) != 1) {
                    potentialMoves.add(row+","+y);
                }				break;
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
            castle = false;
            return 1;
        }
        printError();
        return -1;
    }
}
