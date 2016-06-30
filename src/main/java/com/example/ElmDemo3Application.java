package com.example;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ipacc.enterprise.elm.logging.InfinityLogger;
import com.ipacc.enterprise.elm.logging.InfinityLoggerFactory;
import com.ipacc.enterprise.elm.logging.InfinityTransaction;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ElmDemo3Application {
	private InfinityLogger logger = InfinityLoggerFactory.getInstance();

	@RequestMapping("/endtrans")
	public String transpart1(@RequestParam("globalTransId") String gtid, @RequestParam("clientId") String clientId) {
		System.out.println("Transaction part 2 => Global Trans ID is " + gtid + " clientId that called me " + clientId);
		String result = "success";
		InfinityTransaction trans = null;
		try {
			trans = new InfinityTransaction.Builder()
				.setCallerClientId(clientId)
				.setTransactionType("TransactionExample")
				.setGlobalTransactionId(gtid)
				.build();
			logger.startTransaction(trans);
			Thread.sleep((long)(Math.random() * 1000));

			trans.setResolutionFlag(true);
		} catch (Exception e) {
			trans.setResolutionFlag(false);
			result = e.getMessage();
			logger.error(e.getMessage());
		}
		finally {
			logger.endTransaction(trans);
		}
		return result;
	}

	@RequestMapping("/")
	public String home() {
		return "Hello World";
	}
	public static void main(String[] args) {
		SpringApplication.run(ElmDemo3Application.class, args);
	}
}


