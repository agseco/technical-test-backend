package com.playtomic.tests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = "test")
public class ApplicationIT {

	@Test
	public void contextShouldStart() {
	}
}
