package com.cdjmdev.regex;

import com.cdjmdev.oracle.dao.DAOFactory;
import com.cdjmdev.oracle.dao.OracleDAOFactory;
import com.cdjmdev.oracle.exception.UserLimitedException;
import com.cdjmdev.oracle.model.User;
import com.cdjmdev.regex.chatservice.ChatGPTService;
import com.cdjmdev.regex.chatservice.ChatService;
import com.cdjmdev.regex.chatservice.UserService;
import com.cdjmdev.oracle.exception.AuthtokenExpiredException;
import com.cdjmdev.regex.exception.ChatMessageTooLargeException;
import com.cdjmdev.regex.key.OpenAIKey;
import com.cdjmdev.regex.prompt.ChatGPTPrompt;
import com.cdjmdev.regex.prompt.Prompt;

public class RegexController {

    private ChatService service;
    private DAOFactory factory;
    private UserService userService;

    private String str = "Respond with only a syntactically correct regex that will " +
                         "work in Javascript for the following request. " +
                         "Do not not include any code formatting. " +
                         "If the request is for regex to replace occurrences then " +
                         "just provide the regex to remove and not to replace.";

    public static class Query {
        public String authtoken;
        public String query;
    }

    public static class QueryResult {
        public String regex;
        public int status = 200;
        public String message;
    }

    public RegexController() {
        Prompt prompt = new ChatGPTPrompt(str);
        service = new ChatGPTService(new OpenAIKey(), prompt);
        factory = new OracleDAOFactory();
        userService = new UserService(factory);
    }

    public QueryResult handleRequest(Query query) throws UserLimitedException {

        QueryResult result = new QueryResult();

        try {

            User user = userService.invokeService(query.authtoken);

            result.regex = service.getResponse(user, query.query);
            result.message = "Ok";
        } catch(NullPointerException | AuthtokenExpiredException e) {
            result.regex = null;
            result.status = 401;
            result.message = "User is not logged in";
        } catch(UserLimitedException e) {
            result.regex = null;
            result.status = 423;
            result.message = "User has reached tier service limits. " +
                "Please upgrade account to access to service.";
        } catch (ChatMessageTooLargeException e) {
            result.regex = null;
            result.status = 500;
            result.message = e.getMessage();
        } catch(Exception e) {
            result.regex = null;
            result.status = 500;
            result.message = "Service is unavailable at this time.";
        }

        return result;
    }

}