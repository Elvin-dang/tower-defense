package Slider;

import WizardTD.App;

public class Slider {
  private App app;
  private float x;
  private float y;
  private float width;
  private float height;
  private float handlePos;
  private float min;
  private float max;
  private String title;

  public Slider(App app, float x, float y, float width, float height, String title) {
    this.app = app;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.title = title;

    this.handlePos = x + width / 2;
    this.min = x;
    this.max = x + width;
  }

  public void display() {
    app.fill(0);
    app.textSize(12);
    app.text(title, x, y + 12);
    app.fill(200);
    app.rect(x, y + 17 + height / 4, width + 10, height / 2);
    app.fill(50);
    app.rect(handlePos, y + 17, 10, height);
  }

  public void mouseDragged() {
    if (app.mouseX >= x && app.mouseX <= x + width + 10 && app.mouseY >= y + 17 && app.mouseY <= y + 17 + height) {
      handlePos = App.constrain(app.mouseX, min, max);
    }
  }

  public float getValue() {
    return App.map(handlePos, min, max, 0f, 1f);
  }

  public void setHandlePos(float handlePos) {
    this.handlePos = App.map(handlePos, 0f, 1f, min, max);
  }
}
