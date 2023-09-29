package Fireball;

import Monsters.Monster;
import WizardTD.App;
import processing.core.PImage;

public class Fireball {
  private final float originalSpeed = 5;
  private final App app;
  private float x;
  private float y;
  private int width;
  private int height;
  private float damage;
  private Monster target;
  private PImage image;

  private float speed = originalSpeed;
  private boolean isHitTarget = false;

  public Fireball(App app, float x, float y, int width, int height, float damage, Monster target, PImage image) {
    this.app = app;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.damage = damage;
    this.target = target;
    this.image = image;
  }

  public void display() {
    if (!isHitTarget) {
      float distanceToTarget = (float) Math
          .sqrt(Math.pow(target.getBodyX() - x, 2) + Math.pow(target.getBodyY() - y, 2));
      if (speed >= distanceToTarget) {
        x = target.getBodyX();
        y = target.getBodyY();
        target.getHit(damage);
        isHitTarget = true;
      } else {
        x += (target.getBodyX() - x) * speed / distanceToTarget;
        y += (target.getBodyY() - y) * speed / distanceToTarget;
      }
      app.image(image, x - width / 2, y - height / 2, width, height);
    }
  }

  public void setSpeed(float multiplier) {
    speed = originalSpeed * multiplier;
  }

  public boolean getIsHitTarget() {
    return isHitTarget;
  }
}
