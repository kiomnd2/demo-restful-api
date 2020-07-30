package com.kiomnd2.demorestapi.accounts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {


    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    public void findByUsername() {
        //Given
        String password = "qwer1234";
        String email = "kiomnd2@naver.com";
        Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN)).build();

        Account save = this.accountRepository.save(account);


        //When
        UserDetailsService userDetailsService = (UserDetailsService) accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername("kiomnd2");

        // then
        assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat(userDetails.getUsername()).isEqualTo(email);
    }

}
