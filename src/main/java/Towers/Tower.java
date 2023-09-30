package Towers;

import java.util.Collections;
import java.util.List;

import Button.Clickable;
import Button.Hoverable;
import Fireball.Fireball;
import Monsters.Monster;
import Navigation.Tuple;
import Tiles.Tile;
import WizardTD.App;
import WizardTD.GameController;
import processing.core.PImage;

public class Tower extends Tile implements Clickable, Hoverable {
  private final PImage upgrade1;
  private final PImage upgrade2;
  private final GameController gc;
  private float range;
  private float damage;
  private float firingSpeed;

  protected int rangeLv = 0;
  protected int firingSpeedLv = 0;
  protected int damageLv = 0;
  protected boolean isHovered = false;
  protected int shouldShowRangeUpgradeCost = 0;
  protected int shouldShowFiringSpeedUpgradeCost = 0;
  protected int shouldShowDamageUpgradeCost = 0;
  protected float timeMultiplier = 1;
  protected long timer = 0;

  public Tower(App app, GameController gc, int height, int width, int x, int y, PImage image, PImage upgrade1,
      PImage upgrade2,
      float range, float damage, float firingSpeed, int rangeLv, int firingSpeedLv, int damageLv) {
    super(app, height, width, x, y, image);
    this.upgrade1 = upgrade1;
    this.upgrade2 = upgrade2;
    this.gc = gc;
    this.range = range;
    this.damage = damage;
    this.firingSpeed = firingSpeed;
    this.rangeLv = rangeLv;
    this.firingSpeedLv = firingSpeedLv;
    this.damageLv = damageLv;
  }

  @Override
  public void display() {
    int presentRangeLv = rangeLv;
    int presentFiringSpeedLv = firingSpeedLv;
    int presentDamageLv = damageLv;

    if (rangeLv >= 2 && firingSpeedLv >= 2 && damageLv >= 2) {
      app.image(upgrade2, x, y, width, height);
      presentRangeLv -= 2;
      presentFiringSpeedLv -= 2;
      presentDamageLv -= 2;
    } else if (rangeLv >= 1 && firingSpeedLv >= 1 && damageLv >= 1) {
      app.image(upgrade1, x, y, width, height);
      presentRangeLv -= 1;
      presentFiringSpeedLv -= 1;
      presentDamageLv -= 1;
    } else {
      app.image(image, x, y, width, height);
    }

    app.noFill();
    app.stroke(123, 180, 255);
    app.strokeWeight(presentFiringSpeedLv);
    app.rect(x + 6, y + 6, 20, 20);
    app.fill(247, 51, 247);
    app.textSize(10);
    app.text(String.join("", Collections.nCopies(presentRangeLv, "O")), x, y + 8);
    app.text(String.join("", Collections.nCopies(presentDamageLv, "X")), x, y + height);
  }

  public void displayTowerRange() {
    if (isHovered) {
      app.noFill();
      app.stroke(215, 223, 98);
      app.strokeWeight(2);
      app.ellipse(x + width / 2, y + height / 2, (range + rangeLv * width) * 2, (range + rangeLv * height) * 2);
    }
  }

  public void displayUpgradeCostInfo() {
    if (isHovered) {
      onHover();
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

  public void setSpeed(float multiplier) {
    timeMultiplier = multiplier;
  }

  public void shoot(List<Tuple<Long, Monster>> monsters, List<Fireball> fireballs) {
    if (timer >= 1000 / (firingSpeed + 0.5 * firingSpeedLv)) {
      Monster nearestMonster = null;
      double minDistance = Float.MAX_VALUE;
      for (Tuple<Long, Monster> tuple : monsters) {
        Monster monster = tuple.getValue();
        if (!monster.isDead()) {
          double distance = Math.sqrt(Math.pow(monster.getX() - x, 2) + Math.pow(monster.getY() - y, 2));
          if (distance <= range + rangeLv * width && distance < minDistance) {
            minDistance = distance;
            nearestMonster = monster;
          }
        }
      }
      if (nearestMonster != null) {
        timer = 0;
        playSoundEffect();
        fireballs.add(new Fireball(app, x + width / 2, y + height / 2, 6, 6, damage * (1 + damageLv / 2),
            nearestMonster, app.gr.fireball));
      }
    }
    timer += timeMultiplier * 1000 / App.FPS;
  }

  private void playSoundEffect() {
    if (rangeLv >= 2 && firingSpeedLv >= 2 && damageLv >= 2) {
      app.gr.shoot2.play();
    } else if (rangeLv >= 1 && firingSpeedLv >= 1 && damageLv >= 1) {
      app.gr.shoot1.play();
    } else {
      app.gr.shoot.play();
    }
  }

  @Override
  public Tile duplicate() {
    return new Tower(app, gc, height, width, x, y, image, upgrade1, upgrade2, range, damage, firingSpeed, rangeLv,
        firingSpeedLv, damageLv);
  }

  @Override
  public void toggleOn() {
    if (gc.getIsUpgradeRange() == 1 && gc.getMana() >= (10 + (rangeLv + 1) * 10)) {
      gc.dropMana(10 + (rangeLv + 1) * 10);
      rangeLv += 1;
    }
    if (gc.getIsUpgradeFiringSpeed() == 1 && gc.getMana() >= (10 + (firingSpeedLv + 1) * 10)) {
      gc.dropMana(10 + (firingSpeedLv + 1) * 10);
      firingSpeedLv += 1;
    }
    if (gc.getIsUpgradeDamage() == 1 && gc.getMana() >= (10 + (damageLv + 1) * 10)) {
      gc.dropMana(10 + (damageLv + 1) * 10);
      damageLv += 1;
    }
  }

  @Override
  public void toggleOff() {
  }

  @Override
  public void onHover() {
    shouldShowDamageUpgradeCost = gc.getIsUpgradeDamage();
    shouldShowRangeUpgradeCost = gc.getIsUpgradeRange();
    shouldShowFiringSpeedUpgradeCost = gc.getIsUpgradeFiringSpeed();
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
