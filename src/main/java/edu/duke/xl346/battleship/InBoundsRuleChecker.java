package edu.duke.xl346.battleship;
public class InBoundsRuleChecker<T> extends PlacementRuleChecker<T>{

	@Override
	protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
		// TODO Auto-generated method stub
    int height = theBoard.getHeight();
    int width = theBoard.getWidth();
    for (Coordinate c : theShip.getCoordinates()) {
      int row = c.getRow();
      int column = c.getColumn();
      if (row < 0) {
        return "That placement is invalid: the ship goes off the top of the board.";
      }
      if (row >= height) {
        return "That placement is invalid: the ship goes off the bottom of the board.";
      }
      if (column < 0) {
        return "That placement is invalid: the ship goes off the left of the board.";
      }
      if (column >= width) {
        return "That placement is invalid: the ship goes off the right of the board.";
      }
    }
		return null;
	}

  public InBoundsRuleChecker(PlacementRuleChecker<T> next){
    super(next);
  }
}












