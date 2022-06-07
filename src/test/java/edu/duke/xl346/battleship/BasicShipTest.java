package edu.duke.xl346.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class BasicShipTest {
  @Test
  public void test_Constructors() {
    RectangleShip<Character> s1 = new RectangleShip<Character>(new Coordinate(2, 2), 's', '*');
    assertEquals(true, s1.occupiesCoordinates(new Coordinate(2, 2)));
    assertEquals(false, s1.occupiesCoordinates(new Coordinate(3, 3)));
    /**
     List<Coordinate> list = new ArrayList<Coordinate>() {
      {
        add(new Coordinate(2, 2));
        add(new Coordinate(2, 3));
        add(new Coordinate(1, 2));
      }
    };
    Ship<Character> s2 = new RectangleShip<Character>(list, new SimpleShipDisplayInfo<Character>('s', '*'));
    assertEquals(true, s2.occupiesCoordinates(new Coordinate(2, 2)));
    assertEquals(true, s2.occupiesCoordinates(new Coordinate(2, 3)));
    assertEquals(true, s2.occupiesCoordinates(new Coordinate(1, 2)));
    assertEquals(false, s2.occupiesCoordinates(new Coordinate(2, 4)));
    */
  }
  @Test
  public void test_recordHitAt_and_wasHitAt_and_getDisplayInfoAt(){
    RectangleShip<Character> s1 = new RectangleShip<Character>("submarine", new Coordinate(0, 0), 2, 2, 's', '*');
    s1.recordHitAt(new Coordinate(0, 1));
    assertEquals(true, s1.wasHitAt(new Coordinate(0,1)));
    assertEquals(false, s1.wasHitAt(new Coordinate(1,0)));
    assertThrows(IllegalArgumentException.class, () -> s1.recordHitAt(new Coordinate(2, 2)));
    assertThrows(IllegalArgumentException.class, () -> s1.wasHitAt(new Coordinate(2, 2)));
    assertEquals('*', s1.getDisplayInfoAt(new Coordinate(0, 1), true));
    assertEquals('s', s1.getDisplayInfoAt(new Coordinate(0 ,0), true));
    assertEquals('s', s1.getDisplayInfoAt(new Coordinate(1, 0), true));
    assertEquals('s', s1.getDisplayInfoAt(new Coordinate(1, 1), true));
    assertEquals(null, s1.getDisplayInfoAt(new Coordinate(0, 0), false));
    assertEquals('s', s1.getDisplayInfoAt(new Coordinate(0, 1), false));
    assertThrows(IllegalArgumentException.class, () -> s1.getDisplayInfoAt(new Coordinate(2, 3), true));
  }
  @Test
  public void test_isSunk() {
    RectangleShip<Character> s1 = new RectangleShip<Character>("submarine", new Coordinate(0, 0), 2, 2, 's', '*');
    s1.recordHitAt(new Coordinate(0, 1));
    assertEquals(false, s1.isSunk());
    s1.recordHitAt(new Coordinate(1, 0));
    assertEquals(false, s1.isSunk());
    s1.recordHitAt(new Coordinate(0, 0));
    assertEquals(false, s1.isSunk());
    s1.recordHitAt(new Coordinate(1, 1));
    assertEquals(true, s1.isSunk());
  }

  @Test
  public void test_getCoordinates(){ 
    RectangleShip<Character> s1 = new RectangleShip<Character>("submarine", new Coordinate(0, 0), 2, 2, 's', '*');
    Set<Coordinate> expectedSet = new HashSet<Coordinate>(){{
      add(new Coordinate(0,0));
      add(new Coordinate(0,1));
      add(new Coordinate(1,0));
      add(new Coordinate(1,1));
      }};
    assertEquals(expectedSet, s1.getCoordinates());
  }

  @Test
  public void test_moveTo() {
    RectangleShip<Character> s1 = new RectangleShip<Character>("submarine", new Coordinate(0, 0), 2, 2, 's', '*');
    assertEquals(true, s1.occupiesCoordinates(new Coordinate(1, 1)));
    s1.moveTo(new Placement(new Coordinate(4, 4), 'H'));
    assertEquals(true, s1.occupiesCoordinates(new Coordinate(4, 4)));
    assertEquals(true, s1.occupiesCoordinates(new Coordinate(4, 5)));
    assertEquals(false, s1.occupiesCoordinates(new Coordinate(0, 0)));
    assertEquals(false, s1.occupiesCoordinates(new Coordinate(1, 0)));
  }
}












