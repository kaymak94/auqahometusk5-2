package ru.netology.apibank.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.apibank.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.apibank.data.DataGenerator.Registration.getUser;
import static ru.netology.apibank.data.DataGenerator.getRandomLogin;
import static ru.netology.apibank.data.DataGenerator.getRandomPassword;

public class AuthTest {
    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should login if registered user")
    void shouldLoginIfRegisteredUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $x("//span[@class='button__text']").click();
        $("[class='heading heading_size_l heading_theme_alfa-on-white']").shouldHave(Condition.exactText("Личный кабинет"),
                Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should show msg about problem if login not registered  user")
    void shouldShowMsgAboutProblemIfLoginNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $x("//span[@class='button__text']").click();
        $("[class='notification__content']").shouldHave(Condition.exactText(("Ошибка! Неверно указан логин или пароль")),
                Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should show msg about problem if login registered user with wrong password")
    void shouldShowMsgAboutProblemIfLoginRegisteredUserWithWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $x("//span[@class='button__text']").click();
        $("[class='notification__content']").shouldHave(Condition.exactText(("Ошибка! Неверно указан логин или пароль")),
                Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should show msg about problem if login registered user with wrong login")
    void shouldShowMsgAboutProblemIfLoginRegisteredUserWithWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $x("//span[@class='button__text']").click();
        $("[class='notification__content']").shouldHave(Condition.exactText(("Ошибка! Неверно указан логин или пароль")),
                Duration.ofSeconds(15));
    }


    @Test
    @DisplayName("Should show msg about problem if login blocked user")
    void shouldShowMsgAboutProblemIfLoginBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $x("//span[@class='button__text']").click();
        $("[class='notification__content']").shouldHave(Condition.exactText(("Ошибка! Пользователь заблокирован")),
                Duration.ofSeconds(15));
    }
}
