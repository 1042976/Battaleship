package edu.duke.xl346.battleship;
public class SimpleShipDisplayInfo<T> implements ShipDisplayInfo<T> {

  T myData, onHit;

  /**
   *Takes two Ts and initializes myData and onHit.
   *@param myData Type T.
   *@param onHit Type T.  
   */
  public SimpleShipDisplayInfo(T myData, T onHit) {
    this.myData = myData;
    this.onHit = onHit;
  }
  
  /**
   *Check if(hit) and returns onHit if so, and myData otherwise.
   *@param where Coordinate to be judged if hit.
   *@param hit True if hit.
   *@return myData if not hit.  
   *@return onHit if hit.
   */
  @Override
	public T getInfo(Coordinate where, boolean hit) {
		// TODO Auto-generated method stub
    if (hit) {
      return onHit;
    } else {
      return myData;
    }
	}
  
}













