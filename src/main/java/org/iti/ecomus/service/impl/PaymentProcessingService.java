package org.iti.ecomus.service.impl;

import org.iti.ecomus.entity.User;
import org.iti.ecomus.enums.PayType;
import org.iti.ecomus.exceptions.BadRequestException;
import org.iti.ecomus.exceptions.InsufficientCreditException;
import org.iti.ecomus.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class PaymentProcessingService {

    @Autowired
    private UserRepo userRepo;

    public void processPayment(User user, PayType paymentMethod, BigDecimal totalPrice) {
        switch (paymentMethod) {
            case PayType.CASH:
                processCashPayment(user, totalPrice);
                break;
            case PayType.CREDIT:
            processCreditPayment(user, totalPrice);
                break;
            default:
                throw new BadRequestException("Unsupported payment method: " + paymentMethod);
        }
        // Cash payment doesn't require additional processing
    }

    private void processCashPayment(User user, BigDecimal totalPrice) {
    }

    private void processCreditPayment(User user, BigDecimal totalPrice) {
        BigDecimal creditLimit = user.getCreditLimit();

        if (creditLimit == null) {
            throw new InsufficientCreditException("Please update your profile to set your credit card limit.");
        }

        if (totalPrice.compareTo(creditLimit)== 1) {
            throw new InsufficientCreditException(
                    String.format("Your order total ($%d) exceeds your available credit limit ($%d).",
                            totalPrice, creditLimit));
        }

        user.setCreditLimit(creditLimit.subtract( totalPrice));
        userRepo.save(user);
    }
}