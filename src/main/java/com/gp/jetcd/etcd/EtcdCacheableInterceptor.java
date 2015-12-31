package com.gp.jetcd.etcd;

import lombok.extern.slf4j.Slf4j;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.promises.EtcdResponsePromise;
import mousio.etcd4j.responses.EtcdKeysResponse;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.concurrent.TimeUnit;

@Slf4j
public class EtcdCacheableInterceptor implements MethodInterceptor {
	private final EtcdClient etcdClient;

	public EtcdCacheableInterceptor(EtcdClient etcdClient) {
		this.etcdClient = etcdClient;
	}

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		final Object[] value = new Object[1];
		EtcdResponsePromise<EtcdKeysResponse> etcdResponsePromise = null;
		String key = methodInvocation.getMethod().getName();
		try {
			etcdResponsePromise = etcdClient.get(key).timeout(100, TimeUnit.SECONDS).send();
			final EtcdKeysResponse[] outerResponse = new EtcdKeysResponse[1];
			etcdResponsePromise.addListener(promise -> {
				while (promise.getNow() == null) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					outerResponse[0] = promise.getNow();
					if (outerResponse[0] != null) {
						value[0] = outerResponse[0].node.value;
						log.debug("Coming out of etcd -> {}", value[0]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			if(value[0]==null) {
				value[0] = methodInvocation.proceed();
				etcdClient.put(key, value[0].toString()).send().get();
			}
		} finally {
			if (null != etcdResponsePromise) {
			}
		}
		return value[0];
	}
}
