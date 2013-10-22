package br.com.caiosousa.security.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import br.com.caiosousa.security.vo.Sessao;

import com.mongodb.MongoClient;

@Configuration
public class SpringConfiguration {

	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(new MongoClient("127.0.0.1"), "simplesystem");
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		return mongoTemplate;
	}
	
	@Bean
	public JedisConnectionFactory jedisConnFactory() throws Exception {
		return new JedisConnectionFactory();
	}

	@Bean
	public RedisTemplate<String, Sessao> redisTemplate() throws Exception {
		RedisTemplate<String, Sessao> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnFactory());
		return redisTemplate;
	}
	
}
