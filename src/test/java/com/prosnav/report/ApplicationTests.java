package com.prosnav.report;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
public class ApplicationTests {
	
	private final static Logger LOGGER = LogManager.getLogger(ApplicationTests.class);
	
//	@Autowired
//	@Qualifier("mongoTemplate")
//	private MongoTemplate mongoCrm;
//	
//	@Autowired
//	@Qualifier("mongoTemplateUpm")
//	private MongoTemplate mongoUpm;
//	
//	@Autowired
//	private StringRedisTemplate redisTemplate;
	
	@Test
	public void contextLoads() {
		//LOGGER.debug("contextLoads");
	}

	@Test
	public void mongoTemplate() {
//		Customer c = mongoCrm.findOne(new Query(), Customer.class);
//		LOGGER.debug("Customer : " + c);
//		
//		User u = mongoUpm.findOne(new Query(), User.class);
//		LOGGER.debug("User : " + u);
	}
	
	@Test
	public void redisTemplate() {
//		ValueOperations<String, String> ops = redisTemplate.opsForValue();
//		ops.set("report.redis.test", "111", 10, TimeUnit.SECONDS);
//		String value = ops.get("report.redis.test");
//		Assert.assertEquals("111", value);
		
//		String userStr = ops.get("ps:user:nbQC9rEPpdl4VBri9jznxea1");
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		try {
//			User user = mapper.readValue(userStr, User.class);
//			Assert.assertNotNull(user);
//		} catch (JsonParseException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
