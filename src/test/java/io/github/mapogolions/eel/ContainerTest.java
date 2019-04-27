package io.github.mapogolions.eel;

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
    FkService service = new FkService();
    di.put(FkService.class, c -> service);
    Assert.assertTrue(di.contains(FkService.class));
    Assert.assertEquals(di.get(FkService.class), service);
    Assert.assertTrue(di.get(FkService.class) instanceof FkService);
  }

  @Test
  public void testContainerReturnsTheDifferentInstances() {
    Container di = new Container();
    di.put(FkService.class, c -> new FkService());
    Assert.assertNotEquals(di.get(FkService.class), di.get(FkService.class));
  }

  @Test
  public void testContainerReturnsTheSameInstance() {
    Container di = new Container();
    di.share(FkService.class, c -> new FkService());
    Assert.assertEquals(di.get(FkService.class), di.get(FkService.class));
  }

  @Test(expected = FrozenServiceException.class)
  public void testOverrideService() {
    Container di = new Container();
    di.put(FkService.class, c -> new FkService());
    di.put(FkService.class, c -> new FkService());
  }

  @Test
  public void testDefineGlobalVariable() {
    Container di = new Container();
    di.define("param", "value");
    Assert.assertSame(di.variable("param"), "value");
  }

  @Test(expected = UnknownIdentifierException.class)
  public void testUseUnsetGlobalVariable() {
    Container di = new Container();
    di.variable("name");
  }

  @Test
  public void testReassignGlobalVariable() {
    Container di = new Container();
    di.define("lucky number", 7);
    di.define("lucky number", 9);
    Assert.assertSame(9, di.variable("lucky number"));
  }

  @Test
  public void testSharedGlobalMutableState() {
    Container di = new Container();
    di.define("name", "Balto");
    di.put(FkHero.class, c -> new FkHero(c.variable("name", String.class)));
    Assert.assertSame("Balto", di.get(FkHero.class).getName());
    di.define("name", "Superman");
    Assert.assertSame("Superman", di.get(FkHero.class).getName());
  }

  @Test
  public void testUnsetGlobalVariable() {
    Container di = new Container();
    di.define("defined global variable", 10);
    Assert.assertTrue(di.delete("defined global variable"));
    Assert.assertFalse(di.delete("undefined global variable"));
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
    di.define("name", "Balto");
    di.put(FkHero.class, c -> new FkHero((String) c.variable("name")));
    Assert.assertSame(di.variable("name"), di.get(FkHero.class).getName());
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testInjectOneServiceToAnother() {
    Container di = new Container();
    di.put(FkCacheItem.class, c -> new FkCacheItem<String, Integer>("one", 1));
    di.put(FkCache.class, c -> {
      FkCache<String, Integer> cache = new FkCache<>();
      cache.save(c.get(FkCacheItem.class));
      return cache;
    });
    Assert.assertTrue(di.contains(FkCacheItem.class));
    Assert.assertTrue(di.contains(FkCache.class));
    Assert.assertSame(
      di.get(FkCacheItem.class).getValue(),
      di.get(FkCache.class).obtain(0).getValue()
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

  @Test(expected = UnknownIdentifierException.class)
  public void testExtendValidatesKeyIsPresent() {
    Container di = new Container();
    di.extend(FkService.class, (entity, c) -> entity);
  }

  @Test(expected = FrozenServiceException.class)
  public void testExtendSharedService() {
    Container di = new Container();
    di.share(FkService.class, c -> new FkService());
    di.get(FkService.class);
    di.extend(FkService.class, (entity, c) -> entity);
  }

  @Test
  public void testExtend() {
    Container di = new Container();
    di.put(FkPerson.class, c -> new FkPerson());
    di.extend(FkPerson.class, (person, c) -> {
      person.firstName = "John";
      person.lastName = "Smith";
      return person;
    });
    Assert.assertSame("John", di.get(FkPerson.class).firstName);
    Assert.assertSame("Smith", di.get(FkPerson.class).lastName);
    Assert.assertSame(0, di.get(FkPerson.class).age);
  }
}
