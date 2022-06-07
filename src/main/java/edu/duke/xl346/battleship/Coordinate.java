package edu.duke.xl346.battleship;
public class Coordinate {
  private final int row;
  private final int column;

  /**
   * This method return the number of rows.
   * 
   * @return number of rows.
   */
  public int getRow() {
    return row;
  }

  /**
   * This method return the number of columns.
   * 
   * @return number of columns.
   */
  public int getColumn() {
    return column;
  }

  /**
   * Contructs a Coordinate with the specified row and column.
   * 
   * @param row    is the number of rows of the newly constructed coordinate.
   * @param column is the number of columns of the newly constructed coordinate.
   */
  public Coordinate(int row, int column) {
    this.row = row;
    this.column = column;
  }

  /**
   * Construcs a Coordinate with a given string descr.
   * 
   * @parma descr is a string that contains information of row and column.
   * @throws IllegalArgumentException if the length is not two or the first
   *                                  charactor is not a letter or the second
   *                                  letter is not a number.
   */
  public Coordinate(String descr) {
    descr = descr.toUpperCase();
    int len = descr.length();
    if (len != 2) {
      throw new IllegalArgumentException("length of string should be exactly 2");
    }
    char c1 = descr.charAt(0);
    if (c1 < 'A' || c1 > 'Z') {
      throw new IllegalArgumentException("The first charactor of string should be a uppercase-letter");
    }
    char c2 = descr.charAt(1);
    if (c2 < '0' || c2 > '9') {
      throw new IllegalArgumentException("The second charactor should be a single number");
    }
    row = c1 - 'A';
    column = c2 - '0';
  }

  /**
   * This method judge if two objects built from the same class and if their rows
   * and columns are the same number.
   * 
   * @param o is another object to compare.
   * @return false if the two objacts are not built from the same class or if
   *         their rows or columns are not the same number.
   */
  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      Coordinate c = (Coordinate) o;
      return row == c.row && column == c.column;
    }
    return false;
  }

  /**
   * This method get the string version of the coordinate.
   * 
   * @return (row,column)
   */
  @Override
  public String toString() {

    return "(" + row + ", " + column + ")";

  }

  /**
   * This method get hashCode of a given coordinate.
   * 
   * @return hashCode of (row, column).
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}
