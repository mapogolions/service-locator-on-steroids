package io.github.mapogolions.maybedi;

final class InvalidServiceIdentifierException extends IllegalArgumentException {
  private static final long serialVersionUID = -5145219757561215162L;

  public InvalidServiceIdentifierException(String id) {
    super(String.format("Identifier %s does not contain an object definition", id));
  }
}