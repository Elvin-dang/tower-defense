package Errors;

public class InvalidInputFileException extends Exception {

  public InvalidInputFileException(String filename) {
    super(filename + " is an invalid layout file");
  }
}
