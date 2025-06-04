package org.iti.ecomus.controller.customer;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.iti.ecomus.config.AppConstants;
import org.iti.ecomus.dto.CartDTO;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.dto.WishlistDTO;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.iti.ecomus.paging.PagingAndSortingParam;
import org.iti.ecomus.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/wishlist")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Customer - Wishlist", description = "Customer wishlist")
@Validated
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

//    @GetMapping
//    public List<WishlistDTO> getAll(@AuthenticationPrincipal User user) {
//        return wishlistService.getAllByUserId(user.getUserId());
//    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<ProductDTO>> getWishItems(@AuthenticationPrincipal User user, @PagingAndSortingParam(
                                                                       model = AppConstants.WISH_MODEL,
                                                                       isUser = true,
                                                                       defaultSortField = "userId"
                                                               ) PagingAndSortingHelper helper,
                                                                  @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNum,
                                                                  @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
        return ResponseEntity.ok(wishlistService.getall(helper, pageNum, pageSize, user.getUserId()));
    }


    @GetMapping(path = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WishlistDTO> getById(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        //return wishlistService.getByUserIdProductId(user.getUserId(), productId);
        WishlistDTO wishlistItem = wishlistService.getByUserIdProductId(user.getUserId(), productId);
        return ResponseEntity.ok(wishlistItem);
    }

    @PostMapping(path = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> add(@AuthenticationPrincipal User user, @PathVariable Long productId) {

//        return wishlistService.add(user.getUserId(), productId);
        ProductDTO product = wishlistService.add(user.getUserId(), productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        wishlistService.delete(user.getUserId(), productId);
        return ResponseEntity.noContent().build();
    }
    }