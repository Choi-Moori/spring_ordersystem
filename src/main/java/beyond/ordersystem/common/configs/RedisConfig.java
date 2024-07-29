package beyond.ordersystem.common.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * 이 부분으로 정보 가져와서 사용
     * 💡 application.yml의 spring.redis.host의 정보를 소스코드 변수로 가져옴
     */
    @Value("${spring.redis.host}")
    public String host; // 이 host에 정보가 들어옴

    @Value("${spring.redis.port}")
    public int port; // 이 port에 port 정보가 들어옴


    /**
     * RedisConnectionFactory는 Redis 서버와의 연결을 설정하는 역할
     * LettuceConnectionFactory는 RedisConnectionFactory는의 구현체임 (실질적 역할 수행)
     */
    @Bean
    @Qualifier("2")
    public RedisConnectionFactory redisConnectionFactory() {
        // connection 정보 넣기

        // 방법 1 => 근데 이건 유연성 떨어짐
//        return new LettuceConnectionFactory("localhost", 6379);
        // yml에 이미 host와 port를 지정했는데 이렇게 코드 작성하는게 좋은 코드인가?
        // 만약 추후에 port가 바뀌면 어쩔거??? => 해결 : 정보를 받아오면 됨 (상단 value부분)

        // 방법 2 -> 이거 추천
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
//        configuration.setDatabase(2); // 유연하게 추가 가능
//        configuration.setPassword("1234");
        configuration.setDatabase(1);
        return new LettuceConnectionFactory(configuration);

    }


    /**
     * redisTemplate 정의
     * redisTemplate는 redis와 상호작용 할 때 redis key, value의 형식을 정의
     * 위에서의 연결정보가 아래 파라미터 redisConnectionFactory에 주입이 됨
     */
    @Bean
    @Qualifier("2")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("2") RedisConnectionFactory redisConnectionFactory){

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // string 형태를 직렬화 시키게따 (java에 string으로 들어가게)
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 제이슨 직렬화 툴 세팅
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 주입받은 connection을 넣어줌
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * redisTemplate를 불러다가 .opsForValue().set(key,value)
     * redisTemplate.opsForValue().get(key)
     * redisTemplate.opsForValue().increment 또는 decrement
     * => redisTemplate를 통해 메서드가 제공됨
     */

}
