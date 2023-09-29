package Button;

import WizardTD.App;
import WizardTD.GameController;

public class ManaPoolButton extends Button {

  public ManaPoolButton(App app, GameController gc, int x, int y, String description, String name, char hotKey,
      boolean showTooltip) {
    super(app, gc, x, y, description, name, hotKey, showTooltip);
  }

  @Override
  public void toggleOn() {
    if (gc.getMana() >= gc.getPoolManaCost()) {
      gc.dropMana(gc.getPoolManaCost());
      gc.setManaCap(gc.getManaCap() * app.gr.manaPoolSpellCapMultiplier);
      gc.setAdditionalManaGainMultiplier(
          gc.getAdditionalManaGainMultiplier() + app.gr.manaPoolSpellManaGainedMultiplier - 1);
      gc.setPoolManaCost(gc.getPoolManaCost() + app.gr.manaPoolSpellCostIncreasePerUse);
      description = "Mana pool cost: " + (int) gc.getPoolManaCost();
    }
    isClicked = false;
  }

  @Override
  public void toggleOff() {
  }

  @Override
  public void onHover() {
    cost = (int) gc.getPoolManaCost();
  }
}
