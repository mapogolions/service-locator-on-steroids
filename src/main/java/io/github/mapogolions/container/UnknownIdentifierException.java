package io.github.mapogolions.container;

final class UnknownIdentifierException extends IllegalArgumentException {
  private static final long serialVersionUID = -3195583549492361423L;

  public UnknownIdentifierException(String id) {
    super(String.format("Indetifier %s is not defined", id));
  }
}