package io.github.mapogolions.maybedi;

import org.junit.Test;
import org.junit.Assert;
import io.github.mapogolions.fixtures.*;


public class ContainerTest {
  @Test(expected = UnknownIdentifierException.class)
  public void testGetUnregisteredService() {
    Container di = new Container();
    di.get(FkService.class);
  }

  @Test
  public void testGetRegisteredService() {
    Container di = new Container();
    FkPerson person = new FkPerson();
    di.put(FkPerson.class, c -> person);
    Assert.assertTrue(di.contains(FkPerson.class));
    Assert.assertEquals(di.get(FkPerson.class), person);
    Assert.assertTrue(di.get(FkPerson.class) instanceof FkPerson);
  }

  @Test
  public void testContainerReturnsTheSameIntance() {
    Container di = new Container();
    di.put(FkService.class, c -> new FkService());
    Assert.assertEquals(di.get(FkService.class), di.get(FkService.class));
  }

  @Test
  public void testContainerReturnsTheDifferentInstances() {
    Container di = new Container();
    di.assemble(FkService.class, c -> new FkService());
    Assert.assertNotEquals(di.get(FkService.class), di.get(FkService.class));
  }

  @Test(expected = FrozenServiceException.class)
  public void testOverrideService() {
    Container di = new Container();
    di.put(FkService.class, c -> new FkService());
    di.put(FkService.class, c -> new FkService());
  }

  @Test(expected = FrozenServiceException.class)
  public void testOverrideassembleService() {
    Container di = new Container();
    di.assemble(FkService.class, c -> new FkService());
    di.assemble(FkService.class, c -> new FkService());
  }

  @Test
  public void testDefineGlobalVariable() {
    Container di = new Container();
    di.define("param", "value");
    Assert.assertSame(di.var("param"), "value");
  }

  @Test
  public void testReassignGlobalVariable() {
    Container di = new Container();
    di.define("lucky number", 7);
    di.define("lucky number", 9);
    Assert.assertSame(9, di.var("lucky number"));
  }

  @Test
  public void testGlobalMutableStateWithassemble() {
    Container di = new Container();
    di.define("name", "Balto");
    di.assemble(FkHero.class, c -> new FkHero((String) c.var("name")));
    Assert.assertSame("Balto", di.get(FkHero.class).getName());
    di.define("name", "Superman");
    Assert.assertSame("Superman", di.get(FkHero.class).getName());
  }

  @Test
  public void testUseDroppedGlobalVariable() {
    Container di = new Container();
    di.define("n", 10);
    Assert.assertTrue(di.del("n"));
  }

  @Test
  public void testWithAnonymousFunction() {
    Container di = new Container();
    di.put(FkService.class, c -> new FkService());
    Assert.assertTrue(di.get(FkService.class) instanceof FkService);
  }

  @Test
  public void testPassContainerAsParameter() {
    Container di = new Container();
    di.put(Container.class, c -> c);
    Assert.assertEquals(di, di.get(Container.class));
  }

  @Test
  public void testInjectGlobaldefineiableToServiceCtor() {
    Container di = new Container();
    di.define("name", "Balto");
    di.put(FkHero.class, c -> new FkHero((String) c.var("name")));
    Assert.assertSame(di.var("name"), di.get(FkHero.class).getName());
  }

  @Test
  public void testInjectOneServiceToAnother() {
    Container di = new Container();
    di.put(FkHero.class, c -> new FkHero("some hero"));
    di.put(FkSuperHeroes.class, c -> new FkSuperHeroes(c.get(FkHero.class)));
    Assert.assertEquals(
      di.get(FkHero.class), 
      di.get(FkSuperHeroes.class).dreamTeam().get(0)
    );
  }

  @Test 
  public void testRemoveUnregesteredService() {
    Container di = new Container();
    Assert.assertFalse(di.remove(FkService.class));
  }

  @Test 
  public void testRemoveRegesteredService() {
    Container di = new Container();
    di.put(FkService.class, c -> new FkService());
    Assert.assertTrue(di.remove(FkService.class));
  }

  @Test
  public void testPutServiceAgainAfterRemoval() {
    Container di = new Container();
    di.put(FkService.class, c -> new FkService());
    Assert.assertTrue(di.remove(FkService.class));
    di.put(FkService.class, c -> new FkService());
  }

  @Test
  public void testContainsService() {
    Container di = new Container();
    Assert.assertFalse(di.contains(FkService.class));
    di.put(FkService.class, c -> new FkService());
    Assert.assertTrue(di.contains(FkService.class));
  }
}
