package Button;

import WizardTD.App;
import WizardTD.GameController;

public class FastForwardButton extends Button {

  public FastForwardButton(App app, GameController gc, int x, int y, String description, String name, char hotKey,
      boolean showTooltip) {
    super(app, gc, x, y, description, name, hotKey, showTooltip);
  }

  @Override
  public void toggleOn() {
    gc.changeGameSpeed(2);
  }

  @Override
  public void toggleOff() {
    if (gc.getGameSpeed() == 2)
      gc.changeGameSpeed(1);
  }

  @Override
  public void onHover() {
  }
}
