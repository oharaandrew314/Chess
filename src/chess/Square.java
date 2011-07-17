////////////////////////////////////Square/////////////////////////////////////
/**
 * A square on the board.  Every Square has-a Piece.
 * @author Andrew O'Hara
 */

package chess;
import javax.swing.JButton;
import java.awt.Color;

public class Square extends JButton{

    private Piece piece;
    private Board board;
    private int gridX, gridY;
/////////////////////////////////Constructor///////////////////////////////////

    public Square(int gridX, int gridY, Color color, Board board, IOManager io){
        setSize(Board.WINDOW_SIZE / 8, Board.WINDOW_SIZE / 8);        
        setBackground(color);
        this.board = board;
        addActionListener(io);
        this.gridX = gridX;
        this.gridY = gridY;
        piece = null;
    }

//////////////////////////////////Accessors/////////////////////////////////////

    public Piece getPiece(){
        return piece;
    }

    public Board getBoard(){
        return board;
    }

    public int getGridX(){
        return gridX;
    }

    public int getGridY(){
        return gridY;
    }

///////////////////////////////////Mutators/////////////////////////////////////

    /**
     * Takes the name of the piece and the player who it is to be given to and
     * puts that piece on the current square instance
     * @param name
     * @param player
     */
    public void setPiece(String name, Player player){
        if (name.equals("empty")){
            piece = null;
            setIcon(null);
        }
        else if (name.equals("Pawn")){
            piece = new Pawn(player,this);
        }
        else if (name.equals("Rook")){
            piece = new Rook(player,this);
        }
        else if (name.equals("Knight")){
            piece = new Knight(player,this);
        }
        else if (name.equals("Bishop")){
            piece = new Bishop(player,this);
        }
        else if (name.equals("Queen")){
            piece = new Queen(player,this);
        }
        else if (name.equals("King")){
            piece = new King(player,this);
        }
        else{
            IOManager.alert("Attempted to place invalid piece on the board.");
        }
    }  
}
///////////////////////////////////////////////////////////////////////////////