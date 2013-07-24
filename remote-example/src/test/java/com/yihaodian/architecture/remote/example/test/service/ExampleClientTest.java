package com.yihaodian.architecture.remote.example.test.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yihaodian.architecture.remote.example.service.DataCreator;
import com.yihaodian.architecture.remote.example.service.ExampleService;
import com.yihaodian.architecture.remote.example.service.ParameterVal;

public class ExampleClientTest extends AbstractServiceTest {

	@Autowired
	public ExampleService exampleClient;

	@Test
	public void TestExampleService() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			exampleClient.testService();
		}

		Thread.sleep(100000);
	}

	@Test
	public void TestExampleOtherService() throws InterruptedException {
		exampleClient.testOtherService();
		Thread.sleep(1000);
	}

	@Test
	public void TestDataSize() throws Exception {
		AtomicInteger count = new AtomicInteger(0);
		for (int i = 0; i < 10000; i++) {
			exampleClient.testDataService(DataCreator.createObject(1));
			System.out.println(i);
		}
		ExecutorService es = new ThreadPoolExecutor(40, 40, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(50000));
		final AtomicInteger ai = new AtomicInteger(0);
		final Long curr = System.currentTimeMillis();
		for (int i = 0; i < 50000; i++) {
			es.execute(new Runnable() {

				public void run() {
					String a = exampleClient.testDataService(DataCreator.createObject(1));
					if (ai.incrementAndGet() == 10000) {
						System.out.println("10K request cost:" + (System.currentTimeMillis() - curr));
					}
					if (ai.incrementAndGet() >= 50000) {
						System.out.println("50K request cost:" + (System.currentTimeMillis() - curr));
					}
				}
			});
		}
		Thread.sleep(1000000);
	}

	@Test
	public void TestExampleService3() {
		for (int i = 0; i < 100; i++) {
			String name = "helloworld";
			ParameterVal parameter = new ParameterVal("start");
			exampleClient.testService(name, parameter);
			// System.out.println(exampleClient.testService(name,
			// parameter).getValue());
		}
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}