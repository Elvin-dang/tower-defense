package Tiles;

import java.util.ArrayList;
import java.util.List;

import WizardTD.App;
import processing.core.PImage;

public abstract class ManipulatedTile extends Tile {
  private ManipulatedTile prevTile;
  public int g;
  public int f;

  public ManipulatedTile(App app, int height, int width, int x, int y, PImage image) {
    super(app, height, width, x, y, image);
    prevTile = null;
    g = 0;
    f = 0;
  }

  public void setPrevTile(ManipulatedTile prevTile) {
    this.prevTile = prevTile;
  }

  public ManipulatedTile getPrevTile() {
    return prevTile;
  }

  public List<ManipulatedTile> getNeighborTiles(Tile[][] tiles) {
    int i = row();
    int j = col();
    List<ManipulatedTile> neighbors = new ArrayList<>();

    if (i < App.BOARD_WIDTH - 1 && (tiles[i + 1][j] instanceof ManipulatedTile))
      neighbors.add((ManipulatedTile) tiles[i + 1][j]);
    if (i > 0 && (tiles[i - 1][j] instanceof ManipulatedTile))
      neighbors.add((ManipulatedTile) tiles[i - 1][j]);
    if (j < App.BOARD_WIDTH - 1 && (tiles[i][j + 1] instanceof ManipulatedTile))
      neighbors.add((ManipulatedTile) tiles[i][j + 1]);
    if (j > 0 && (tiles[i][j - 1] instanceof ManipulatedTile))
      neighbors.add((ManipulatedTile) tiles[i][j - 1]);

    return neighbors;
  }

  public void resetState() {
    prevTile = null;
    g = 0;
    f = 0;
  }
}
