package com.mentoring.pageobject;

import com.mentoring.model.User;

public interface MainPage {

    MainPage fillLoginForm(String username, String password);

    CharacterSelectionPage login(User user);

    void clickLoginButton();

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
