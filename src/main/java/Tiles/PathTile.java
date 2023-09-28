package Tiles;

import Navigation.Vector;
import WizardTD.App;
import processing.core.PImage;

public class PathTile extends ManipulatedTile {
  // private Direction direction;
  private Vector start;

  public PathTile(App app, int height, int width, int x, int y, PImage image) {
    super(app, height, width, x, y, image);
  }

  public Vector getStart() {
    return start;
  }

  public void setStart(Vector start) {
    this.start = start;
  }

  // public void setDirection(Direction direction, Vector start, PImage image) {
  // this.direction = direction;
  // this.image = image;
  // this.start = start;
  // }

  // public Direction getDirection() {
  // return direction;
  // }
}
