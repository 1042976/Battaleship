package edu.duke.xl346.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BoardTextViewTest {
  private Ship<Character> getSubmarine(Coordinate co, char ch) {
    V1ShipFactory f = new V1ShipFactory();
    Placement v1_2 = new Placement(co, ch);
    return f.makeSubmarine(v1_2);
  }
  @Test
  public void test_display_empty_3by2() {
    String expectedHeader = "  0|1\n";
    String expectedBody =
      "A  |  A\n"+
      "B  |  B\n"+
      "C  |  C\n";
    emptyBoardHelper(3, 2, expectedHeader, expectedBody);
  }

  @Test
  public void test_display_empty_3by5() {
    String expectedHeader = "  0|1|2|3|4\n";
    String expectedBody =
      "A  | | | |  A\n"+
      "B  | | | |  B\n"+
      "C  | | | |  C\n";
    emptyBoardHelper(3, 5, expectedHeader, expectedBody);
  }
  private void emptyBoardHelper(int h, int w, String expectedHeader, String expectedBody) {
    Board<Character> b1 = new BattleShipBoard<Character>(w, h, 'X');
    BoardTextView view = new BoardTextView(b1);
    assertEquals(expectedHeader, view.makeHeader());
    String expected = expectedHeader + expectedBody + expectedHeader;
    assertEquals(expected, view.displayMyOwnBoard());
  }
  @Test
  public void test_display_empty_2by2() {
    Board<Character> b1 = new BattleShipBoard<Character>(2, 2, 'X');
    BoardTextView view = new BoardTextView(b1);
    String expectedHeader= "  0|1\n";
    assertEquals(expectedHeader, view.makeHeader());

    String expected=
      expectedHeader+
      "A  |  A\n"+
      "B  |  B\n"+
      expectedHeader;
    assertEquals(expected, view.displayMyOwnBoard());
  }
  @Test
  public void test_dispaly_nonempty_2by2() {
    Board<Character> board1 = new BattleShipBoard<Character>(2, 2, 'X');
    RectangleShip<Character> s1 = new RectangleShip<Character>(new Coordinate(0,1), 's', '*');
    board1.tryAddShip(s1);
    BoardTextView view = new BoardTextView(board1);
    String expectedHeader = "  0|1\n";
    String expected =
      expectedHeader+
      "A  |s A\n"+
      "B  |  B\n"+
      expectedHeader;
    assertEquals(expected, view.displayMyOwnBoard());

    String expectedForEnemy =
      expectedHeader+
      "A  |  A\n"+
      "B  |  B\n"+
      expectedHeader;
    assertEquals(expectedForEnemy, view.displayEnemyBoard());

    board1.fireAt(new Coordinate(0, 1));
    String expectedForEnemy2 =
      expectedHeader+
      "A  |s A\n"+
      "B  |  B\n"+
      expectedHeader;
    assertEquals(expectedForEnemy2, view.displayEnemyBoard());
  }
  @Test
  public void test_invalid_board_size() {
    Board<Character> wideBoard = new BattleShipBoard<Character>(11, 20, 'X');
    Board<Character> tailBoard = new BattleShipBoard<Character>(10, 27, 'X');
    assertThrows(IllegalArgumentException.class, () -> new BoardTextView(wideBoard));
    assertThrows(IllegalArgumentException.class, () -> new BoardTextView(tailBoard));
  }

  @Test
  public void test_desplayMyBoardWithEnemyNextToIt(){
    //build my Board
    Board<Character> b1 = new BattleShipBoard<Character>(4, 2, 'X');
    Ship<Character> subm0_0 = getSubmarine(new Coordinate(0, 0), 'V');
    b1.tryAddShip(subm0_0);
    //build enemy Board
    Board<Character> b2 = new BattleShipBoard<Character>(4, 2, 'X');
    Ship<Character> subm1_1 = getSubmarine(new Coordinate(0, 1), 'V');
    b2.tryAddShip(subm1_1);
    b2.fireAt(new Coordinate(0, 1));
    //build exceptiontest Board
    Board<Character> b3 = new BattleShipBoard<Character>(4, 1, 'X');
    //build my view and enemy view
    BoardTextView myView = new BoardTextView(b1);
    BoardTextView enemyView = new BoardTextView(b2);
    //build exceptiontest text view
    BoardTextView view3 = new BoardTextView(b3);
    String result = myView.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", "Player B's ocean");
    String expected =
          "     Your ocean                         Player B's ocean\n"+
          "  0|1|2|3                              0|1|2|3\n" + 
          "A s| | |  A                          A  |s| |  A\n"+
          "B s| | |  B                          B  | | |  B\n"+
          "  0|1|2|3                              0|1|2|3\n";
    assertEquals(expected, result);
    assertThrows(IllegalArgumentException.class,
        () -> myView.displayMyBoardWithEnemyNextToIt(view3, "Your ocean", "Player B's ocean"));
  }
}

      











