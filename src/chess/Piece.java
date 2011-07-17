///////////////////--////////////////Piece/////////////////////////////////////
/**
 * Superclass for all Pieces.  Sits on top of a square.
 * @author zalpha314
 */

package chess;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.Point;

public abstract class Piece{

    protected Player player;
    protected Square square;
    protected String name;
    protected ArrayList<Point> attackSpaces;
    protected boolean moved;
    protected BackupManager backup;

/////////////////////////////////Constructor///////////////////////////////////

    public Piece(Player player, Square square){
        this.player = player;
        this.square = square;
        attackSpaces = new ArrayList<Point>();
        moved = true;
        backup = square.getBoard().getBackupManager();
    }

///////////////////////////////////Accessors////////////////////////////////////

    public Player getPlayer(){
        return player;
    }
    
    public Square getSquare(){
        return square;
    }

    public Square getSquare(int x, int y){
        return square.getBoard().getSquare(x, y);
    }

    public int getGridX(){
        return (int) square.getGridX();
    }

    public int getGridY(){
        return (int) square.getGridY();
    }
    
    public String getName(){
        return name;
    }
    
    public boolean getMoved(){
        return moved;
    }
    
    public boolean canAttack(int i, int j){
        return attackSpaces.contains(new Point(i,j));
    }
    
    /**
     * @param i
     * @param j
     * @return Returns a String message of what type of piece is at the given coordinates
     */
    public String checkSpace(int i, int j){
        if (i < Board.GRID_SIZE && i >= 0 && j < Board.GRID_SIZE && j >= 0){
            if (getSquare(i,j).getPiece() == null){
                return "empty";
            }        
            else if (getSquare(i,j).getPiece().getPlayer() != getPlayer()){
                return "enemy";
            }
        }
        return "bad";
    }
    
    public Player getEnemyPlayer(){
        if (getPlayer().getNumber() == 1){
            return square.getBoard().getPlayer(2);
        }else{
            return square.getBoard().getPlayer(1);
        }
    }

////////////////////////////////////Mutators////////////////////////////////////
    
    public abstract void updateAttackSpaces();

    public void setMoved(boolean bool){
        moved = bool;
    }

    /**
     * Simulates putting the piece down by repainting the squares
     */
    public void putDown(){
        for (int j = 0 ; j < Board.GRID_SIZE ; j++){
            for (int i = 0 ; i < Board.GRID_SIZE ; i++){
                getSquare(i,j).setBackground(Board.COLORS[(i + j)%2]);
            }
        }
    }
    
    /**
     * The square currently holding the piece to be moved moves the piece to the new square
     * @param newSquare 
     */
    public boolean move(Square newSquare){
        backup.clear();
        backup.backupAdd(square);
        backup.backupAdd(newSquare);
        newSquare.setPiece(name, getPlayer());
        square.setPiece("empty",null);
        square.getBoard().updateSpaces();
        return kingSafe();
    }

    public void pickUp(){
        square.setBackground(Color.green);
        for (Point point : attackSpaces){
            int i = (int) point.getX();
            int j = (int) point.getY();
            if (checkSpace(i,j).equals("empty")){
                getSquare(i, j).setBackground(Color.blue);
            }
            else if (checkSpace(i,j).equals("enemy")){
                getSquare(i,j).setBackground(Color.red);
            }
        }
    }
   
    /**
     * Checks if your move would put your king in check.  If so, undoes the move
     * @return Returns true if your king would be put in check by your move
     */
    protected boolean kingSafe(){
        King king = getPlayer().getKing();
        for (Piece enemyPiece : getEnemyPlayer().getPieces()){
            if (enemyPiece.canAttack(king.getGridX(), king.getGridY())){
                king.getSquare().setBackground(Color.orange);
                IOManager.alert("You cannot move this piece there without endangering your king.");
                backup.backupRestore();
                return false;
            }
        }
        return true;
    }
    
    /**
     * Updates the attackSpaces array accordingly for each coordinate and returns whether it can keep checking in this direction
     * @param i
     * @param j
     * @return Returns whether you can keep checking in the current direction for the current piece
     */
    protected boolean canCheckAnother(int i, int j){
        if (checkSpace(i,j).equals("empty")){
            attackSpaces.add(new Point(i,j));
            return true;
        }
        else if (checkSpace(i,j).equals("enemy")){
            attackSpaces.add(new Point(i,j));
            return false;
        }
        return false;                
    }
}
///////////////////////////////////////////////////////////////////////////////