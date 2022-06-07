package edu.duke.xl346.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NoCollisionRuleCheckerTest {
  @Test
  public void test_checkMyRule() {
    V1ShipFactory f = new V1ShipFactory();
    Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
    Placement v0_2 = new Placement(new Coordinate(0, 2), 'V');
    Placement v1_1H = new Placement(new Coordinate(1, 1), 'H');
    Ship<Character> subm = f.makeSubmarine(v1_2);
    Ship<Character> subm_to_test = f.makeSubmarine(v0_2);
    Ship<Character> subm_to_test2 = f.makeSubmarine(v1_1H);
    PlacementRuleChecker<Character> rule = new NoCollisionRuleChecker<Character>(null);
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20, new NoCollisionRuleChecker<Character>(null), 'X');
    assertEquals(null, rule.checkPlacement(subm, b1));
    b1.tryAddShip(subm);
    String expected = "That placement is invalid: the ship overlaps another ship.";
    assertEquals(expected, rule.checkPlacement(subm_to_test, b1));
    assertEquals(expected, rule.checkPlacement(subm_to_test2, b1));  
  }

  @Test
  public void test_combine_checkers() {
    V1ShipFactory f = new V1ShipFactory();

    //base ship
    Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
    Ship<Character> subm1_2 = f.makeSubmarine(v1_2);
    String expected1_2 = null;
    //goes off the top of the board
    Placement vn1_2 = new Placement(new Coordinate(-1, 2), 'V');
    Ship<Character> submn1_2 = f.makeSubmarine(vn1_2);
    String expectedn1_2 = "That placement is invalid: the ship goes off the top of the board.";
    //goes off the bottom of the board
    Placement v19_0 = new Placement(new Coordinate(19, 0), 'V');
    Ship<Character> subm19_0= f.makeSubmarine(v19_0);
    String expected19_0 = "That placement is invalid: the ship goes off the bottom of the board.";
    //goes off the left of the board.
    Placement v10_n1H = new Placement(new Coordinate(10, -1), 'H');
    Ship<Character> subm10_n1H = f.makeSubmarine(v10_n1H);
    String expected10_n1H = "That placement is invalid: the ship goes off the left of the board.";
    //goes off the right of the board.
    Placement v10_9H = new Placement(new Coordinate(10, 9), 'H');
    Ship<Character> subm10_9H = f.makeSubmarine(v10_9H);
    String expected10_9H = "That placement is invalid: the ship goes off the right of the board.";
    //the ship overlaps another ship.
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

}
