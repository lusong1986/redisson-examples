/**
 * Copyright 2016 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.redisson.example.objects;

import org.redisson.Redisson;
import org.redisson.api.RAtomicDouble;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class AtomicDoubleExamples {

    public static void main(String[] args) {
	String[] nodeAddresses = { "172.16.59.113:46321",
		"172.16.59.114:46321", "172.16.59.115:46321",
		"172.16.59.116:46321", "172.16.59.117:46321",
		"172.16.59.118:46321", "172.16.59.119:46321",
		"172.16.57.97:46321" };
	Config config = new Config();
	config.useClusterServers().setScanInterval(2000)

	.setConnectTimeout(3000).setIdleConnectionTimeout(10000)
		.setPingTimeout(2000).setTimeout(5000)
		.setMasterConnectionPoolSize(20).addNodeAddress(nodeAddresses);
	RedissonClient redisson = Redisson.create(config);

	try {
	    RAtomicDouble atomicDouble = redisson.getAtomicDouble("myDouble");
	    System.out.println(atomicDouble.get());
	    atomicDouble.set(10.01);
	    atomicDouble.getAndDecrement();
	    atomicDouble.getAndIncrement();
	    System.out.println(atomicDouble.get());

	   // System.out.println(redisson.getClusterNodesGroup());

	    atomicDouble.addAndGet(10.323);
	    atomicDouble.compareAndSet(29.4, 412.91);
	    System.out.println(atomicDouble.get());

//	    atomicDouble.decrementAndGet(); 执行这个方法会抛出 ERR value is not an integer or out of range
	    atomicDouble.incrementAndGet();
	    System.out.println(atomicDouble.get());

	    atomicDouble.getAndAdd(302.00);
	    atomicDouble.getAndDecrement();
	    atomicDouble.getAndIncrement();
	    System.out.println(atomicDouble.get());
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    // redisson.shutdown();
	}

    }

}
