package com.lambda.aws;

import com.amazonaws.services.lambda.runtime.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataTypes {

    private Double instanceVariable = Math.random();
    private static Double staticVariable = Math.random();

    static {
        System.out.println("Static Block executed");
    }

    public DataTypes() {
        System.out.println("Inside Constructor");
    }

    public void coldstartBasics() throws InterruptedException {
//        Thread.sleep(5000);
        Double localVariable = Math.random();
        System.out.println("Instance: " + instanceVariable + " Static Variable: " + staticVariable + " localVariable: " + localVariable);
    }

    public boolean getNumber(float number) {
        return number > 100;
    }

    public List<Integer> getScores(List<String> names) {

        Map<String, Integer> studentScores = new HashMap<>();
        studentScores.put("John", 90);
        studentScores.put("Bob", 80);
        studentScores.put("Ahmed", 100);

        List<Integer> matchingScores = new ArrayList<>();

        names.forEach(name -> {
            matchingScores.add(studentScores.get(name));
        });

        return matchingScores;
    }

    public void saveEmployeeDetails(Map<String, Integer> employee) {
        System.out.println(employee);
    }

    public Map<String, List<Integer>> getStudentScores() {
        Map<String, List<Integer>> studentScores = new HashMap<String, List<Integer>>();
        studentScores.put("John", Arrays.asList(80, 90, 100));
        studentScores.put("Bob", Arrays.asList(80, 70, 90));
        studentScores.put("Doug", Arrays.asList(80, 90, 20));

        return studentScores;
    }

    public ClinicalData getClinicalData(Patient patient) {
        System.out.println(patient.getName());
        System.out.println(patient.getSsn());
        ClinicalData clinicalData = new ClinicalData();
        clinicalData.setBp("80/120");
        clinicalData.setHeartRate("80");
        return clinicalData;
    }

    public void getOutput(InputStream input, OutputStream output, Context context) throws IOException {
        System.out.println(System.getenv("restapiurl"));
        System.out.println(context.getAwsRequestId());
        System.out.println(context.getFunctionName());
        System.out.println(context.getRemainingTimeInMillis());
        System.out.println(context.getMemoryLimitInMB());
        System.out.println(context.getLogGroupName());

        int data;
        while ((data = input.read()) != -1) {
            output.write(Character.toLowerCase(data));
        }
    }

}
