package root.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import root.entity.Stock;
import root.entity.User;
import root.repository.StockRepository;
import root.repository.UserRepository;
import root.util.JwtUtil;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Get all available stocks
    @GetMapping("/all")
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // ✅ Subscribe user to a stock
    @PostMapping("/subscribe/{ticker}")
    public Map<String, Object> subscribe(
            @RequestHeader("Authorization") String token,
            @PathVariable String ticker) {

        Map<String, Object> response = new HashMap<>();

        String email = jwtUtil.extractEmail(token.substring(7)); // remove 'Bearer '
        User user = userRepository.findByEmail(email).orElseThrow();

        Stock stock = stockRepository.findByTicker(ticker.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        user.getSubscribedStocks().add(stock);
        userRepository.save(user);

        response.put("message", "Subscribed to " + ticker);
        response.put("subscriptions", user.getSubscribedStocks());
        return response;
    }

    // ✅ Unsubscribe user from a stock
    @DeleteMapping("/unsubscribe/{ticker}")
    public Map<String, Object> unsubscribe(
            @RequestHeader("Authorization") String token,
            @PathVariable String ticker) {

        Map<String, Object> response = new HashMap<>();

        String email = jwtUtil.extractEmail(token.substring(7));
        User user = userRepository.findByEmail(email).orElseThrow();

        Stock stock = stockRepository.findByTicker(ticker.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        user.getSubscribedStocks().remove(stock);
        userRepository.save(user);

        response.put("message", "Unsubscribed from " + ticker);
        response.put("subscriptions", user.getSubscribedStocks());
        return response;
    }

    // ✅ Get user’s subscribed stocks
    @GetMapping("/my")
    public Set<Stock> getMyStocks(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        User user = userRepository.findByEmail(email).orElseThrow();
        return user.getSubscribedStocks();
    }
}
