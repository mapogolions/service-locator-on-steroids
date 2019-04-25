package io.github.mapogolions.maybedi;

import org.junit.Test;
import org.junit.Assert;
import io.github.mapogolions.maybedi.Container;
import io.github.mapogolions.fixtures.FakePerson;


public class ContainerTest {
  @Test
  public void testGetAServiceFromTheContainer() {
    Container di = new Container();
    FakePerson person = new FakePerson();
    di.put(FakePerson.class, container -> person);
    Assert.assertTrue(di.contains(FakePerson.class));
    Assert.assertEquals(di.get(FakePerson.class), person);
    Assert.assertTrue(di.get(FakePerson.class) instanceof FakePerson);
  }
}
