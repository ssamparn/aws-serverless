package com.lambda.aws.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambda.aws.event.PatientCheckoutEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PatientCheckout {

    private static final String PATIENT_CHECKOUT_TOPIC = System.getenv("PATIENT_CHECKOUT_TOPIC");

    private final AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();

    public void handler(S3Event event, Context context) {
        Logger logger = LoggerFactory.getLogger(PatientCheckout.class);

        event.getRecords().forEach(record -> {
            S3ObjectInputStream s3inputStream = s3Client
                    .getObject(record.getS3().getBucket().getName(),
                            record.getS3().getObject().getKey()
                    )
                    .getObjectContent();
            try {
                logger.info("Reading data from s3");
                List<PatientCheckoutEvent> patientCheckoutEvents = Arrays
                        .asList(objectMapper.readValue(s3inputStream, PatientCheckoutEvent[].class));
                logger.info(patientCheckoutEvents.toString());
                s3inputStream.close();
                logger.info("Message being published to SNS");
                publishMessageToSNS(patientCheckoutEvents);
            } catch (JsonMappingException e) {
                logger.error("Exception is:", e);
                throw new RuntimeException("Error while processing S3 event",e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void publishMessageToSNS(List<PatientCheckoutEvent> patientCheckoutEvents) {
        patientCheckoutEvents.forEach(checkoutEvent -> {
            try {
                snsClient.publish(PATIENT_CHECKOUT_TOPIC, objectMapper.writeValueAsString(checkoutEvent));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
