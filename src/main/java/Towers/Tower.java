package Towers;

import Button.Clickable;
import Button.Hoverable;
import Tiles.Tile;
import WizardTD.App;
import processing.core.PImage;

public abstract class Tower extends Tile implements Clickable, Hoverable {
  private final PImage upgrade1;
  private final PImage upgrade2;
  private float range;
  private float damage;
  private float firingSpeed;

  protected int rangeLv = 0;
  protected int damageLv = 0;
  protected int firingSpeedLv = 0;
  protected boolean isHovered = false;
  protected int shouldShowRangeUpgradeCost = 0;
  protected int shouldShowFiringSpeedUpgradeCost = 0;
  protected int shouldShowDamageUpgradeCost = 0;

  public Tower(App app, int height, int width, int x, int y, PImage image, PImage upgrade1, PImage upgrade2,
      float range, float damage, float firingSpeed) {
    super(app, height, width, x, y, image);
    this.upgrade1 = upgrade1;
    this.upgrade2 = upgrade2;
    this.range = range;
    this.damage = damage;
    this.firingSpeed = firingSpeed;
  }

  @Override
  public void display() {
    app.image(image, x, y, width, height);
    if (isHovered) {
      onHover();
      app.noFill();
      app.stroke(215, 223, 98);
      app.strokeWeight(2);
      app.ellipse(x + width / 2, y + height / 2, (range + rangeLv * width) * 2, (range + rangeLv * height) * 2);

      if (shouldShowDamageUpgradeCost + shouldShowRangeUpgradeCost + shouldShowFiringSpeedUpgradeCost > 0) {
        int bodyHeight = (shouldShowDamageUpgradeCost + shouldShowRangeUpgradeCost + shouldShowFiringSpeedUpgradeCost)
            * 20;
        int nextLine = 586;
        int totalCost = (shouldShowDamageUpgradeCost * (10 + (damageLv + 1) * 10)
            + shouldShowRangeUpgradeCost * (10 + (rangeLv + 1) * 10)
            + shouldShowFiringSpeedUpgradeCost * (10 + (firingSpeedLv + 1) * 10));
        app.fill(255);
        app.stroke(0);
        app.strokeWeight(1);
        app.rect(App.BOARD_WIDTH * App.CELLSIZE + 10, 550, 90, 20);
        app.rect(App.BOARD_WIDTH * App.CELLSIZE + 10, 570, 90, bodyHeight);
        app.rect(App.BOARD_WIDTH * App.CELLSIZE + 10, 570 + bodyHeight, 90, 20);
        app.textSize(12);
        app.fill(0);
        app.text("Upgrade cost", App.BOARD_WIDTH * App.CELLSIZE + 14, 566);
        if (shouldShowRangeUpgradeCost == 1) {
          app.text("range:      " + (10 + (rangeLv + 1) * 10), App.BOARD_WIDTH * App.CELLSIZE + 14, nextLine);
          nextLine += 20;
        }
        if (shouldShowFiringSpeedUpgradeCost == 1) {
          app.text("speed:      " + (10 + (firingSpeedLv + 1) * 10), App.BOARD_WIDTH * App.CELLSIZE + 14, nextLine);
          nextLine += 20;
        }
        if (shouldShowDamageUpgradeCost == 1) {
          app.text("damage:   " + (10 + (damageLv + 1) * 10), App.BOARD_WIDTH * App.CELLSIZE + 14, nextLine);
          nextLine += 20;
        }
        app.text("Total:       " + totalCost, App.BOARD_WIDTH * App.CELLSIZE + 14, 586 + bodyHeight);
      }
    }
  }

  @Override
  public void mousePressed() {
    if (app.mouseX >= x && app.mouseX <= x + width && app.mouseY >= y && app.mouseY <= y + height) {
      toggleOn();
    }
  }

  @Override
  public void mouseMoved() {
    if (app.mouseX >= x && app.mouseX <= x + width && app.mouseY >= y && app.mouseY <= y + height) {
      isHovered = true;
    } else {
      isHovered = false;
    }
  }
}
