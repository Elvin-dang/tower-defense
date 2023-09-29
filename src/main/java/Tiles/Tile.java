package Tiles;

import WizardTD.App;
import processing.core.PImage;

public abstract class Tile {
  protected final App app;
  protected final int height;
  protected final int width;
  protected int x;
  protected int y;
  protected PImage image;

  public Tile(App app, int height, int width, int x, int y, PImage image) {
    this.x = x;
    this.y = y;
    this.height = height;
    this.width = width;
    this.image = image;
    this.app = app;
  }

  public void setImage(PImage image) {
    this.image = image;
  }

  public void display() {
    app.image(image, x, y, width, height);
  }

  public int row() {
    return (y - App.TOPBAR) / height;
  }

  public int col() {
    return x / width;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public abstract Tile duplicate();
}
