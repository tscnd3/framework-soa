package com.xinyue.framework.redis.util;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisLock {

	private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);

	private RedisTemplate<String, Object> redisTemplate;

	public static final String LOCK_PREFIX = "redis_lock:";

	// 锁的名字
	protected String lockKey;

	// 锁的有效时长(秒)
	protected long lockExpires;

	public RedisLock(String lockKey, long lockExpires, RedisTemplate<String, Object> redisTemplate) {
		this.lockKey = LOCK_PREFIX + lockKey;
		this.lockExpires = lockExpires;
		this.redisTemplate = redisTemplate;
	}

	public RedisLock(long lockExpires, RedisTemplate<String, Object> redisTemplate) {
		this.lockKey = LOCK_PREFIX + UUID.randomUUID().toString();
		this.lockExpires = lockExpires;
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 阻塞式获取锁的实现
	 * 
	 * @param unit
	 * @param timeout
	 *            业务超时间
	 * @return
	 */
	public boolean tryLock(long timeout, TimeUnit unit) {
		boolean bool = false;
		long start = System.currentTimeMillis();
		long end = unit.toMillis(timeout);
		long lockExpireTime = lockExpires * 1000;// 锁超时时间
		while (isTimeout(start, end)) {
			if (lock(lockKey, lockExpireTime)) { // 获取到锁
				bool = true;
				break;
			}
			try {
				TimeUnit.MILLISECONDS.sleep(200);
			} catch (InterruptedException e) {
				logger.error("lock异常", e);
				Thread.currentThread().interrupt();
			}
		}
		return bool;
	}

	public void unlock() {
		redisTemplate.delete(lockKey);
	}

	public boolean lock(String key, long lockExpireTime) {
		String lock = key;
		// 利用lambda表达式
		return (Boolean) redisTemplate.execute((RedisCallback<Object>) connection -> {
			long expireAt = System.currentTimeMillis() + lockExpireTime + 1;
			Boolean acquire = connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());
			if (acquire) {
				return true;
			} else {
				byte[] value = connection.get(lock.getBytes());

				if (Objects.nonNull(value) && value.length > 0) {

					long expireTime = Long.parseLong(new String(value));

					if (expireTime < System.currentTimeMillis()) {
						// 如果锁已经过期
						byte[] oldValue = connection.getSet(lock.getBytes(),
								String.valueOf(System.currentTimeMillis() + lockExpireTime + 1).getBytes());
						// 防止死锁
						return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
					}
				}
			}
			return false;
		});
	}

	private static boolean isTimeout(long start, long end) {
		return start + end > System.currentTimeMillis();
	}

}
