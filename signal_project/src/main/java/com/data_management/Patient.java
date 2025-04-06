package com.data_management;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private int patientId;
    private List<PatientRecord> patientRecords;

    public Patient(int patientId) {
        this.patientId = patientId;
        this.patientRecords = new ArrayList<>();
    }

    public void addRecord(double measurementValue, String recordType, long timestamp) {
        PatientRecord record = new PatientRecord(this.patientId, measurementValue, recordType, timestamp);
        this.patientRecords.add(record);
    }

    public List<PatientRecord> getRecords(long startTime, long endTime) {
        List<PatientRecord> matchingRecords = new ArrayList<>();
        
        // Iterate through each record and check the timestamp
        for (PatientRecord record : this.patientRecords) {
            if (record.getTimestamp() >= startTime && record.getTimestamp() <= endTime) {
                matchingRecords.add(record); // Add to the list if it matches the time range
            }
        }
        
        return matchingRecords; // Return the list of matching records
    }
}
