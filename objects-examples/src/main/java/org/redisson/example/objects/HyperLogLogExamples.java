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

import java.util.Arrays;

import org.redisson.Redisson;
import org.redisson.api.RHyperLogLog;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class HyperLogLogExamples {

    public static void main(String[] args) {
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

	RHyperLogLog<String> hyperLogLog = redisson
		.getHyperLogLog("hyperLogLog");
	hyperLogLog.add("1");
	hyperLogLog.add("2");
	hyperLogLog.add("3");
	hyperLogLog.addAll(Arrays.asList("10", "20", "30"));

	RHyperLogLog<String> hyperLogLog1 = redisson
		.getHyperLogLog("hyperLogLog1");
	hyperLogLog1.add("4");
	hyperLogLog1.add("5");
	hyperLogLog1.add("6");

	RHyperLogLog<String> hyperLogLog2 = redisson
		.getHyperLogLog("hyperLogLog2");
	hyperLogLog1.add("4");
	hyperLogLog1.add("5");
	hyperLogLog1.add("6");

	hyperLogLog2.mergeWith(hyperLogLog1.getName());
	hyperLogLog2.countWith(hyperLogLog1.getName());

	redisson.shutdown();
    }

}
