package Tiles;

import WizardTD.App;
import processing.core.PImage;

public class WizardHouseTile extends ManipulatedTile {
  private PImage grass;

  public WizardHouseTile(App app, int height, int width, int x, int y, PImage image, PImage grass) {
    super(app, height, width, x, y, image);
    this.grass = grass;
  }

  public void display() {
    app.image(grass, x + 8, y + 8, width - 16, height - 16);
    app.image(image, x, y, width, height);
  }
}
