package sjs.fy.opt.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"sjs.fy.opt.api"})
public class BootStartup {

    public static void main( String[] args ) {
        ApplicationContext context = SpringApplication.run(BootStartup.class, args);
        context.getBean(ApplicationService.class).run();
    }
}
