package com.gp.jetcd;

import com.gp.jetcd.etcd.EtcdCacheable;

public class RandomNumberGenerator {
	@EtcdCacheable
	public double getRandomInt() {
		double result = Math.random();
		return result;
	}
}
