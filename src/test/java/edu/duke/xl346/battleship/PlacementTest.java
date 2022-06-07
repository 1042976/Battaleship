package edu.duke.xl346.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PlacementTest {
  @Test
  public void test_equals() {
    Coordinate c1 = new Coordinate("B3");
    Placement p1 = new Placement(c1, 'v');
    Placement p2 = new Placement(c1, 'V');
    Placement p3 = new Placement(c1, 'H');
    assertEquals(p1, p2);
    assertEquals("(1, 3)V",p1.toString());
    assertNotEquals(p2, p3);
    assertNotEquals(p3, c1);
  }
  @Test
  public void test_hashCode() {
    Placement p1 = new Placement("B3V");
    Placement p2 = new Placement("B3V");
    Placement p3 = new Placement("B3H");
    assertEquals(p1.hashCode(), p2.hashCode());
    assertNotEquals(p2.hashCode(), p3.hashCode());
  }
  @Test
  public void test_string_constructor_cases(){
    Placement p1 = new Placement("B3v");
    Placement p2 = new Placement("B3V");
    assertEquals(p1, p2);
    assertThrows(IllegalArgumentException.class, ()->new Placement("B3"));
    assertThrows(IllegalArgumentException.class, ()->new Placement("B3V3"));
    //assertThrows(IllegalArgumentException.class, ()->new Placement("B3K"));
  }
  @Test
  public void test_getter(){
    Placement p1 = new Placement("B3V");
    assertEquals(new Coordinate("B3"), p1.getWhere());
    assertEquals('V', p1.getOrientation());
  }

  @Test
  public void test_placement_constructor_error_cases() {
    assertThrows(IllegalArgumentException.class, () -> new Placement("A2VV"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("12V"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("AAV"));
    // assertThrows(IllegalArgumentException.class, () -> new Placement("A3K"));
  }
}












