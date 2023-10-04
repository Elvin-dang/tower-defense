package Monsters;

import WizardTD.App;
import WizardTD.GameResource;
import processing.core.PImage;

public class Beetle extends Monster {

  public Beetle(App app, GameResource gr, PImage image, float hp, float speed, float armour,
      float manaGainedOnKill) {
    super(app, gr, image, hp, speed, armour, manaGainedOnKill);
  }

  @Override
  public Monster duplicate() {
    return new Beetle(app, gr, image, hp, speed, armour, manaGainedOnKill);
  }

  @Override
  public void dead() {
    if (deadFrame < 4)
      app.image(gr.beetle, x, y, WIDTH, HEIGHT);
    else if (deadFrame < 8) {
    } else if (deadFrame < 12)
      app.image(gr.beetle, x, y, WIDTH, HEIGHT);
    else if (deadFrame < 16) {
    } else if (deadFrame < 20)
      app.image(gr.beetle, x, y, WIDTH, HEIGHT);
    else {
      shouldRemove = true;
    }
    deadFrame++;
  }
}
