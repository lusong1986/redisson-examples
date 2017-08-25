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

import org.redisson.Redisson;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class QueueExamples {

	public static void main(String[] args) {
		String[] nodeAddresses = { "redis://172.16.59.113:46321", "redis://172.16.59.114:46321",
				"redis://172.16.59.115:46321", "redis://172.16.59.116:46321", "redis://172.16.59.117:46321",
				"redis://172.16.59.118:46321", "redis://172.16.59.119:46321", "redis://172.16.57.97:46321" };
		Config config = new Config();
		config.useClusterServers().setScanInterval(2000).setConnectTimeout(3000).setIdleConnectionTimeout(10000)
				.setPingTimeout(2000).setTimeout(5000).setMasterConnectionPoolSize(20).addNodeAddress(nodeAddresses);
		RedissonClient redisson = Redisson.create(config);

		RQueue<String> queue = redisson.getQueue("myQueue");
		queue.add("1");
		queue.add("2");
		queue.add("3");
		queue.add("4");

		queue.contains("1");
		System.out.println(">>>>>>>>>>>>peek:" + queue.peek());
		System.out.println(">>>>>>>>>>>>poll:" + queue.poll());
		System.out.println(">>>>>>>>>>>>element:" + queue.element());

		for (String string : queue) {
			System.out.println(string);
		}

		boolean removedValue = queue.remove("1");
		queue.removeAll(Arrays.asList("1", "2", "3"));
		queue.containsAll(Arrays.asList("4", "1", "0"));

		List<String> secondList = new ArrayList<>();
		secondList.add("4");
		secondList.add("5");
		queue.addAll(secondList);

		RQueue<String> secondQueue = redisson.getQueue("mySecondQueue");

		// CROSSSLOT Keys in request don't hash to the same slot.
		// queue.pollLastAndOfferFirstTo(secondQueue.getName());

		redisson.shutdown();
	}

}
