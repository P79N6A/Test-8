grep "<dubbo:registry address=\"multicast://224.5.6.7:1234?unicast=false\" />" -rl ./ |xargs sed -i "s/"\<dubbo:registry\ address=\"multicast:\\/\\/224.5.6.7:1234?unicast=false\"\ \\/\>"/"\<dubbo:registry\ address=\"zookeeper:\\/\\/172.168.1.211:2181\"\ \\/\>"/"