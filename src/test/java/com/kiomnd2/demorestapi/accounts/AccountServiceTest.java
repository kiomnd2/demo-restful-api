package com.kiomnd2.demorestapi.accounts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {



    @Test
    public void findByUsername() {
        //Given
        String password = "qwer1234";
        String email = "kiomnd2@naver.com";
    }

    @Test
    public void findByUsernameFail() {
        String username = "kiomnd2";
    }


}
