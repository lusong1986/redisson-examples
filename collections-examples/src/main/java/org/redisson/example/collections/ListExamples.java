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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class ListExamples {

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

		RList<String> list = redisson.getList("myList"+RandomUtils.nextDouble(10.00, 10000000000.00));
		list.add("1");
		list.add("2");
		list.add("3");

		System.out.println(list.contains("1"));

		String valueAtIndex = list.get(2);
		System.out.println(valueAtIndex);

		for (String string : list) {
			// iteration through bulk loaded values
		}

		boolean removedValue = list.remove("1");
		list.removeAll(Arrays.asList("1", "2", "3"));
		System.out.println(list.containsAll(Arrays.asList("4", "1", "0")));

		List<String> secondList = new ArrayList<>();
		secondList.add("4");
		secondList.add("5");
		list.addAll(secondList);

		// fetch all objects
		List<String> allValues = list.readAll();
		System.out.println(allValues);

		list.addAfter("3", "7");
		list.addBefore("4", "6");

		// use fast* methods when previous value is not required
		list.fastSet(1, "6");
		
		System.out.println(list);
		list.fastRemove(2);
		System.out.println(list);

		//redisson.shutdown();
	}

}
