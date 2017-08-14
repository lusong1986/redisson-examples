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
import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class BitSetExamples {

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

        RBitSet bs = redisson.getBitSet("testbitset");
        bs.set(0, 5);
        bs.clear(0, 1);
        bs.length();

        bs.clear();
        bs.set(28);
        bs.get(28);

        bs.not();

        bs.cardinality();

        bs.set(3, true);
        bs.set(41, false);

        RBitSet bs1 = redisson.getBitSet("testbitset1");
        bs1.set(3, 5);

        RBitSet bs2 = redisson.getBitSet("testbitset2");
        bs2.set(4);
        bs2.set(10);
        bs1.and(bs2.getName());
        bs1.or(bs2.getName());
        bs1.xor(bs2.getName());
        
        redisson.shutdown();
    }
    
}
