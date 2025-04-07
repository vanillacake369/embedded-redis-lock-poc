package com.example.embeddedredislockpoc.global.config;


import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
@Profile({"container","embedded"})
public class HostRedisConfig {

    private final static int REDIS_SERVER_MAX_MEMORY = 128;
    private final static String REDIS_SERVER_MAX_MEMORY_SETTING = String.format("maxmemory %dM", REDIS_SERVER_MAX_MEMORY);
    private final RedisProperties redisProperties;

    private LettuceConnectionFactory createClusterConnection() {
        // Socket 설정
        SocketOptions socketOptions = SocketOptions
            .builder()
            .connectTimeout(redisProperties.getTimeout()) // LettuceConnection 생성 시간 초과 값
            .keepAlive(true) // TCP 연결 활성 시간 (keepAlive)
            .build();

        // Cluster topology refresh 설정
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions
            .builder()
            .dynamicRefreshSources(true) // 모든 Redis 노드로부터 topology 정보 획득
            .enableAllAdaptiveRefreshTriggers() // Redis 클러스터에서 발생하는 모든 이벤트(MOVE, ACK)등에 대해서 topology 갱신
            .enablePeriodicRefresh(redisProperties.getLettuce().getCluster().getRefresh().getPeriod()) // 주기적으로 토폴로지 갱신 시간
            .build();

        // Cluster Client 설정
        ClientOptions clientOptions = ClusterClientOptions
            .builder()
            .pingBeforeActivateConnection(true) // 커넥션을 사용하기 위하여 PING 명령어를 사용하여 검증
            .autoReconnect(true) // 자동 재접속
            .socketOptions(socketOptions)
            .topologyRefreshOptions(clusterTopologyRefreshOptions)
            .maxRedirects(redisProperties.getCluster().getMaxRedirects())
            .build();

        // Lettuce Client 설정
        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
            .clientOptions(clientOptions)
            .commandTimeout(Duration.ofMillis(3000L))
            .build();

        // RedisClusterConfiguration 설정
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
        clusterConfig.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());

        // LettuceConnectionFactory 옵션
        LettuceConnectionFactory factory = new LettuceConnectionFactory(clusterConfig, clientConfiguration);
        factory.setValidateConnection(false);
        return factory;
    }

    @Bean
    @Primary
    public LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    @ConditionalOnSingleCandidate(LettuceConnectionFactory.class)
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(LettuceConnectionFactory lettuceConnectionFactory) {
        return lettuceConnectionFactory;
    }

    @Bean
    @ConditionalOnSingleCandidate(LettuceConnectionFactory.class)
    public RedisConnectionFactory redisConnectionFactory(LettuceConnectionFactory lettuceConnectionFactory) {
        return lettuceConnectionFactory;
    }

    @Bean
    public StringRedisSerializer stringSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}