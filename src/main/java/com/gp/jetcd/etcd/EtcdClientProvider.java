package com.gp.jetcd.etcd;

import com.google.inject.Singleton;
import com.typesafe.config.Config;
import mousio.etcd4j.EtcdClient;

import java.net.URI;

@Singleton
public class EtcdClientProvider{
	private static EtcdClient etcdClient;
	public EtcdClientProvider(EtcdClient etcdClient) {
		this.etcdClient = etcdClient;
	}

	public static EtcdClient get(Config config){
		if(etcdClient==null){
			return new EtcdClient(URI.create(config.getString("etcd.endpoint")));
		}
		return etcdClient;
	}
}
