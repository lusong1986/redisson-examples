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
package org.redisson.example.collections;

import org.redisson.Redisson;
import org.redisson.api.RBoundedBlockingQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class BoundedBlockingQueueExamples {

    public static void main(String[] args) throws InterruptedException {
        // connects to 127.0.0.1:6379 by default
		String[] nodeAddresses = { "redis://172.16.59.113:46321", "redis://172.16.59.114:46321",
				"redis://172.16.59.115:46321", "redis://172.16.59.116:46321", "redis://172.16.59.117:46321",
				"redis://172.16.59.118:46321", "redis://172.16.59.119:46321", "redis://172.16.57.97:46321" };
		Config config = new Config();
		config.useClusterServers().setScanInterval(2000).setConnectTimeout(3000).setIdleConnectionTimeout(10000)
				.setPingTimeout(2000).setTimeout(5000).setMasterConnectionPoolSize(20).addNodeAddress(nodeAddresses);
		RedissonClient redisson = Redisson.create(config);

      final  RBoundedBlockingQueue<String> queue = redisson.getBoundedBlockingQueue("myQueue");
        queue.add("1");
        queue.add("2");
        queue.add("3");
        queue.add("4");
        queue.add("5");
        
        queue.trySetCapacity(5);
        
        Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
	            try {
	                String element = queue.take();
	                
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }				
			}
		});
        
        t.start();
        
        queue.put("6");
        
        redisson.shutdown();
    }
    
}
