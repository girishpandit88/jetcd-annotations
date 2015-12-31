EtcdJava annotations
======================================
[coreos/etcd](https://github.com/coreos/etcd) is a distributed highly available key-value store. There are many java etcd clients out there. This project adds ability to annotate methods, variables for caching values into etcd using one such java etcd client.

##Usage

####Installing the EtcdCacheableModule
```java
final Injector injector = Guice.createInjector(
	new AbstractModule() {
		@Override
		protected void configure() {
			install(new EtcdCacheableModule(config));
		}
	});
```

####Slamming the annotations on methods, fields, local variables
```java
@EtcdCacheable
public double getRandomInt() {
    double result = Math.random();
    return result;
}
```

The ```@EtcdCacheable``` will first lookup etcd for matching key, in this case the method name, if found, will return 
without executing the function. If value is not found, it will execute the method and store the key-value in etcd store.
