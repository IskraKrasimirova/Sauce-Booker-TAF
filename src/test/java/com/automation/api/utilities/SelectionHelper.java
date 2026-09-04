package com.automation.api.utilities;

import java.util.Random;

public class SelectionHelper {
    private SelectionHelper() {
    }

    public static int getRandomIndex(int totalItems) {
        return new Random().nextInt(totalItems);
    }
}
