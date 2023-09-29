package Tiles;

import WizardTD.App;
import processing.core.PImage;

public class ShrubTile extends Tile {

  public ShrubTile(App app, int height, int width, int x, int y, PImage image) {
    super(app, height, width, x, y, image);
  }

  @Override
  public Tile duplicate() {
    return new ShrubTile(app, height, width, x, y, image);
  }
}