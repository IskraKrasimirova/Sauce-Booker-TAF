package com.automation.api.builders;

import com.automation.api.models.Booking;
import com.automation.api.models.BookingDates;
import net.datafaker.Faker;

import java.time.LocalDate;

public class BookingBuilder {
    private static final Faker faker = new Faker();

    private String firstName = faker.name().firstName();
    private String lastName = faker.name().lastName();
    private int totalPrice = faker.number().numberBetween(50, 500);
    private boolean depositPaid = true;
    private String checkin = LocalDate.now().plusDays(5).toString();
    private String checkout = LocalDate.now().plusDays(10).toString();
    private String additionalNeeds = faker.options().option(
            "Breakfast",
            "Late check-in",
            "Airport transfer",
            "Extra bed",
            "Quiet room",
            "No additional needs"
    );

    public BookingBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public BookingBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public BookingBuilder withTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public BookingBuilder withDepositPaid(boolean depositPaid) {
        this.depositPaid = depositPaid;
        return this;
    }

    public BookingBuilder withCheckin(String checkin) {
        this.checkin = checkin;
        return this;
    }

    public BookingBuilder withCheckout(String checkout) {
        this.checkout = checkout;
        return this;
    }

    public BookingBuilder withAdditionalNeeds(String additionalNeeds) {
        this.additionalNeeds = additionalNeeds;
        return this;
    }

    public Booking build() {
        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin(checkin);
        bookingDates.setCheckout(checkout);

        Booking booking = new Booking();
        booking.setFirstname(firstName);
        booking.setLastname(lastName);
        booking.setTotalprice(totalPrice);
        booking.setDepositpaid(depositPaid);
        booking.setBookingdates(bookingDates);
        booking.setAdditionalneeds(additionalNeeds);

        return booking;
    }
}
