package Monsters;

import WizardTD.App;
import WizardTD.GameResource;
import processing.core.PImage;

public class Gremlin extends Monster {
  private int deadFrame = 0;

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
      app.image(gr.gremlin1, x, y, width, height);
    else if (deadFrame < 8)
      app.image(gr.gremlin2, x, y, width, height);
    else if (deadFrame < 12)
      app.image(gr.gremlin3, x, y, width, height);
    else if (deadFrame < 16)
      app.image(gr.gremlin4, x, y, width, height);
    else if (deadFrame < 20)
      app.image(gr.gremlin5, x, y, width, height);
    else {
      shouldRemove = true;
    }
    deadFrame++;
  }
}
