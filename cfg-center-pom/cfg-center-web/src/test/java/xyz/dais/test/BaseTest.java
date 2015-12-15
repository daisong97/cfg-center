package xyz.dais.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class BaseTest {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void test(){
		int queryForInt = jdbcTemplate.queryForObject("select count(*) from test",Integer.class);
		System.out.println(queryForInt);
	}
	
}
