package org.redisson.example.objects;

import org.apache.commons.lang3.RandomUtils;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class AtomicLongExamples {

    public static void main(String[] args) throws InterruptedException {
	String[] nodeAddresses = { "redis://172.16.59.113:46321",
		"redis://172.16.59.114:46321", "redis://172.16.59.115:46321",
		"redis://172.16.59.116:46321", "redis://172.16.59.117:46321",
		"redis://172.16.59.118:46321", "redis://172.16.59.119:46321",
		"redis://172.16.57.97:46321" };
	Config config = new Config();
	config.useClusterServers().setScanInterval(2000)
	.setConnectTimeout(3000).setIdleConnectionTimeout(10000)
		.setPingTimeout(2000).setTimeout(5000)
		.setMasterConnectionPoolSize(20).addNodeAddress(nodeAddresses);
	RedissonClient redisson = Redisson.create(config);
	try {
	    RAtomicLong atomicLong = redisson.getAtomicLong("myLong");
	    atomicLong.delete();
	    atomicLong.getAndDecrement();
	    atomicLong.getAndIncrement();
	    System.out.println(atomicLong.get());

	    atomicLong.addAndGet(10L);
	    atomicLong.compareAndSet(29, 412);
	    System.out.println(atomicLong.get());

	    atomicLong.decrementAndGet();
	    atomicLong.incrementAndGet();
	    System.out.println(atomicLong.get());

	    atomicLong.getAndAdd(302);
	    atomicLong.getAndDecrement();
	    atomicLong.getAndIncrement();
	    System.out.println(atomicLong.get());
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    redisson.shutdown(); 
	}
	System.out.println("finish");
    }

}
