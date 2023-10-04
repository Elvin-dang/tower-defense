package Monsters;

import WizardTD.App;
import WizardTD.GameResource;
import processing.core.PImage;

public class Gremlin extends Monster {

  public Gremlin(App app, GameResource gr, PImage image, float hp, float speed, float armour,
      float manaGainedOnKill) {
    super(app, gr, image, hp, speed, armour, manaGainedOnKill);
  }

  @Override
  public Monster duplicate() {
    return new Gremlin(app, gr, image, hp, speed, armour, manaGainedOnKill);
  }

  @Override
  public void dead() {
    if (deadFrame < 4)
      app.image(gr.gremlin1, x, y, WIDTH, HEIGHT);
    else if (deadFrame < 8)
      app.image(gr.gremlin2, x, y, WIDTH, HEIGHT);
    else if (deadFrame < 12)
      app.image(gr.gremlin3, x, y, WIDTH, HEIGHT);
    else if (deadFrame < 16)
      app.image(gr.gremlin4, x, y, WIDTH, HEIGHT);
    else if (deadFrame < 20)
      app.image(gr.gremlin5, x, y, WIDTH, HEIGHT);
    else {
      shouldRemove = true;
    }
    deadFrame++;
  }
}
