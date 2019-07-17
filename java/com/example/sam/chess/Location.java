package com.example.sam.chess;


/**
 * @author Laishek Tso, Andrew Ma
 *
 */


/**
 * This class is a place holder for each location for the board, each location should holds a piece with X and Y coordinates, otherwise holds null
 *
 */
public class Location {
    /**
     * X coordinate of the location on the chess board
     */
    int x;
    /**
     * Y coordinate of the location on the chess board
     */
    int y;
    /**
     * This boolean determines rather the location has a black background - ##
     */
    boolean blackspot = false;
    /**
     * It holds a piece, Knight/Bishop/King/etc
     */
    Piece piece = null;
    /**
     * Constructor for location
     * @param x - X coordinate of the location
     * @param y - Y coordinate of the location
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
        if(x%2!=0 && y%2==0  ) {
            this.blackspot = true;
        }
        if(x%2==0 && y%2!=0) {
            this.blackspot = true;
        }
    }

    /**
     * Another constructor for location that takes an extra param Piece
     * @param x - X coordinate of the location
     * @param y - Y coordinate of the location
     * @param piece - Holds a piece, Knight/Bishop/King/etc
     */
    public Location(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        if(x%2!=0 && y%2==0  ) {
            this.blackspot = true;
        }
        if(x%2==0 && y%2!=0) {
            this.blackspot = true;
        }
        this.piece = piece;
    }
    /**
     * @return reference to the Piece object
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * @return reference to the piece name, ex. "wB", "bK", "bP", etc
     */
    public String getName() {
        return piece.name;
    }

}

