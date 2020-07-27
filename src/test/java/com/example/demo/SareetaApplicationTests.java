package com.example.demo;

import com.example.demo.security.SecurityConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SareetaApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void security_constants_valid() {
		SecurityConstants constants = new SecurityConstants();
		long expirationTime = 864_000_000; // 10 days
		String tokenPrefix = "Bearer ";
		String headerString = "Authorization";
		String signUpUrl = "/api/user/create";
		assertNotNull(constants.SECRET);
		assertEquals(expirationTime, constants.EXPIRATION_TIME);
		assertEquals(tokenPrefix, constants.TOKEN_PREFIX);
		assertEquals(headerString, constants.HEADER_STRING);
		assertEquals(signUpUrl, constants.SIGN_UP_URL);
	}
}
