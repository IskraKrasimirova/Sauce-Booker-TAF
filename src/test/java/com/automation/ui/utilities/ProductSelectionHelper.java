package com.automation.ui.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class ProductSelectionHelper {
    private ProductSelectionHelper() {
    }

    public static int getRandomProductIndex(int totalProducts) {
        return new Random().nextInt(totalProducts);
    }

    public static List<Integer> getRandomProductIndices(int totalProducts, int numberOfIndices) {
        if (numberOfIndices < 1 || numberOfIndices > totalProducts) {
            throw new IllegalArgumentException("Number of indices must be between 1 and total products.");
        }

        List<Integer> indices = new ArrayList<Integer>();

        for (int i = 0; i < totalProducts; i++) {
            indices.add(i);
        }

        Collections.shuffle(indices);
        return indices.subList(0, numberOfIndices);
    }
}
