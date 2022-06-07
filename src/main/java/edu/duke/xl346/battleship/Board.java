package edu.duke.xl346.battleship;
import java.util.ArrayList;

public interface Board<T> {
  public int getWidth();

  public int getHeight();

  //public PlacementRuleChecker<T> getPlacementChecker();

  public String tryAddShip(Ship<T> toAdd);

  public T whatIsAtForSelf(Coordinate where);

  public T whatIsAtForEnemy(Coordinate where);

  public Ship<T> fireAt(Coordinate c);

  public boolean ifAllSunk();

  public boolean move(Coordinate c, Placement p);

  public int sonarScanning(Coordinate c, T target);

  public Coordinate pickSunkShip();

  public Coordinate randomlyPickShip();

  public Placement pickPlacement(ArrayList<Placement> emptyPlacements);

  public int getNumOfSunkShips();

  public boolean isInRange(Coordinate where);

  public ArrayList<Coordinate> getAvailablePoints(boolean isToGetAll);

  public ArrayList<Placement> getAvailablePlacements(boolean isToGetAll, char type_of_ship);

  public String removeShipFromBoardIfExist(Coordinate where);
  public boolean isSameCoordinate(Coordinate c1, Coordinate c2);
  public int numberOfShips();
}













