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
package org.redisson.example.objects;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.redisson.config.Config;

public class BucketStringExamples {

	public static void main(String[] args) {
		String[] nodeAddresses = { "" };
		Config config = new Config();
		config.useClusterServers().setScanInterval(2000).setConnectTimeout(3000).setIdleConnectionTimeout(10000)
				.setPingTimeout(2000).setTimeout(5000).setMasterConnectionMinimumIdleSize(10)
				.setMasterConnectionPoolSize(20).addNodeAddress(nodeAddresses);
		RedissonClient redisson = Redisson.create(config);

		
		
        RBucket<Object> rBucket = redisson.getBucket("a111aa", ByteArrayCodec.INSTANCE);
        System.out.println(rBucket);
		
		RBucket<String> bucket = redisson.getBucket("test");
		bucket.set("123");
		System.out.println(bucket.get());

		boolean isUpdated = bucket.compareAndSet("123", "4934");
		System.out.println(isUpdated);
		
		String prevObject = bucket.getAndSet("321");
		System.out.println(prevObject);
		
		boolean isSet = bucket.trySet("901");
		System.out.println(isSet);
		
		long objectSize = bucket.size();
		System.out.println(objectSize);
		System.out.println(bucket.get());

		// set with expiration
		bucket.set("value", 10, TimeUnit.SECONDS);
		System.out.println(bucket.get());
		
		boolean isNewSet = bucket.trySet("nextValue", 10, TimeUnit.SECONDS);
		System.out.println(isNewSet);
		
		redisson.shutdown();
	}

}
