package edu.duke.xl346.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SimpleShipDisplayInfoTest {
  @Test
  public void test_constructor_and_getInfo() {
    ShipDisplayInfo<Character> sdi = new SimpleShipDisplayInfo<Character>('a', 'b');
    assertEquals('a', sdi.getInfo(new Coordinate(1, 1), false));
    assertEquals('b', sdi.getInfo(new Coordinate(1, 1), true));
  }

}











