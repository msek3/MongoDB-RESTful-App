package Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"web", "data"})
@EnableMongoRepositories(basePackages = "data.repository")
public class App {
    public static void main(String[] args){
        SpringApplication.run(App.class);
    }
}
