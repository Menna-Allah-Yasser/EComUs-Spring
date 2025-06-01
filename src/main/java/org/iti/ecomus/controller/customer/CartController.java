package org.iti.ecomus.controller.customer;

import java.util.List;

import org.iti.ecomus.dto.CartDTO;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Validated
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartDTO>> getCartItems(@AuthenticationPrincipal User user) {
        List<CartDTO> items = cartService.getCartItemsByUserId(user.getUserId());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<CartDTO> getCartItem(@AuthenticationPrincipal User user,
                                               @PathVariable Long productId) {
        CartDTO cartItem = cartService.getCartItem(user.getUserId(), productId);
        return ResponseEntity.ok(cartItem);
    }

    @PostMapping
    public ResponseEntity<Void> addOrUpdateCartItem(@AuthenticationPrincipal User user,
                                                    @Valid @RequestBody CartDTO cartDTO) {
        cartService.addOrUpdateCartItem(user.getUserId(),
                cartDTO.getProduct().getProductId(),
                cartDTO.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> removeOrUpdateCartItem(@AuthenticationPrincipal User user,
                                                       @Valid @RequestBody CartDTO cartDTO) {
        cartService.removeOrUpdateCartItem(user.getUserId(),
                cartDTO.getProduct().getProductId(),
                cartDTO.getQuantity());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeCartItem(@AuthenticationPrincipal User user,
                                               @PathVariable Long productId) {
        cartService.removeCartItem(user.getUserId(), productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total-quantity")
    public ResponseEntity<Integer> getTotalQuantity(@AuthenticationPrincipal User user) {
        Integer totalQuantity = cartService.getTotalQuantity(user.getUserId());
        return ResponseEntity.ok(totalQuantity != null ? totalQuantity : 0);
    }

    @GetMapping("/total-price")
    public ResponseEntity<Integer> getTotalPrice(@AuthenticationPrincipal User user) {
        Integer totalPrice = cartService.getTotalPrice(user.getUserId());
        return ResponseEntity.ok(totalPrice != null ? totalPrice : 0);
    }

    @GetMapping("/product-total/{productId}")
   public ResponseEntity<Integer> getProductTotalPrice(@AuthenticationPrincipal User user,
                                                    @PathVariable Long productId) {
    Integer total = cartService.getProductTotalPrice(user.getUserId(), productId);
    return ResponseEntity.ok(total != null ? total : 0);
}

}
