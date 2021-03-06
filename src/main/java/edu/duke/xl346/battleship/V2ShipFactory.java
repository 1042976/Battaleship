package edu.duke.xl346.battleship;
public class V2ShipFactory implements AbstractShipFactory<Character> {
  protected Ship<Character> createRectangleShip(Placement where, int w, int h, char letter, String name) {
    if (where.getOrientation() == 'H') {
      int temp = w;
      w = h;
      h = temp;
    } else if (where.getOrientation() != 'V') {
      throw new IllegalArgumentException("Orientation should be H or V, but is" + where.getOrientation());
    }
    return new RectangleShip<Character>(name, where.getWhere(), w, h, letter, '*');

  }

  protected Ship<Character> createBattleShip(Placement where, char letter, String name) {
    char orientation = where.getOrientation();
    if (orientation != 'U' && orientation != 'R' && orientation != 'D' && orientation != 'L') {
      throw new IllegalArgumentException("Orientation should be U, R, D or L , but is " + orientation);
    }
    return new BattleShip<Character>(name, where.getWhere(), where.getOrientation(), letter, '*');
  }

  protected Ship<Character> createCarrier(Placement where, char letter, String name) {
    char orientation = where.getOrientation();
    if (orientation != 'U' && orientation != 'R' && orientation != 'D' && orientation != 'L') {
      throw new IllegalArgumentException("Orientation should be U, R, D or L , but is " + orientation);
    }
    return new Carrier<Character>(name, where.getWhere(), where.getOrientation(), letter, '*');
  }

  @Override
  public Ship<Character> makeSubmarine(Placement where) {
    // TODO Auto-generated method stub
    return createRectangleShip(where, 1, 2, 's', "Submarine");
  }

  @Override
  public Ship<Character> makeDestroyer(Placement where) {
    // TODO Auto-generated method stub
    return createRectangleShip(where, 1, 3, 'd', "Destroyer");
  }

  @Override
  public Ship<Character> makeBattleship(Placement where) {
    // TODO Auto-generated method stub
    return createBattleShip(where,  'b', "Battleship");
  }

  @Override
  public Ship<Character> makeCarrier(Placement where) {
    // TODO Auto-generated method stub
    return createCarrier(where,  'c', "Carrier");
  }

}











