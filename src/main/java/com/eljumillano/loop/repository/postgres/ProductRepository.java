package com.eljumillano.loop.repository.postgres;
import org.springframework.data.jpa.repository.JpaRepository;
import com.eljumillano.loop.model.Product;
import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByOrderAsc();
}
