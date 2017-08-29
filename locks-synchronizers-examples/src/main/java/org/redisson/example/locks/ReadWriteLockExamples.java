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
import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class ReadWriteLockExamples {

    public static void main(String[] args) throws InterruptedException {
		String[] nodeAddresses = { "redis://172.16.59.113:46321", "redis://172.16.59.114:46321",
				"redis://172.16.59.115:46321", "redis://172.16.59.116:46321", "redis://172.16.59.117:46321",
				"redis://172.16.59.118:46321", "redis://172.16.59.119:46321", "redis://172.16.57.97:46321" };
		Config config = new Config();
		config.useClusterServers().setScanInterval(2000).setConnectTimeout(3000).setIdleConnectionTimeout(10000)
				.setPingTimeout(2000).setTimeout(5000).setMasterConnectionPoolSize(20).addNodeAddress(nodeAddresses);
		final RedissonClient redisson = Redisson.create(config);

        final RReadWriteLock lock = redisson.getReadWriteLock("lock" +new Random().nextFloat());

        lock.writeLock().tryLock(5000L,TimeUnit.MILLISECONDS);
		System.out.println(">>>>>>>1 write locked");
        
        Thread t = new Thread() {
            public void run() {
                 RLock r = lock.readLock();
                 r.lock();
         		System.out.println(">>>>>>>1 read locked");

                 try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                r.unlock();
                System.out.println(">>>>>>>1 read unlocked");
            };
        };

        t.start();
        t.join(1000);

        lock.writeLock().unlock();
		System.out.println(">>>>>>>1 write unlocked");

        t.join();
        
        redisson.shutdown();
    }
    
}
