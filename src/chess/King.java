/**
 * The King piece.
 * @author zalpha314
 */

package chess;
import javax.swing.ImageIcon;
import java.awt.Point;
import java.awt.Color;

public class King extends Piece{
    
    private Point[] points;

    public King(Player player, Square square){
        super(player, square);
        name = "King";
        if (player.getNumber() == 1){
            square.setIcon(new ImageIcon(getClass().getResource("/chess/resources/img/king_white.png")));
        }else{
            square.setIcon(new ImageIcon(getClass().getResource("/chess/resources/img/king_black.png")));
        }
        points = new Point[8];
        for (int i = 0 ; i < 8 ; i++){
            points[i] = new Point(0,0);
        }
    }
    
    public void updateAttackSpaces(){ 
        getPlayer().addPiece(this);
        attackSpaces.clear();
        points[0].setLocation(getGridX() +1, getGridY());
        points[1].setLocation(getGridX() +1, getGridY() +1);
        points[2].setLocation(getGridX() +1, getGridY() -1);
        points[3].setLocation(getGridX() -1, getGridY());
        points[4].setLocation(getGridX() -1, getGridY() +1);
        points[5].setLocation(getGridX() -1, getGridY() -1);
        points[6].setLocation(getGridX(), getGridY() +1);
        points[7].setLocation(getGridX(), getGridY() -1);
        
        int i,j;
        for (int z = 0 ; z < 8 ; z++){
            i = (int) points[z].getX();
            j = (int) points[z].getY();
            if (!checkSpace(i,j).equals("bad")){
                attackSpaces.add(points[z]);
            }
        }   
    }
    
    /**
     * Overrides the pickup method to add a check for castling
     */
    @Override
    public void pickUp(){
        super.pickUp();        
        if (moved == false){
            int j = 0;
            if (getPlayer().getNumber() == 1){
                j = 7;
            }      
            if (getSquare(0,j).getPiece() instanceof Rook && getSquare(1,j).getPiece() == null && getSquare(2,j).getPiece() == null && getSquare(2,j).getPiece() == null){
                if (getSquare(0,j).getPiece().getPlayer() == getPlayer() && !getSquare(0,j).getPiece().getMoved()){ //Queen-side Castle available
                    getSquare(0,j).setBackground(Color.cyan);
                }
            }
            if (getSquare(7,j).getPiece() instanceof Rook && getSquare(5,j).getPiece() == null && getSquare(6,j).getPiece() == null){
                if (getSquare(7,j).getPiece().getPlayer() == getPlayer() && !getSquare(7,j).getPiece().getMoved()){ //King-side Castle available
                    getSquare(7,j).setBackground(Color.cyan);
                }
            }
        }
    }
    
    /**
     * If possible, attempt to King/Queen-side castle the King
     * @param newSquare
     * @return 
     */
    public boolean castle(Square rookSquare){  
        backup.clear();
        backup.backupAdd(rookSquare); //Backup Rook
        backup.backupAdd(square); //Backup King
        int j = getGridY(); 
        if (rookSquare.getGridX() == 7){ //King-side castle   
            backup.backupAdd(getSquare(6,j));
            backup.backupAdd(getSquare(5,j));
            getSquare(6, j).setPiece("King", getPlayer());
            getSquare(5, j).setPiece("Rook", getPlayer());
        }
        else{ //Queen-side castle
            backup.backupAdd(getSquare(2,j));
            backup.backupAdd(getSquare(3,j));
            getSquare(2, j).setPiece("King", getPlayer());
            getSquare(3, j).setPiece("Rook", getPlayer());
        }
        rookSquare.setPiece("empty", null); //Clear Rook's previous space
        square.setPiece("empty", null); //Clear King's previous space
        square.getBoard().updateSpaces();
        return kingSafe();
    }
    
    /**
     * @return Returns true if your move puts the enemy king in check
     */
    public boolean isInCheck(){
        for (Piece piece : getEnemyPlayer().getPieces()){
            if (piece.canAttack(getGridX(), getGridY())){
                getSquare().setBackground(Color.orange);
                IOManager.print("Check.");
                return true;
            }
        }
        return false;
    }
}