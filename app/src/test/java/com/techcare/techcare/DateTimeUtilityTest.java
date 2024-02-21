package com.techcare.techcare;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeUtilityTest {
    @Test
    public void testGetCurrentDateTimeNotNull() {
        String dateTime = DateTimeUtility.getCurrentDateTime();
        assertNotNull(dateTime);
    }

    @Test
    public void testGetCurrentDateTimeConsistency() {
        String dateTime1 = DateTimeUtility.getCurrentDateTime();
        String dateTime2 = DateTimeUtility.getCurrentDateTime();
        assertEquals("Date times are inconsistent", dateTime1, dateTime2);
    }
}