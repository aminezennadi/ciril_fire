package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class Launcher {

    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
        System.out.println("The Server is running...");
    }
}