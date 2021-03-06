package edu.duke.xl346.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class V2ShipFactoryTest {
  private void checkShip(Ship<Character> testShip, String expectedName, char expectedLetter,
      Coordinate... expectedLocs) {

    assertEquals(expectedName, testShip.getName());
    for (Coordinate epl : expectedLocs) {
      assertEquals(expectedLetter, testShip.getDisplayInfoAt(epl, true));
    }
  }

  @Test
  public void test_makeShip() {
    V2ShipFactory f = new V2ShipFactory();
    Placement v1_2V = new Placement(new Coordinate(1, 2), 'V');
    Placement v1_2H = new Placement(new Coordinate(1, 2), 'H');
    Placement v00U = new Placement(new Coordinate(0, 0), 'U');
    Placement v00R = new Placement(new Coordinate(0, 0), 'R');
    Placement v00D = new Placement(new Coordinate(0, 0), 'D');
    Placement v00L = new Placement(new Coordinate(0, 0), 'L');
    Ship<Character> submV = f.makeSubmarine(v1_2V);
    Ship<Character> distV = f.makeDestroyer(v1_2V);
    Ship<Character> submH = f.makeSubmarine(v1_2H);
    Ship<Character> distH = f.makeDestroyer(v1_2H);    
    Ship<Character> battU = f.makeBattleship(v00U);
    Ship<Character> carrU = f.makeCarrier(v00U);
    Ship<Character> battR = f.makeBattleship(v00R);
    Ship<Character> carrR = f.makeCarrier(v00R);
    Ship<Character> battD = f.makeBattleship(v00D);
    Ship<Character> carrD = f.makeCarrier(v00D);
    Ship<Character> battL = f.makeBattleship(v00L);
    Ship<Character> carrL = f.makeCarrier(v00L);
    checkShip(submV, "Submarine", 's', new Coordinate(1, 2), new Coordinate(2, 2));
    checkShip(distV, "Destroyer", 'd', new Coordinate(1, 2), new Coordinate(2, 2), new Coordinate(3, 2));
    checkShip(submH, "Submarine", 's', new Coordinate(1, 2), new Coordinate(1, 3));
    checkShip(distH, "Destroyer", 'd', new Coordinate(1, 2), new Coordinate(1, 3), new Coordinate(1, 4)); 
    checkShip(battU, "Battleship", 'b', new Coordinate(0, 1), new Coordinate(1, 0), new Coordinate(1, 1),
              new Coordinate(1, 2));
    checkShip(battR, "Battleship", 'b', new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1),
              new Coordinate(2, 0));
    checkShip(battD, "Battleship", 'b', new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2),
              new Coordinate(1, 1));
    checkShip(battL, "Battleship", 'b', new Coordinate(0, 1), new Coordinate(1, 0), new Coordinate(1, 1),
              new Coordinate(2, 1));
    checkShip(carrU, "Carrier", 'c', new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0),
              new Coordinate(2, 1), new Coordinate(3, 1), new Coordinate(4, 1));
    checkShip(carrR, "Carrier", 'c', new Coordinate(0, 2), new Coordinate(0, 3), new Coordinate(0, 4),
              new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(1, 2));
    checkShip(carrD, "Carrier", 'c', new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(2, 0),
              new Coordinate(2, 1), new Coordinate(3, 0), new Coordinate(4, 0));
    checkShip(carrL, "Carrier", 'c', new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2),
    new Coordinate(1, 2), new Coordinate(1, 3), new Coordinate(1, 4));
    
    Placement v0_2 = new Placement(new Coordinate(0, 2), 'E');
    assertThrows(IllegalArgumentException.class, ()->f.makeSubmarine(v0_2));
    assertThrows(IllegalArgumentException.class, ()->f.makeCarrier(v0_2));
    assertThrows(IllegalArgumentException.class, ()->f.makeBattleship(v0_2));
  }
}







