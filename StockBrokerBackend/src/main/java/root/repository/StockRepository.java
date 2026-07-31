package root.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import root.entity.Stock;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByTicker(String ticker);
}
