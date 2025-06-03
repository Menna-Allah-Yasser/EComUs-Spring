package org.iti.ecomus.controller.customer;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.iti.ecomus.dto.WishlistDTO;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/wishlist")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Customer - Addresses", description = "Customer address management")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping
    public List<WishlistDTO> getAll(@AuthenticationPrincipal User user) {
        return wishlistService.getAllByUserId(user.getUserId());
    }

    @GetMapping("/{productId}")
    public WishlistDTO getById(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        return wishlistService.getByUserIdProductId(user.getUserId(), productId);
    }

    @PostMapping("/{productId}")
    public WishlistDTO add(@AuthenticationPrincipal User user, @PathVariable Long productId) {

        return wishlistService.add(user.getUserId(), productId);
    }

    @DeleteMapping("/{productId}")
    public void delete(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        wishlistService.delete(user.getUserId(), productId);
    }
    }