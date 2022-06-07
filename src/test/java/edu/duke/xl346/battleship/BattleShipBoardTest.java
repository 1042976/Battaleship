package edu.duke.xl346.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BattleShipBoardTest {
  private Ship<Character> getSubmarine(Coordinate co, char ch) {
    V1ShipFactory f = new V1ShipFactory();
    Placement v1_2 = new Placement(co, ch);
    return f.makeSubmarine(v1_2);
  }

  private Board<Character> getBoard() {
    NoCollisionRuleChecker<Character> rule = new NoCollisionRuleChecker<Character>(
        new InBoundsRuleChecker<Character>(null));
    return new BattleShipBoard<Character>(10, 20, rule, 'X');
  }

  @Test
  public void test_randomlyPickFrom() {
    int k = BattleShipBoard.randomlyPickFrom(0, 3);
    assertTrue(k >= 0 && k <= 3);
  }

  @Test
  public void test_width_and_height() {
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20, 'X');
    assertEquals(10, b1.getWidth());
    assertEquals(20, b1.getHeight());
  }

  @Test
  public void test_invalid_dimensions() {
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, 0, 'X'));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(0, 20, 'X'));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, -5, 'X'));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(-8, 20, 'X'));
  }

  private <T> void checkWhatIsAtBoard(Board<T> b, T[][] expected) {
    if (expected == null || expected.length == 0 || expected.length != b.getWidth()
        || expected[0].length != b.getHeight())
      return;
    for (int i = 0; i < b.getWidth(); ++i) {
      for (int j = 0; j < b.getHeight(); ++j) {
        assertEquals(expected[i][j], b.whatIsAtForSelf(new Coordinate(i, j)));
      }
    }
  }

  @Test
  public void test_myShips() {
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20, 'X');
    RectangleShip<Character> s1 = new RectangleShip<Character>(new Coordinate(5, 5), 's', '*');
    b1.tryAddShip(s1);
    Character c1 = b1.whatIsAtForSelf(new Coordinate(5, 5));
    Character c2 = b1.whatIsAtForSelf(new Coordinate(5, 4));
    assertEquals(c1, 's');
    assertEquals(c2, null);

    Board<Character> b2 = new BattleShipBoard<Character>(2, 2, 'X');
    RectangleShip<Character> s00 = new RectangleShip(new Coordinate(0, 0), 's', '*');
    RectangleShip<Character> s01 = new RectangleShip(new Coordinate(0, 1), 's', '*');
    RectangleShip<Character> s10 = new RectangleShip(new Coordinate(1, 0), 's', '*');
    RectangleShip<Character> s11 = new RectangleShip(new Coordinate(1, 1), 's', '*');
    Character[][] array1 = { { null, null }, { null, null } };
    checkWhatIsAtBoard(b2, array1);
    b2.tryAddShip(s00);
    b2.tryAddShip(s01);
    b2.tryAddShip(s10);
    b2.tryAddShip(s11);
    Character[][] array2 = { { 's', 's' }, { 's', 's' } };
    Character[][] array3 = { { 's', 's', 's' } };
    Character[][] array4 = { { 's', 's' }, { 's', 's' }, { 's', 's' } };
    checkWhatIsAtBoard(b2, array2);
    checkWhatIsAtBoard(b2, array3);
    checkWhatIsAtBoard(b2, array4);
    checkWhatIsAtBoard(b2, null);
  }

  @Test
  public void test_tryAddShip() {
    V1ShipFactory f = new V1ShipFactory();

    // base ship
    Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
    Ship<Character> subm1_2 = f.makeSubmarine(v1_2);
    String expected1_2 = null;
    // goes off the top of the board
    Placement vn1_2 = new Placement(new Coordinate(-1, 2), 'V');
    Ship<Character> submn1_2 = f.makeSubmarine(vn1_2);
    String expectedn1_2 = "That placement is invalid: the ship goes off the top of the board.";
    // goes off the bottom of the board
    Placement v19_0 = new Placement(new Coordinate(19, 0), 'V');
    Ship<Character> subm19_0 = f.makeSubmarine(v19_0);
    String expected19_0 = "That placement is invalid: the ship goes off the bottom of the board.";
    // goes off the left of the board.
    Placement v10_n1H = new Placement(new Coordinate(10, -1), 'H');
    Ship<Character> subm10_n1H = f.makeSubmarine(v10_n1H);
    String expected10_n1H = "That placement is invalid: the ship goes off the left of the board.";
    // goes off the right of the board.
    Placement v10_9H = new Placement(new Coordinate(10, 9), 'H');
    Ship<Character> subm10_9H = f.makeSubmarine(v10_9H);
    String expected10_9H = "That placement is invalid: the ship goes off the right of the board.";
    // the ship overlaps another ship.
    Placement v0_2 = new Placement(new Coordinate(0, 2), 'V');
    Ship<Character> subm0_2 = f.makeSubmarine(v0_2);
    String expected0_2 = "That placement is invalid: the ship overlaps another ship.";
    Placement v2_1H = new Placement(new Coordinate(2, 1), 'H');
    Ship<Character> subm2_1H = f.makeSubmarine(v2_1H);
    String expected2_1H = "That placement is invalid: the ship overlaps another ship.";

    NoCollisionRuleChecker<Character> rule = new NoCollisionRuleChecker<Character>(
        new InBoundsRuleChecker<Character>(null));
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20, rule, 'X');
    assertEquals(expected1_2, b1.tryAddShip(subm1_2));
    assertEquals(expectedn1_2, b1.tryAddShip(submn1_2));
    assertEquals(expected19_0, b1.tryAddShip(subm19_0));
    assertEquals(expected10_n1H, b1.tryAddShip(subm10_n1H));
    assertEquals(expected10_9H, b1.tryAddShip(subm10_9H));
    assertEquals(expected0_2, b1.tryAddShip(subm0_2));
    assertEquals(expected2_1H, b1.tryAddShip(subm2_1H));
  }

  @Test
  public void test_fireAt() {
    Ship<Character> subm1_2 = getSubmarine(new Coordinate(1, 2), 'V');
    Board<Character> b1 = getBoard();
    b1.tryAddShip(subm1_2);
    assertEquals(null, b1.whatIsAtForEnemy(new Coordinate(1, 2)));
    assertSame(subm1_2, b1.fireAt(new Coordinate(1, 2)));
    assertSame(null, b1.fireAt(new Coordinate(3, 3)));
    assertFalse(subm1_2.isSunk());
    b1.fireAt(new Coordinate(2, 2));
    assertTrue(subm1_2.isSunk());
    assertEquals('s', b1.whatIsAtForEnemy(new Coordinate(2, 2)));
    b1.fireAt(new Coordinate(4, 4));
    assertEquals('X', b1.whatIsAtForEnemy(new Coordinate(4, 4)));
  }

  @Test
  public void test_ifAllSunk() {
    Ship<Character> subm1_2 = getSubmarine(new Coordinate(1, 2), 'V');
    Board<Character> b1 = getBoard();
    b1.tryAddShip(subm1_2);
    assertFalse(b1.ifAllSunk());
    b1.fireAt(new Coordinate(1, 2));
    assertFalse(b1.ifAllSunk());
    b1.fireAt(new Coordinate(2, 2));
    assertTrue(b1.ifAllSunk());
  }

  @Test
  public void test_move() {
    Ship<Character> subm1_2 = getSubmarine(new Coordinate(1, 2), 'V');
    Board<Character> b1 = getBoard();
    b1.tryAddShip(subm1_2);
    assertEquals('s', b1.whatIsAtForSelf(new Coordinate(1, 2)));
    assertEquals('s', b1.whatIsAtForSelf(new Coordinate(2, 2)));
    b1.fireAt(new Coordinate(2, 2));
    assertEquals('*', b1.whatIsAtForSelf(new Coordinate(2, 2)));
    assertTrue(b1.move(new Coordinate(1, 2), new Placement(new Coordinate(5, 5), 'U')));
    assertEquals('s', b1.whatIsAtForSelf(new Coordinate(5, 5)));
    assertEquals('*', b1.whatIsAtForSelf(new Coordinate(6, 5)));
    assertEquals(null, b1.whatIsAtForSelf(new Coordinate(1, 2)));
    assertEquals(null, b1.whatIsAtForSelf(new Coordinate(2, 2)));
    assertFalse(b1.move(new Coordinate(1, 2), new Placement(new Coordinate(5, 5), 'U')));
    assertFalse(b1.move(new Coordinate(5, 5), new Placement(new Coordinate(9, 19), 'U')));
  }

  @Test
  public void test_SonarScanning() {
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> subm = f.makeSubmarine(new Placement(new Coordinate(0, 0), 'V'));
    Ship<Character> dest = f.makeDestroyer(new Placement(new Coordinate(0, 1), 'V'));
    Ship<Character> batt = f.makeBattleship(new Placement(new Coordinate(0, 2), 'R'));
    Ship<Character> carr = f.makeCarrier(new Placement(new Coordinate(0, 4), 'U'));
    Board<Character> b1 = getBoard();
    b1.tryAddShip(subm);
    b1.tryAddShip(dest);
    b1.tryAddShip(batt);
    b1.tryAddShip(carr);
    
    
    assertEquals(2, b1.sonarScanning(new Coordinate(1, 2), 's'));
    assertEquals(3, b1.sonarScanning(new Coordinate(1, 2), 'd'));
    assertEquals(4, b1.sonarScanning(new Coordinate(1, 2), 'b'));
    assertEquals(3, b1.sonarScanning(new Coordinate(1, 2), 'c'));
  }
}












