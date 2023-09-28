package Monsters;

import WizardTD.App;
import processing.core.PImage;

public class Beetle extends Monster {
  public Beetle(App app, PImage image, float hp, float speed, float armour, float manaGainedOnKill) {
    super(app, image, hp, speed, armour, manaGainedOnKill);
  }

  @Override
  public Monster duplicate() {
    return new Beetle(app, image, hp, speed, armour, manaGainedOnKill);
  }
}
