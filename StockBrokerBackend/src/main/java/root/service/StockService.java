package root.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import root.entity.Stock;
import root.repository.StockRepository;

import java.util.*;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final List<String> supportedTickers = Arrays.asList("GOOG", "TSLA", "AMZN", "META", "NVDA");
    private final Random random = new Random();

    @PostConstruct
    public void initStocks() {
        for (String ticker : supportedTickers) {
            stockRepository.findByTicker(ticker).orElseGet(() -> {
                Stock s = new Stock();
                s.setTicker(ticker);
                s.setPrice(1000 + random.nextDouble() * 1000);
                return stockRepository.save(s);
            });
        }
    }

    // Update every 1 second
    @Scheduled(fixedRate = 1000)
    public void updateStockPrices() {
        List<Stock> allStocks = stockRepository.findAll();
        for (Stock stock : allStocks) {
            double newPrice = stock.getPrice() + (random.nextDouble() * 20 - 10); // random ±10 change
            stock.setPrice(Math.max(newPrice, 0));
            stockRepository.save(stock);
        }
        messagingTemplate.convertAndSend("/topic/stocks", allStocks);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
}
