package com.example.myPro.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

//手动配置redis，这是redis的配置类，理论上使用spring boot的application.properties更省事
@Configuration
//@PropertySource("classpath:properties/redis.properties")
public class RedisConfig {
//    @Value("${redis.hostName}")
    private String hostName;
//    @Value("${redis.port}")
    private Integer port;
//    @Value("${redis.password}")
    private String password;
//    @Value("${redis.timeout}")
    private Integer timeout;

//    @Value("${redis.maxIdle}")
    private Integer maxIdle;
//    @Value("${redis.maxTotal}")
    private Integer maxTotal;
//    @Value("${redis.maxWaitMillis}")
    private Integer maxWaitMillis;
//    @Value("${redis.minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;
//    @Value("${redis.numTestsPerEvictionRun}")
    private Integer numTestsPerEvictionRun;
//    @Value("${redis.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;
//    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;
//    @Value("${redis.testWhileIdle}")
    private boolean testWhileIdle;
    //下面是redis集群配置
//    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;
//    @Value("${spring.redis.cluster.max-redirects}")
    private Integer mmaxRedirectsac;
    //下面是redis哨兵模式配置
//    @Value("${redis.sentinel.host1}")
    private String senHost1;
//    @Value("${redis.sentinel.port1}")
    private Integer senPort1;
//    @Value("${redis.sentinel.host2}")
    private String senHost2;
//    @Value("${redis.sentinel.port2}")
    private Integer senPort2;

    /**
     * @Author andi
     * @Date: 2018/11/19 20:31:00
     * @Description : JedisPoolConfig 连接池
     */
//    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲数
        jedisPoolConfig.setMaxIdle(maxIdle);
        // 连接池的最大数据库连接数
        jedisPoolConfig.setMaxTotal(maxTotal);
        // 最大建立连接等待时间
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
        return jedisPoolConfig;
    }
    /**
     * @Author andi
     * @Date: 2018/11/19 20:30:45
     * @Description : 单机版配置
     */
//    @Bean
    public JedisConnectionFactory JedisConnectionFactory(JedisPoolConfig jedisPoolConfig){
        //单机版jedis
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration();
        //设置redis服务器的host或者ip地址
        redisStandaloneConfiguration.setHostName("localhost");
        //设置默认使用的数据库
        redisStandaloneConfiguration.setDatabase(0);
        //设置密码
        redisStandaloneConfiguration.setPassword(RedisPassword.of("123456"));
        //设置redis的服务的端口号
        redisStandaloneConfiguration.setPort(6380);
        //获得默认的连接池构造器(怎么设计的，为什么不抽象出单独类，供用户使用呢)
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        //指定jedisPoolConifig来修改默认的连接池构造器（真麻烦，滥用设计模式！）
        jpcb.poolConfig(jedisPoolConfig);
        //通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();
        //单机配置 + 客户端配置 = jedis连接工厂
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    /**
     * @Author andi
     * @Date: 2018/11/19 20:30:25
     * @Description : Redis集群的配置
     */
//    @Bean
    public RedisClusterConfiguration redisClusterConfiguration(){
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        String[] serverArray = clusterNodes.split(",");
        Set<RedisNode> nodes = new HashSet<>();
        for(String ipPort:serverArray){
            String[] ipAndPort = ipPort.split(":");
            nodes.add(new RedisNode(ipAndPort[0].trim(),Integer.valueOf(ipAndPort[1])));
        }
        redisClusterConfiguration.setClusterNodes(nodes);
        redisClusterConfiguration.setMaxRedirects(mmaxRedirectsac);
        return redisClusterConfiguration;
    }

    /**
     * @Author andi
     * @Date: 2018/11/19 20:43:03
     * @Description : 配置redis的哨兵
     */
//    @Bean
    public RedisSentinelConfiguration sentinelConfiguration(){
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
        //配置matser的名称
        RedisNode redisNode = new RedisNode(hostName, port);
        redisNode.setName("mymaster");
        redisSentinelConfiguration.master(redisNode);
        //配置redis的哨兵sentinel
        RedisNode senRedisNode1 = new RedisNode(senHost1,senPort1);
        RedisNode senRedisNode2 = new RedisNode(senHost2,senPort2);
        Set<RedisNode> redisNodeSet = new HashSet<>();
        redisNodeSet.add(senRedisNode1);
        redisNodeSet.add(senRedisNode2);
        redisSentinelConfiguration.setSentinels(redisNodeSet);
        return redisSentinelConfiguration;
    }

    /**
     * @Author andi
     * @Date: 2018/11/19 20:31:20
     * @Description : 实例化 RedisTemplate 对象
     * 这里可以使用spring boot里的application.properties配置，加上Configuration注解，声明Bean
     * 重写redisTemplate，spring boot的默认redisTemplate就会停用
     */
    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 开启事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
    /**
     * @Author andi
     * @Date: 2018/11/19 20:31:33
     * @Description : 实例化 StringRedisTemplate 对象
     */
//    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
    }
}
