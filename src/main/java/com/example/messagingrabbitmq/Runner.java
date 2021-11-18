package com.example.messagingrabbitmq;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;
	private final Receiver receiver;

	public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
		this.receiver = receiver;
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your username: ");
		String username = scanner.nextLine();
		System.out.println("You have been accessed to messaging. Enter \"quit\" to quit messaging");
		while (true) {
			String message = scanner.nextLine();
			if (message.equals("quit")) {
				sendMessage(username + " is disconnected");
				break;
			}
			if (message.isBlank()) continue;
			sendMessage(username + ": " + message);
		}
	}

	private void sendMessage(String message) throws InterruptedException{
		rabbitTemplate.convertAndSend(MessagingRabbitmqApplication.fanoutExchangeName, "", message);
		receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
	}

}
