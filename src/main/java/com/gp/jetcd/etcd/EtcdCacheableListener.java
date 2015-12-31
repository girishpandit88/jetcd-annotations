package com.gp.jetcd.etcd;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.sun.istack.internal.Nullable;
import mousio.etcd4j.EtcdClient;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Method;

public class EtcdCacheableListener implements TypeListener{
 	private final EtcdClient etcJavaClient;
	@Inject
	public EtcdCacheableListener(EtcdClient etcJavaClient) {
		this.etcJavaClient = etcJavaClient;
	}

	@Override
	public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
		Class<? super I> klass = typeLiteral.getRawType();
		do {
			for (Method method : klass.getDeclaredMethods()) {
				final MethodInterceptor interceptor = getInterceptor(method);
				if (interceptor != null) {
					typeEncounter.bindInterceptor(Matchers.only(method), interceptor);
				}
			}
		} while ((klass = klass.getSuperclass()) != null);
	}
	@Nullable
	private MethodInterceptor getInterceptor(Method method) {
		final EtcdCacheable annotation = method.getAnnotation(EtcdCacheable.class);
		if (annotation != null) {
			String key = annotation.annotationType()+annotation.cacheName();
			return new EtcdCacheableInterceptor(etcJavaClient);
		}
		return null;
	}
}
