package edu.duke.xl346.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class TextPlayerTest {
  private TextPlayer createTextPlayer(String name, int w, int h, String inputData, OutputStream bytes) {
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(w, h, 'X');
    V1ShipFactory shipFactory = new V1ShipFactory();
    return new TextPlayer(name, board, input, output, shipFactory);
  }

  private TextPlayer createTextPlayerV2(String name, int w, int h, String inputData, OutputStream bytes) {
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(w, h, 'X');
    V2ShipFactory shipFactory = new V2ShipFactory();
    return new TextPlayer(name, board, input, output, shipFactory);
  }

  @Test
  void test_read_placement() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer("A", 10, 20, "B2V\nC8H\na4v\n", bytes);
    String prompt = "Please enter a location for a ship:";
    Placement[] expected = new Placement[3];
    expected[0] = new Placement(new Coordinate(1, 2), 'V');
    expected[1] = new Placement(new Coordinate(2, 8), 'H');
    expected[2] = new Placement(new Coordinate(0, 4), 'V');
    for (int i = 0; i < expected.length; ++i) {
      Placement p = player.readPlacement(prompt);
      assertEquals(p, expected[i]);
      assertEquals(prompt + "\n", bytes.toString());
      bytes.reset();
    }
    TextPlayer player2 = createTextPlayer("A", 10, 20, "", bytes);
    assertThrows(EOFException.class, () -> player2.readPlacement(prompt));
  }

  @Test
  void test_read_coordinate() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String prompt = "Please enter the coordinate to fire at: ";
    // test the exception situation.
    TextPlayer player1 = createTextPlayer("A", 10, 20, "", bytes);
    assertThrows(EOFException.class, () -> player1.readCoordinate(prompt));
    bytes.reset();
    // test normal situation
    TextPlayer player2 = createTextPlayer("A", 10, 20, "B2", bytes);
    Coordinate c = player2.readCoordinate(prompt);
    assertEquals(new Coordinate("B2"), c);
    assertEquals(prompt + "\n", bytes.toString());
    bytes.reset();
  }

  @Test
  void test_doOnePlacement_normal() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer("A", 3, 2, "A0V\nA1H\nb1h\nA0V\n", bytes);
    String[] expectedStr = new String[3];
    String expectedHeader = "  0|1|2\n";
    String prompt = "Player A where do you want to place a Submarine?";
    expectedStr[0] = expectedHeader + "A s| |  A\nB s| |  B\n" + expectedHeader;
    expectedStr[1] = expectedHeader + "A s|s|s A\nB s| |  B\n" + expectedHeader;
    expectedStr[2] = expectedHeader + "A s|s|s A\nB s|s|s B\n" + expectedHeader;
    for (int i = 0; i < expectedStr.length; ++i) {
      player.doOnePlacement("Submarine", player.shipCreationFns.get("Submarine"));
    }
    // player.doOnePlacement("Submarine", player.shipCreationFns.get("Submarine"));
    assertEquals(prompt + "\n" + expectedStr[0] + "\n" + prompt + "\n" + expectedStr[1] + "\n" + prompt + "\n"
        + expectedStr[2] + "\n", bytes.toString());

  }

  @Test
  void test_do_one_placements_BadPlacementString() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer("A", 4, 4, "A2Q\nA2V", bytes);
    V1ShipFactory sf = new V1ShipFactory();
    player.doOnePlacement("Submarine", (p) -> sf.makeSubmarine(p));
    String expected = "Player A where do you want to place a Submarine?\n"
        + "That placement is invalid: it does not have the correct format.\n" + "  0|1|2|3\n" + "A  | | |  A\n"
        + "B  | | |  B\n" + "C  | | |  C\n" + "D  | | |  D\n" + "  0|1|2|3\n\n"
        + "Player A where do you want to place a Submarine?\n" + "  0|1|2|3\n" + "A  | |s|  A\n" + "B  | |s|  B\n"
        + "C  | | |  C\n" + "D  | | |  D\n" + "  0|1|2|3\n\n";

    assertEquals(expected, bytes.toString());
  }

  @Test
  void test_do_one_placements_BadPlacementLocation() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer("A", 4, 4, "D2V\nB2V", bytes);
    V1ShipFactory sf = new V1ShipFactory();
    player.doOnePlacement("Submarine", (p) -> sf.makeSubmarine(p));
    String expected = "Player A where do you want to place a Submarine?\n"
        + "That placement is invalid: the ship goes off the bottom of the board.\n" + "  0|1|2|3\n" + "A  | | |  A\n"
        + "B  | | |  B\n" + "C  | | |  C\n" + "D  | | |  D\n" + "  0|1|2|3\n\n"
        + "Player A where do you want to place a Submarine?\n" + "  0|1|2|3\n" + "A  | | |  A\n" + "B  | |s|  B\n"
        + "C  | |s|  C\n" + "D  | | |  D\n" + "  0|1|2|3\n\n";
    assertEquals(expected, bytes.toString());
  }

  // @Test
  // void test_doPlacementPhase() throws IOException {
  //   ByteArrayOutputStream bytes = new ByteArrayOutputStream();
  //   String s1 = "A0H\nA2H\n";
  //   String d1 = "B0H\nB3H\nC0H\n";
  //   String b1 = "D0H\nE0H\nF0H\n";
  //   String c1 = "g0h\nh0h\n";
  //   TextPlayer player = createTextPlayer("A", 6, 8, s1 + d1 + b1 + c1, bytes);
  //   String expectedHeader = "  0|1|2|3|4|5\n";
  //   String preprompt = "Player A where do you want to place a ";
  //   String[] prompt = new String[10];
  //   for (int i = 0; i < 2; ++i)
  //     prompt[i] = "Submarine";
  //   for (int i = 2; i < 5; ++i)
  //     prompt[i] = "Destroyer";
  //   for (int i = 5; i < 8; ++i)
  //     prompt[i] = "Battleship";
  //   for (int i = 8; i < 10; ++i)
  //     prompt[i] = "Carrier";

  //   // expectedPart1
  //   String expectedPart1 = expectedHeader + "A  | | | | |  A\n" + "B  | | | | |  B\n" + "C  | | | | |  C\n"
  //       + "D  | | | | |  D\n" + "E  | | | | |  E\n" + "F  | | | | |  F\n" + "G  | | | | |  G\n" + "H  | | | | |  H\n"
  //       + expectedHeader;

  //   // expectedPart2
  //   String expectedPart2 = "--------------------------------------------------------------------------------\n"
  //       + "Player A: you are going to place the following ships (which are all\n"
  //       + "rectangular). For each ship, type the coordinate of the upper left\n"
  //       + "side of the ship, followed by either H (for horizontal) or V (for\n"
  //       + "vertical).  For example M4H would place a ship horizontally starting\n"
  //       + "at M4 and going to the right.  You have\n" + "\n" + "2 \"Submarines\" ships that are 1x2\n"
  //       + "3 \"Destroyers\" that are 1x3\n" + "3 \"Battleships\" that are 1x4\n" + "2 \"Carriers\" that are 1x6\n"
  //       + "--------------------------------------------------------------------------------\n";
  //   // expectedPart3
  //   String[] expectedStr = new String[10];
  //   expectedStr[0] = expectedHeader + "A s|s| | | |  A\n" + "B  | | | | |  B\n" + "C  | | | | |  C\n"
  //       + "D  | | | | |  D\n" + "E  | | | | |  E\n" + "F  | | | | |  F\n" + "G  | | | | |  G\n" + "H  | | | | |  H\n"
  //       + expectedHeader;
  //   expectedStr[1] = expectedHeader + "A s|s|s|s| |  A\n" + "B  | | | | |  B\n" + "C  | | | | |  C\n"
  //       + "D  | | | | |  D\n" + "E  | | | | |  E\n" + "F  | | | | |  F\n" + "G  | | | | |  G\n" + "H  | | | | |  H\n"
  //       + expectedHeader;
  //   expectedStr[2] = expectedHeader + "A s|s|s|s| |  A\n" + "B d|d|d| | |  B\n" + "C  | | | | |  C\n"
  //       + "D  | | | | |  D\n" + "E  | | | | |  E\n" + "F  | | | | |  F\n" + "G  | | | | |  G\n" + "H  | | | | |  H\n"
  //       + expectedHeader;
  //   expectedStr[3] = expectedHeader + "A s|s|s|s| |  A\n" + "B d|d|d|d|d|d B\n" + "C  | | | | |  C\n"
  //       + "D  | | | | |  D\n" + "E  | | | | |  E\n" + "F  | | | | |  F\n" + "G  | | | | |  G\n" + "H  | | | | |  H\n"
  //       + expectedHeader;
  //   expectedStr[4] = expectedHeader + "A s|s|s|s| |  A\n" + "B d|d|d|d|d|d B\n" + "C d|d|d| | |  C\n"
  //       + "D  | | | | |  D\n" + "E  | | | | |  E\n" + "F  | | | | |  F\n" + "G  | | | | |  G\n" + "H  | | | | |  H\n"
  //       + expectedHeader;
  //   expectedStr[5] = expectedHeader + "A s|s|s|s| |  A\n" + "B d|d|d|d|d|d B\n" + "C d|d|d| | |  C\n"
  //       + "D b|b|b|b| |  D\n" + "E  | | | | |  E\n" + "F  | | | | |  F\n" + "G  | | | | |  G\n" + "H  | | | | |  H\n"
  //       + expectedHeader;
  //   expectedStr[6] = expectedHeader + "A s|s|s|s| |  A\n" + "B d|d|d|d|d|d B\n" + "C d|d|d| | |  C\n"
  //       + "D b|b|b|b| |  D\n" + "E b|b|b|b| |  E\n" + "F  | | | | |  F\n" + "G  | | | | |  G\n" + "H  | | | | |  H\n"
  //       + expectedHeader;
  //   expectedStr[7] = expectedHeader + "A s|s|s|s| |  A\n" + "B d|d|d|d|d|d B\n" + "C d|d|d| | |  C\n"
  //       + "D b|b|b|b| |  D\n" + "E b|b|b|b| |  E\n" + "F b|b|b|b| |  F\n" + "G  | | | | |  G\n" + "H  | | | | |  H\n"
  //       + expectedHeader;
  //   expectedStr[8] = expectedHeader + "A s|s|s|s| |  A\n" + "B d|d|d|d|d|d B\n" + "C d|d|d| | |  C\n"
  //       + "D b|b|b|b| |  D\n" + "E b|b|b|b| |  E\n" + "F b|b|b|b| |  F\n" + "G c|c|c|c|c|c G\n" + "H  | | | | |  H\n"
  //       + expectedHeader;
  //   expectedStr[9] = expectedHeader + "A s|s|s|s| |  A\n" + "B d|d|d|d|d|d B\n" + "C d|d|d| | |  C\n"
  //       + "D b|b|b|b| |  D\n" + "E b|b|b|b| |  E\n" + "F b|b|b|b| |  F\n" + "G c|c|c|c|c|c G\n" + "H c|c|c|c|c|c H\n"
  //       + expectedHeader;
  //   String expectedPart3 = "";
  //   for (int i = 0; i < expectedStr.length; ++i) {
  //     expectedPart3 += (preprompt + prompt[i] + "?\n" + expectedStr[i] + "\n");
  //   }

  //   player.doPlacementPhase();
  //   assertEquals(expectedPart1 + "\n" + expectedPart2 + "\n" + expectedPart3, bytes.toString());
  // }

  @Test
  public void test_ifHasLost() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer("A", 4, 4, "A0H", bytes);
    V1ShipFactory sf = new V1ShipFactory();
    player.doOnePlacement("Submarine", (p) -> sf.makeSubmarine(p));
    assertFalse(player.ifHasLost());
    player.theBoard.fireAt(new Coordinate(0, 0));
    player.theBoard.fireAt(new Coordinate(0, 1));
    assertTrue(player.ifHasLost());

  }

  // @Test
  // public void test_playOneTurn() throws IOException {
  // // build player1
  // ByteArrayOutputStream bytes = new ByteArrayOutputStream();
  // TextPlayer player1 = createTextPlayer("A", 4, 2, "A0V\nA11\nA4\nc0\nA1\nA0",
  // bytes);
  // V1ShipFactory sf = new V1ShipFactory();
  // player1.doOnePlacement("Submarine", (p) -> sf.makeSubmarine(p));
  // // build player2
  // TextPlayer player2 = createTextPlayer("B", 4, 2, "A0V", bytes);
  // player2.doOnePlacement("Submarine", (p) -> sf.makeSubmarine(p));
  // // reset bytes
  // bytes.reset();
  // // execute playOne method
  // player1.playOneTurn(player2.theBoard, player2.view, player2.name);
  // // create expected printing result.
  // // part1
  // String expected1 = " Your ocean Player B's ocean\n"
  // + " 0|1|2|3 0|1|2|3\n" + "A s| | | A A | | | A\n"
  // + "B s| | | B B | | | B\n" + " 0|1|2|3 0|1|2|3\n";
  // // part2
  // String expected2 = "Please enter a valid coordinate to fire at:";

  // // part3
  // String expected3_missed = "You missed!";
  // String expected3_hit = "You hit a submarine!";

  // // part4
  // String expected4_missed = " Your ocean Player B's ocean\n"
  // + " 0|1|2|3 0|1|2|3\n" + "A s| | | A A |X| | A\n"
  // + "B s| | | B B | | | B\n" + " 0|1|2|3 0|1|2|3\n";
  // String expected4_hit = " Your ocean Player B's ocean\n"
  // + " 0|1|2|3 0|1|2|3\n" + "A s| | | A A s|X| | A\n"
  // + "B s| | | B B | | | B\n" + " 0|1|2|3 0|1|2|3\n";
  // // assert
  // assertEquals(expected1 + "\n" + expected2 + "\n" + expected2 + "\n" +
  // expected2 + "\n" + expected2 + "\n"
  // + expected3_missed + "\n" + expected4_missed + "\n", bytes.toString());
  // bytes.reset();
  // player1.playOneTurn(player2.theBoard, player2.view, player2.name);
  // assertEquals(expected4_missed + "\n" + expected2 + "\n" + expected3_hit +
  // "\n" + expected4_hit + "\n",
  // bytes.toString());
  // }

  // @Test
  // public void test_sonar() throws IOException {
  //   ByteArrayOutputStream bytes = new ByteArrayOutputStream();
  //   TextPlayer player = createTextPlayerV2("A", 10, 20, "A0V\nA1U\nA4V\nA5U", bytes);
  //   V2ShipFactory sf = new V2ShipFactory();
  //   player.doOnePlacement("Submarine", (p) -> sf.makeSubmarine(p));
  //   player.doOnePlacement("Battleship", (p) -> sf.makeBattleship(p));
  //   String result = player.sonarScanning2(new Coordinate(0, 1));
  //   String expected = "Submarines occupy " + 2 + " squares\n" +

  //       "Destroyers occupy " + 0 + " squares\n" +

  //       "Battleships occupy " + 4 + " squares\n" +

  //       "Carriers occupy " + 0 + " squares\n";
  //   assertEquals(expected, result);

  //   player.doOnePlacement("Destroyer", (p) -> sf.makeDestroyer(p));
  //   player.doOnePlacement("Carrier", (p) -> sf.makeCarrier(p));

  //   // s-----------------------------
  //   String result_s = player.sonar(new Coordinate(4, 0));
  //   String expected_s = "Submarines occupy " + 1 + " square\n" +

  //       "Destroyers occupy " + 0 + " squares\n" +

  //       "Battleships occupy " + 0 + " squares\n" +

  //       "Carriers occupy " + 0 + " squares\n";
  //   assertEquals(expected_s, result_s);
  //   // b-----------------------------------
  //   String result_b = player.sonar(new Coordinate(4, 1));
  //   String expected_b = "Submarines occupy " + 0 + " squares\n" +

  //       "Destroyers occupy " + 0 + " squares\n" +

  //       "Battleships occupy " + 1 + " square\n" +

  //       "Carriers occupy " + 0 + " squares\n";
  //   assertEquals(expected_b, result_b);
  //   // d---------------------------
  //   String result_d = player.sonar(new Coordinate(5, 4));
  //   String expected_d = "Submarines occupy " + 0 + " squares\n" +

  //       "Destroyers occupy " + 1 + " square\n" +

  //       "Battleships occupy " + 0 + " squares\n" +

  //       "Carriers occupy " + 1 + " square\n";

  //   assertEquals(expected_d, result_d);

  //   // //c------------------------
  //   // String result = player.sonar(new Coordinate(0, 1));
  //   // String expected = "Submarines occupy " + 2 + " squares\n" +

  //   // "Destroyers + occupy " + 0 + " squares\n" +

  //   // "Battleships + occupy " + 4 + " squares\n" +

  //   // "Carriers + occupy " + 0 + " squares\n";
  //   // assertEquals(expected, result);
  // }
}
