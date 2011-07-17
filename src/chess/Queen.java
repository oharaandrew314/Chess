/**
 * The Queen piece.
 * @author zalpha314
 */

package chess;

import javax.swing.ImageIcon;

public class Queen extends Piece{

    public Queen(Player player, Square square){
        super(player, square);
        name = "Queen";
        if (player.getNumber() == 1){
            square.setIcon(new ImageIcon(getClass().getResource("/chess/resources/img/queen_white.png")));
        }else{
            square.setIcon(new ImageIcon(getClass().getResource("/chess/resources/img/queen_black.png")));
        }
    }
    
    public void updateAttackSpaces(){
        getPlayer().addPiece(this);
        attackSpaces.clear();
        int i = getGridX();
        //Checking vertically and horizontally
        for (int j = getGridY() +1 ; j <= Board.GRID_SIZE ; j++){  //Checking spaces below
            if (!canCheckAnother(i,j)){
                break;
            }   
        }
        for (int j = getGridY() -1 ; j >= 0 ; j--){ //Checking spaces above
            if (!canCheckAnother(i,j)){
                break;
            }   
        }
        int j = getGridY();
        for (i = getGridX() +1 ; i <= Board.GRID_SIZE ; i++){ //Checking spaces to the right
            if (!canCheckAnother(i,j)){
                break;
            }   
        }
        for (i = getGridX() -1 ; i >= 0 ; i--){ //Checking spaces to the left
            if (!canCheckAnother(i,j)){
                break;
            }   
        }
        
        //Checking diagonally
        i = getGridX() +1;
        for (j = getGridY() +1 ; j < Board.GRID_SIZE ; j++){ //check bottom-right
            if (!canCheckAnother(i,j)){
                break;
            }   
            i++;
        }
        i = getGridX() -1;
        for (j = getGridY() +1 ; j < Board.GRID_SIZE ; j++){ //check bottom-left
            if (!canCheckAnother(i,j)){
                break;
            }   
            i--;
        }
        i = getGridX() +1;
        for (j = getGridY() -1 ; j >= 0 ; j--){ //check top-right
            if (!canCheckAnother(i,j)){
                break;
            }   
            i++;
        }
        i = getGridX() -1;
        for (j = getGridY() -1 ; j >= 0 ; j--){ //check top-left
            if (!canCheckAnother(i,j)){
                break;
            }    
            i--;
        }
    }
}
