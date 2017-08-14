package org.redisson.example.objects;

import java.net.URISyntaxException;
import java.util.Iterator;

import org.apache.commons.lang3.RandomUtils;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class KeysTest {

    public static void main(String[] args) throws InterruptedException,
	    URISyntaxException {
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

	 RKeys keys = redisson.getKeys();
	 Iterator<String> it = keys.getKeys().iterator();
	 while(it.hasNext()){
	 System.out.println(it.next());
	 }



    }

}
