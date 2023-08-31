package com.paymybuddy.paymybuddysapp.unittest;

import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.PersonalBankAccount;
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


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    private static User dummy;
    private static PayMyBuddyBankAccount dummyAccount;
    private static PayMyBuddyBankAccount ymmudAccount;
    private static PersonalBankAccount dummyPersonnalAccount;
    private static User ymmud;
    private static List<User> dummies;
    private static UserDto dummyDto;


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
        dummyAccount = new PayMyBuddyBankAccount();
        dummyAccount.setId(1);
        dummyAccount.setAccountBalance(1234);
        dummy.setPayMyBuddyBankAccount(dummyAccount);
        dummyPersonnalAccount = new PersonalBankAccount();
        dummyPersonnalAccount.setId(5);
        dummyPersonnalAccount.setIban("123456789");
        dummyPersonnalAccount.setAccountBalance(12345);
        dummy.setPersonalBankAccount(dummyPersonnalAccount);

        ymmud = new User();
        ymmud.setId(2);
        ymmud.setEmail("ym@mud");
        ymmud.setPassword("4321");
        ymmud.setFirstName("muD");
        ymmud.setLastName("YM");
        ymmud.setUsersConnexions(null);
        ymmud.setUsersConnected(Arrays.asList(dummy));
        ymmud.setPayMyBuddyBankAccount(new PayMyBuddyBankAccount());
        ymmudAccount = new PayMyBuddyBankAccount();
        ymmudAccount.setId(2);
        ymmudAccount.setAccountBalance(4321);
        ymmud.setPayMyBuddyBankAccount(ymmudAccount);

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
        Assertions.assertThat(dummy.getPersonalBankAccount()).isEqualTo(dummyPersonnalAccount);
    }

    @Test
    public void saveUserTest() {

        userService.saveUser(dummy);

        Mockito.verify(userRepository, Mockito.times(1)).save(dummy);
    }

    @Test
    public void deleteUserTest() {

        userService.deleteUser(1);

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    public void createNewUserTest() {
        String passwordNotEncoded = dummy.getPassword();
        String passwordEncoded = "encodedPassword";

        Mockito.when(passwordEncoder.encode(dummy.getPassword())).thenReturn(passwordEncoded);

        userService.createNewUser(dummy);

        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(passwordNotEncoded);
        Mockito.verify(userRepository, Mockito.times(1)).save(dummy);
        Assertions.assertThat(dummy.getPassword()).isEqualTo(passwordEncoded);
    }

    @Test
    public void getUsersTest() {
        Mockito.when(userRepository.findAll()).thenReturn(dummies);

        List<User> users = userService.getUsers();

        Assertions.assertThat(users).isEqualTo(dummies);
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getUserByPayMyBuddyBankAccountTest(){

        Mockito.when(userRepository.findByPayMyBuddyBankAccount(dummyAccount)).thenReturn(dummy);

        User user = userService.getUserByBankAccount(dummyAccount);

        Mockito.verify(userRepository, Mockito.times(1))
                .findByPayMyBuddyBankAccount(dummyAccount);
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
    public void getUserByBankAccountTestWithPayMyBuddyBankAccount() {
        Mockito.when(userRepository.findByPayMyBuddyBankAccount(dummyAccount)).thenReturn(dummy);

        User user = userService.getUserByBankAccount(dummyAccount);

        Mockito.verify(userRepository,Mockito.times(1))
                .findByPayMyBuddyBankAccount(dummyAccount);
        Assertions.assertThat(user).isEqualTo(dummy);
    }

    @Test
    public void getUserByBankAccountTestWithPersonalBankAccount() {
        Mockito.when(userRepository.findByPersonalBankAccount(dummyPersonnalAccount)).thenReturn(dummy);

        User user = userService.getUserByBankAccount(dummyPersonnalAccount);
        Mockito.verify(userRepository,Mockito.times(1))
                .findByPersonalBankAccount(dummyPersonnalAccount);
        Assertions.assertThat(user).isEqualTo(dummy);
    }

    //TODO: ATTENTION le test ci-dessous a été déplacé, le set up est a revoir, récupérer des éléments de
    // UserServiceTest pour gagner du temps.
    /*@Test
    public void getAllTransferSentByUserTest () {
        Transfer transferSend1 = new Transfer();
        transferSend1.setId(1);
        transferSend1.setAmount(69);
        transferSend1.setSenderAccount(dummyAccount);
        transferSend1.setRecipientAccount(ymmudAccount);
        transferSend1.setDescription("for my ymmud");
        transferSend1.setDate("30/08/2023");

        PersonalBankAccount personalBankAccountOfdummy = new PersonalBankAccount();
        personalBankAccountOfdummy.setId(5);
        personalBankAccountOfdummy.setAccountBalance(1000);
        personalBankAccountOfdummy.setIban("11111111");

        Transfer transferSend2 = new Transfer();
        transferSend1.setId(1);
        transferSend1.setAmount(96);
        transferSend1.setSenderAccount(personalBankAccountOfdummy);
        transferSend1.setRecipientAccount(ymmudAccount);
        transferSend1.setDescription("for my ymmud");
        transferSend1.setDate("30/08/2023");

        dummyAccount.addSentTransfer(transferSend1);


        userService.getAllTransfersSentByUser(dummy);
    }*/
}