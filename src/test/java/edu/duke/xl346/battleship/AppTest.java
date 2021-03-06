/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.xl346.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

class AppTest {
  /**
   * private App getNewApp(String placementInf, ByteArrayOutputStream bytes,
   * boolean ifAutoFlush, int width, int height) { StringReader sr = new
   * StringReader(placementInf); //ByteArrayOutputStream bytes = new
   * ByteArrayOutputStream(); PrintStream ps = new PrintStream(bytes,
   * ifAutoFlush); Board<Character> b = new BattleShipBoard<Character>(width,
   * height); return new App(b, sr, ps); }
   * 
   * @Test void test_read_placement() throws IOException { StringReader sr = new
   *       StringReader("B2V\nC8H\na4v\n"); ByteArrayOutputStream bytes = new
   *       ByteArrayOutputStream(); PrintStream ps = new PrintStream(bytes, true);
   *       Board<Character> b = new BattleShipBoard<Character>(10, 20); App app =
   *       new App(b, sr, ps); String prompt = "Please enter a location for a
   *       ship:"; Placement[] expected = new Placement[3]; expected[0] = new
   *       Placement(new Coordinate(1, 2), 'V'); expected[1] = new Placement(new
   *       Coordinate(2, 8), 'H'); expected[2] = new Placement(new Coordinate(0,
   *       4), 'V'); for (int i = 0; i < expected.length; ++i) { Placement p =
   *       app.readPlacement(prompt); assertEquals(p, expected[i]);
   *       assertEquals(prompt + "\n", bytes.toString()); bytes.reset(); } }
   * 
   * @Test void test_doOnePlacement() throws IOException{ String boardInf =
   *       "A0V\nA1H\nb0v\nc0v\nb2h\n"; ByteArrayOutputStream bytes = new
   *       ByteArrayOutputStream(); App app = getNewApp(boardInf, bytes, true, 3,
   *       2); String[] expectedStr = new String[3]; String expectedHeader = "
   *       0|1|2\n"; String prompt = "Where would you like to put your ship?";
   *       expectedStr[0] = expectedHeader+"A s| | A\nB | | B\n"+expectedHeader;
   *       expectedStr[1] = expectedHeader+"A s|s| A\nB | | B\n"+expectedHeader;
   *       expectedStr[2] = expectedHeader+"A s|s| A\nB s| | B\n"+expectedHeader;
   *       for (int i = 0; i < expectedStr.length; ++i) { app.doOnePlacement(); }
   *       assertEquals(prompt+"\n"+expectedStr[0]+"\n"+
   *       prompt+"\n"+expectedStr[1]+"\n"+ prompt + "\n" + expectedStr[2] + "\n",
   *       bytes.toString()); }
   */
  @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
  void dealWithDifferntFile(String input_txt, String output_txt) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true);
    InputStream input = getClass().getClassLoader().getResourceAsStream(input_txt);
    assertNotNull(input);
    InputStream expectedStream = getClass().getClassLoader().getResourceAsStream(output_txt);
    assertNotNull(expectedStream);
    InputStream oldIn = System.in;
    PrintStream oldOut = System.out;
    try {
      System.setIn(input);
      System.setOut(out);
      App.main(new String[0]);
    } finally {
      System.setIn(oldIn);
      System.setOut(oldOut);
    }
    String expected = new String(expectedStream.readAllBytes());
    String actual = bytes.toString();
    assertEquals(expected, actual);
  }

  @Disabled
  @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
  void test_main() throws IOException {
    // dealWithDifferntFile("input_h_c.txt", "output_h_c.txt");
    // dealWithDifferntFile("input_c_h.txt", "output_c_h.txt");
    dealWithDifferntFile("input_c_c.txt", "output_c_c.txt");

  }

  @Disabled
  @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
  void test_main_player2_win() throws IOException {
    dealWithDifferntFile("input2.txt", "output2.txt");
  }

  /**
   * }
   * 
   * @Test void appHasAGreeting() { App classUnderTest = new App();
   *       assertNotNull(classUnderTest.getGreeting(), "app should have a
   *       greeting"); }
   */
}
