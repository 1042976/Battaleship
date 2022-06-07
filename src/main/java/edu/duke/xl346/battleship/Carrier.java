package edu.duke.xl346.battleship;
import java.util.HashSet;

public class Carrier<T> extends BasicShip<T> {
  final String name;
  static HashSet<Coordinate> makeCoords(Coordinate upperleft, char orientation){
    HashSet<Coordinate> res = new HashSet<Coordinate>();
    int startRow = upperleft.getRow();
    int startColumn = upperleft.getColumn();
    if(orientation == 'U'){
      res.add(new Coordinate(startRow, startColumn));
      res.add(new Coordinate(startRow+1, startColumn));
      res.add(new Coordinate(startRow+2, startColumn));
      res.add(new Coordinate(startRow+2, startColumn+1));
      res.add(new Coordinate(startRow+3, startColumn+1));
      res.add(new Coordinate(startRow+4, startColumn+1));
    }else if(orientation == 'R'){
      res.add(new Coordinate(startRow, startColumn+2));
      res.add(new Coordinate(startRow, startColumn+3));
      res.add(new Coordinate(startRow, startColumn+4));
      res.add(new Coordinate(startRow+1, startColumn));
      res.add(new Coordinate(startRow+1, startColumn+1));
      res.add(new Coordinate(startRow+1, startColumn+2));
    }else if(orientation == 'D'){
      res.add(new Coordinate(startRow, startColumn+1));
      res.add(new Coordinate(startRow+1, startColumn+1));
      res.add(new Coordinate(startRow+2, startColumn));
      res.add(new Coordinate(startRow+2, startColumn+1));
      res.add(new Coordinate(startRow+3, startColumn));
      res.add(new Coordinate(startRow+4, startColumn));
    }else{
      res.add(new Coordinate(startRow, startColumn));
      res.add(new Coordinate(startRow, startColumn+1));
      res.add(new Coordinate(startRow, startColumn+2));
      res.add(new Coordinate(startRow+1, startColumn+2));
      res.add(new Coordinate(startRow+1, startColumn+3));
      res.add(new Coordinate(startRow+1, startColumn+4));
    }
    return res;
  }
  public Carrier(String name, Coordinate upperLeft, char orientation,
                    SimpleShipDisplayInfo<T> myDisplayInfo,
                    SimpleShipDisplayInfo<T> enemyDisplayInfo){
    super(upperLeft, makeCoords(upperLeft, orientation), myDisplayInfo, enemyDisplayInfo);
    this.name = name;
  }  
  public Carrier(String name, Coordinate upperLeft, char orientation, T data, T onHit) {
    this(name, upperLeft, orientation, new SimpleShipDisplayInfo<T>(data, onHit),
         new SimpleShipDisplayInfo<T>(null, data));
  }

  public String getName() {
    return this.name;
  }
}













