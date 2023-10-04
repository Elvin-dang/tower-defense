package Navigation;

public class Tuple<T, N> {
  private T key;
  private N value;

  public Tuple(T key, N value) {
    this.key = key;
    this.value = value;
  }

  public T getKey() {
    return key;
  }

  public N getValue() {
    return value;
  }
}
