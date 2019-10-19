/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.alunos.lostandfound.model.repository;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.edu.utfpr.alunos.lostandfound.model.entity.User;

/**
 *
 * @author ronifabio
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    public UserRepositoryTest() {
    }

    @Before
    public void setUp() {
        User s = new User("Lucas", "123","42090912039", "lukassamuka88@gmail.com");
        userRepository.save(s);
    }

    @After
    public void tearDown() {
    	userRepository.deleteAll();
    }

    @Test
    public void findByUser() {
        User users = userRepository.findByLogin("Lucas");
        assertNotNull(users);
    }
}
