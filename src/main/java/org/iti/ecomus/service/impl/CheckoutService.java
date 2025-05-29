package org.iti.ecomus.service.impl;

import org.iti.ecomus.dto.CheckOutOrderDTO;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.*;
import org.iti.ecomus.exceptions.CartEmptyException;
import org.iti.ecomus.exceptions.InsufficientInventoryException;
import org.iti.ecomus.repository.*;
import org.iti.ecomus.util.MailMessenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CheckoutService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private InventoryValidationService inventoryValidationService;

    @Autowired
    private PaymentProcessingService paymentProcessingService;

    @Autowired
    private OrderCreationService orderCreationService;

    @Autowired
    private MailMessenger mailMessenger;

    public Order processCheckout(Long userId, CheckOutOrderDTO checkoutRequest) {
        // Validate user
        User user = userRepo.findById(userId).get();

        // Get cart items
        List<Cart> cartItems = user.getCarts();
        if (cartItems == null || cartItems.isEmpty()) {
            throw new CartEmptyException("Cart is empty");
        }

        // Validate inventory
        Map.Entry<Boolean, String> booleanStringEntry = inventoryValidationService.validateInventory(cartItems);
        Boolean inventoryResult =
                booleanStringEntry.getKey();

        if (!inventoryResult) {
            throw new InsufficientInventoryException(booleanStringEntry.getValue());
        }

        // Calculate total price
        BigDecimal totalPrice = calculateTotalPrice(cartItems);

        // Process payment
        paymentProcessingService.processPayment(user, checkoutRequest.getPayType(), totalPrice);

        // Create order
        Order order = orderCreationService.createOrder(user, checkoutRequest.getAddress(),checkoutRequest.getPayType(), totalPrice);

        // update inventory
        UpdateInventory(order, cartItems);

        // Clear cart
        cartRepo.deleteByUserUserId(userId);

        // Send confirmation email
        mailMessenger.successfullyOrderPlaced(user.getUsername(),user.getEmail(),order.getOrderId().toString(),order.getDate().toString());

        return order;
    }

    private BigDecimal calculateTotalPrice(List<Cart> cartItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Cart cart : cartItems) {

            totalPrice = totalPrice.add(cart.getProduct().getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
        }
        return totalPrice;
    }

    private void UpdateInventory(Order order, List<Cart> cartItems) {
        for (Cart cartItem : cartItems) {

            Product product = cartItem.getProduct();

            int finalQuantity = cartItem.getQuantity();

            // Update product inventory
            product.setQuantity(product.getQuantity() - finalQuantity);
            productRepo.save(product);
        }
    }
}