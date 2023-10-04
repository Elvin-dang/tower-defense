package Navigation;

import Tiles.ManipulatedTile;
import Tiles.PathTile;

public enum Vector {
  UP(0, -1),
  DOWN(0, 1),
  LEFT(-1, 0),
  RIGHT(1, 0);

  public int x;
  public int y;

  Vector(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public static Vector get(ManipulatedTile t1, ManipulatedTile t2) {
    Vector v;
    if (t2 == null)
      v = ((PathTile) t1).getStart();
    else {
      if (t1.row() > t2.row())
        v = UP;
      else if (t1.row() < t2.row())
        v = DOWN;
      else {
        if (t1.col() < t2.col())
          v = RIGHT;
        else
          v = LEFT;
      }
    }
    return v;
  }
}
