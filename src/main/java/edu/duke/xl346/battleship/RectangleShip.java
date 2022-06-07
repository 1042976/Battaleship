package edu.duke.xl346.battleship;
import java.util.HashSet;

public class RectangleShip<T> extends BasicShip<T> {
  final String name;
  /**
   *This method generates the set of coordinates for a rectangle 
   *  starting at upperLeft whose width and height are as specified 
   *@param upperleft The upperleft point of the rectangle.
   *@param width The width of the rectangle.
   *@param height The height of the rectangle.
   *@return res The hash set of all points of the rectangle.  
   */
  static HashSet<Coordinate> makeCoords(Coordinate upperleft, int width, int height){
    HashSet<Coordinate> res = new HashSet<Coordinate>();
    int startRow = upperleft.getRow();
    int startColumn = upperleft.getColumn();
    for (int i = 0; i < height; ++i) {
      for (int j = 0; j < width; ++j) {
        res.add(new Coordinate(startRow + i, startColumn + j));
      }
    }
    return res;
  }
  public RectangleShip(String name, Coordinate upperLeft, int width, int height,
                       SimpleShipDisplayInfo<T> myDisplayInfo,
                       SimpleShipDisplayInfo<T> enemyDisplayInfo){
    super(upperLeft, makeCoords(upperLeft, width, height), myDisplayInfo, enemyDisplayInfo);
    this.name = name;
  }
  public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit) {
    this(name, upperLeft, width, height, new SimpleShipDisplayInfo<T>(data, onHit),
         new SimpleShipDisplayInfo<T>(null, data));
  }

  public RectangleShip(Coordinate upperLeft, T data, T onHit){
    this("testship", upperLeft, 1, 1, data, onHit);
  }

  public String getName() {
    return this.name;
  }
}













