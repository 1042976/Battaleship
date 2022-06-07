package edu.duke.xl346.battleship;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class BasicShip<T> implements Ship<T> {
  protected Coordinate upperleft;
  protected HashMap<Coordinate, Boolean> myPieces;
  protected ShipDisplayInfo<T> myDisplayInfo;
  protected ShipDisplayInfo<T> enemyDisplayInfo;
  /**
   * Constructs BasicShip with a set of Coordinates and myDisplayInfo.
   * 
   * @param where An iterable set of coordinates.
   *@param myDisplayInfo Type ShipDisplayInfo.  
   */
  public BasicShip(Coordinate upperleft, Iterable<Coordinate> where, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
    this.upperleft = upperleft;
    myPieces = new HashMap<Coordinate, Boolean>();
    for (Coordinate c : where) {
      myPieces.put(c, false);
    }
    this.myDisplayInfo = myDisplayInfo;
    this.enemyDisplayInfo = enemyDisplayInfo;
  }

  /**
   *A protected helper method to abstratc out the exception throwing
   *@param c The coordinate to be judged if is on the ship.
   */
  protected void checkCoordinateInThisShip(Coordinate c) {
    if (myPieces.get(c) == null) {
      throw new IllegalArgumentException("The coordinate is not in this ship");
    }
  }


  /**
   * This method judge if the given coordinate is where the ship occupies.
   * 
   * @param where The coordinate to be judged.
   * @return true if myPieces contain such coordinate.
   */
  @Override
  public boolean occupiesCoordinates(Coordinate where) {
    // TODO Auto-generated method stub
    return myPieces.containsKey(where);
  }

  @Override
  public boolean isSunk() {
    // TODO Auto-generated method stub
    for (Coordinate key : myPieces.keySet()) {
      if (myPieces.get(key) == false)
        return false;
    }
    return true;
  }

  @Override
  public void recordHitAt(Coordinate where) {
    // TODO Auto-generated method stub
    checkCoordinateInThisShip(where);
    myPieces.put(where, true);
  }

  @Override
  public boolean wasHitAt(Coordinate where) {
    // TODO Auto-generated method stub
    checkCoordinateInThisShip(where);
    return myPieces.get(where);
  }

  @Override
  public T getDisplayInfoAt(Coordinate where, boolean myShip) {
    // TODO Auto-generated method stub
    checkCoordinateInThisShip(where);
    if(myShip) return myDisplayInfo.getInfo(where, wasHitAt(where));
    return enemyDisplayInfo.getInfo(where, wasHitAt(where));
  }

  @Override
  public Iterable<Coordinate> getCoordinates(){
    return myPieces.keySet();
  }

  @Override
  public Coordinate getUpperLeft() {
    return upperleft;
  }
  @Override
  public void moveTo(Placement p){
    HashMap<Coordinate, Boolean> myPieces_new = new HashMap<Coordinate, Boolean>();
    Coordinate destination = p.getWhere();
    int delta_x = destination.getRow()-upperleft.getRow();
    int delta_y = destination.getColumn()-upperleft.getColumn();
    for (Coordinate c : myPieces.keySet()) {
      Boolean beFired = myPieces.get(c);
      Coordinate new_c = new Coordinate(c.getRow() + delta_x, c.getColumn() + delta_y);
      myPieces_new.put(new_c, beFired);
    }
    myPieces = myPieces_new;
    upperleft = destination;
  }
}













