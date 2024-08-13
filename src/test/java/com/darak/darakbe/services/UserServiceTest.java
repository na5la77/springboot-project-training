//package com.darak.darakbe.services;
//
//import com.darak.darakbe.entities.User;
//import com.darak.darakbe.repos.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//class UserServiceTest {
//
//    @InjectMocks
//    private UserService userService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    public UserServiceTest() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createUserTest() {
//        User user = new User();
//        user.setName("Test User");
//        when(userRepository.save(user)).thenReturn(user);
//        User createdUser = userService.createUser(user);
//        assertThat(createdUser.getName()).isEqualTo("Test User");
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    void getUserByIdTest() {
//        User user = new User();
//        user.setId(1L);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        Optional<User> retrievedUser = userService.getUserById(1L);
//        assertThat(retrievedUser).isPresent();
//        assertThat(retrievedUser.get().getId()).isEqualTo(1L);
//        verify(userRepository, times(1)).findById(1L);
//    }
//}
