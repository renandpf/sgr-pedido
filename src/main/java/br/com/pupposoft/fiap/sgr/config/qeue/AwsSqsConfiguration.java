package br.com.pupposoft.fiap.sgr.config.qeue;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;

import jakarta.jms.Session;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@EnableJms
@RequiredArgsConstructor
public class AwsSqsConfiguration {

	@Value("${cloud.sqs.concurrency}")
	private String concurrency;

	@Value("${cloud.sqs.status-pedido.endpoint}")
	private String endpoint;

	@NonNull
	private Environment environment;

	@Bean
	public DefaultJmsListenerContainerFactory statusPedidoSqsFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(createSQSConnectionFactory());
		factory.setDestinationResolver(new DynamicDestinationResolver());
		factory.setConcurrency(concurrency);
//		factory.setMessageConverter(jacksonJmsMessageConverter());
		factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
		return factory;
	}

//	@Bean // Serialize message content to json using TextMessage
//	public MessageConverter jacksonJmsMessageConverter() {
//		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//		converter.setTargetType(MessageType.TEXT);
//		converter.setTypeIdPropertyName("_type");
//		return converter;
//	}

	private SQSConnectionFactory createSQSConnectionFactory() {

		final SqsClient sqsClient;

		String awsProfile = environment.getProperty("awsProfile");
		if(awsProfile == null) {
			sqsClient = SqsClient.builder()
					.region(Region.US_WEST_2)
					.endpointOverride(URI.create(endpoint))
					.credentialsProvider(EnvironmentVariableCredentialsProvider.create())//AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY and AWS_SESSION_TOKEN environment variables
					.build();
		} else {
			ProfileCredentialsProvider awsProvider = ProfileCredentialsProvider.create(awsProfile);
			sqsClient = SqsClient.builder()
					.region(Region.US_WEST_2)
					.endpointOverride(URI.create(endpoint))
					.credentialsProvider(awsProvider)
					.build();
		}


		return new SQSConnectionFactory(new ProviderConfiguration(), sqsClient);
	}



}
