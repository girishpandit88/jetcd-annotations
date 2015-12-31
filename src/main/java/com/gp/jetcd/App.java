package com.gp.jetcd;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.gp.jetcd.etcd.EtcdCacheableModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
	public static void main(String[] args) {
		Config config = ConfigFactory.load("application.conf");
		final Injector injector = Guice.createInjector(
				new AbstractModule() {
					@Override
					protected void configure() {
						bind(RandomNumberGenerator.class);
						install(new EtcdCacheableModule(config));
					}
				}
		);
		final RandomNumberGenerator instance = injector.getInstance(RandomNumberGenerator.class);
		log.info("{}", (double)instance.getRandomInt());
		log.info("{}", (double)instance.getRandomInt());
		System.exit(0);
	}
}
