package com.cdjmdev.regex;

import com.cdjmdev.oracle.dao.DAOFactory;
import com.cdjmdev.oracle.dao.UserDAO;
import com.cdjmdev.oracle.dao.AuthtokenDAO;
import com.cdjmdev.oracle.exception.AuthtokenExpiredException;
import com.cdjmdev.oracle.exception.UserLimitedException;
import com.cdjmdev.oracle.model.Authtoken;
import com.cdjmdev.oracle.model.User;
import com.cdjmdev.oracle.util.Utilities;
import com.cdjmdev.regex.chatservice.UserService;
import com.cdjmdev.regex.exception.ChatMessageTooLargeException;
import com.fnproject.fn.testing.*;
import com.google.gson.Gson;
import org.junit.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

public class RegexControllerTest {

    @Rule
    public final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    @Disabled
    public void shouldReturnRegex() {

        Gson gson = new Gson();
        RegexController.Query query = new RegexController.Query();

        query.authtoken = "c27afdea0c2d14396b48b533ef61e880";
        query.query = "Give me regex to check the validity of a phone number with dashes";

        testing.givenEvent()
            .withBody(gson.toJson(query))
            .enqueue();

        testing.thenRun(RegexController.class, "handleRequest");

        FnResult result = testing.getOnlyResult();
        System.out.println(result.getBodyAsString());
        assertNotNull(result);
    }

    private static DAOFactory mockFactory;
    private static UserDAO uMock;
    private static AuthtokenDAO aMock;

    @Test
    @DisplayName("Test UserService success")
    public void testUserService() {
        aMock = mock(AuthtokenDAO.class);
        uMock = mock(UserDAO.class);

        mockFactory = mock(DAOFactory.class);

        Mockito.when(mockFactory.getUserDAO()).thenReturn(uMock);
        Mockito.when(mockFactory.getAuthtokenDAO()).thenReturn(aMock);

        User user = new User("", "", "", "");
        Authtoken token = new Authtoken();
        token.user_id = user.id;
        token.expires = Utilities.getFutureTimestamp();
        token.id = "";

        Mockito.when(aMock.getByID(Mockito.any())).thenReturn(token);
        Mockito.when(uMock.getByID(Mockito.any())).thenReturn(user);

        UserService service = new UserService(mockFactory);

        assertDoesNotThrow(() -> {
            service.invokeService(token.id, 100);
        });
    }

    @Test
    @DisplayName("Test UserService token expired")
    public void testTokenExpired() {
        aMock = mock(AuthtokenDAO.class);
        uMock = mock(UserDAO.class);

        mockFactory = mock(DAOFactory.class);

        Mockito.when(mockFactory.getUserDAO()).thenReturn(uMock);
        Mockito.when(mockFactory.getAuthtokenDAO()).thenReturn(aMock);

        User user = new User("", "", "", "");
        Authtoken token = new Authtoken();
        token.user_id = user.id;
        token.expires = 0;
        token.id = "";

        Mockito.when(aMock.getByID(Mockito.any())).thenReturn(token);
        Mockito.when(uMock.getByID(Mockito.any())).thenReturn(user);

        UserService service = new UserService(mockFactory);

        assertThrows(AuthtokenExpiredException.class, () -> {
            service.invokeService(token.id, 100);
        });
    }

    @Test
    @DisplayName("Test UserService request too big")
    public void testRequestMassive() {
        aMock = mock(AuthtokenDAO.class);
        uMock = mock(UserDAO.class);

        mockFactory = mock(DAOFactory.class);

        Mockito.when(mockFactory.getUserDAO()).thenReturn(uMock);
        Mockito.when(mockFactory.getAuthtokenDAO()).thenReturn(aMock);

        User user = new User("", "", "", "");
        Authtoken token = new Authtoken();
        token.user_id = user.id;
        token.expires = Utilities.getFutureTimestamp();
        token.id = "";

        Mockito.when(aMock.getByID(Mockito.any())).thenReturn(token);
        Mockito.when(uMock.getByID(Mockito.any())).thenReturn(user);

        UserService service = new UserService(mockFactory);

        assertThrows(ChatMessageTooLargeException.class, () -> {
            service.invokeService(token.id, 501);
        });
    }

    @Test
    @DisplayName("Test UserService user reached service cap")
    public void testUserLimited() {
        aMock = mock(AuthtokenDAO.class);
        uMock = mock(UserDAO.class);

        mockFactory = mock(DAOFactory.class);

        Mockito.when(mockFactory.getUserDAO()).thenReturn(uMock);
        Mockito.when(mockFactory.getAuthtokenDAO()).thenReturn(aMock);

        User user = new User("", "", "", "");
        user.lastUse = Utilities.getCurrentTimestamp();
        user.dailyUses = 10;

        Authtoken token = new Authtoken();
        token.user_id = user.id;
        token.expires = Utilities.getFutureTimestamp();
        token.id = "";

        Mockito.when(aMock.getByID(Mockito.any())).thenReturn(token);
        Mockito.when(uMock.getByID(Mockito.any())).thenReturn(user);

        UserService service = new UserService(mockFactory);

        assertThrows(UserLimitedException.class, () -> {
            service.invokeService(token.id, 100);
        });
    }

    @Test
    @DisplayName("Test UserService user reached service cap but on new day")
    public void testUserLimitedNewDay() {
        aMock = mock(AuthtokenDAO.class);
        uMock = mock(UserDAO.class);

        mockFactory = mock(DAOFactory.class);

        Mockito.when(mockFactory.getUserDAO()).thenReturn(uMock);
        Mockito.when(mockFactory.getAuthtokenDAO()).thenReturn(aMock);

        User user = new User("", "", "", "");
        user.lastUse = 0;
        user.dailyUses = 10;

        Authtoken token = new Authtoken();
        token.user_id = user.id;
        token.expires = Utilities.getFutureTimestamp();
        token.id = "";

        Mockito.when(aMock.getByID(Mockito.any())).thenReturn(token);
        Mockito.when(uMock.getByID(Mockito.any())).thenReturn(user);

        UserService service = new UserService(mockFactory);

        assertDoesNotThrow(() -> {
            service.invokeService(token.id, 100);
        });
    }
}