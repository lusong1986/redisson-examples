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

import org.redisson.Redisson;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedLockExamples {

    public static void main(String[] args) throws InterruptedException {
		String[] nodeAddresses = { "redis://172.16.59.113:46321", "redis://172.16.59.114:46321",
				"redis://172.16.59.115:46321", "redis://172.16.59.116:46321", "redis://172.16.59.117:46321",
				"redis://172.16.59.118:46321", "redis://172.16.59.119:46321", "redis://172.16.57.97:46321" };
		Config config = new Config();
		config.useClusterServers().setScanInterval(2000).setConnectTimeout(3000).setIdleConnectionTimeout(10000)
				.setPingTimeout(2000).setTimeout(5000).setMasterConnectionPoolSize(20).addNodeAddress(nodeAddresses);
		final RedissonClient client1 = Redisson.create(config);
		final RedissonClient client2 = Redisson.create(config);
        
        final RLock lock1 = client1.getLock("lock1");
        final  RLock lock2 = client1.getLock("lock2");
        final RLock lock3 = client2.getLock("lock3");
        
        Thread t1 = new Thread() {
            public void run() {
                lock3.lock();
                System.out.println(">>>>>>>>lock3 locked");
            };
        };
        t1.start();
        t1.join();
        
        Thread t = new Thread() {
            public void run() {
                RedissonMultiLock lock = new RedissonRedLock(lock1, lock2, lock3);
                lock.lock();
                System.out.println(">>>>>>>>redlock locked");
                
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                lock.unlock();
                System.out.println(">>>>>>>>redlock unlocked");
            };
        };
        t.start();
        t.join(1000);

        lock3.delete();
        System.out.println(">>>>>>>>lock3 deleted");
        
        RedissonMultiLock lock = new RedissonRedLock(lock1, lock2, lock3);
        lock.lock();
        System.out.println(">>>>>>>>redlock 2 locked ");
        lock.unlock();
        System.out.println(">>>>>>>>redlock 2 unlocked ");

        client1.shutdown();
        client2.shutdown();
    }
    
}
