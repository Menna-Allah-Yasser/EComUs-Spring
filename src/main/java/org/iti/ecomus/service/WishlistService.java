package org.iti.ecomus.service;

import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.dto.WishlistDTO;
import org.iti.ecomus.paging.PagingAndSortingHelper;

import java.util.List;

public interface WishlistService {
    List<WishlistDTO> getAllByUserId(Long userId);
    WishlistDTO getByUserIdProductId(Long userId, Long productId);
    ProductDTO add(Long userId, Long productId);
    void delete(Long userId, Long productId);

    PagedResponse<ProductDTO> getall(PagingAndSortingHelper helper, int pageNum, int pageSize, Long userId);
}