package com.paymybuddy.paymybuddysapp.unittest;

import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.repository.UserRepository;
import com.paymybuddy.paymybuddysapp.service.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    private static User dummy;
    private static User ymmud;
    private static List<User> dummies;
    private static UserDto dummyDto;


    /*User DTO
    *     private List<User> usersConnexions = new ArrayList<>();

    private PayMyBuddyBankAccount payMyBuddyBankAccount;*/

    /*User
     * private PayMyBuddyBankAccount payMyBuddyBankAccount;*/
    @BeforeEach
    public void setup() {
        dummy = new User();
        dummy.setId(1);
        dummy.setEmail("dum@my");
        dummy.setPassword("1234");
        dummy.setFirstName("Dum");
        dummy.setLastName("MY");
        dummy.setUsersConnexions(Arrays.asList(ymmud));
        dummy.setUsersConnected(null);
        PayMyBuddyBankAccount dummyAccount = new PayMyBuddyBankAccount();
        dummyAccount.setId(1);
        dummyAccount.setAccountBalance(1234);
        dummy.setPayMyBuddyBankAccount(dummyAccount);

        ymmud = new User();
        ymmud.setId(2);
        ymmud.setEmail("ym@mud");
        ymmud.setPassword("4321");
        ymmud.setFirstName("muD");
        ymmud.setLastName("YM");
        dummy.setUsersConnexions(null);
        dummy.setUsersConnected(Arrays.asList(dummy));
        ymmud.setPayMyBuddyBankAccount(new PayMyBuddyBankAccount());
        PayMyBuddyBankAccount ymmudAccount = new PayMyBuddyBankAccount();
        ymmudAccount.setId(2);
        ymmudAccount.setAccountBalance(4321);
        dummy.setPayMyBuddyBankAccount(ymmudAccount);

        dummies = Arrays.asList(dummy, ymmud);

        dummyDto = new UserDto();
        dummyDto.setId(1);
        dummyDto.setEmail("dum@my");
        dummyDto.setPassword("1234");
        dummyDto.setFirstName("Dum");
        dummyDto.setLastName("MY");
        dummyDto.setPayMyBuddyBankAccount(dummyAccount);
        dummyDto.setUsersConnexions(new ArrayList<>());
    }


    @Test
    public void getUserByEmailTest() {
        Mockito.when(userRepository.findByEmail(dummy.getEmail())).thenReturn(dummy);

        User user = userService.getUserByEmail(dummy.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(dummy.getEmail());
        Assertions.assertThat(user.getId()).isEqualTo(dummy.getId());
        Assertions.assertThat(user.getEmail()).isEqualTo(dummy.getEmail());
        Assertions.assertThat(user.getPassword()).isEqualTo(dummy.getPassword());
        Assertions.assertThat(user.getFirstName()).isEqualTo(dummy.getFirstName());
        Assertions.assertThat(user.getLastName()).isEqualTo(dummy.getLastName());
        Assertions.assertThat(user.getPayMyBuddyBankAccount().getAccountBalance())
                .isEqualTo(dummy.getPayMyBuddyBankAccount().getAccountBalance());
        Assertions.assertThat(user.getUsersConnexions()).isEqualTo(dummy.getUsersConnexions());
        Assertions.assertThat((user.getUsersConnected())).isEqualTo(dummy.getUsersConnected());
    }

    @Test
    public void getUserDtoByEmailTest() {
        Mockito.when(userRepository.findByEmail(dummy.getEmail())).thenReturn(dummy);

        UserDto userDto = userService.getUserDtoByEmail(dummy.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(dummy.getEmail());
        Assertions.assertThat(userDto.getId()).isEqualTo(dummy.getId());
        Assertions.assertThat(userDto.getEmail()).isEqualTo(dummy.getEmail());
        Assertions.assertThat(userDto.getPassword()).isEqualTo(dummy.getPassword());
        Assertions.assertThat(userDto.getFirstName()).isEqualTo(dummy.getFirstName());
        Assertions.assertThat(userDto.getLastName()).isEqualTo(dummy.getLastName());
        Assertions.assertThat(userDto.getPayMyBuddyBankAccount().getAccountBalance())
                .isEqualTo(dummy.getPayMyBuddyBankAccount().getAccountBalance());
        Assertions.assertThat(userDto.getUsersConnexions()).isEqualTo(dummy.getUsersConnexions());
    }

    @Test
    public void getUsersDtoTest() {
        Mockito.when(userRepository.findAll()).thenReturn(dummies);


        List<UserDto> usersDto = userService.getUsersDto();
        UserDto userDto1 = usersDto.get(0);
        UserDto userDto2 = usersDto.get(1);


        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        Assertions.assertThat(usersDto).hasSize(2);

        Assertions.assertThat(userDto1.getId()).isEqualTo(dummy.getId());
        Assertions.assertThat(userDto1.getEmail()).isEqualTo(dummy.getEmail());
        Assertions.assertThat(userDto1.getPassword()).isEqualTo(dummy.getPassword());
        Assertions.assertThat(userDto1.getFirstName()).isEqualTo(dummy.getFirstName());
        Assertions.assertThat(userDto1.getLastName()).isEqualTo(dummy.getLastName());
        Assertions.assertThat(userDto1.getPayMyBuddyBankAccount().getAccountBalance())
                .isEqualTo(dummy.getPayMyBuddyBankAccount().getAccountBalance());
        Assertions.assertThat(userDto1.getUsersConnexions()).isEqualTo(dummy.getUsersConnexions());


        Assertions.assertThat(userDto2.getId()).isEqualTo(ymmud.getId());
        Assertions.assertThat(userDto2.getEmail()).isEqualTo(ymmud.getEmail());
        Assertions.assertThat(userDto2.getPassword()).isEqualTo(ymmud.getPassword());
        Assertions.assertThat(userDto2.getFirstName()).isEqualTo(ymmud.getFirstName());
        Assertions.assertThat(userDto2.getLastName()).isEqualTo(ymmud.getLastName());
        Assertions.assertThat(userDto2.getPayMyBuddyBankAccount().getAccountBalance())
                .isEqualTo(ymmud.getPayMyBuddyBankAccount().getAccountBalance());
        Assertions.assertThat(userDto2.getUsersConnexions()).isEqualTo(ymmud.getUsersConnexions());
    }

    @Test
    public void saveUserTest() {

        Mockito.when(passwordEncoder.encode(dummyDto.getPassword())).thenReturn("encodedPassword");
        userService.createNewUser(dummyDto);

        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(dummyDto.getPassword());
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }


    @Test
    public void deleteUserTest() {

        userService.deleteUser(1);

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1);
    }
}