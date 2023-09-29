package Button;

import WizardTD.App;
import WizardTD.GameController;

public class PauseButton extends Button {
  public PauseButton(App app, GameController gc, int x, int y, String description, String name, char hotKey,
      boolean showTooltip) {
    super(app, gc, x, y, description, name, hotKey, showTooltip);
  }

  @Override
  public void toggleOn() {
    gc.changeGameSpeed(0);
  }

  @Override
  public void toggleOff() {
    if (gc.getGameSpeed() == 0)
      gc.changeGameSpeed(1);
  }

  @Override
  public void onHover() {
  }
}
