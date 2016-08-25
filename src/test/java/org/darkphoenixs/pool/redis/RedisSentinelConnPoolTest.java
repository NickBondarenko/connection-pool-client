package org.darkphoenixs.pool.redis;

import org.darkphoenixs.pool.PoolConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;

public class RedisSentinelConnPoolTest {

    private ServerSocket serverSocket;

    @Before
    public void before() throws Exception {

        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    serverSocket = new ServerSocket(6379);

                    serverSocket.accept();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        th.setDaemon(true);
        th.start();
    }

    @After
    public void after() throws Exception {

        serverSocket.close();
    }

    @Test
    public void test() throws Exception {

        try {
            RedisSentinelConnPool pool = new RedisSentinelConnPool("localhost",
                    new HashSet<String>(
                            Arrays.asList(new String[]{"localhost:6379"})));
            pool.close();
        } catch (Exception e) {

        }

        try {
            RedisSentinelConnPool pool = new RedisSentinelConnPool("localhost",
                    new HashSet<String>(Arrays
                            .asList(new String[]{"localhost:6379"})));
            pool.close();
        } catch (Exception e) {
        }

        Properties properties = new Properties();

        try {
            RedisSentinelConnPool pool = new RedisSentinelConnPool(
                    new PoolConfig(), properties);
            pool.close();
        } catch (Exception e) {
        }

        properties.setProperty(RedisConfig.MASTERNAME_PROPERTY, "mymaster");

        try {
            RedisSentinelConnPool pool = new RedisSentinelConnPool(
                    new PoolConfig(), properties);
            pool.close();
        } catch (Exception e) {
        }

        properties
                .setProperty(RedisConfig.SENTINELS_PROPERTY, "localhost:6379");

        properties.setProperty(RedisConfig.CONN_TIMEOUT_PROPERTY,
                RedisConfig.DEFAULT_TIMEOUT + "");
        properties.setProperty(RedisConfig.SO_TIMEOUT_PROPERTY,
                RedisConfig.DEFAULT_TIMEOUT + "");
        properties.setProperty(RedisConfig.DATABASE_PROPERTY,
                RedisConfig.DEFAULT_DATABASE + "");
        properties.setProperty(RedisConfig.CLIENTNAME_PROPERTY, "Test");
        properties.setProperty(RedisConfig.PASSWORD_PROPERTY, "Test");

        try {
            RedisSentinelConnPool pool = new RedisSentinelConnPool(
                    new PoolConfig(), properties);

            pool.close();
        } catch (Exception e) {
        }

        try {

            JedisSentinelPool jsp = null;

            RedisSentinelConnPool pool = new RedisSentinelConnPool(jsp);

            pool.returnConnection(null);

            pool.invalidateConnection(null);

            RedisConnectionPool spool = new RedisConnectionPool(
                    RedisConfig.DEFAULT_HOST, RedisConfig.DEFAULT_PORT);

            Jedis jedis = spool.getConnection();

            pool.returnConnection(jedis);

            pool.invalidateConnection(jedis);

            pool.close();

        } catch (Exception e) {

        }

    }

}
