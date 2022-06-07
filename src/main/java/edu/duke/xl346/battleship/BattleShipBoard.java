package edu.duke.xl346.battleship;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class BattleShipBoard<T> implements Board<T> {
  private final int width;
  private final int height;
  final ArrayList<Ship<T>> myShips;
  final T missInfo;
  private final PlacementRuleChecker<T> placementChecker;
  HashSet<Coordinate> enemyMisses;

  public static int randomlyPickFrom(int a, int b) {
    return a + (int) (Math.random() * (b - a + 1));
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  /**
   * public PlacementRuleChecker<T> getPlacementChecker() { return
   * placementChecker; }
   */
  /**
   * 
   * Constructs a BattleShipBoard with the specified width and height
   * 
   * @param w is the width of the newly constructed board.
   * @param h is the height of the newly constructed board.
   * @throws IllegalArgumentException if the width or height are less than or
   *                                  equal to zero.
   */
  BattleShipBoard(int w, int h, PlacementRuleChecker<T> placementChecker, T missInfo) {
    if (w <= 0) {
      throw new IllegalArgumentException("BattleShipBoard's width must be positive but is " + w);
    }
    if (h <= 0) {
      throw new IllegalArgumentException("BattleShipBoard's height must be positive but is " + h);
    }
    this.width = w;
    this.height = h;
    this.myShips = new ArrayList<Ship<T>>();
    this.placementChecker = placementChecker;
    this.missInfo = missInfo;
    this.enemyMisses = new HashSet<Coordinate>();
  }

  BattleShipBoard(int w, int h, T missInfo) {
    this(w, h, new NoCollisionRuleChecker<T>(new InBoundsRuleChecker<T>(null)), missInfo);
  }

  /**
   * This method first check if the ship to add is valid and then if so add to the
   * Board.
   * 
   * @param toAdd Ship to be added to the Board.
   * @return myShips.add(toAdd) if toAdd is valid. Otherwise is false.
   */
  public String tryAddShip(Ship<T> toAdd) {
    String placementProblem = placementChecker.checkPlacement(toAdd, this);
    if (placementProblem == null) {
      myShips.add(toAdd);
    }
    return placementProblem;
  };

  public int numberOfShips(){
    System.out.println(myShips.size());
    return myShips.size();
  }

  /**
   * Get the data of self ships at given coordinate
   * 
   * @param where The target coordinate to get the data.
   * @return whatIsAt(where, true) True means self ships.
   */
  public T whatIsAtForSelf(Coordinate where) {
    return whatIsAt(where, true);
  }

  /**
   * Get the data of enemy ships at given coordinate
   * 
   * @param where The target coordinate to get the data.
   * @return whatIsAt(where, true) False means enemy ships.
   */
  public T whatIsAtForEnemy(Coordinate where) {
    return whatIsAt(where, false);
  }

  /**
   * Called by whatIsAtForSelf and whatIsAtForEnemy
   * 
   * @param where  The target coordinate to get the data.
   * @param isSelf Whether to get self's data or enemy's data.
   * @return s.getDisplayInfoAt(where, isSelf) If there's ship occuping this
   *         point.
   * @return missInfo If the point is hit by the enemy but no self ships here and
   *         get data from the perspective of enemy.
   * @return null If the point has miss and get data from the perspective of self
   *         Or the point is empty.
   */
  protected T whatIsAt(Coordinate where, boolean isSelf) {
    for (Ship<T> s : myShips) {
      if (s.occupiesCoordinates(where)) {
        return s.getDisplayInfoAt(where, isSelf);
      }
    }
    if (!isSelf && enemyMisses.contains(where)) {
      return missInfo;
    }
    return null;
  }
  public boolean isSameCoordinate(Coordinate c1, Coordinate c2){
    return c1.getRow()==c2.getRow() && c1.getColumn()==c2.getColumn();
  }
  public String removeShipFromBoardIfExist(Coordinate where){
    for (Ship<T> s : myShips) {
      if (isSameCoordinate(s.getUpperLeft(), where)) {
        //remove this Ship from the Board.
        myShips.remove(s);
        return null;
      }
    }
    return "No Ship At ("+where.getRow()+","+where.getColumn();
  }
  public Ship<T> fireAt(Coordinate c) {
    for (Ship<T> s : myShips) {
      if (s.occupiesCoordinates(c)) {
        // record hit here
        s.recordHitAt(c);
        return s;
      }
    }
    enemyMisses.add(c);
    return null;
  }

  /**
   * Check if all the ships on the Board are sunk.
   * 
   * @return true If all the ships on the Board are sunk. Otherwise is false.
   */
  public boolean ifAllSunk() {
    for (Ship<T> s : myShips) {
      if (s.isSunk() == false)
        return false;
    }
    return true;
  }

  public boolean move(Coordinate c, Placement p) {
    Ship<T> target = null;
    Coordinate original = null;
    for (Ship<T> s : myShips) {
      if (s.getUpperLeft() == c || s.occupiesCoordinates(c)) {
        original = s.getUpperLeft();
        target = s;
        myShips.remove(s);
        break;
      }
    }
    if (target == null){
      //System.out.println("Coordinate " + c.toString());
      //System.out.println("num of sunk ships: "+ getNumOfSunkShips());
      //System.out.println("target == null");
      return false;
    }
    target.moveTo(p);
    String placementProblem = placementChecker.checkPlacement(target, this);
    if (placementProblem != null) {
      target.moveTo(new Placement(original, 'U'));
      myShips.add(target);
      return false;
    }
    myShips.add(target);
    return true;
  }

  public int sonarScanning(Coordinate c, T target) {
    int row = c.getRow();
    int column = c.getColumn();
    int count = 0;
    for (int i = row + 3; i >= row - 3; --i) {
      if (target == whatIsAtForSelf(new Coordinate(i, column))) {
        ++count;
      }

    }
    for (int len = 2; len >= 0; --len) {
      for (int i = row + len; i >= row - len; --i) {
        if (target == whatIsAtForSelf(new Coordinate(i, column - 3 + len))) {
          ++count;
        }
        if (target == whatIsAtForSelf(new Coordinate(i, column + 3 - len))) {
          ++count;
        }
      }
    }
    return count;

  }

  public Coordinate pickSunkShip() {
    Coordinate res = null;
    for (Ship<T> ship : myShips) {
      if (ship.isSunk()) {
        if (res == null || BattleShipBoard.randomlyPickFrom(0, 1) == 1) {
          res = ship.getUpperLeft();
        }
      }
    }
    return res;
  }

  public Coordinate randomlyPickShip() {
    Coordinate res = null;
    for (Ship<T> ship : myShips) {
      if (res == null || BattleShipBoard.randomlyPickFrom(0, 1) == 1) {
        res = ship.getUpperLeft();
      }
    }
    return res;
  }

  public ArrayList<Coordinate> getAvailablePoints(boolean isToGetAll) {
    ArrayList<Coordinate> availablePoints = new ArrayList<Coordinate>();
    for (int i = 0; i < height; ++i) {
      for (int j = 0; j < width; ++j) {
        if (isToGetAll) {
          availablePoints.add(new Coordinate(i, j));
        }else if (whatIsAtForSelf(new Coordinate(i, j)) == null) {
          availablePoints.add(new Coordinate(i, j));
        }
      }
    }
    return availablePoints;
  }

  public ArrayList<Placement> getAvailablePlacements(boolean isTOGetAll, char type_of_ship) {
    ArrayList<Coordinate> availablePoints = getAvailablePoints(isTOGetAll);
    ArrayList<Placement> availablePlacements = new ArrayList<Placement>();
    if (type_of_ship == 'S' || type_of_ship == 'D') {
      for (int i = 0; i < availablePoints.size(); ++i) {
        availablePlacements.add(new Placement(availablePoints.get(i), 'H'));
        availablePlacements.add(new Placement(availablePoints.get(i), 'V'));
      }
    } else {
      for (int i = 0; i < availablePoints.size(); ++i) {
        availablePlacements.add(new Placement(availablePoints.get(i), 'U'));
        availablePlacements.add(new Placement(availablePoints.get(i), 'R'));
        availablePlacements.add(new Placement(availablePoints.get(i), 'D'));
        availablePlacements.add(new Placement(availablePoints.get(i), 'L'));
      }
    }
    return availablePlacements;
  }
  public Placement pickPlacement(ArrayList<Placement> availablePlacements) {
    // ArrayList<Coordinate> emptyPoints = getEmptyPoints();
    // int row = BattleShipBoard.randomlyPickFrom(0, height);
    // int column = BattleShipBoard.randomlyPickFrom(0, width);
    int random_point = BattleShipBoard.randomlyPickFrom(0, availablePlacements.size() - 1);
    // char orientation_of_ship = ' ';
    // int random_choice = BattleShipBoard.randomlyPickFrom(0, 3);
    // if (type_of_ship == 'S' || type_of_ship == 'D') {
    //   if (random_choice == 0 || random_choice == 2) {
    //     orientation_of_ship = 'V';
    //   } else {
    //     orientation_of_ship = 'H';
    //   }
    // } else {
    //   if (random_choice == 0) {
    //     orientation_of_ship = 'U';
    //   } else if (random_choice == 1) {
    //     orientation_of_ship = 'R';
    //   } else if (random_choice == 2) {
    //     orientation_of_ship = 'D';
    //   } else {
    //     orientation_of_ship = 'L';
    //   }
    // }
    // Coordinate res = emptyPoints.remove(random_point);
    
    return availablePlacements.remove(random_point);

  }

  public int getNumOfSunkShips() {
    int num = 0;
    for (Ship<T> ship : myShips) {
      if (ship.isSunk())
        ++num;
    }
    return num;
  }

  public boolean isInRange(Coordinate where) {
    return where.getRow() >= 0 && where.getRow() < height && where.getColumn() >= 0 && where.getColumn() < width;
  }
}














