package com.example.shoestore.infrastructure.utils;

import com.example.shoestore.infrastructure.constants.DatePattern;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateReset implements Runnable{

    private int currentNumber;
    private boolean isRunning;

    public DateReset(int currentNumber) {
        this.currentNumber = currentNumber;
        this.isRunning = true;
    }

    public void stop() {
        this.isRunning = false;
    }

    @Override
    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE_RESET);
        LocalDate currentDate = LocalDate.now();
        String currentDateStr = currentDate.format(formatter);

        while (isRunning) {
            String formattedNumber = String.format("%03d", currentNumber);

            String newInvoiceNumber = "HD" + formattedNumber + currentDateStr;
//            System.out.println("New Invoice Number: " + newInvoiceNumber);

            currentNumber++;

            for (int i = 0; i < 10; i++) {
                System.out.println(i);
                try {
                    // Sleep cho đến khi qua ngày mới
                    LocalDate nextDay = currentDate.plusDays(1);
                    LocalDate nextDayStart = nextDay.atStartOfDay().toLocalDate();
                    long sleepTime = nextDayStart.atStartOfDay().toEpochSecond(ZoneOffset.UTC) - System.currentTimeMillis() / 1000;
                    Thread.sleep(sleepTime * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            currentDate = LocalDate.now();
            currentDateStr = currentDate.format(formatter);
        }
    }
}

