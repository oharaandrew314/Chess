///////////////////////////////////Player///////////////////////////////////////
/**
 * A player profile
 * @author zalpha314
 */

package chess;
import java.util.ArrayList;

public class Player{

    private ArrayList<Piece> pieces;
    private String name;
    private int number;
    private King king;
    
//////////////////////////////////Constructor///////////////////////////////////

    public Player(String name, int number){
        pieces = new ArrayList<Piece>();
        this.name = name;
        this.number = number;
    }
    
//////////////////////////////////Accessors/////////////////////////////////////

    public String getName(){
        return name;
    }

    public int getNumber(){
        return number;
    }
    
    public ArrayList<Piece> getPieces(){
        return pieces;
    }
    
    public King getKing(){
        return king;
    }

///////////////////////////////////Mutators/////////////////////////////////////

    /**
     * Adds the given piece the the player's ArrayList of pieces
     * @param piece 
     */
    public void addPiece(Piece piece){
        if (piece instanceof King){
            king = (King) piece;
        }
        pieces.add(piece);
    }
    
    /**
     * Clears the player's ArrayList of pieces so they can be properly re-added
     */
    public void clearPieces(){
        pieces.clear();
    }
}
////////////////////////////////////////////////////////////////////////////////