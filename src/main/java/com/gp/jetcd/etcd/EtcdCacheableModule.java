package com.gp.jetcd.etcd;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import mousio.etcd4j.EtcdClient;

@Slf4j
public class EtcdCacheableModule extends AbstractModule {
	private static EtcdClient etcdClient;
	private final Config config;
	private final Matcher<? super TypeLiteral<?>> matcher;
	public EtcdCacheableModule(Config config) {
		this.config = config;
		this.etcdClient = EtcdClientProvider.get(this.config);
		this.matcher = Matchers.any();
	}

	public static EtcdClient getEtcdClient(){
		return etcdClient;
	}

	@Override
	protected void configure() {
		log.debug(etcdClient.getVersion());
		bindListener(matcher, new EtcdCacheableListener(etcdClient));
	}
}
