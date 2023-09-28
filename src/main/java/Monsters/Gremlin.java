package Monsters;

import WizardTD.App;
import processing.core.PImage;

public class Gremlin extends Monster {
  public Gremlin(App app, PImage image, float hp, float speed, float armour, float manaGainedOnKill) {
    super(app, image, hp, speed, armour, manaGainedOnKill);
  }

  @Override
  public Monster duplicate() {
    return new Gremlin(app, image, hp, speed, armour, manaGainedOnKill);
  }
}
