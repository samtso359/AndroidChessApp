package com.example.sam.chess;

/**
 * @author Laishek Tso, Andrew Ma
 *
 */

/**
 * This board class defines a chess board as a 2-D 8 by 8 array
 *
 */
public class Board {
    /**
     *  This is the base board structure for initialization of the 8 by 8 2D array chess board
     */
    public Location[][] board = new Location[8][8];
    /**
     * This is the white king's X index position
     */
    int whiteKingX = 7;
    /**
     * This is the white king's Y index position
     */
    int whiteKingY = 4;
    /**
     * This is the black king's X index position
     */
    int blackKingX = 0;
    /**
     * This is the black king's Y index position
     */
    int blackKingY = 4;
    /**
     * This is the constructor for Board class, which assign Location object into each position of the 2-D array
     */
    public Board() {
        for(int i = 0; i<board.length ; i++) {
            for(int j = 0; j<board[i].length; j++) {
                this.board[i][j] = new Location(i,j);
            }
        }
    }
    /**
     * Initializes the board into a standard start up of a chess game
     */
    public void init() {
        board[0][0] = new Location(0,0, new Rook(0,0, "bR", this));
        board[0][1] = new Location(0,1, new Knight(0,1, "bN", this));
        board[0][2] = new Location(0,2, new Bishop(0,2, "bB", this));
        board[0][3] = new Location(0,3, new Queen(0,3, "bQ", this));
        board[0][4] = new Location(0,4, new King(0,4, "bK", this));
        board[0][5] = new Location(0,5, new Bishop(0,5, "bB", this));
        board[0][6] = new Location(0,6, new Knight(0,6, "bN", this));
        board[0][7] = new Location(0,7, new Rook(0,7, "bR", this));
        board[1][0] = new Location(1,0, new Pawn(1,0, "bp", this));
        board[1][1] = new Location(1,1, new Pawn(1,1, "bp", this));
        board[1][2] = new Location(1,2, new Pawn(1,2, "bp", this));
        board[1][3] = new Location(1,3, new Pawn(1,3, "bp", this));
        board[1][4] = new Location(1,4, new Pawn(1,4, "bp", this));
        board[1][5] = new Location(1,5, new Pawn(1,5, "bp", this));
        board[1][6] = new Location(1,6, new Pawn(1,6, "bp", this));
        board[1][7] = new Location(1,7, new Pawn(1,7, "bp", this));

        board[7][0] = new Location(7,0, new Rook(7,0, "wR", this));
        board[7][1] = new Location(7,1, new Knight(7,1, "wN", this));
        board[7][2] = new Location(7,2, new Bishop(7,2, "wB", this));
        board[7][3] = new Location(7,3, new Queen(7,3, "wQ", this));
        board[7][4] = new Location(7,4, new King(7,4, "wK", this));
        board[7][5] = new Location(7,5, new Bishop(7,5, "wB", this));
        board[7][6] = new Location(7,6, new Knight(7,6, "wN", this));
        board[7][7] = new Location(7,7, new Rook(7,7, "wR", this));
        board[6][0] = new Location(6,0, new Pawn(6,0, "wp", this));
        board[6][1] = new Location(6,1, new Pawn(6,1, "wp", this));
        board[6][2] = new Location(6,2, new Pawn(6,2, "wp", this));
        board[6][3] = new Location(6,3, new Pawn(6,3, "wp", this));
        board[6][4] = new Location(6,4, new Pawn(6,4, "wp", this));
        board[6][5] = new Location(6,5, new Pawn(6,5, "wp", this));
        board[6][6] = new Location(6,6, new Pawn(6,6, "wp", this));
        board[6][7] = new Location(6,7, new Pawn(6,7, "wp", this));
        for(int row = 2;row<6; row++) {
            for (int col =0; col <8; col++) {
                board[row][col].piece=null;
            }
        }
    }

    /**
     * Check to see if a king will be in danger by a knight at a position
     * @param chessBoard - reference to main chessboard
     * @param kingX - x position you want to check
     * @param kingY - y position you want to check
     * @param white - color of the king
     * @return return 1 on check, -1 if no check
     */
    public int knightCheck (Board chessBoard, int kingX, int kingY, boolean white) {
        int row = 0;
        int col = 0;
        for (row = 0; row <8; row++) {
            for (col = 0; col <8; col++) {
                if (chessBoard.board[row][col].piece instanceof Knight) {
                    if(chessBoard.board[row][col].piece.white != white) {
                        if(Math.abs(row - kingX) ==2 && Math.abs(col - kingY) == 1) {
                            return 1;
                        }
                        else if(Math.abs(row - kingX) ==1 && Math.abs(col - kingY) == 2) {
                            return 1;
                        }
                    }

                }
            }
        }
        return -1;
    }

    /**
     * Check to see if a king will be in danger diagonally at a position
     * @param chessBoard - reference to main chessboard
     * @param kingX - x position you want to check
     * @param kingY - y position you want to check
     * @param white - color of the king
     * @return return 1 on check, -1 if no check
     */
    public int diagCheck (Board chessBoard, int kingX, int kingY, boolean white) {
        for (int row = kingX-1, col = kingY-1;Math.min(row,col)>=0;row--,col-- ) {
            if (chessBoard.board[row][col].piece != null) {
                if (chessBoard.board[row][col].piece instanceof Bishop || chessBoard.board[row][col].piece instanceof Queen) {
                    if(chessBoard.board[row][col].piece.white != white) {
                        return 1;
                    }
                }
                break;
            }
        }
        for (int row = kingX+1, col = kingY+1;Math.max(row,col)<8;row++,col++ ) {
            if (chessBoard.board[row][col].piece != null) {
                if (chessBoard.board[row][col].piece instanceof Bishop || chessBoard.board[row][col].piece instanceof Queen) {
                    if(chessBoard.board[row][col].piece.white != white) {
                        return 1;
                    }
                }
                break;
            }
        }
        for (int row = kingX+1, col = kingY-1;row <8 && col >=0;row++,col-- ) {
            if (chessBoard.board[row][col].piece != null) {
                if (chessBoard.board[row][col].piece instanceof Bishop || chessBoard.board[row][col].piece instanceof Queen) {
                    if(chessBoard.board[row][col].piece.white != white) {
                        return 1;
                    }
                }
                break;
            }
        }
        for (int row = kingX-1, col = kingY+1;row >=0 && col <8;row--,col++) {
            if (chessBoard.board[row][col].piece != null) {
                if (chessBoard.board[row][col].piece instanceof Bishop || chessBoard.board[row][col].piece instanceof Queen) {
                    if(chessBoard.board[row][col].piece.white != white) {
                        return 1;
                    }
                }
                break;
            }
        }
        return -1;
    }

    /**
     * Check to see if a king will be in danger by a pawn at a position
     * @param chessBoard - reference to main chessboard
     * @param kingX - x position you want to check
     * @param kingY - y position you want to check
     * @param white - color of the king
     * @return return 1 on check, -1 if no check
     */
    public int pawnCheck (Board chessBoard, int kingX, int kingY, boolean white) {
        int diag = 0;
        if (white== true) {
            if(kingX == 0) {
                return -1;
            }
            diag = -1;
        }
        else {
            if(kingX == 7) {
                return -1;
            }
            diag = 1;
        }
        if (kingY != 0) {
            if(	chessBoard.board[kingX + diag][kingY - 1].piece instanceof Pawn) {
                if (chessBoard.board[kingX+diag][kingY-1].piece.white != white){
                    return 1;
                }
            }
        }
        if (kingY != 7) {
            if(chessBoard.board[kingX + diag][kingY + 1].piece instanceof Pawn ) {
                if (chessBoard.board[kingX+diag][kingY+1].piece.white != white){
                    return 1;
                }
            }
        }
        return -1;
    }

    /**
     * Check to see if a king will be in danger horizontally or vertically at a position
     * @param chessBoard - reference to main chessboard
     * @param kingX - x position you want to check
     * @param kingY - y position you want to check
     * @param white - color of the king
     * @return return 1 on check, -1 if no check
     */
    public int linCheck (Board chessBoard, int kingX, int kingY, boolean white) {
        for (int col = kingY+1; col <8; col++) {
            if (chessBoard.board[kingX][col].piece != null) {
                if (chessBoard.board[kingX][col].piece instanceof Rook || chessBoard.board[kingX][col].piece instanceof Queen) {
                    if(chessBoard.board[kingX][col].piece.white != white) {
                        return 1;
                    }
                }
                break;
            }
        }
        for (int col = kingY- 1; col >=0; col--) {
            if (chessBoard.board[kingX][col].piece != null) {
                if (chessBoard.board[kingX][col].piece instanceof Rook || chessBoard.board[kingX][col].piece instanceof Queen) {
                    if(chessBoard.board[kingX][col].piece.white != white) {
                        return 1;
                    }
                }
                break;
            }
        }
        for (int row = kingX+ 1; row <8; row++) {
            if (chessBoard.board[row][kingY].piece != null) {
                if (chessBoard.board[row][kingY].piece instanceof Rook || chessBoard.board[row][kingY].piece instanceof Queen) {
                    if(chessBoard.board[row][kingY].piece.white != white) {
                        return 1;
                    }
                }
                break;
            }
        }
        for (int row = kingX-  1; row >=0; row--) {
            if (chessBoard.board[row][kingY].piece != null) {
                if (chessBoard.board[row][kingY].piece instanceof Rook || chessBoard.board[row][kingY].piece instanceof Queen) {
                    if(chessBoard.board[row][kingY].piece.white != white) {
                        return 1;
                    }
                }
                break;
            }
        }
        return -1;
    }

    /**
     * Check to see if a king will be in danger by another king at a position
     * @param chessBoard - reference to main chessboard
     * @param kingX - x position you want to check
     * @param kingY - y position you want to check
     * @param white - color of the king
     * @return return 1 on check, -1 if no check
     */
    public int kingCheck (Board chessBoard, int kingX, int kingY, boolean white) {
        for(int row =0; row <8; row++) {
            for (int col =0; col < 8; col++) {
                if(Math.abs(col-kingY) <= 1 && Math.abs(row-kingX) <= 1) {
                    if (col == kingY && row == kingX) {
                    }
                    if (chessBoard.board[kingX][col].piece != null) {
                        if(chessBoard.board[row][col].piece instanceof King) {
                            if (chessBoard.board[row][col].piece.white != white){
                                return 1;
                            }

                        }
                    }
                }
            }
        }
        return -1;
    }


    /**
     * Check to see if a king will be in danger at a position
     * @param chessBoard - reference to main chessboard
     * @param kingX - x position you want to check
     * @param kingY - y position you want to check
     * @param white - color of the king
     * @return return 1 on check, -1 if no check
     */
    public int positionalCheck(Board chessBoard, int kingX, int kingY, boolean white) {

        if (knightCheck(chessBoard, kingX, kingY, white) == 1) {
            return 1;
        }
        if (pawnCheck(chessBoard, kingX, kingY, white) == 1) {
            return 1;
        }
        if (diagCheck(chessBoard, kingX,kingY, white)==1){
            return 1;
        }
        if (linCheck(chessBoard, kingX,kingY, white)==1){
            return 1;
        }
        if (kingCheck(chessBoard, kingX,kingY, white)==1) {
            return 1;
        }

        return -1;
    }

    /**
     * see if white king is in check at a given board state
     * @param chessBoard - reference to main chessboard
     * @return return 1 on check, -1 if no check
     */
    public int checkWhite(Board chessBoard) {
        return positionalCheck(chessBoard, chessBoard.whiteKingX,chessBoard.whiteKingY, true);
    }


    /**
     * see if white king is in check at a given board state
     * @param chessBoard - reference to main chessboard
     * @return return 1 on check, -1 if no check
     */
    public int checkBlack(Board chessBoard) {
        return positionalCheck(chessBoard, chessBoard.blackKingX,chessBoard.blackKingY, false);
    }


    /**
     * test a move and see if own king would be in check
     * @param piece - piece to move
     * @param chessBoard - reference to main chessboard
     * @param oldX - old x position of piece to move
     * @param oldY - old y position of piece to move
     * @param newX - new x position of piece to move
     * @param newY - new y position of piece to move
     * @return returns -1 if move would put own king in danger, 1 if king would be safe
     */
    public int testMove(Piece piece, Board chessBoard,int oldX, int oldY, int newX, int newY) {
        int result = -1;
        Piece tempOrigPiece = chessBoard.board[oldX][oldY].piece;
        Piece tempPiece = chessBoard.board[newX][newY].piece;
        if (piece instanceof Rook) {
            chessBoard.board[newX][newY].piece = new Rook(piece.x,piece.y,piece.name,chessBoard);
        }
        else if (piece instanceof Bishop) {
            chessBoard.board[newX][newY].piece = new Bishop(piece.x,piece.y,piece.name,chessBoard);
        }
        else if (piece instanceof Knight) {
            chessBoard.board[newX][newY].piece = new Knight(piece.x,piece.y,piece.name,chessBoard);
        }
        else if (piece instanceof Pawn) {
            chessBoard.board[newX][newY].piece = new Pawn(piece.x,piece.y,piece.name,chessBoard);
        }
        else if (piece instanceof King) {
            chessBoard.board[newX][newY].piece = new King(piece.x,piece.y,piece.name,chessBoard);

        }
        else if (piece instanceof Queen) {
            chessBoard.board[newX][newY].piece = new Queen(piece.x,piece.y,piece.name,chessBoard);
        }
        chessBoard.board[oldX][oldY].piece = null;
        chessBoard.findKings();

        if (piece.white ) {
            if(checkWhite(chessBoard) ==1) {
                result = 1;
            }
        }
        else {
            if(checkBlack(chessBoard) ==1) {
                result = 1;
            }
        }
        chessBoard.board[oldX][oldY].piece = tempOrigPiece;
        chessBoard.board[newX][newY].piece = tempPiece;
        chessBoard.findKings();
        return result;
    }


    /**
     * check to see if in checkmate
     * @param white - who to check to see if they are in checkmate
     * if white = true, check if white has potential moves.
     * @return return true if in checkmate, return false if not
     */
    public boolean checkmate(boolean white) {
        Piece holder;
        for(int i = 0; i<board.length ; i++) {
            for(int j = 0; j<board[i].length; j++) {
                if(board[i][j].piece!=null) {
                    holder = board[i][j].piece;
                    if (holder.white== white){
                        //System.out.println("potentialMoves");
                        for (int k=0; k < holder.potentialMoves.size();k++) {
                            //System.out.println(holder.potentialMoves.get(k));
                            if(testMove(holder,this,i,j,Character.getNumericValue(holder.potentialMoves.get(k).charAt(0)),
                                    Character.getNumericValue(holder.potentialMoves.get(k).charAt(2))) == -1) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }


    /**
     * finds location of both white and black king.
     */
    public void findKings() {
        for(int i = 0; i<board.length ; i++) {
            for(int j = 0; j<board[i].length; j++) {
                if (board[i][j].piece != null) {
                    if (board[i][j].piece.name.charAt(1) == 'K') {
                        if(board[i][j].piece.white) {
                            whiteKingX=i;
                            whiteKingY=j;
                        }
                        else {
                            blackKingX=i;
                            blackKingY=j;
                        }
                    }
                }
            }
        }
    }

    /**
     * Uses system.out.print to print out the 2-D array board
     */
    public void printBoard(){
        int rank = 8;
        for(int i = 0; i<board.length ; i++) {
            for(int j = 0; j<board[i].length; j++) {
                if(board[i][j].piece!=null) {
                    System.out.print(board[i][j].getName()+" ");
                }
                else if(board[i][j].blackspot==true) {
                    System.out.print("## ");
                }
                else {
                    System.out.print("   ");
                }
            }
            System.out.print(rank);
            rank--;
            System.out.println("");
        }
        System.out.println(" a  b  c  d  e  f  g  h");
        System.out.println("");
    }
}
