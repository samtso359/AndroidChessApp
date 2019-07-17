package com.example.sam.chess;

/**
 * @author Laishek Tso, Andrew Ma
 *
 */
import java.util.ArrayList;
import java.util.List;

/**
 * This is a super class of all chess pieces
 *
 */
public class Piece {
    /**
     * X coordinate of a piece on the 2-D array board
     */
    int x;
    /**
     * Y coordinate of a piece on the 2-D array board
     */
    int y;
    /**
     * the abbreviation for each piece. Ex. wQ, bK
     */
    String name;
    /**
     * A reference to the main board
     */
    Board currentBoard;
    /**
     * check rather the piece is a white piece or black piece
     */
    boolean white = false;
    /**
     * an arrayList that stores all the potential legal moves of the piece
     */
    List<String> potentialMoves = new ArrayList<String>();
    /**
     * The superclass constructor for all pieces
     * @param x - X coordinate of the piece
     * @param y - Y coordinate of the piece
     * @param name - the abbreviation of the piece
     * @param currentBoard - a reference to the main chess board
     */
    public Piece(int x, int y, String name, Board currentBoard) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.currentBoard = currentBoard;
        if(name.charAt(0)=='w') {
            this.white = true;
        }
    }

    //color is boolean, True for White, False for Black
    /**This method moves a piece to a new location
     * @param oldX - the original X coordinate
     * @param oldY - the original Y coordinate
     * @param newX - the new X coordinate
     * @param newY - the new Y coordinate
     * @param color - boolean that determines the color of the piece, black or white piece
     * @param promo - a character for the purpose of pawn promotion , 'R' or 'Q' or 'B' or 'N', by default is Q stands for queen, can be changed by user input
     * @return - returns 1 if a move is successfully made(allowed), -1 and prints illegal move otherwise
     */
    public int move(int oldX, int oldY, int newX, int newY, boolean color, char promo) {
        if ( currentBoard.board[oldX][oldY].piece.white != color) {
            System.out.println("Illegal move, try again");
            return -1;
        }

        currentBoard.board[newX][newY].piece = currentBoard.board[oldX][oldY].piece;
        currentBoard.board[oldX][oldY].piece = null;
        return 1;
    }

    /**
     * A simple 1 line method that prints "Illegal move, try again"
     */
    public void printError() {
        System.out.println("Illegal move, try again");
    }

    /**
     * A method in super class, is later to be overwritten in all other sub classes. Adds a legal move into potenialMoves array list.
     * @param color - takes the color of a piece, black or white
     */
    public void getPotentialMoves(boolean color) {
    }




}
