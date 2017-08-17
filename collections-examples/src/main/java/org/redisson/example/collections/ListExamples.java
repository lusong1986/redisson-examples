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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.api.SortOrder;
import org.redisson.config.Config;

public class ListExamples {

	public static void main(String[] args) {
		String[] nodeAddresses = { "redis://172.16.59.113:46321", "redis://172.16.59.114:46321",
				"redis://172.16.59.115:46321", "redis://172.16.59.116:46321", "redis://172.16.59.117:46321",
				"redis://172.16.59.118:46321", "redis://172.16.59.119:46321", "redis://172.16.57.97:46321" };
		Config config = new Config();
		config.useClusterServers().setScanInterval(2000).setConnectTimeout(3000).setIdleConnectionTimeout(10000)
				.setPingTimeout(2000).setTimeout(5000).setMasterConnectionPoolSize(20).addNodeAddress(nodeAddresses);
		RedissonClient redisson = Redisson.create(config);

		try {
			 RList<Double> dlist = redisson.getList("double");
			 dlist.clear(); 
			 dlist.expire(100, TimeUnit.SECONDS);
			 dlist.expireAt(new Date());
			 dlist.add(1D);
			 dlist.add(2D);
			 dlist.add(3D);
			 System.out.println(dlist.readSort(SortOrder.DESC));
			 System.out.println(dlist.readAll());
			 System.out.println(">>>>>>>remainTimeToLive:"+dlist.remainTimeToLive());  //为啥一直都是-1????
			 
			 dlist.sortTo("double", SortOrder.DESC);
			 System.out.println(">>>>>>>after sort>>>"+dlist.readAll());
			

			RList<String> list = redisson.getList("myList" + RandomUtils.nextDouble(10.00, 10000000000.00));
			list.add("1");
			list.add("2");
			list.add("3");

			System.out.println(list.contains("1"));

			String valueAtIndex = list.get(2);
			System.out.println(valueAtIndex);

			for (String string : list) {
				System.out.println(">>>>>>>>>>>" + string);
			}

			boolean removedValue = list.remove("1");
			System.out.println(">>>>>>>>>removedValue:" + removedValue);
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
			System.out.println(">>>>>>>>addAfter:" + list.readAll());

			list.addBefore("4", "6");
			System.out.println(">>>>>>>>addBefore:" + list);

			// use fast* methods when previous value is not required
			list.fastSet(1, "7");

			System.out.println(">>>>>>>>>>>>size:" + list.size());

			list.clearExpire();
			System.out.println(">>>>>>>>clearExpire:" + list.readAll());

			// list.clear();
			System.out.println(">>>>>>>>clear:" + list.readAll());

			// list.delete();
			System.out.println(">>>>>>>>delete:" + list.readAll());

			//list.expire(10, TimeUnit.SECONDS);

			//System.out.println(">>>>>>>>readSort:" + list.readSort(SortOrder.DESC)); //string不能sort
			System.out.println(">>>>>>>>list:" + list.readAll());

			list.fastRemove(2);
			System.out.println(list);

			redisson.shutdown();

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
