package com.in.mzncvmc;

import com.in.mzncvmc.content.company.CompanyRepository;
import com.in.mzncvmc.content.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableScheduling
@SpringBootApplication
public class MzncvmcApplication implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(MzncvmcApplication.class, args);
    }


    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("START SERVER!!!");
    }
}
