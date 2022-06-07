package edu.duke.xl346.battleship;
import java.util.function.Function;

/**
 * This class handles textual display of 
 * a Board (i.e., converting it to a string to show
 * to the user).
 * It supports two ways to display the Boards;
 * one for he players's own board, and one for the
 * enemy's board.
 */
public class BoardTextView {
  /**
   * The Board to display
   */
  private final Board<Character> toDisplay;
  /**
   * Constucts a BoardView, given the board it will display.
   * @param toDisplay is the Board to display
   * @throws IllegalArgumentException if the board is larger than 10x26.
   */
  public BoardTextView(Board<Character> toDisplay){
    this.toDisplay = toDisplay;
    if(toDisplay.getWidth() > 10 || toDisplay.getHeight() > 26) {
      throw new IllegalArgumentException("Board must be no larger than 10x26, but is " +
                                         toDisplay.getWidth() + "x" + toDisplay.getHeight());
    }
  }

  /** 
   *This make use of function makeHeader() and display the Board with specific width and height
   *@param getSquareFn Input a Coordinate and output a Character.  
   *@return the String that includes both header and body of the Board  
   */  
  protected String displayAnyBoard(Function<Coordinate, Character> getSquareFn) {
    StringBuilder ans = new StringBuilder("");
    String header = makeHeader();
    ans.append(header);
    int letter = 65;
    String sep = "";
    for (int row = 0; row< toDisplay.getHeight(); ++row) {
      ans.append((char) letter);
      ans.append(" ");
      for (int column = 0; column < toDisplay.getWidth(); ++column) {
        ans.append(sep);
        if (getSquareFn.apply(new Coordinate(row, column)) != null) {
          ans.append(getSquareFn.apply(new Coordinate(row, column)));
        } else {
          ans.append(" ");
        }
        sep = "|";
      }
      ans.append(" ");
      ans.append((char) letter);
      ans.append("\n");
      ++letter;
      sep = "";
    }
    ans.append(header);
    return ans.toString(); //this is a placehold
  }

  /**
   *Make use of displayAnyBoard(Function) with a lambda that calls toDisplay.whatIsAtForSelf.  
   */  
  public String displayMyOwnBoard() {
    return displayAnyBoard((c) -> toDisplay.whatIsAtForSelf(c));
  }
  
  /**
   *Make use of displayAnyBoard(Function) with a lambda that calls toDisplay.whatIsAtForEnemy.  
   */ 
  public String displayEnemyBoard() {
    return displayAnyBoard((c) -> toDisplay.whatIsAtForEnemy(c));
  }
  /**
   * This makes the header line, e.g. 0|1|2|3|4\n
   *
   * @return the String that is the hearder line for the given board
  t   */
  String makeHeader(){
    StringBuilder ans  = new StringBuilder("  ");//README shows two spaces at
    String sep=""; //start with nothing to seperate, then switch to | to seperate
    for (int i = 0; i < toDisplay.getWidth(); ++i) {
      ans.append(sep);
      ans.append(i);
      sep = "|";
    }
    ans.append("\n");
    return ans.toString();
  }

  private void appendNSpace(StringBuilder sb, int n) {
    for (int i = 0; i < n; ++i) {
      sb.append(" ");
    }
  }

  /**
   *This method displays myBoard from my view and from enemy's view.
   *@param enemyView Enemy's view.
   *@param myHeader The header above my board
   *@param enemyHeader  The header above enemy's board
   *@return answer.toString() The String of my board plus enemy's board.
   *@throws IllegalArgumentException  If the lines of my board is not the same as enemy's board.
   */
  public String displayMyBoardWithEnemyNextToIt(BoardTextView enemyView, String myHeader, String enemyHeader) {
    String[] myLines = displayMyOwnBoard().split("\n");
    String[] enemyLines = enemyView.displayEnemyBoard().split("\n");
    if (myLines.length != enemyLines.length) {
      throw new IllegalArgumentException("Boards must be the same height to put side by side");
    }
    StringBuilder answer = new StringBuilder();
    int enemyHdrStartCol = toDisplay.getWidth() * 2 + 32;
    int enemyBoardStartCol = toDisplay.getWidth() * 2 + 29;
    appendNSpace(answer, 5);
    answer.append(myHeader);
    appendNSpace(answer, enemyHdrStartCol - myHeader.length() - 5);
    answer.append(enemyHeader);
    answer.append("\n");
    for (int i = 0; i < myLines.length; ++i) {
      int nSpaces = enemyBoardStartCol - myLines[i].length();
      answer.append(myLines[i]);
      appendNSpace(answer, nSpaces);
      answer.append(enemyLines[i]);
      answer.append("\n");
    }
    return answer.toString();
  }

  
}













