/**
 *This interface represents and Abstract Factory pattern for Ship creation.
 */
package edu.duke.xl346.battleship;
public interface AbstractShipFactory<T> {
  /**
   *Make a submarine.
   *  
   *@param where specifies the location and orientation of the ship to make
   *@return the Ship created for the submarine.  
   */
  public Ship<T> makeSubmarine(Placement where);

  /**
   *Make a battleship.
   *
   *@param where specifies the loction and orientation ofh the ship to make
   *@return the Ship created for the battleship.  
   */
  public Ship<T> makeBattleship(Placement where);

  /**
   *Make a carrier.
   *
   *@param where specifies the location and orientation of the ship to make
   *@return the Ship created for the carrier.  
   */
  public Ship<T> makeCarrier(Placement where);

  /**
   *Make a destroyer.
   *
   *@param where specifies the location and orientation fo the ship to make
   *@return the Ship created for the destroyer.
   */
  public Ship<T> makeDestroyer(Placement where);

}
