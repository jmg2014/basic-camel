package com.camel.example.route;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;

import static org.junit.Assert.*;

@ActiveProfiles("dev")
@RunWith(CamelSpringBootRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest

public class SimpleCamelRouteTest {

	@Autowired
	ProducerTemplate producerTemplate;

	@Autowired
	Environment environment;

	@BeforeClass
	public static void startCleanUp() throws IOException {

		FileUtils.cleanDirectory(new File("data/input"));
		FileUtils.deleteDirectory(new File("data/output"));
		FileUtils.deleteDirectory(new File("data/input/error"));
	}

	@Test
	public void testMoveFile() throws InterruptedException {

		String message = "ACTION,ITEMID,ITEM_DESCRIPTION,PRICE\n" +
				"ADD,100,Samsung TV,500\n" +
				"ADD,101,LG TV,500";
		String fileName = "fileTest.txt";

		producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, fileName);

		File outFile = new File("data/output/" + fileName);

		await().atMost(1, TimeUnit.MINUTES).until(() -> outFile.exists());

		assertTrue(outFile.exists());

	}

	@Test
	public void testMoveFile_ADD() throws InterruptedException, IOException {

		String message = "ACTION,ITEMID,ITEM_DESCRIPTION,PRICE\n" +
				"ADD,100,Samsung TV,500\n" +
				"ADD,101,LG TV,500";
		String fileName = "fileTest.txt";

		producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, fileName);

		File outFile = new File("data/output/" + fileName);
		await().atMost(1, TimeUnit.MINUTES).until(() -> outFile.exists());
		assertTrue(outFile.exists());
		String outputMessage = "Data Updated SuccessFully";

		File successFile = new File("data/output/Success.txt");
		await().atMost(1, TimeUnit.MINUTES).until(() -> successFile.exists());
		String output = new String(Files.readAllBytes(Paths.get("data/output/Success.txt")));

		assertEquals(outputMessage, output);
	}

	@Test
	public void testMoveFile_ADD_Exception() throws InterruptedException, IOException {

		String message = "ACTION,ITEMID,ITEM_DESCRIPTION,PRICE\n" +
				"ADD,,Samsung TV,500\n" +
				"ADD,101,LG TV,500";
		String fileName = "error.txt";

		producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, fileName);

		File outFile = new File("data/output/" + fileName);
		await().atMost(1, TimeUnit.MINUTES).until(() -> outFile.exists());

		assertTrue(outFile.exists());

		File errorDirectory = new File("data/input/error");
		await().atMost(1, TimeUnit.MINUTES).until(() -> errorDirectory.exists());
		assertTrue(errorDirectory.exists());
	}

}
