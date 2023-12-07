package com.francisconicolau.pruebainditex.domain.repository;


import com.francisconicolau.pruebainditex.domain.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, Integer>, JpaSpecificationExecutor<Price> {

/*    @Query(
            value= "SELECT new com.francisconicolau.pruebainditex.application.dto.PricesDTO from Prices where :date BETWEEN start_date AND end_date " +
            "AND product_id = :productId AND brand_id = :brandId order by priority DESC LIMIT 1")
    List<PricesDTO> findByParameters(LocalDateTime date, Integer productId, int brandId);*/

    Optional<List<Price>> findByProductId(int productId);
}
