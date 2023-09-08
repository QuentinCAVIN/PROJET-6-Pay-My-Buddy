package com.paymybuddy.paymybuddysapp.unittest;

import com.paymybuddy.paymybuddysapp.dto.ReducedUserDto;
import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.mapper.UserMapper;
import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.PersonalBankAccount;
import com.paymybuddy.paymybuddysapp.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserMapperTest {

    private final UserMapper userMapperUnderTest = new UserMapper();

    private User dummy;
    private User ymmud;
    private List<User> dummies;
    private UserDto dummyDto;
    private UserDto ymmudDto;
    private List<UserDto> dummiesDto;

    @BeforeEach
    public void setup() {
        dummy = new User();
        ymmud = new User();

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
        PersonalBankAccount dummyPersonalAccount = new PersonalBankAccount();
        dummyPersonalAccount.setId(5);
        dummyPersonalAccount.setId(1000);
        dummy.setPersonalBankAccount(dummyPersonalAccount);

        ymmud.setId(2);
        ymmud.setEmail("ym@mud");
        ymmud.setPassword("4321");
        ymmud.setFirstName("muD");
        ymmud.setLastName("YM");
        ymmud.setUsersConnexions(null);
        ymmud.setUsersConnected(Arrays.asList(dummy));
        ymmud.setPayMyBuddyBankAccount(new PayMyBuddyBankAccount());
        PayMyBuddyBankAccount ymmudAccount = new PayMyBuddyBankAccount();
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

        ymmudDto = new UserDto();
        ymmudDto.setId(2);
        ymmudDto.setEmail("ym@mud");
        ymmudDto.setPassword("4321");
        ymmudDto.setFirstName("muD");
        ymmudDto.setLastName("YM");
        ymmudDto.setUsersConnexions(null);
        ymmudDto.setPayMyBuddyBankAccount(new PayMyBuddyBankAccount());
        ymmud.setPayMyBuddyBankAccount(ymmudAccount);

        dummiesDto = Arrays.asList(dummyDto, ymmudDto);
    }

    @Test
    public void convertUsertoUserDtoTest() {
        UserDto userDto = userMapperUnderTest.convertUsertoUserDto(dummy);
        Assertions.assertThat(userDto.getId()).isEqualTo(dummy.getId());
        Assertions.assertThat(userDto.getEmail()).isEqualTo(dummy.getEmail());
        Assertions.assertThat(userDto.getFirstName()).isEqualTo(dummy.getFirstName());
        Assertions.assertThat(userDto.getLastName()).isEqualTo(dummy.getLastName());
        Assertions.assertThat(userDto.getPassword()).isEqualTo(dummy.getPassword());
        Assertions.assertThat(userDto.getPayMyBuddyBankAccount()).isEqualTo(dummy.getPayMyBuddyBankAccount());
        Assertions.assertThat(userDto.getPersonalBankAccount()).isEqualTo(dummy.getPersonalBankAccount());
        Assertions.assertThat(userDto.getUsersConnexions() instanceof ReducedUserDto);

        String buddysUsernameReducedUserDto = userDto.getUsersConnexions().get(0).getEmail();
        String dummysBuddysUsername = dummy.getUsersConnexions().get(0).getEmail();
        Assertions.assertThat(buddysUsernameReducedUserDto).isEqualTo(dummysBuddysUsername);
    }

    @Test
    public void convertUserListToUserDtoListTest() {
        List<UserDto> usersDto = userMapperUnderTest.convertUserListToUserDtoList(dummies);

       for (int i = 0; i < 2; i++) {
            Assertions.assertThat(usersDto.get(i).getId()).isEqualTo(dummies.get(i).getId());
            Assertions.assertThat(usersDto.get(i).getEmail()).isEqualTo(dummies.get(i).getEmail());
            Assertions.assertThat(usersDto.get(i).getFirstName()).isEqualTo(dummies.get(i).getFirstName());
            Assertions.assertThat(usersDto.get(i).getLastName()).isEqualTo(dummies.get(i).getLastName());
            Assertions.assertThat(usersDto.get(i).getPassword()).isEqualTo(dummies.get(i).getPassword());
            Assertions.assertThat(usersDto.get(i).getPayMyBuddyBankAccount())
                    .isEqualTo(dummies.get(i).getPayMyBuddyBankAccount());
           Assertions.assertThat(usersDto.get(i).getPersonalBankAccount())
                   .isEqualTo(dummies.get(i).getPersonalBankAccount());
           Assertions.assertThat(usersDto.get(i).getUsersConnexions() instanceof ReducedUserDto);

           String buddysUsernameReducedUserDto = usersDto.get(0).getUsersConnexions().get(0).getEmail();
           String dummysBuddysUsername = dummy.getUsersConnexions().get(0).getEmail();
           Assertions.assertThat(buddysUsernameReducedUserDto).isEqualTo(dummysBuddysUsername);
        }
    }

    @Test
    public void convertUserDtotoUserTest() {
        User user = userMapperUnderTest.convertUserDtotoUser(dummyDto);
        Assertions.assertThat(user.getId()).isEqualTo(dummyDto.getId());
        Assertions.assertThat(user.getEmail()).isEqualTo(dummyDto.getEmail());
        Assertions.assertThat(user.getFirstName()).isEqualTo(dummyDto.getFirstName());
        Assertions.assertThat(user.getLastName()).isEqualTo(dummyDto.getLastName());
        Assertions.assertThat(user.getPassword()).isEqualTo(dummyDto.getPassword());
        Assertions.assertThat(user.getPayMyBuddyBankAccount()).isEqualTo(dummyDto.getPayMyBuddyBankAccount());
        Assertions.assertThat(user.getPersonalBankAccount()).isEqualTo(dummyDto.getPersonalBankAccount());
    }
}


