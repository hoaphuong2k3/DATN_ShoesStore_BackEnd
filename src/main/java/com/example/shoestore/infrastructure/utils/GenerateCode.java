package com.example.shoestore.infrastructure.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GenerateCode {
    private static final int MAX_NUMBER = 999999;
    private static final int MIN_NUMBER = 100000;
    private static int currentInvoiceNumber = 1;
    private static String lastGeneratedDate = "";
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    public static String code(String code) {
        Random random = new Random();
        int numberRandom = random.nextInt(MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER;
        return code + numberRandom;
    }

    public static int code() {
        Random random = new Random();
        return random.nextInt(MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER;
    }


    public static String generateCodeInvoice() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String currentDate = dateFormat.format(date);

        if (!currentDate.equals(lastGeneratedDate)) {
            lastGeneratedDate = currentDate;
            currentInvoiceNumber = 1;
        }

        String invoiceNumber = "HĐ" + currentDate + currentInvoiceNumber;

        currentInvoiceNumber++;

        return invoiceNumber;
    }

    public static String RandomStringGenerator() {

        int length = 10;

        Random random = new Random();

        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);

            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }


}
