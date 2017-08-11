/**
 * Copyright 2016 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.redisson.example.collections;

import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;
import org.redisson.Redisson;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class SetExamples {

	public static void main(String[] args) {
		String[] nodeAddresses = { "172.16.59.113:46321", "172.16.59.114:46321", "172.16.59.115:46321",
				"172.16.59.116:46321", "172.16.59.117:46321", "172.16.59.118:46321", "172.16.59.119:46321",
				"172.16.57.97:46321" };
		Config config = new Config();
		config.useClusterServers().setScanInterval(2000)
				.setClientName("cluster" + RandomUtils.nextDouble(10.00, 10000000000.00)).setConnectTimeout(3000)
				.setIdleConnectionTimeout(10000).setPingTimeout(2000).setTimeout(5000).setMasterConnectionPoolSize(20)
				.addNodeAddress(nodeAddresses);
		RedissonClient redisson = Redisson.create(config);

		RSet<String> set = redisson.getSet("mySet" + RandomUtils.nextDouble(10.00, 10000000000.00));
		set.add("1");
		set.add("2");
		set.add("3");

		set.contains("1");

		for (String string : set) {
			System.out.println(string);
		}

		boolean removedValue = set.remove("1");
		set.removeAll(Arrays.asList("1", "2", "3"));
		System.out.println(set.containsAll(Arrays.asList("4", "1", "0")));

		String randomRemovedValue = set.removeRandom();
		System.out.println(randomRemovedValue);
		String randomValue = set.random();
		System.out.println(randomValue);

		RSet<String> secondsSet = redisson.getSet("mySecondsSet" + RandomUtils.nextDouble(10.00, 10000000000.00));
		secondsSet.add("4");
		secondsSet.add("5");
		System.out.println(secondsSet.readAll());
		System.out.println(secondsSet.getName());

		// union with "mySecondsSet" and write it
		// set.union(secondsSet.getName()); //CROSSSLOT Keys in request don't hash to the same slot
		System.out.println(set.readAll());

		// union with "mySecondsSet" without change of set
		// set.readUnion(secondsSet.getName()); //CROSSSLOT Keys in request don't hash to the same slot

		// diff with "mySecondsSet" and write it
		set.diff(secondsSet.getName()); // CROSSSLOT Keys in request don't hash to the same slot
		// diff with "mySecondsSet" without change of set
		System.out.println(set.readDiff(secondsSet.getName())); // CROSSSLOT Keys in request don't hash to the same slot

		// intersect with "mySecondsSet" and write it
		set.intersection(secondsSet.getName()); // CROSSSLOT Keys in request don't hash to the same slot
		// intersect with "mySecondsSet" without change of set
		System.out.println(set.readIntersection(secondsSet.getName())); // CROSSSLOT Keys in request don't hash to the
																		// same slot

		Set<String> allValues = set.readAll();
		System.out.println(allValues);

		// redisson.shutdown();
	}

}
