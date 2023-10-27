package com.emp.gw.task;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaskApplicationTests {

	private final SpringBootContextLoader springBootContextLoader = new SpringBootContextLoader();
	@Test
	void contextLoads() {
		MatcherAssert.assertThat(springBootContextLoader, CoreMatchers.is(CoreMatchers.notNullValue()));
	}

}
