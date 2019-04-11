package com.mentoring.pageobject;

public interface MainPage {

    MainPage fillRegistrationForm(String username, String password, String emailAddress);

    MainPage fillRegistrationForm(String username, String password, String confirmPassword, String emailAddress);

    void enterRegistrationUsername(String username);

    void enterRegistrationPassword(String password);

    void enterRegistrationConfirmPassword(String confirmPassword);

    void enterRegistrationEmailAddress(String emailAddress);

    boolean isUserNameInvalid();

    boolean isPasswordInvalid();

    boolean isConfirmPasswordInvalid();

    boolean isEmailAddressInvalid();

    boolean isSubmitButtonEnabled();

    CharacterSelectionPage submitRegistration();
}
