///////////////////////////////////Board///////////////////////////////////////
/**
 * Main class.  Includes the GUI.
 * @author zalpha314
 *
 * Board Grid Layout
 * (0,0) (1,0) (2,0)...
 * (1,0) (1,1) (1,2)...
 * (2,0) (2,1) (2,2)...
 * .....
 */

package chess;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Color;

public class Board extends JFrame{
    public static final int GRID_SIZE = 8, WINDOW_SIZE = 700;
    public static final Color[] COLORS = {Color.white, Color.black };
    public static final double VERSION = 1.11;
    private Square grid[][];
    private Player currentPlayer, player[];
    private IOManager io;
    private BackupManager backup;

////////////////////////////////Constructor/////////////////////////////////////

    public Board(Player player[]){
        setSize(WINDOW_SIZE, WINDOW_SIZE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        setResizable(false);        
        this.player = player;
        setTitle(player[1].getName() + "'s turn.");
        io = new IOManager(this);
        backup = new BackupManager(this);

        //Place and colour squares
        grid = new Square[GRID_SIZE][GRID_SIZE];
        for (int j = 0 ; j < GRID_SIZE ; j++){
            for (int i = 0 ; i < GRID_SIZE ; i++){
                grid[i][j] = new Square(i,j,COLORS[(i + j)%2], this, io);
                add(grid[i][j]);
            }
        }        
    }

///////////////////////////////////Accessors///////////////////////////////////
  
    public Player getPlayer(){
        return currentPlayer;
    }
    
    public Player getPlayer(int i){
        return player[i];
    }
    
    public Square getSquare(int x, int y){
        if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE){
            IOManager.alert("Error from getSquare: Square " + x + "," + y + " does not exist.");
            return null;
        }
        return grid[x][y];
    }

    public IOManager getIOManager(){
        return io;
    }

    public BackupManager getBackupManager(){
        return backup;
    }

//////////////////////////////////Methods///////////////////////////////////////

    public void createMenu(){        
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem quit = new JMenuItem("Quit");
        newGame.addActionListener(io);
        quit.addActionListener(io);
        fileMenu.add(newGame);
        fileMenu.add(quit);

        JMenu game = new JMenu("Game");
        JMenuItem surrender = new JMenuItem("Surrender");
        surrender.addActionListener(io);
        game.add(surrender);

        JMenu about = new JMenu("About");
        JMenuItem aboutChess = new JMenuItem("About Chess");
        JMenuItem changeLog = new JMenuItem("Change log");
        aboutChess.addActionListener(io);
        changeLog.addActionListener(io);
        about.add(aboutChess);
        about.add(changeLog);

        menuBar.add(fileMenu);
        menuBar.add(game);
        menuBar.add(about);        
        setJMenuBar(menuBar);
    }
    
    /**
     * Places all of the pieces on the board
     */
    private void setPieces(){
        //Clear middle of board existing pieces
        for (int j = 2 ; j < Board.GRID_SIZE -2 ; j++){
            for (int i = 0 ; i < Board.GRID_SIZE ; i++){
                if (grid[i][j].getPiece() != null){
                    grid[i][j].setPiece("empty", null);
                }
            }
        }
        
        //Place pawns
        for (int i = 0 ; i < GRID_SIZE ; i++){
            grid[i][1].setPiece("Pawn",player[2]); //Player 2
            grid[i][6].setPiece("Pawn",player[1]); //Player 1
        }
        grid[0][0].setPiece("Rook",player[2]);
        grid[7][0].setPiece("Rook",player[2]);
        grid[0][7].setPiece("Rook",player[1]);
        grid[7][7].setPiece("Rook",player[1]);
        
        grid[1][0].setPiece("Knight", player[2]);
        grid[6][0].setPiece("Knight", player[2]);
        grid[1][7].setPiece("Knight", player[1]);
        grid[6][7].setPiece("Knight", player[1]);
        
        grid[2][0].setPiece("Bishop", player[2]);
        grid[5][0].setPiece("Bishop", player[2]);
        grid[2][7].setPiece("Bishop", player[1]);
        grid[5][7].setPiece("Bishop", player[1]);
        
        grid[3][0].setPiece("Queen",player[2]);
        grid[3][7].setPiece("Queen",player[1]);
        
        grid[4][0].setPiece("King",player[2]);
        grid[4][7].setPiece("King",player[1]);

        //Set all pieces' moved status to false
        for (int j = 0 ; j < Board.GRID_SIZE ; j++){
            for (int i = 0 ; i < Board.GRID_SIZE ; i++){
                if (grid[i][j].getPiece() != null){
                    grid[i][j].getPiece().setMoved(false);
                }
            }
        }
    }

    /**
     * Check if your move put the enemy king in check
     */
    public void enemyInCheck(){
        King enemyKing;
        if (currentPlayer.getNumber() == 1){
            enemyKing = getPlayer(2).getKing();
        }
        else{
            enemyKing = getPlayer(1).getKing();
        }
        if (enemyKing.isInCheck()){
           setTitle(getPlayer().getName() + "'s turn.     CHECK!");
        }else{
            setTitle(getPlayer().getName() + "'s turn.");
        }
    }

    /**
     * Change whose turn it currently is
     */
    public void switchPlayer(){
        if (getPlayer().getNumber() == 1){
            currentPlayer = player[2];
        }else{
            currentPlayer = player[1];
        }
    }
    
    /**
     * Forces each piece to calculate which spaces they can move to and sends their player their reference
     */
    public void updateSpaces(){
        player[1].clearPieces();
        player[2].clearPieces();
        for (int j = 0 ; j < GRID_SIZE ; j++){
            for (int i = 0 ; i < GRID_SIZE ; i++){
                if (grid[i][j].getPiece() != null){
                    grid[i][j].getPiece().updateAttackSpaces();
                }
            }
        }
    }

    public void newGame(){
        setVisible(false);
        setPieces();
        updateSpaces();
        currentPlayer = getPlayer(1);
        setVisible(true);
    }

////////////////////////////////Static Methods//////////////////////////////////

    public static void main(String[] args){  
        Player player[] = new Player[3];
        for (int i = 1 ; i  < 3 ; i++){
            String name = JOptionPane.showInputDialog("Player " + i + " name:");
            player[i] = new Player(name, i);
        }
        Board board = new Board(player);
        board.setVisible(false);
        board.createMenu();
        board.newGame();
    }
}
///////////////////////////////////////////////////////////////////////////////