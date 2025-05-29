package org.iti.ecomus.service.impl;

import org.iti.ecomus.entity.Cart;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InventoryValidationService {

    @Autowired
    private ProductRepo productRepo;

    public Map.Entry<Boolean,String> validateInventory(List<Cart> cartItems) {
        Boolean allInventoryAvailable = true;
        StringBuilder inventoryMessage = new StringBuilder();

        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getQuantity() < cartItem.getQuantity()) {
                allInventoryAvailable = false;
                inventoryMessage.append("Product ").append(product.getProductName())
                        .append(" has only ").append(product.getQuantity())
                        .append(" items available, but ").append(cartItem.getQuantity())
                        .append(" were requested. ");
            }
        }

        return Map.entry(allInventoryAvailable, inventoryMessage.toString());
    }
}