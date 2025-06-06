package org.iti.ecomus.controller.customer;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.iti.ecomus.config.AppConstants;
import org.iti.ecomus.dto.CartDTO;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.dto.ShoppingCartDTO;
import org.iti.ecomus.dto.UserDTO;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.iti.ecomus.paging.PagingAndSortingParam;
import org.iti.ecomus.service.CartService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/cart")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Customer - Cart", description = "Shopping cart operations")
public class CartController {

    private final CartService cartService;

//    @GetMapping
//    public ResponseEntity<List<CartDTO>> getCartItems(@AuthenticationPrincipal User user) {
//        List<CartDTO> items = cartService.getCartItemsByUserId(user.getUserId());
//        return ResponseEntity.ok(items);
//    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<CartDTO>> getCartItems(@AuthenticationPrincipal User user,@PagingAndSortingParam(
                                                                      model = AppConstants.CART_MODEL,
                                                                      isUser = true,
                                                                      defaultSortField = "userId"
                                                              ) PagingAndSortingHelper helper,
                                                              @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNum,
                                                              @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
        return ResponseEntity.ok(cartService.getall(helper, pageNum, pageSize, user.getUserId()));
    }


    @GetMapping(path = "/{productId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartDTO> getCartItem(@AuthenticationPrincipal User user,
                                               @PathVariable @Min(1) Long productId) {
        CartDTO cartItem = cartService.getCartItem(user.getUserId(), productId);
        return ResponseEntity.ok(cartItem);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartDTO> addOrUpdateCartItem(@AuthenticationPrincipal User user,
                                                    @Valid @RequestBody ShoppingCartDTO cartDTO) {
        CartDTO cartDTO1 = cartService.addOrUpdateCartItem(user.getUserId(),
                cartDTO.getProductId(),
                cartDTO.getQuantity());
        return ResponseEntity.ok(cartDTO1);
    }

    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartDTO> removeOrUpdateCartItem(@AuthenticationPrincipal User user,
                                                       @Valid @RequestBody ShoppingCartDTO cartDTO) {
        CartDTO cartDTO1 = cartService.removeOrUpdateCartItem(user.getUserId(),
                cartDTO.getProductId(),
                cartDTO.getQuantity());
        return ResponseEntity.ok(cartDTO1);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeCartItem(@AuthenticationPrincipal User user,
                                               @PathVariable @Min(1) Long productId) {
        cartService.removeCartItem(user.getUserId(), productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/total-quantity",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getTotalQuantity(@AuthenticationPrincipal User user) {
        Integer totalQuantity = cartService.getTotalQuantity(user.getUserId());
        return ResponseEntity.ok(totalQuantity != null ? totalQuantity : 0);
    }

    @GetMapping(path = "/total-price",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getTotalPrice(@AuthenticationPrincipal User user) {
        Integer totalPrice = cartService.getTotalPrice(user.getUserId());
        return ResponseEntity.ok(totalPrice != null ? totalPrice : 0);
    }

    @GetMapping(value = "/product-total/{productId}",produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Integer> getProductTotalPrice(@AuthenticationPrincipal User user,
                                                    @PathVariable Long productId) {
    Integer total = cartService.getProductTotalPrice(user.getUserId(), productId);
    return ResponseEntity.ok(total != null ? total : 0);
}

}
