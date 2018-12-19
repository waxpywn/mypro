package com.example.myPro;

import com.example.myPro.model.dto.TestProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyProApplicationTests {

	@Autowired
	private TestProperties testProperties;

	@Test
	public void contextLoads() {
		System.out.println(testProperties.getTestValue());
	}

}
