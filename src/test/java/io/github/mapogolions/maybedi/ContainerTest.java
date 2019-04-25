package io.github.mapogolions.maybedi;

import org.junit.Test;
import org.junit.Assert;
import io.github.mapogolions.maybedi.Container;
import io.github.mapogolions.fixtures.*;


public class ContainerTest {
  @Test
  public void testInitialState() {
    Container di = new Container();
    Assert.assertSame(0, di.getServices().size());
    Assert.assertSame(0, di.getAssemblies().size());
    Assert.assertSame(0, di.getFactories().size());
    Assert.assertSame(0, di.getParams().size());
  }

  @Test
  public void testGetAServiceFromTheContainer() {
    Container di = new Container();
    FkPerson person = new FkPerson();
    di.put(FkPerson.class, container -> person);
    Assert.assertTrue(di.contains(FkPerson.class));
    Assert.assertEquals(di.get(FkPerson.class), person);
    Assert.assertTrue(di.get(FkPerson.class) instanceof FkPerson);
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
    di.put(FkService.class, container -> new FkService());
    Assert.assertTrue(di.get(FkService.class) instanceof FkService);
  }

  @Test
  public void testPassContainerAsParameter() {
    Container di = new Container();
    di.put(Container.class, container -> container);
    Assert.assertEquals(di, di.get(Container.class));
  }

  @Test
  public void testAssemblyLineOfServices() {
    Container di = new Container();
    di.assemblyLine(FkService.class, container -> new FkService());
    Assert.assertNotSame(di.get(FkService.class), di.get(FkService.class));
  }
}