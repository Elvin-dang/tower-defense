package Tiles;

import Navigation.Vector;
import WizardTD.App;
import processing.core.PImage;

public class PathTile extends ManipulatedTile {
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

  @Override
  public Tile duplicate() {
    return new PathTile(app, height, width, x, y, image);
  }
}
