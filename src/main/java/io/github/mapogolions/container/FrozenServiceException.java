package io.github.mapogolions.container;


final class FrozenServiceException extends RuntimeException {
  private static final long serialVersionUID = -2602588008457578464L;

  public FrozenServiceException(String id) {
    super(String.format("Cannot override frozen service %s", id));
  }
}