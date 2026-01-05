package com.in.mzncvmc;

import com.in.mzncvmc.content.company.Company;
import com.in.mzncvmc.content.company.CompanyRepository;
import com.in.mzncvmc.content.users.Users;
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
        // 테스트용 사용자 생성 (개발 환경에서만)
        if (!usersRepository.existsByUsername("manager")) {

            Company company = companyRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("Company not found"));


            Users admin = new Users();
            admin.setUsername("manager");
            admin.setCompanyId(company);
            admin.setFullName("김나나");
            admin.setEmail("hong@gmail.com");
            admin.setPhone("010-1234-5678");
            admin.setRole(Users.Role.valueOf("ADMIN"));
            admin.setStatus(Users.Status.valueOf("ACTIVE"));
            admin.setPassword(passwordEncoder.encode("1212"));

            usersRepository.save(admin);
            System.out.println("테스트 관리자 계정 생성: admin / 1212");
        }

        if (!usersRepository.existsByUsername("user")) {
            Company company = companyRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("Company not found"));


            Users user = new Users();
            user.setUsername("user");
            user.setCompanyId(company);
            user.setFullName("박유져");
            user.setEmail("park@gmail.com");
            user.setPhone("010-7777-8888");
            user.setRole(Users.Role.valueOf("USER"));
            user.setStatus(Users.Status.valueOf("ACTIVE"));
            user.setPassword(passwordEncoder.encode("1212"));

            usersRepository.save(user);
            System.out.println("테스트 사용자 계정 생성: user / user123");
        }
    }
}
