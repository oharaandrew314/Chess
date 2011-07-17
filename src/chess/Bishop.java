/**
 * The Bishop piece.
 * @author zalpha314
 */

package chess;

import javax.swing.ImageIcon;

public class Bishop extends Piece{

    public Bishop(Player player, Square square){
        super(player, square);
        name = "Bishop";
        if (player.getNumber() == 1){
            square.setIcon(new ImageIcon(getClass().getResource("/chess/resources/img/bishop_white.png")));
        }else{
            square.setIcon(new ImageIcon(getClass().getResource("/chess/resources/img/bishop_black.png")));
        }
    }
    
    public void updateAttackSpaces(){
        getPlayer().addPiece(this);
        attackSpaces.clear();
        int i = getGridX() +1;
        for (int j = getGridY() +1 ; j < Board.GRID_SIZE ; j++){ //check bottom-right
            if (!canCheckAnother(i,j)){
                break;
            } 
            i++;
        }
        i = getGridX() -1;
        for (int j = getGridY() +1 ; j < Board.GRID_SIZE ; j++){ //check bottom-left
            if (!canCheckAnother(i,j)){
                break;
            } 
            i--;
        }
        i = getGridX() +1;
        for (int j = getGridY() -1 ; j >= 0 ; j--){ //check top-right
            if (!canCheckAnother(i,j)){
                break;
            } 
            i++;
        }
        i = getGridX() -1;
        for (int j = getGridY() -1 ; j >= 0 ; j--){ //check top-left
            if (!canCheckAnother(i,j)){
                break;
            }  
            i--;
        }
    }        
}