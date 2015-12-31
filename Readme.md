EtcdJava annotations
======================================
[coreos/etcd](https://github.com/coreos/etcd) is a distributed highly available key-value store. There are many java etcd clients out there. This project adds ability to annotate methods, variables for caching values into etcd using one such java etcd client.

##Usage

####Starting etcd locally

This step assumes you have a docker daemon running locally. I use docker-machine for OSX, 
so I use its IP as a HOST_IP for etcd server IP.

```bash
> cd etc
> ./start-jetcd.sh
```
You can verify that etcd is running locally by running following command
```bash
> export ETCD_IP=$(docker-machine ip default)
> curl -L ETCD_IP/version
```
You can then use this IP and supply in `application.conf` under `etcd.endpoint` config property.

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
