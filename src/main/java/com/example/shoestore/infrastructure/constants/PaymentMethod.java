package com.example.shoestore.infrastructure.constants;

public enum PaymentMethod {
    COD(1),
    VNPAY(2),
    BANK_TRANSFER(3),
    CASH_PAYMENT(4);


    private final int value;

    PaymentMethod(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static String getDescription(int value) {
        switch (value) {
            case 1:
                return "Thanh toán sau khi nhận hàng";
            case 2:
                return "Thanh toán qua ví điện tử VNPay";
            case 3:
                return "Thanh toán chuyển khoản";
            case 4:
                return "Thanh toán tiền mặt";
            default:
                return "";
        }
    }
}
