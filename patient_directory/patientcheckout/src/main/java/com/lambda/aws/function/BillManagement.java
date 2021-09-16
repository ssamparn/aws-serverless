package com.lambda.aws.function;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambda.aws.event.PatientCheckoutEvent;

public class BillManagement {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handler(SNSEvent event) {
        event.getRecords().forEach(snsRecord -> {
            try {
                PatientCheckoutEvent patientCheckoutEvent = objectMapper.readValue(snsRecord.getSNS().getMessage(), PatientCheckoutEvent.class);
                System.out.println(patientCheckoutEvent);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
