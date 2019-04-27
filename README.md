## Eel

It's a service locator on steroids.

### Disclaimers

* Based on [Pimple](https://pimple.symfony.com/)
* Motivation - learning by creating
* It isn't production code

### Service Locator / Eel

Eel is (to my thinking) definitely not a canonical *dependency injection container*. Eel does not have many important properties, such as:

* revealing cycle dependencies
* recursive dependency instantiation
* doesn't use reflection API (for smart injection)
* ...

but at the same time, it avoids the main problem inherent in service locator - **hiding class dependencies**.

Let's look at the following code snippets:

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

They are pretty similar, but aren't the same. In the former case, class dependencies are hidden, but in the last - they are visible. As result, code becomes more maintainable and testable. Your API no longer lies to you.


### Eel in action

// Todo: need to implement

### How to use

// Todo: need to implement

### Further reading

* [What is Dependency Injection?](http://fabien.potencier.org/what-is-dependency-injection.html)
* [Dependency Injection Containers are Code Polluters](https://www.yegor256.com/2014/10/03/di-containers-are-evil.html)
* [PHP right way. Dependency Injection](https://phptherightway.com/#dependency_injection)
* [Learning About Dependency Injection and PHP](http://ralphschindler.com/2011/05/18/learning-about-dependency-injection-and-php)
* [Auryn is a recursive dependency injector](https://github.com/rdlowrey/auryn)
* [Article about Pimple](https://habr.com/ru/post/199296/)
