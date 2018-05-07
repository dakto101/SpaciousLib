package org.anhcraft.spaciouslib.utils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ProxyUtils {
    private static boolean initialized = false;
    private static Set<Group<String, Integer>> list = new HashSet<>();

    private static void init(){
        if(initialized){
            return;
        }
        initialized = true;
        put("103.241.207.250",8080);
        put("103.246.225.101",3128);
        put("103.74.247.59",42619);
        put("108.61.87.166",3128);
        put("109.195.150.203",3128);
        put("109.72.234.245",53281);
        put("114.25.61.15",53281);
        put("122.183.139.101",8080);
        put("122.183.139.109",8080);
        put("122.183.139.98",8080);
        put("128.199.125.54",3128);
        put("128.199.198.79",8118);
        put("13.92.196.150",8080);
        put("138.201.134.106",8080);
        put("139.59.233.233",80);
        put("147.135.210.114",54566);
        put("149.56.253.235",3128);
        put("151.80.140.233",54566);
        put("152.231.81.122",53281);
        put("195.4.154.160",3128);
        put("198.13.58.86",80);
        put("198.241.55.218",8080);
        put("200.89.98.38",42619);
        put("203.126.218.186",80);
        put("206.189.83.151",3128);
        put("207.148.120.206",8080);
        put("213.6.40.142",80);
        put("35.161.133.86",8080);
        put("35.189.159.113",80);
        put("35.194.149.252",8080);
        put("36.68.209.37",3128);
        put("36.79.34.236",8080);
        put("36.82.239.184",8080);
        put("41.0.237.195",8080);
        put("41.76.150.154",65205);
        put("45.63.127.117",8080);
        put("45.77.119.127",8123);
        put("45.77.95.158",8123);
        put("47.206.51.67",8080);
        put("5.197.183.222",8080);
        put("51.15.227.220",3128);
        put("51.15.86.88",3128);
        put("51.254.238.225",3128);
        put("61.91.235.226",8080);
        put("66.82.123.234",8080);
        put("77.42.250.249",41525);
        put("80.211.11.90",53);
        put("80.211.189.165",3128);
        put("80.211.201.9",8888);
        put("82.114.65.14",53281);
        put("82.137.255.236",8080);
        put("87.228.29.154",53281);
        put("89.236.17.106",3128);
        put("89.40.125.144",3128);
        put("89.40.127.96",3128);
        put("91.210.98.174",3128);
        put("91.67.64.222",3128);
        put("92.53.73.138",8118);
        put("94.130.14.146",31288);
        put("169.239.45.120",53281);
        put("176.123.230.146",3128);
        put("177.204.85.203",80);
        put("179.219.96.199",8080);
        put("180.234.223.18",63909);
        put("180.249.5.21",3128);
        put("180.250.223.23",3128);
        put("181.210.20.37",8080);
        put("181.215.1.110",9064);
        put("182.253.62.165",3129);
        put("182.48.84.178",8080);
        put("185.85.21.6",53281);
        put("185.93.3.123",8080);
        put("188.120.59.47",53281);
        put("188.40.20.138",808);
        put("189.76.82.29",20183);
        put("190.186.58.198",53281);
        put("191.248.184.49",8080);
        put("192.116.142.153",8080);
        put("194.126.183.141",53281);
    }

    public static Set<Group<String, Integer>> getAll(){
        return list;
    }

    public static void remove(String address, int port){
        if(!initialized){
            init();
        }
        Group<String, Integer> group = new Group<>(address, port);
        list.remove(group);
    }

    public static void put(String address, int port){
        if(!initialized){
            init();
        }
        Group<String, Integer> group = new Group<>(address, port);
        list.add(group);
    }

    public static Proxy getRandom(Proxy.Type type){
        if(!initialized){
            init();
        }
        if(list.size() == 0){
            return null;
        }
        Group<String, Integer> proxy = RandomUtils.pickRandom(new ArrayList<>(list));
        return new Proxy(type, new InetSocketAddress(proxy.getA(), proxy.getB()));
    }
}
