package com.automation.ui.factories;

import com.automation.ui.models.CheckoutInfo;
import net.datafaker.Faker;

public class CheckoutInfoFactory {
    private static final Faker faker = new Faker();

    private CheckoutInfoFactory() {
    }

    public static CheckoutInfo createValidCheckoutInfo() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String zipCode = faker.address().zipCode();

        return new CheckoutInfo(firstName, lastName, zipCode);
    }

    public static CheckoutInfo createWithMissingField(String field) {
        CheckoutInfo checkoutInfo = createValidCheckoutInfo();

        switch (field) {
            case "firstName":
                checkoutInfo.setFirstName("");
                break;
            case "lastName":
                checkoutInfo.setLastName("");
                break;
            case "zipCode":
                checkoutInfo.setPostalCode("");
                break;
            case "all":
                checkoutInfo.setFirstName("");
                checkoutInfo.setLastName("");
                checkoutInfo.setPostalCode("");
                break;
            default:
                throw new IllegalArgumentException("Unsupported checkout field: " + field);
        }

        return checkoutInfo;
    }
}
