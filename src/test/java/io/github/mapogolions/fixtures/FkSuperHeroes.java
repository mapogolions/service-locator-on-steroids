package io.github.mapogolions.fixtures;

import java.util.ArrayList;
import java.util.List;

public class FkSuperHeroes {
  private List<FkHero> team = new ArrayList<>();

  public FkSuperHeroes(FkHero ...heroes) {
    for (FkHero hero : heroes) {
      team.add(hero);
    }
  }

  public List<FkHero> dreamTeam() {
    return team;
  }
}