package root.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Stock> getSubscribedStocks() {
		return subscribedStocks;
	}

	public void setSubscribedStocks(Set<Stock> subscribedStocks) {
		this.subscribedStocks = subscribedStocks;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // hashed password

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_stocks",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "stock_id")
    )
    private Set<Stock> subscribedStocks = new HashSet<>();
}
