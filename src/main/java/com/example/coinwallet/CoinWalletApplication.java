package com.example.coinwallet;

import org.springframework.boot.SpringApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoinWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoinWalletApplication.class, args);
    }

}
