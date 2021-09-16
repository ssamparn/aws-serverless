package com.lambda.aws.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientCheckoutEvent {
    public String firstName;
    public String middleName;
    public String lastName;
    public String ssn;
}
