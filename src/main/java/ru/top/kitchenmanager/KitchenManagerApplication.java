package ru.top.kitchenmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KitchenManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KitchenManagerApplication.class, args);
        System.out.printf("http://localhost:8080/client/menu — меню для клиента\n" +
                "\n" +
                "http://localhost:8080/login — страница входа");
    }
}
