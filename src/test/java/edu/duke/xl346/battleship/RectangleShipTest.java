package edu.duke.xl346.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class RectangleShipTest {
  @Test
  public void test_makeCoords() {
    HashSet<Coordinate> testSet = RectangleShip.makeCoords(new Coordinate(1, 2), 1, 3);
    HashSet<Coordinate> expectedSet = new HashSet<Coordinate>();
    expectedSet.add(new Coordinate(1, 2));
    expectedSet.add(new Coordinate(2, 2));
    expectedSet.add(new Coordinate(3, 2));
    assertEquals(expectedSet, testSet); 
  }
  @Test
  public void test_construtors() {
    RectangleShip<Character> rship = new RectangleShip<Character>(new Coordinate(1, 2), 's', '*');
    RectangleShip<Character> rship2 = new RectangleShip<Character>("submarine", new Coordinate(1, 2), 1, 1, 's', '*');
    HashMap<Coordinate, Boolean> expectedMap = new HashMap<>();
    HashMap<Coordinate, Boolean> expectedMap2 = new HashMap<>();
    expectedMap.put(new Coordinate(1, 2), false);
    expectedMap2.put(new Coordinate(1, 2), false);
    assertEquals(expectedMap, rship.myPieces);
    assertEquals(expectedMap2, rship2.myPieces);
    assertEquals("submarine", rship2.getName());
    assertEquals("testship", rship.getName());
  }
}












