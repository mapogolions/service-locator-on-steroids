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
    di.assemblyLine(FkService.class, c -> new FkService());
    Assert.assertNotEquals(di.get(FkService.class), di.get(FkService.class));
  }

  @Test(expected = FrozenServiceException.class)
  public void testOverrideService() {
    Container di = new Container();
    di.put(FkService.class, c -> new FkService());
    di.put(FkService.class, c -> new FkService());
  }

  @Test(expected = FrozenServiceException.class)
  public void testOverrideAssemblyLineService() {
    Container di = new Container();
    di.assemblyLine(FkService.class, c -> new FkService());
    di.assemblyLine(FkService.class, c -> new FkService());
  }

  @Test
  public void testWithString() {
    Container di = new Container();
    di.pollute("param", "value");
    Assert.assertSame(di.global("param"), "value");
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
  public void testInjectGlobalVariableToServiceCtor() {
    Container di = new Container();
    di.pollute("name", "Balto");
    di.put(FkHero.class, c -> new FkHero((String) c.global("name")));
    Assert.assertSame(di.global("name"), di.get(FkHero.class).getName());
  }

  @Test
  public void testInjectOneServiceToAnother() {
    Container di = new Container();
    di.put(FkHero.class, c -> new FkHero("some hero"));
    di.put(FkSuperHeroes.class, c -> new FkSuperHeroes(c.get(FkHero.class)));
    Assert.assertEquals(di.get(FkHero.class), di.get(FkSuperHeroes.class).dreamTeam().get(0));
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
}
