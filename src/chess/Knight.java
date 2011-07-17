/**
 * The Knight piece.
 * @author zalpha314
 */

package chess;

import javax.swing.ImageIcon;
import java.awt.Point;

public class Knight extends Piece{
    
    private Point[] points;

    public Knight(Player player, Square square){
        super(player, square);
        name = "Knight";
        if (player.getNumber() == 1){
            square.setIcon(new ImageIcon(getClass().getResource("/chess/resources/img/knight_white.png")));
        }else{
            square.setIcon(new ImageIcon(getClass().getResource("/chess/resources/img/knight_black.png")));
        }
        points = new Point[8];
        for (int i = 0 ; i < 8 ; i++){
            points[i] = new Point(0,0);
        }
    }
    
    public void updateAttackSpaces(){
        getPlayer().addPiece(this);
        attackSpaces.clear();
        points[0].setLocation(getGridX() +1, getGridY() -2);
        points[1].setLocation(getGridX() +2, getGridY() -1);
        points[2].setLocation(getGridX() +2, getGridY() +1);
        points[3].setLocation(getGridX() +1, getGridY() +2);
        points[4].setLocation(getGridX() -1, getGridY() +2);
        points[5].setLocation(getGridX() -2, getGridY() +1);
        points[6].setLocation(getGridX() -2, getGridY() -1);
        points[7].setLocation(getGridX() -1, getGridY() -2);
        
        int i,j;
        for (int z = 0 ; z < 8 ; z++){
            i = (int) points[z].getX();
            j = (int) points[z].getY();
            if (!checkSpace(i,j).equals("bad")){
                attackSpaces.add(points[z]);
            }
        }
    }
}