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
package org.redisson.example.locks;

import java.util.Random;

import org.redisson.Redisson;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class SemaphoreExamples {

    public static void main(String[] args) throws InterruptedException {
		String[] nodeAddresses = { "redis://172.16.59.113:46321", "redis://172.16.59.114:46321",
				"redis://172.16.59.115:46321", "redis://172.16.59.116:46321", "redis://172.16.59.117:46321",
				"redis://172.16.59.118:46321", "redis://172.16.59.119:46321", "redis://172.16.57.97:46321" };
		Config config = new Config();
		config.useClusterServers().setScanInterval(2000).setConnectTimeout(3000).setIdleConnectionTimeout(10000)
				.setPingTimeout(2000).setTimeout(5000).setMasterConnectionPoolSize(20).addNodeAddress(nodeAddresses);
		final RedissonClient redisson = Redisson.create(config);

        final String name = "test" +new Random().nextDouble();
		RSemaphore s = redisson.getSemaphore(name);
        System.out.println(">>>>>>>>>>>trySetPermits(5");
        s.trySetPermits(5);
        s.acquire(3);
        System.out.println(">>>>>>>>>>>acquire(3");

        Thread t = new Thread() {
            @Override
            public void run() {
                RSemaphore s = redisson.getSemaphore(name);
                s.release();
                s.release();
                System.out.println(">>>>>>>>>>>release(2");
            }
        };

        t.start();

        t.join();
        
        s.acquire(4);
        System.out.println(">>>>>>>>>>>acquire(4");
        
        redisson.shutdown();
    }
    
}
