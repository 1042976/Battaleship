package edu.duke.xl346.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class V1ShipFactoryTest {
  private void checkShip(Ship<Character> testShip, String expectedName, char expectedLetter, Coordinate...expectedLocs){
    
    assertEquals(expectedName, testShip.getName());
    for(Coordinate epl : expectedLocs){
      assertEquals(expectedLetter, testShip.getDisplayInfoAt(epl, true));
    }
  }
  @Test
  public void test_makeShip() {
    V1ShipFactory f = new V1ShipFactory();
    Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
    Ship<Character> subm = f.makeSubmarine(v1_2);
    Ship<Character> dist = f.makeDestroyer(v1_2);
    Ship<Character> batt = f.makeBattleship(v1_2);
    Ship<Character> carr = f.makeCarrier(v1_2);
    checkShip(subm, "Submarine", 's', new Coordinate(1, 2), new Coordinate(2, 2));
    checkShip(dist, "Destroyer", 'd', new Coordinate(1, 2), new Coordinate(2, 2), new Coordinate(3, 2));
    checkShip(batt, "Battleship", 'b', new Coordinate(1, 2), new Coordinate(2, 2), new Coordinate(3, 2),
              new Coordinate(4, 2));
    checkShip(carr, "Carrier", 'c', new Coordinate(1, 2), new Coordinate(2, 2), new Coordinate(3, 2),
              new Coordinate(4, 2), new Coordinate(5, 2), new Coordinate(6, 2));

    Placement v0_1 = new Placement(new Coordinate(0, 1), 'H');
    Ship<Character> subm2 = f.makeSubmarine(v0_1);
    checkShip(subm2, "Submarine", 's', new Coordinate(0, 1), new Coordinate(0, 2));

    Placement v0_2 = new Placement(new Coordinate(0, 2), 'E');
    assertThrows(IllegalArgumentException.class, ()->f.makeSubmarine(v0_2));
  }

}











