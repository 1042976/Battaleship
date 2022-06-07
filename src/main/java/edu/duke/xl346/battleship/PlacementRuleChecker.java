package edu.duke.xl346.battleship;
public abstract class PlacementRuleChecker<T> {
  private final PlacementRuleChecker<T> next;

  public PlacementRuleChecker(PlacementRuleChecker<T> next){
    this.next = next;
  }

  protected abstract String checkMyRule(Ship<T> theShip, Board<T> theBoard);

  public String checkPlacement(Ship<T> theShip, Board<T> theBoard){
    //if we fail out own rule: stop the placement is not legal
    String temp = checkMyRule(theShip, theBoard);
    if (temp != null) {
      return temp;
    }
    //other wise, ask the rest of the chain
    if (next != null) {
      return next.checkPlacement(theShip, theBoard);
    }

    //if there are no moew rules, then the placement is legal
    return null;
  }
}













