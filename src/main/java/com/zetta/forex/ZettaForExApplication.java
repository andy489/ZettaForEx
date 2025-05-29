package com.zetta.forex;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ZettaForExApplication implements CommandLineRunner {

    public static void main(String[] args)  {

        SpringApplication.run(ZettaForExApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
