//////////////////////////////////Backup Manager////////////////////////////////
/**
 * Handles the backup of pieces so they can be reset if an illegal move is made
 * @author zalpha314
 */
package chess;
import java.util.ArrayList;

public class BackupManager{
    private ArrayList<Square> backup;
    private Board board;

///////////////////////////////////Constructor//////////////////////////////////

    public BackupManager(Board board){
        this.board = board;
         backup = new ArrayList<Square>();
    }

//////////////////////////////////Methods///////////////////////////////////////

    public void clear(){
        backup.clear();
    }
    
    public void backupAdd(Square s){
        backup.add(new Square(s.getGridX(), s.getGridY(), null, board, null));
        if (s.getPiece() != null){
            backup.get(backup.size()-1).setPiece(s.getPiece().getName(), s.getPiece().getPlayer());
            backup.get(backup.size()-1).getPiece().setMoved(s.getPiece().getMoved());
        }
    }

    /**
     * Restores the board to before the last move was made
     * @param backup
     */
    public void backupRestore(){
        for (Square s : backup){
            if (s.getPiece() != null){
                board.getSquare(s.getGridX(), s.getGridY()).setPiece(s.getPiece().getName(), s.getPiece().getPlayer());
                board.getSquare(s.getGridX(), s.getGridY()).getPiece().setMoved(s.getPiece().getMoved());
            }else{
                board.getSquare(s.getGridX(), s.getGridY()).setPiece("empty", null);
            }
        }
        board.updateSpaces();
        board.getPlayer().getKing().putDown(); //This is just to repaint all the squares
    }
}
////////////////////////////////////////////////////////////////////////////////