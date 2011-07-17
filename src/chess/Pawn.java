/**
 * The Pawn piece.
 * @author zalpha314
 */

package chess;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Point;
import java.util.ArrayList;

public class Pawn extends Piece{

    private int dMod; //Determines which direction is forward for this Pawn
    private ArrayList<Point> moveSpaces;

    public Pawn(Player player, Square square){
        super(player, square);       
        name = "Pawn";
        moveSpaces = new ArrayList<Point>();
        if (player.getNumber() == 1){
            dMod = -1;
            square.setIcon(new ImageIcon(getClass().getResource("/chess/resources/img/pawn_white.png")));
        }else{
            dMod = 1;
            square.setIcon(new ImageIcon(getClass().getResource("/chess/resources/img/pawn_black.png")));
        }
    }
    
    /**
     * Paints all squares legal to move and attack for the current pawn.
     * Also paints the square underneath the piece to show that it has been picked up.
     */
    @Override
    public void pickUp(){
        square.setBackground(Color.green);
        int i, j;
        for (Point point : moveSpaces){
            i = (int) point.getX();
            j = (int) point.getY();
            if (checkSpace(i,j).equals("empty")){
                getSquare(i, j).setBackground(Color.blue);
            }
        }
        for (Point point : attackSpaces){
            i = (int) point.getX();
            j = (int) point.getY();
            if (checkSpace(i,j).equals("enemy")){
                getSquare(i,j).setBackground(Color.red);
            }
        }
        
        //En Passant
        i = getGridX();
        j = getGridY();
        if (i > 0){ //Left En Passant
            if (getSquare(i-1,j-dMod).getPiece() instanceof Pawn && getSquare(i-1,j).getPiece() instanceof Pawn && getSquare(i-1,j+dMod).getPiece() == null){
                if (getSquare(i-1,j-dMod).getPiece().getPlayer() == getPlayer() && getSquare(i-1,j).getPiece().getPlayer() != getPlayer()){
                    getSquare(i-1,j+dMod).setBackground(Color.cyan);
                }
            }
        }
        if (i < 7){ //Right En Passant
            if (getSquare(i+1,j-dMod).getPiece() instanceof Pawn && getSquare(i+1,j).getPiece() instanceof Pawn && getSquare(i+1,j+dMod).getPiece() == null){
                if (getSquare(i+1,j-dMod).getPiece().getPlayer() == getPlayer() && getSquare(i+1,j).getPiece().getPlayer() != getPlayer()){
                    getSquare(i+1,j+dMod).setBackground(Color.cyan);
                }
            }
        }
    }
    
    /**
     * The square currently holding the pawn to be moved moves the pawn to the new square and checks if it can then be promoted
     * @param newSquare 
     */
    @Override
    public boolean move(Square newSquare){
        Boolean kingSafe = super.move(newSquare);
        if (newSquare.getGridY() == 0 || newSquare.getGridY() == 7 && kingSafe){
            ((Pawn) newSquare.getPiece()).promote();
            square.getBoard().updateSpaces();
            //check();
        }        
        return kingSafe;
    }
    
    /**
     * If possible, perform an En Passant
     * @param newSquare
     * @return 
     */
    public boolean enPassant(Square newSquare){
        IOManager.print("en Passant");
        int i = newSquare.getGridX();
        int j = square.getGridY();
        backup.clear();
        backup.backupAdd(newSquare);
        backup.backupAdd(square);
        backup.backupAdd(getSquare(i,j)); //Backup the piece being captured
        newSquare.setPiece("Pawn", player);
        square.setPiece("empty", null);
        getSquare(i,j).setPiece("empty", null);
        square.getBoard().updateSpaces();
        return kingSafe();
    }
    
    public void updateAttackSpaces(){
        getPlayer().addPiece(this);
        attackSpaces.clear();
        moveSpaces.clear();
        if (checkSpace(getGridX(),getGridY() + dMod).equals("empty")){
            moveSpaces.add(new Point(getGridX(), getGridY() + dMod));
            if (checkSpace(getGridX(), getGridY() + dMod*2).equals("empty")  && (getGridY() == 1 || getGridY() == 6)){
                moveSpaces.add(new Point(getGridX(), getGridY() + dMod*2));
            }
        }
        if (getGridX() <= 7){
            if (checkSpace(getGridX() +1, getGridY() + dMod).equals("enemy")){
                attackSpaces.add(new Point(getGridX() +1, getGridY() + dMod));
            }
        }
        if (getGridX() >= 0){
            if (checkSpace(getGridX() -1, getGridY() + dMod).equals("enemy")){
                attackSpaces.add(new Point(getGridX() -1, getGridY() + dMod));
            }
        }
    }
    
    /**
     * If the pawn is eligible for promotion, begins the process
     */
    public void promote(){
        String[] options = { "Queen","Bishop","Knight","Rook" };
        String pieceName = (String) JOptionPane.showInputDialog(null, "Which piece would you like the pawn to promote to?", "A pawn is elegible for promotion", 
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        square.setPiece(pieceName, getPlayer());
    }
}