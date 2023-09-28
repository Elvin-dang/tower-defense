package Monsters;

import Navigation.Navigation;
import Navigation.Tuple;
import Navigation.Vector;
import Tiles.PathTile;
import WizardTD.App;
import processing.core.PImage;

public abstract class Monster {
  protected App app;
  protected PImage image;
  protected float hp;
  protected float speed;
  protected float armour;
  protected float manaGainedOnKill;
  protected Navigation navigation;

  protected int index = 0;
  protected float exceed = 0;
  protected float currentHp;
  protected float originalSpeed;

  protected float initialX = Float.MIN_VALUE;
  protected float initialY = Float.MIN_VALUE;
  protected float x = Float.MIN_VALUE;
  protected float y = Float.MIN_VALUE;
  protected int width = 20;
  protected int height = 20;

  private final int HEALTH_BAR_HEIGHT = 3;
  private final int HEALTH_BAR_WIDTH = 30;

  public Monster(App app, PImage image, float hp, float speed, float armour, float manaGainedOnKill) {
    this.app = app;
    this.image = image;
    this.hp = hp;
    this.speed = speed;
    this.armour = armour;
    this.manaGainedOnKill = manaGainedOnKill;
    this.currentHp = hp;

    this.originalSpeed = speed;
  }

  public void setNavigation(Navigation navigation) {
    this.navigation = navigation;
  }

  public void setSpeed(float multiplier) {
    speed = originalSpeed * multiplier;
  }

  public void act() {
    if (x == Float.MIN_VALUE && y == Float.MIN_VALUE) {
      spawn();
    } else {
      if (index == navigation.getRoad().size()) {
        spawn();
      } else {
        move();
      }
    }
    display();
  }

  private void move() {
    Tuple<Vector, Float> tuple = navigation.getRoad().get(index);
    x += tuple.getKey().x * (speed + exceed);
    y += tuple.getKey().y * (speed + exceed);
    float nextX = initialX + tuple.getKey().x * tuple.getValue();
    float nextY = initialY + tuple.getKey().y * tuple.getValue();
    exceed = 0;
    if (x > nextX && tuple.getKey() == Vector.RIGHT || x < nextX && tuple.getKey() == Vector.LEFT) {
      index++;
      exceed = Math.abs(x - nextX);
      x = nextX;
      initialX = x;
      initialY = y;
    }
    if (y > nextY && tuple.getKey() == Vector.DOWN || y < nextY && tuple.getKey() == Vector.UP) {
      index++;
      exceed = Math.abs(y - nextY);
      y = nextY;
      initialX = x;
      initialY = y;
    }
  }

  private void spawn() {
    PathTile spawnTile = (PathTile) navigation.getSpawnTile();
    if (spawnTile.getStart() == Vector.DOWN) {
      x = spawnTile.getX() + ((spawnTile.getWidth() - width) / 2);
      y = spawnTile.getY() - height;
    } else if (spawnTile.getStart() == Vector.UP) {
      x = spawnTile.getX() + ((spawnTile.getWidth() - width) / 2);
      y = spawnTile.getY() + spawnTile.getHeight() + height;
    } else if (spawnTile.getStart() == Vector.LEFT) {
      x = spawnTile.getX() + spawnTile.getWidth() + width;
      y = spawnTile.getY() + ((spawnTile.getHeight() - height) / 2);
    } else if (spawnTile.getStart() == Vector.RIGHT) {
      x = spawnTile.getX() - width;
      y = spawnTile.getY() + ((spawnTile.getHeight() - height) / 2);
    }
    initialX = x;
    initialY = y;
    index = 0;
    exceed = 0;
  }

  private void display() {
    app.fill(253, 1, 3);
    app.noStroke();
    app.rect(x - (HEALTH_BAR_WIDTH - width) / 2, y - HEALTH_BAR_HEIGHT - 1, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
    app.fill(45, 251, 74);
    app.rect(x - (HEALTH_BAR_WIDTH - width) / 2, y - HEALTH_BAR_HEIGHT - 1, currentHp / hp * HEALTH_BAR_WIDTH,
        HEALTH_BAR_HEIGHT);
    app.image(image, x, y, width, height);
  }

  public abstract Monster duplicate();
}
