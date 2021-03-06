package edu.duke.xl346.battleship;
public class Placement {
  private final Coordinate where;
  private final char orientation;

  /**
   *Constrcuts a Placement using where and orientation.
   *@param where is the coordinate
   *@param orientation is the direction to point to  
   */
  public Placement(Coordinate where, char orientation) {
    StringBuffer sc = new StringBuffer("");
    sc.append(orientation);
    String ors = sc.toString();
    ors = ors.toUpperCase();
    this.where = where;
    this.orientation = ors.charAt(0);
  }

  /**
   *Constructs a Placement using a string named rco(row, column, orientation)
   *@param rco contains 3 charactors in total, one for row, one for column and the last one for orientation.
   *@throws IllegalArgumentException if the length of rco is not three or
   *if the last charactor is not 'V','v', 'H' or 'h'.  
   */
  public Placement(String rco) {
    rco = rco.toUpperCase();
    int len = rco.length();
    if (len != 3) {
      throw new IllegalArgumentException("That placement is invalid: the length of input string should be three.");
    }
    String rc = rco.substring(0, 2);
    if (rc.charAt(0) > 'Z' || rc.charAt(0) < 'A') {
      throw new IllegalArgumentException(
          "That placement is invalid: the first character should be from a to z or A to Z.");
    }
    if (rc.charAt(1) < '0' || rc.charAt(1) > '9') {
      throw new IllegalArgumentException("That placement is invalid: the second character should be from 0 to 9.");
    }
    char o = rco.charAt(2);
    // if (o != 'V' && o != 'H') 
    //    throw new IllegalArgumentException("That placement is invalid: the last character should be V or H.");
    // }
    where = new Coordinate(rc);
    orientation = o;
  }

  /**
   *This method return the coordinate names where.
   *@return where is the coordinate(row, column).  
   */
  public Coordinate getWhere() {
    return where;
  }

  /**
   *This method return orientation 
   *@return orientation Direction point to.  
   */
  public char getOrientation() {
    return orientation;
  }

  /**
   *This method judge if two objects are equals.
   *@param o is the Object being compared.
   *@return true if o belongs to class Placement and o's where and orientation are the same as this object.  
   */
  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      Placement p = (Placement) o;
      return this.where.equals(p.where) && this.orientation == p.orientation;
    }
    return false;
  }

  /**
   *This method put information into a string.
   *@return sc.toString() The combination of where and orientation.  
   */
  @Override
  public String toString() {
    StringBuffer sc = new StringBuffer(where.toString());
    sc.append(orientation);
    return sc.toString();
  }

  /**
   *This method return the string.
   *@param toStinrg().hashCode() First call toString() to get the string version of 
   *  the data and then call hasCode()  to get the hash code.
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}












