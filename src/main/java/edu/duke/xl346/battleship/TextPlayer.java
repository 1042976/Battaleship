package edu.duke.xl346.battleship;
import com.sun.management.VMOption;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class TextPlayer {
  final Board<Character> theBoard;
  final BoardTextView view;
  final BufferedReader inputReader;
  final PrintStream out;
  final AbstractShipFactory<Character> shipFactory;
  final String name;
  final ArrayList<String> shipsToPlace;
  final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;
  final int unit;
  private int moves;
  private int sonars;
  private Boolean isAI;
  // private ArrayList<ArrayList<Integer> > scores_matrix;
  private int[][] scores_matrix;
  //private JFrame jFrame;

  /**
   * Contructs the textplayer, given the Board to play, the input , the output and
   * the name of player.
   * @param theBoard    The Board to play.
   * @param inputReader The input from players.
   * @param out         The View of the Board after operation.
   * @parem name The name of player
   */
  public TextPlayer(String name, Board<Character> theBoard, BufferedReader inputReader, PrintStream out,
      AbstractShipFactory<Character> shipFactory) {
    this.name = name;
    this.theBoard = theBoard;
    this.view = new BoardTextView(theBoard);
    this.inputReader = inputReader;
    this.out = out;
    this.shipFactory = shipFactory;
    this.shipsToPlace = new ArrayList<String>();
    this.shipCreationFns = new HashMap<String, Function<Placement, Ship<Character>>>();
    this.moves = 3;
    this.sonars = 3;
    this.isAI = false;
    this.scores_matrix = new int[theBoard.getHeight()][theBoard.getWidth()];
    for (int i = 0; i < theBoard.getHeight(); ++i) {
      // this.scores_matrix[i] = new int[theBoard.getWidth()];
      for (int j = 0; j < theBoard.getWidth(); ++j) {
        this.scores_matrix[i][j] = 0;
      }
    }
    this.unit = 30;

    setupShipCreationMap();
    setupChipCreationList();
  }

  /**
   * Read a placemant entered by the palyer. Serve as the helper function of
   * doOnePlacement().
   * 
   * @param prompt Messsage sent to players, requiring players to give a
   *               placement.
   * @return new Placement(s) The placement from players.
   */
  public Placement readPlacement(String prompt) throws IOException {
    out.println(prompt);
    String s = inputReader.readLine();
    if (s == null) {
      throw new EOFException("Error: Could not read a new string to create Placement.");
    }
    return new Placement(s);
  }

  /**
   * This function is for GUI. Given ship's name and orientation and location, add ship to the Board
   * @param shipNameAndOrientation
   * @param point
   */
  public String doOnePlacementWithoutPrompt(String shipNameAndOrientation, Point point) {
    int len = shipNameAndOrientation.length();
    String shipName = shipNameAndOrientation.substring(0, len-1);
    char orientation = shipNameAndOrientation.charAt(len-1);
    Coordinate location = new Coordinate(point.y-5, point.x-2);
    out.println(location);
    Placement p = new Placement(location, orientation);
    Function<Placement, Ship<Character>> createFn = shipCreationFns.get(shipName);
    Ship<Character> s = createFn.apply(p);
    return theBoard.tryAddShip(s);
  }
  /**
   * Read placement from players and Update the board.
   */
  public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
    String res = null;
    Placement p = null;
    ArrayList<Placement> availablePlacements = theBoard.getAvailablePlacements(false, shipName.toUpperCase().charAt(0));
    do {
      try {
        if (!isAI) {
          p = readPlacement("Player " + name + " where do you want to place a " + shipName + "?");
        } else {
          p = theBoard.pickPlacement(availablePlacements);
          // out.println("Player " + name + " is placing a " + shipName);
        }
        Ship<Character> s = createFn.apply(p);
        res = theBoard.tryAddShip(s);
      } catch (IllegalArgumentException iae) {
        res = "That placement is invalid: it does not have the correct format.";
      }
      if (res != null) {
        if (!isAI) {
          out.println(res);
          out.println(view.displayMyOwnBoard());
        }
      }
    } while (res != null);
    if (!isAI) {
      out.println(view.displayMyOwnBoard());
    } else {
      out.println("Player " + name + " is placing a " + shipName);
    }

  }

  /**
   * This method displays the starting(empty) board, prints the instructions
   * message calls doOnePlacement to place one ship.
   */
  public void doPlacementPhase() throws IOException, InterruptedException {
    if(isAI){
     if (!isAI) {
       out.println((new BoardTextView(theBoard)).displayMyOwnBoard());
       String message = "--------------------------------------------------------------------------------\n" + "Player "
            + name + ": you are going to place the following ships \n"
            + "For each ship, type the coordinate of the upper left\n"
            + "side of the ship. Submarine and Destroyer are followed\n"
           + "by either H (for horizontal) or V (for vertical) while\n"
           + "Battleship and Carrier are followed by U, R, D or L(for up, right, down or left). \n" + "You have\n" + "\n"
           + "2 \"Submarines\"\n" + "3 \"Destroyers\"\n" + "3 \"Battleships\"\n" + "2 \"Carriers\"\n"
           + "--------------------------------------------------------------------------------\n";
       out.println(message);
      }
     for (String nameofShip : shipsToPlace) {
        doOnePlacement(nameofShip, shipCreationFns.get(nameofShip));
      }
    }else{
      JFrame jFrame = new JFrame();
      jFrame.setBounds(150,90, 28*this.unit, 32*this.unit);
      jFrame.setResizable(false);
      jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      jFrame.add(new GamePanel());
      jFrame.setVisible(true);
      while(ImgData.isFinished==false){
        TimeUnit.SECONDS.sleep(1);
      }
      jFrame.dispose();
      ImgData.isFinished = false;
    }
  }

  /**
   * Put the String -> lambda mappings into shipCreationFns
   */
  protected void setupShipCreationMap() {
    shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
    shipCreationFns.put("Destroyer", (p) -> shipFactory.makeDestroyer(p));
    shipCreationFns.put("Battleship", (p) -> shipFactory.makeBattleship(p));
    shipCreationFns.put("Carrier", (p) -> shipFactory.makeCarrier(p));
  }

  /**
   * Put in the ships we wnat to add, in the order we want to add them.
   */
  protected void setupChipCreationList() {
    shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
    shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
    shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
    shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
  }

  /**
   * Checking for win/lose. Lose if all ships in the board is sunk.
   */
  public boolean ifHasLost() {
    return theBoard.ifAllSunk();
  }

  /**
   * Judge if an input coordinate string is invalid.
   * 
   * @param descr The input coordinate string.
   * @return true if the format of descr does not meet the requirement.
   */
  private boolean isInvalid(String descr) {
    descr = descr.toUpperCase();
    int len = descr.length();
    if (len != 2) {
      return true;
    }
    char c1 = descr.charAt(0);
    if (c1 < 'A' || c1 > 'A' + theBoard.getHeight() - 1) {
      return true;
    }
    char c2 = descr.charAt(1);
    if (c2 < '0' || c2 > '0' + theBoard.getWidth() - 1) {
      return true;
    }
    return false;
  }

  /**
   * read coordinate from input.
   * 
   * @param prompt The message sent to player, asking to input a coordinate
   *               string.
   * @return new Coordinate(s) The valid coordinate.
   */
  protected Coordinate readCoordinate(String prompt) throws IOException {
    String s = null;
    do {
      out.println(prompt);
      s = inputReader.readLine();
      if (s == null) {
        throw new EOFException("Error: Could not read a new string to create Coordinate.");
      }
    } while (isInvalid(s));
    return new Coordinate(s);
  }

  protected Placement readPlacementForMove(String prompt) throws IOException {
    String s = null;
    do {
      out.println(prompt);
      s = inputReader.readLine();
      if (s == null) {
        throw new EOFException("Error: Could not read a new string to create Coordinate.");
      }
    } while (isInvalid(s.substring(0, 2)) || s.length() != 3);
    return new Placement(s);
  }

  private Character getChar(String s) {
    return s.charAt(0);
  }

  protected Character toChooseAction() throws IOException {
    if (!isAI) {
      String s = null;
      String prompt1 = "Possible actions for Player " + this.name + "\n\n";
      String prompt_F = " F Fire at a square\n";
      String prompt_M = " M Move a ship to another square (" + moves + " remaininig)\n";
      String prompt_S = " S Sonar scan (" + sonars + " remaining)\n";
      while (true) {
        if (!isAI) {
          out.println(prompt1);
          out.println(prompt_F);

          if (moves > 0) {
            out.println(prompt_M);
          }
          if (sonars > 0) {
            out.println(prompt_S);
          }
          out.println("Please select one valid action above:");

          s = inputReader.readLine();
          s = s.toLowerCase();
          if (s.length() != 1 || getChar(s) == 'm' && moves == 0 || getChar(s) == 's' && sonars == 0
              || getChar(s) != 'm' && getChar(s) != 's' && getChar(s) != 'f') {
            continue;
          } else {
            break;
          }
        }
      }
      if (getChar(s) == 'm') {
        --moves;
        return 'M';
      } else if (getChar(s) == 's') {
        --sonars;
        return 'S';
      } else {
        return 'F';
      }
    } else {
      int option = BattleShipBoard.randomlyPickFrom(0, 2);
      if (option == 0) {
        return 'F';
      } else if (option == 1) {
        if (moves == 0 || theBoard.getNumOfSunkShips() == 0) {
          return 'F';
        } else {
          --moves;
          return 'M';
        }
      } else {
        if (sonars == 0) {
          return 'F';
        } else {
          --sonars;
          return 'S';
        }
      }

    }

  }

  protected void addScores(Coordinate targetCor, int val) {
    scores_matrix[targetCor.getRow()][targetCor.getColumn()] += val;
  }

  protected void addSurrounding(Coordinate targetCor, int val) {
    for (int i = targetCor.getRow() - 1; i <= targetCor.getRow() + 1; ++i) {
      for (int j = targetCor.getColumn() - 1; j <= targetCor.getColumn() + 1; ++j) {
        if (i >= 0 && i < theBoard.getHeight() && j >= 0 && j < theBoard.getWidth()) {
          scores_matrix[i][j] += val;
        }
      }
    }
  }

  protected void addSonarSurrounding(Coordinate targetCor, int val) {
    int row = targetCor.getRow(), column = targetCor.getColumn();
    for (int i = row + 3; i >= row - 3; --i) {
      if (theBoard.isInRange(new Coordinate(i, column))) {
        scores_matrix[i][column] += val;
      }
    }
    for (int len = 2; len >= 0; --len) {
      for (int i = row + len; i >= row - len; --i) {
        if (theBoard.isInRange(new Coordinate(i, column - 3 + len))) {
          scores_matrix[i][column - 3 + len] += val;
        }
        if (theBoard.isInRange(new Coordinate(i, column + 3 - len))) {
          scores_matrix[i][column + 3 - len] += val;
        }
      }
    }
  }

  protected Coordinate getPointWithLargestScore() {
    int max_val = Integer.MIN_VALUE;
    Coordinate res = new Coordinate(0, 0);
    for (int i = 0; i < theBoard.getHeight(); ++i) {
      for (int j = 0; j < theBoard.getWidth(); ++j) {
        if (scores_matrix[i][j] > max_val) {
          res = new Coordinate(i, j);
          max_val = scores_matrix[i][j];
        } else if (scores_matrix[i][j] == max_val && BattleShipBoard.randomlyPickFrom(0, 1) == 1) {
          res = new Coordinate(i, j);
        }
      }
    }
    return res;
  }

  protected Coordinate getPointWithSmallestScore() {
    int min_val = Integer.MAX_VALUE;
    Coordinate res = new Coordinate(0, 0);
    for (int i = 0; i < theBoard.getHeight(); ++i) {
      for (int j = 0; j < theBoard.getWidth(); ++j) {
        if (scores_matrix[i][j] < min_val) {
          res = new Coordinate(i, j);
          min_val = scores_matrix[i][j];
        } else if (scores_matrix[i][j] == min_val && BattleShipBoard.randomlyPickFrom(0, 1) == 1) {
          res = new Coordinate(i, j);
        }
      }
    }
    return res;
  }
  protected Coordinate pickPoint(char action) {
    Coordinate targetCor = null;
    if (action == 'F') {
      return getPointWithLargestScore();
    } else if (action == 'S') {
      return getPointWithSmallestScore();
    } else if (action == 'M') {
      targetCor = theBoard.pickSunkShip();
      if (targetCor == null) {
        targetCor = theBoard.randomlyPickShip();
      }
      return targetCor;
    } else {
      return null;
    }
  }

  protected void firing(Board<Character> enemyBoard) throws IOException {
    Coordinate targetCor = null;
    if (!isAI) {
      // prompt a message to ask user to enter a coordinate(format: A0H.etc) to hit.
      targetCor = readCoordinate("Please enter a valid coordinate to fire at:");
    } else {
      targetCor = pickPoint('F');
    }

    // fire at the chosen coordinate.
    Ship<Character> s = enemyBoard.fireAt(targetCor);
    // show the hitting result(type of ships or missed)
    if (s == null) {
      if (!isAI) {
        out.println("You missed!");
      } else {
        out.println("Player " + name + " missed!");
      }
      // addSurrounding(targetCor, -1);
    } else {
      if (!isAI) {
        out.println("You hit a " + s.getName().toLowerCase() + "!");
      } else {
        out.println("Player " + name + " hit your " + s.getName().toLowerCase() + " at " + targetCor.toString());
      }
      // addSurrounding(targetCor, 2);
    }
    // set the score to -10 at this point.
    addScores(targetCor, -2);
  }

  protected void sonarScanning(Board<Character> enemyBoard) throws IOException {
    Coordinate targetCor = null;
    if (!isAI) {
      targetCor = readCoordinate("Please enter a valid coordinate for sonar scanning:");
    } else {
      targetCor = pickPoint('S');
    }
    sonarScanning2(targetCor);
  }

  protected void moving() throws IOException {
    Coordinate targetCor = null;
    Placement destination = null;
    if (!isAI) {
      do {
        targetCor = readCoordinate("Please enter a valid coordinate to select a ship to move:");
        destination = readPlacementForMove("Please enter a valid placement to move to:");
      } while (!theBoard.move(targetCor, destination));
    } else {
      ArrayList<Placement> availablePlacements = theBoard.getAvailablePlacements(true, 'S');
      targetCor = pickPoint('M');
      out.println("targetCor is " + targetCor.toString());
      do {
        if (availablePlacements.isEmpty()) {
          availablePlacements = theBoard.getAvailablePlacements(true, 'S');
          targetCor = pickPoint('M');
          out.println("targetCor is " + targetCor.toString());
        }
        destination = theBoard.pickPlacement(availablePlacements);

      } while (!theBoard.move(targetCor, destination));
    }
  }

  protected void askIfChangeToAI(String prompt) throws IOException {
    String s = null;
    do {
      out.println(prompt);
      s = inputReader.readLine().toLowerCase();
    } while (!s.equals("yes") && !s.equals("no"));
    if (s.equals("yes")) {
      this.isAI = true;
    } else {
      this.isAI = false;
    }
  }

  public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException {
    // show both boards from self view
    if (!isAI) {
      out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", "Player " + enemyName + "'s ocean"));
    }
    if (moves == 0 && sonars == 0) {
      firing(enemyBoard);
    } else {
      Character action = toChooseAction();
      if (action == 'F') {
        firing(enemyBoard);
      } else {
        if (isAI) {
          out.println("Player " + name + " used a special action");
        }
        if (action == 'M') {
          moving();
        } else {
          sonarScanning(enemyBoard);
        }
      }
    }

    // show the boards after hitting
    if (!isAI) {
      out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", "Player " + enemyName + "'s ocean"));
      askIfChangeToAI("Player " + name + " would you like to let computer play for you in this game?(yes/no)");
    }
    // return.
  }

  public void sonarScanning2(Coordinate c) {
    int s_num = theBoard.sonarScanning(c, 's');
    int d_num = theBoard.sonarScanning(c, 'd');
    int b_num = theBoard.sonarScanning(c, 'b');
    int c_num = theBoard.sonarScanning(c, 'c');
    addSonarSurrounding(c, s_num + d_num + b_num + c_num);
    if (isAI) {
      return;
    }
    String ss = " squares\n";
    String dd = " squares\n";
    String bb = " squares\n";
    String cc = " squares\n";
    if (s_num == 1)
      ss = " square\n";
    if (d_num == 1)
      dd = " square\n";
    if (b_num == 1)
      bb = " square\n";
    if (c_num == 1)
      cc = " square\n";
    String res = "Submarines occupy " + s_num + ss +

        "Destroyers occupy " + d_num + dd +

        "Battleships occupy " + b_num + bb +

        "Carriers occupy " + c_num + cc;
    out.println(res);
  }

  public void selectMode() throws IOException {
    String prompt = "Whether Player " + name + " is a human player(H or h) or to be played by the computer(C or c): ";
    String s = null;
    do {
      out.println(prompt);
      s = inputReader.readLine();
      if (s == null) {
        throw new EOFException("Error: Could not read a new string to select mode.");
      }
      s = s.toUpperCase();
    } while (!s.equals("H") && !s.equals("C"));
    if (s.equals("H")) {
      this.isAI = false;
      out.println("Player " + name + " is a human player.");
    } else {
      this.isAI = true;
      out.println("Player " + name + " is to be played by the computer.");
    }
  }

  public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, MouseInputListener, ActionListener {
    //ship name plus direction
    String sV = "SubmarineV", sH = "SubmarineH", dV = "DestroyerV", dH = "DestroyerH",
      bU = "BattleshipU", bR = "BattleshipR", bD = "BattleshipD", bL = "BattleshipL",
      cU = "CarrierU", cR = "CarrierR", cD = "CarrierD", cL = "CarrierL";
    //Choice Location for each type of ship
    Point[] points_for_submarine;
    Point[] points_for_destroyer;
    Point[] points_for_battleship;
    Point[] points_for_carrier;

    //upper left points and ship type and orientation
    HashMap<Point, String> placementOfShips;
    //initial points of each ship piece.
    ArrayList<Point> submarine1, submarine2, destroyer1, destroyer2, destroyer3, battleship1, battleship2,
    battleship3, carrier1, carrier2;
    //Point to move
    Point originalPoint = null;
    String originalShip = null;
    Point pointToMove = null;
    String shipToMove = null;
    Point pointOfMouse = new Point(0,0);

    //flash time
    Timer timer = new Timer(100,this);

    //rotating menu
    JPopupMenu popupHV = new JPopupMenu();
    JPopupMenu popupURDL = new JPopupMenu();
    Point popupHV_Location = new Point(0,0);
    Point popupURDL_Location = new Point(0,0);

    //Done button
    JButton doneButton = new JButton("Done");

    public GamePanel(){
      init();
      this.setFocusable(true);
      this.addMouseListener(this);
      this.addMouseMotionListener(this);
      timer.start();
    }
    private void drawTheShip(Graphics g, String shipType, int x, int y){
      if(shipType.equals(sV)){
        ImgData.upperLeftIcon_s.paintIcon(this,g,x*unit,y*unit);
        ImgData.submarine.paintIcon(this,g,x*unit,(y+1)*unit);
      }else if(shipType.equals(sH)){
        ImgData.upperLeftIcon_s.paintIcon(this,g,x*unit,y*unit);
        ImgData.submarine.paintIcon(this,g,(x+1)*unit,y*unit);
      }else if(shipType.equals(dV)){
        ImgData.upperLeftIcon_d.paintIcon(this,g,x*unit,y*unit);
        ImgData.destroyer.paintIcon(this,g,x*unit,(y+1)*unit);
        ImgData.destroyer.paintIcon(this,g,x*unit,(y+2)*unit);
      }else if(shipType.equals(dH)){
        ImgData.upperLeftIcon_d.paintIcon(this,g,x*unit,y*unit);
        ImgData.destroyer.paintIcon(this,g,(x+1)*unit,y*unit);
        ImgData.destroyer.paintIcon(this,g,(x+2)*unit,y*unit);
      }else if(shipType.equals(bU)){
        ImgData.upperLeftIcon.paintIcon(this,g,x*unit,y*unit);
        ImgData.battleShip.paintIcon(this,g,(x+1)*unit,y*unit);
        ImgData.battleShip.paintIcon(this,g,x*unit,(y+1)*unit);
        ImgData.battleShip.paintIcon(this,g,(x+1)*unit,(y+1)*unit);
        ImgData.battleShip.paintIcon(this,g,(x+2)*unit,(y+1)*unit);
      }else if(shipType.equals(bR)){
        ImgData.upperLeftIcon_b.paintIcon(this,g,x*unit,y*unit);
        ImgData.battleShip.paintIcon(this,g,x*unit,(y+1)*unit);
        ImgData.battleShip.paintIcon(this,g,(x+1)*unit,(y+1)*unit);
        ImgData.battleShip.paintIcon(this,g,x*unit,(y+2)*unit);
      }else if(shipType.equals(bD)){
        ImgData.upperLeftIcon_b.paintIcon(this,g,x*unit,y*unit);
        ImgData.battleShip.paintIcon(this,g,(x+1)*unit,y*unit);
        ImgData.battleShip.paintIcon(this,g,(x+2)*unit,y*unit);
        ImgData.battleShip.paintIcon(this,g,(x+1)*unit,(y+1)*unit);
      }else if(shipType.equals(bL)){
        ImgData.upperLeftIcon.paintIcon(this,g,x*unit,y*unit);
        ImgData.battleShip.paintIcon(this,g,(x+1)*unit,y*unit);
        ImgData.battleShip.paintIcon(this,g,x*unit,(y+1)*unit);
        ImgData.battleShip.paintIcon(this,g,(x+1)*unit,(y+1)*unit);
        ImgData.battleShip.paintIcon(this,g,(x+1)*unit,(y+2)*unit);
      }else if(shipType.equals(cU)){
        ImgData.upperLeftIcon_c.paintIcon(this,g,x*unit,y*unit);
        ImgData.carrier.paintIcon(this,g,x*unit,(y+1)*unit);
        ImgData.carrier.paintIcon(this,g,x*unit,(y+2)*unit);
        ImgData.carrier.paintIcon(this,g,(x+1)*unit,(y+2)*unit);
        ImgData.carrier.paintIcon(this,g,(x+1)*unit,(y+3)*unit);
        ImgData.carrier.paintIcon(this,g,(x+1)*unit,(y+4)*unit);
      }else if(shipType.equals(cR)){
        ImgData.upperLeftIcon.paintIcon(this,g,x*unit,y*unit);
        ImgData.carrier.paintIcon(this,g,(x+2)*unit,y*unit);
        ImgData.carrier.paintIcon(this,g,(x+3)*unit,y*unit);
        ImgData.carrier.paintIcon(this,g,(x+4)*unit,y*unit);
        ImgData.carrier.paintIcon(this,g,x*unit,(y+1)*unit);
        ImgData.carrier.paintIcon(this,g,(x+1)*unit,(y+1)*unit);
        ImgData.carrier.paintIcon(this,g,(x+2)*unit,(y+1)*unit);
      }else if(shipType.equals(cD)){
        ImgData.upperLeftIcon.paintIcon(this,g,x*unit,y*unit);
        ImgData.carrier.paintIcon(this,g,(x+1)*unit,y*unit);
        ImgData.carrier.paintIcon(this,g,(x+1)*unit,(y+1)*unit);
        ImgData.carrier.paintIcon(this,g,x*unit,(y+2)*unit);
        ImgData.carrier.paintIcon(this,g,(x+1)*unit,(y+2)*unit);
        ImgData.carrier.paintIcon(this,g,x*unit,(y+3)*unit);
        ImgData.carrier.paintIcon(this,g,x*unit,(y+4)*unit);
      }else if(shipType.equals(cL)){
        ImgData.upperLeftIcon_c.paintIcon(this,g,x*unit,y*unit);
        ImgData.carrier.paintIcon(this,g,(x+1)*unit,y*unit);
        ImgData.carrier.paintIcon(this,g,(x+2)*unit,y*unit);
        ImgData.carrier.paintIcon(this,g,(x+2)*unit,(y+1)*unit);
        ImgData.carrier.paintIcon(this,g,(x+3)*unit,(y+1)*unit);
        ImgData.carrier.paintIcon(this,g,(x+4)*unit,(y+1)*unit);
      }
      //ImgData.upperLeftIcon.paintIcon(this,g,x*unit,y*unit);
    }
    private void drawShipsOnPanel(Graphics g){
      for(Point point : placementOfShips.keySet()){
        String shipType = placementOfShips.get(point);
        int x = point.x;
        int y = point.y;
        drawTheShip(g,shipType,x,y);
      }
      if(pointToMove != null){
        drawTheShip(g, shipToMove, pointToMove.x, pointToMove.y);
      }
    }
    private void addNewShip(){
      if (pointToMove != null) {
        String addRes = doOnePlacementWithoutPrompt(shipToMove, pointToMove);
        if (addRes != null) {
          shipToMove = originalShip;
          pointToMove = originalPoint;
        }
        placementOfShips.put(pointToMove, shipToMove);
        doOnePlacementWithoutPrompt(shipToMove, pointToMove);
        pointToMove = null;
        shipToMove = null;
        originalShip = null;
        originalPoint = null;
      }
    }
    private ActionListener actionListener = new ActionListener() {
      private void removeOldShipWhenRotating(Point curPoint, String newDirec){
        if(placementOfShips.containsKey(curPoint)){
          originalShip = placementOfShips.get(curPoint);
          originalPoint = curPoint;
          int len = originalShip.length();
          shipToMove = originalShip.substring(0,len-1)+newDirec;
          pointToMove = curPoint;
          placementOfShips.remove(curPoint);
          theBoard.removeShipFromBoardIfExist(new Coordinate(curPoint.y-5, curPoint.x-2));
          out.println(originalShip + " with ("+(originalPoint.y-5) + ", "+(originalPoint.x-2)+ " is removed");
        }
      }
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        String action = actionEvent.getActionCommand();
        if(action.equals("H") || action.equals("V")){
          removeOldShipWhenRotating(popupHV_Location, action);
        }else if(action.equals("U") || action.equals("R")||action.equals("D")||action.equals("L")){
          removeOldShipWhenRotating(popupURDL_Location, action);
        }
        addNewShip();
      }
    };
    protected JMenuItem createMenuItem( String action, String text) {
      JMenuItem item = new JMenuItem(text);
      item.setActionCommand(action);
      item.addActionListener(actionListener);
      return item;
    }
    private boolean allShipsOnBoard(){
      return theBoard.numberOfShips() == 10;
    }
    public void init(){
      placementOfShips = new HashMap<>();

      points_for_submarine = new Point[2];
      points_for_submarine[0] = new Point(14,5);
      points_for_submarine[1] = new Point(17,5);
      points_for_destroyer = new Point[3];
      points_for_destroyer[0] = new Point(14,9);
      points_for_destroyer[1] = new Point(17,9);
      points_for_destroyer[2] = new Point(20,9);
      points_for_battleship = new Point[3];
      points_for_battleship[0] = new Point(14,14);
      points_for_battleship[1] = new Point(19,14);
      points_for_battleship[2] = new Point(24,14);
      points_for_carrier = new Point[2];
      points_for_carrier[0] = new Point(14,18);
      points_for_carrier[1] = new Point(17,18);

      placementOfShips.put(new Point(14,5), sV);
      placementOfShips.put(new Point(17,5), sV);
      placementOfShips.put(new Point(14,9), dV);
      placementOfShips.put(new Point(17,9), dV);
      placementOfShips.put(new Point(20,9), dV);
      placementOfShips.put(new Point(14,14), bU);
      placementOfShips.put(new Point(19,14), bU);
      placementOfShips.put(new Point(24,14), bU);
      placementOfShips.put(new Point(14,18), cU);
      placementOfShips.put(new Point(17,18), cU);
      popupHV.add(createMenuItem("H","H"));
      popupHV.add(createMenuItem( "V","V"));
      popupURDL.add(createMenuItem("U", "U"));
      popupURDL.add(createMenuItem("R", "R"));
      popupURDL.add(createMenuItem("D", "D"));
      popupURDL.add(createMenuItem("L", "L"));
      this.setLayout(null);
      //Done Button
      doneButton.setFont(new Font("TimesRoman", Font.BOLD, 40));
      doneButton.setBounds(22*unit, 23*unit,4*unit,2*unit);
      this.add(doneButton);
      doneButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
          ImgData.isFinished = allShipsOnBoard();
        }
      });
      doneButton.setVisible(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      this.setBackground(Color.WHITE);
      //Draw the Board
      for(int i = 2; i < 12; ++i){
        for(int j = 5; j < 25; ++j){
          ImgData.boardPiece.paintIcon(this, g, i*unit, j*unit);
        }
      }
      //Draw ships
      drawShipsOnPanel(g);

      //Draw the prompt text:
      g.setColor(Color.MAGENTA);
      g.setFont(new Font("TimesRoman", Font.BOLD, 30));
      g.drawString("Player "+ name + ": Please drag all ships to the board", 110, 80);
    }
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if(SwingUtilities.isLeftMouseButton(mouseEvent)){
          pointOfMouse = new Point(mouseEvent.getX()/unit, mouseEvent.getY()/unit);
          if(placementOfShips.containsKey(pointOfMouse)){
           originalShip = placementOfShips.get(pointOfMouse);
           originalPoint = pointOfMouse;
           shipToMove = placementOfShips.get(pointOfMouse);
           pointToMove = pointOfMouse;
           placementOfShips.remove(pointOfMouse);
           String removeRes = theBoard.removeShipFromBoardIfExist(new Coordinate(pointOfMouse.y-5, pointOfMouse.x-2));
          }
        }else if(SwingUtilities.isRightMouseButton(mouseEvent)){
          pointOfMouse = new Point(mouseEvent.getX()/unit, mouseEvent.getY()/unit);
          if(placementOfShips.containsKey(pointOfMouse)){
            String currentShip = placementOfShips.get(pointOfMouse);
            int len = currentShip.length();
            char direc = currentShip.charAt(len-1);
            if(direc == 'H' || direc == 'V'){
              popupHV.show(mouseEvent.getComponent(),mouseEvent.getX(),mouseEvent.getY());
              popupHV_Location = new Point(mouseEvent.getX()/unit, mouseEvent.getY()/unit);
            }else if(direc == 'U' || direc == 'R' || direc == 'D' || direc == 'L'){
              popupURDL.show(mouseEvent.getComponent(),mouseEvent.getX(),mouseEvent.getY());
              popupURDL_Location = new Point(mouseEvent.getX()/unit, mouseEvent.getY()/unit);
            }
          }
        }
    }
    private void setBackToChoice(String shipName){
      if(shipName.equals("Submarine")){
        for(int i = 0; i < 2;++i){
          if(!placementOfShips.containsKey(points_for_submarine[i])){
            placementOfShips.put(points_for_submarine[i], "SubmarineV");
            break;
          }
        }
      }else if(shipName.equals("Destroyer")){
        for(int i = 0; i < 3; ++i){
          if(!placementOfShips.containsKey(points_for_destroyer[i])){
            placementOfShips.put(points_for_destroyer[i], "DestroyerV");
            break;
          }
        }
      }else if(shipName.equals("Battleship")){
        for(int i = 0; i < 3; ++i){
          if(!placementOfShips.containsKey(points_for_battleship[i])){
            placementOfShips.put(points_for_battleship[i], "BattleshipU");
            break;
          }
        }
      }else if(shipName.equals("Carrier")){
        for(int i = 0; i < 2; ++i){
          if(!placementOfShips.containsKey(points_for_carrier[i])){
            placementOfShips.put(points_for_carrier[i], "CarrierU");
            break;
          }
        }
      }
      pointToMove = null;
      shipToMove = null;
      originalShip = null;
      originalPoint = null;
    }
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
      if(shipToMove != null){
        pointOfMouse = new Point(mouseEvent.getX()/unit, mouseEvent.getY()/unit);
        if(SwingUtilities.isLeftMouseButton(mouseEvent)){
          if( pointOfMouse.x>=2&&pointOfMouse.x <12 && pointOfMouse.y>=5&&pointOfMouse.y<25) {
           addNewShip();
         }else{
           String shipName = originalShip.substring(0,originalShip.length()-1);
           setBackToChoice(shipName);
         }
        }
      }
      if(allShipsOnBoard()){
        doneButton.setVisible(true);
      }else{
        doneButton.setVisible(false);
      }
    }
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
      if(SwingUtilities.isLeftMouseButton(mouseEvent)) {
        Point newPoint = new Point(mouseEvent.getX() / unit, mouseEvent.getY() / unit);
        if (pointToMove != null) {
          pointToMove = newPoint;
        }
      }
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      repaint();
      timer.start();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }



    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }


    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
  }
}
