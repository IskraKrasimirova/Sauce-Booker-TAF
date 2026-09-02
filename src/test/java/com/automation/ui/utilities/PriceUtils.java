package com.automation.ui.utilities;

import java.math.BigDecimal;

public final class PriceUtils {
    private PriceUtils() {}

    public static BigDecimal parsePrice(String price) {
        String number = price.replaceAll("[^0-9.]", "");
        return new BigDecimal(number);
    }
}
