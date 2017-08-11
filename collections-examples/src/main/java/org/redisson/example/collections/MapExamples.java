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

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class MapExamples {

    public static void main(String[] args) throws IOException {
		String[] nodeAddresses = { "172.16.59.113:46321", "172.16.59.114:46321", "172.16.59.115:46321",
				"172.16.59.116:46321", "172.16.59.117:46321", "172.16.59.118:46321", "172.16.59.119:46321",
				"172.16.57.97:46321" };
		Config config = new Config();
		config.useClusterServers().setScanInterval(2000)
				.setClientName("cluster" + RandomUtils.nextDouble(10.00, 10000000000.00)).setConnectTimeout(3000)
				.setIdleConnectionTimeout(10000).setPingTimeout(2000).setTimeout(5000).setMasterConnectionPoolSize(20)
				.addNodeAddress(nodeAddresses);
		RedissonClient redisson = Redisson.create(config);
        
        RMap<String, Integer> map =  redisson.getMap("myMap"+ RandomUtils.nextDouble(10.00, 10000000000.00));
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        System.out.println(map);
        
        boolean contains = map.containsKey("a");
        
        Integer value = map.get("c");
        Integer updatedValue = map.addAndGet("a", 32);
        System.out.println(updatedValue);
        
        Integer valueSize = map.valueSize("c");
        
        Set<String> keys = new HashSet<String>();
        keys.add("a");
        keys.add("b");
        keys.add("c");
        Map<String, Integer> mapSlice = map.getAll(keys);
        System.out.println(mapSlice);
        
        // use read* methods to fetch all objects
        Set<String> allKeys = map.readAllKeySet();
        Collection<Integer> allValues = map.readAllValues();
        Set<Entry<String, Integer>> allEntries = map.readAllEntrySet();
        System.out.println(allKeys);
        
        // use fast* methods when previous value is not required
        boolean isNewKey = map.fastPut("a", 100);
        System.out.println(isNewKey);
        boolean isNewKeyPut = map.fastPutIfAbsent("d", 33);
        System.out.println(isNewKeyPut);
        long removedAmount = map.fastRemove("b");
        System.out.println(removedAmount);
        
        System.out.println(map.readAllEntrySet());
        
    }
    
}
