package org.iti.ecomus.util;

import org.iti.ecomus.annotation.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;


@IntegrationTest
class MailMessengerTest {

    @Autowired
    private MailMessenger mailMessenger;

//    @Test
//    void successfullyRegister() {
//        mailMessenger.successfullyRegister("uasd","blabla7423@gmail.com");
//    }
}