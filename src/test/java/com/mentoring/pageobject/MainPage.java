package com.mentoring.pageobject;

public interface MainPage {

    MainPage fillRegistrationForm(String username, String password, String emailAddress);

    CharacterSelectionPage submitRegistration();
}
