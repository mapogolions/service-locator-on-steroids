## Eel

It's a service locator on steroids.

### Disclaimers

* Based on - [Pimple](https://pimple.symfony.com/)
* Motivation - learning by creating
* It's not production, it's attempt to release
* Pimple is a strange beast. Article about Pimple on [habr](https://habr.com/ru/post/199296/). See comments.

*Service Locator*

```java
Container di = new Container();
di.put(Config.class, new Config(di));
di.put(Db.class, new Db(di));
```

*Eel*

```java
Container di = new Container();
di.put(Config.class, c -> new Config());
di.put(Db.class, c -> new Db(c.get(Config.class)));
```

Pretty similar, but isn't the same. In the former case class dependencies are hidden, but in the last - they are visible. As result, code becomes more maintainable and testable. Your API no longer lies to you.

### Further reading

* [PHP right way. Dependency Injection](https://phptherightway.com/#dependency_injection)
* [What is Dependency Injection?](http://fabien.potencier.org/what-is-dependency-injection.html)
* [Learning About Dependency Injection and PHP](http://ralphschindler.com/2011/05/18/learning-about-dependency-injection-and-php)
* [What is Dependency Injection?](http://fabien.potencier.org/what-is-dependency-injection.html)
* ["True" di container. See README.md](https://github.com/rdlowrey/auryn)
* [Dependency Injection Containers are Code Polluters](https://www.yegor256.com/2014/10/03/di-containers-are-evil.html)
