package org.redisson.example.objects;

import org.apache.commons.lang3.RandomUtils;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class AtomicLongExamples {

    public static void main(String[] args) throws InterruptedException {
	String[] nodeAddresses = { "172.16.59.113:46321",
		"172.16.59.114:46321", "172.16.59.115:46321",
		"172.16.59.116:46321", "172.16.59.117:46321",
		"172.16.59.118:46321", "172.16.59.119:46321",
		"172.16.57.97:46321" };
	Config config = new Config();
	config.useClusterServers().setMasterConnectionPoolSize(20)
		.addNodeAddress(nodeAddresses);
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
	    // redisson.shutdown(); 调用会抛出 cannot be started once stopped
	}
	System.out.println("finish");
    }

}
