package Monsters;

import WizardTD.App;
import WizardTD.GameResource;
import processing.core.PImage;

public class Worm extends Monster {

  public Worm(App app, GameResource gr, PImage image, float hp, float speed, float armour,
      float manaGainedOnKill) {
    super(app, gr, image, hp, speed, armour, manaGainedOnKill);
  }

  @Override
  public Monster duplicate() {
    return new Worm(app, gr, image, hp, speed, armour, manaGainedOnKill);
  }

  @Override
  public void dead() {
    shouldRemove = true;
  }
}
