package ru.alexandr.BookingCinemaTickets.testUtils.asserts;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import ru.alexandr.BookingCinemaTickets.controller.web.enums.ToastType;
import ru.alexandr.BookingCinemaTickets.testUtils.ui.fragment.ToastFragment;

public class ToastAssert extends AbstractAssert<ToastAssert, ToastFragment> {
    protected ToastAssert(ToastFragment toastFragment) {
        super(toastFragment, ToastAssert.class);
    }

    public static ToastAssert assertThat(ToastFragment toastFragment) {
        return new ToastAssert(toastFragment);
    }

    public ToastAssert isVisible() {
        Assertions.assertThat(actual.isVisible()).isTrue();
        return this;
    }

    public ToastAssert isNotVisible() {
        Assertions.assertThat(actual.isVisible()).isFalse();
        return this;
    }

    public ToastAssert hasMessageText(String text) {
        Assertions.assertThat(actual.getMessage()).isEqualTo(text);
        return this;
    }

    public ToastAssert hasType(ToastType type) {
        Assertions.assertThat(actual.getType()).isEqualTo(type);
        return this;
    }

}
