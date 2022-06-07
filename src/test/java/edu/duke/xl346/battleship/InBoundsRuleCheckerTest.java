package edu.duke.xl346.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class InBoundsRuleCheckerTest {
  @Test
  public void test_checkMyRule() {
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

    Board<Character> b1 = new BattleShipBoard<Character>(10, 20, new InBoundsRuleChecker<Character>(null), 'X');
    assertEquals(expected1_2, b1.tryAddShip(subm1_2));
    assertEquals(expectedn1_2, b1.tryAddShip(submn1_2));
    assertEquals(expected19_0, b1.tryAddShip(subm19_0));
    assertEquals(expected10_n1H, b1.tryAddShip(subm10_n1H));
    assertEquals(expected10_9H, b1.tryAddShip(subm10_9H));

    /**
    V1ShipFactory f = new V1ShipFactory();
    Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
    Ship<Character> subm = f.makeSubmarine(v1_2);
    Placement v100_2 = new Placement(new Coordinate(100, 2), 'V');
    Placement v2_100 = new Placement(new Coordinate(2, 100), 'V');
    Ship<Character> subm2 = f.makeSubmarine(v100_2);
    Ship<Character> subm3 = f.makeSubmarine(v2_100);
    PlacementRuleChecker<Character> rule = new InBoundsRuleChecker<Character>(null);
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20, new InBoundsRuleChecker<Character>(null));
    assertEquals(true, rule.checkPlacement(subm, b1));
    assertEquals(false, rule.checkPlacement(subm2, b1));
    assertEquals(false, rule.checkPlacement(subm3, b1));
    */
  }

}











