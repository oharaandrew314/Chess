/////////////////////////////////IO Manager/////////////////////////////////////
/**
 * Handles input and output to the user
 * @author zalpha314
 */
package chess;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;
import javax.swing.JMenuItem;
import java.awt.event.*;
import java.awt.Color;
import java.io.File;
import java.util.Scanner;

public class IOManager implements ActionListener{
    private Board board;
    private Square selectedSquare;

/////////////////////////////////////Constructor////////////////////////////////

    public IOManager(Board board){
        this.board = board;
    }

///////////////////////////////Non-static Methods///////////////////////////////

    public int surrenderDialog(){
        String title = (board.getPlayer().getKing().getEnemyPlayer().getName() + " wins!");
        if(JOptionPane.showConfirmDialog(null, "Are you sure you want to surrender?", "Really Surrender?", JOptionPane.YES_NO_OPTION) == 0){
            String message = "Would you like to play another game?";
            int choice = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
            return choice;
        }
        return 2;
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() instanceof Square){
            buttonAction((Square) e.getSource());
        }else{
            menuAction((JMenuItem) e.getSource());
        }
    }

    public void buttonAction(Square newSquare){
        //Move or attack
        if (newSquare.getBackground().equals(Color.red) || newSquare.getBackground().equals(Color.blue)){
            selectedSquare.getPiece().putDown();
            if (selectedSquare.getPiece().move(newSquare)){ //If the move was succesful (didn't put your king in check)
                board.enemyInCheck();
                board.switchPlayer();
            }
            selectedSquare = null;
        }
        //Put Down
        else if (newSquare == selectedSquare){
            newSquare.getPiece().putDown();
            selectedSquare = null;
            board.getPlayer().getKing().isInCheck(); //If your king was in check, then this will repaint him in check
        }
        //Pickup
        else if (newSquare.getPiece() != null  && selectedSquare == null){
            if (newSquare.getPiece().getPlayer() == board.getPlayer()){
                selectedSquare = newSquare;
                newSquare.getPiece().pickUp();
            }
        }
        //Special Move
        else if (newSquare.getBackground().equals(Color.cyan)){
            selectedSquare.getPiece().putDown();
            if (newSquare.getPiece() instanceof Rook){ //Castle
                if (((King) selectedSquare.getPiece()).castle(newSquare)){ //If the castling was succesful (didn't put your king in check)
                    board.enemyInCheck();
                    board.switchPlayer();
                }
            }
            else if (newSquare.getPiece() == null){ //En Passant
                if (((Pawn) selectedSquare.getPiece()).enPassant(newSquare)){ //If the En Passant was succesful (didn't put your king in check)
                    board.enemyInCheck();
                    board.switchPlayer();
                }
            selectedSquare = null;
            }
        }
    }

    public void menuAction(JMenuItem item){
            if (item.getText().equals("Quit")){
                board.dispose();
            }
            else if (item.getText().equals("New Game")){
                board.newGame();
                selectedSquare = null;
            }
            else if (item.getText().equals("Surrender")){
                int choice = surrenderDialog();
                if (choice == 0){
                    board.newGame();
                    selectedSquare = null;
                }
                else if (choice == 1){
                    board.dispose();
                }
            }
            else if (item.getText().equals("About Chess")){
                about();
            }
            else if (item.getText().equals("Change log")){
                changeLog();
            }
            else{
                alert("Error: Uncaught menu action!");
            }
    }

    private void changeLog(){
        File file = new File(getClass().getResource("/chess/resources/changeLog.txt").getPath());
        try {
            Scanner scanner = new Scanner(file).useDelimiter("--END_OF_FILE--");
            alert(scanner.next());
        } catch (FileNotFoundException ex) {
            alert("ChangeLog not found!");
        }
    }

//////////////////////////////Static Methods/////////////////////////////////////

    private static void about(){
        String message = "Chess - Written by Andrew O'Hara\nVersion " + Board.VERSION +
                "\n~1 200 lines of code\nLicensed under GNU GPL v3\nEdited piece icons are the property of their respective owner\nWritten in Java with the Java Swing Framework";
        JOptionPane.showMessageDialog(null, message, "About Chess - By Andrew O'Hara", JOptionPane.INFORMATION_MESSAGE);
    }

     /**
     *Outputs an alert message via dialog box
     * @param message
     */
    public static void alert(String message){
        JOptionPane.showMessageDialog(null,message);
    }

    /**
     * Prints a message via the console
     * @param message
     */
    public static void print(String message){
        System.out.println(message);
    }
}
////////////////////////////////////////////////////////////////////////////////