package edu.duke.xl346.battleship;
public class NoCollisionRuleChecker<T> extends InBoundsRuleChecker<T> {

	@Override
	protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
		// TODO Auto-generated method stub
    for (Coordinate c : theShip.getCoordinates()) {
      if (theBoard.whatIsAtForSelf(c) != null)
        return "That placement is invalid: the ship overlaps another ship.";
    }
		return null;
	}

    /**
     *Constructs NoCollisionRuleChecker using an InBoundsRuleChecker
     *@param next The next Rule to be checked.    
     */
  public NoCollisionRuleChecker(PlacementRuleChecker<T> next){
    super(next);
  }
}











