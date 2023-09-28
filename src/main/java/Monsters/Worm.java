package Monsters;

import WizardTD.App;
import processing.core.PImage;

public class Worm extends Monster {
  public Worm(App app, PImage image, float hp, float speed, float armour, float manaGainedOnKill) {
    super(app, image, hp, speed, armour, manaGainedOnKill);
  }

  @Override
  public Monster duplicate() {
    return new Worm(app, image, hp, speed, armour, manaGainedOnKill);
  }
}
