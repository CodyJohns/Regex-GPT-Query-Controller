package com.cdjmdev.regex.chatservice;

import com.cdjmdev.oracle.dao.DAOFactory;
import com.cdjmdev.oracle.exception.UserLimitedException;
import com.cdjmdev.oracle.model.Authtoken;
import com.cdjmdev.oracle.model.Tiers;
import com.cdjmdev.oracle.model.User;
import com.cdjmdev.oracle.exception.AuthtokenExpiredException;
import com.cdjmdev.regex.exception.ChatMessageTooLargeException;

public class UserService {

	private final int MAX_FREE_QUERY = 500;
	private DAOFactory factory;

	public UserService(DAOFactory factory) {
		this.factory = factory;
	}

	public User invokeService(String authtoken, int requestSize) throws UserLimitedException, AuthtokenExpiredException, ChatMessageTooLargeException {

		Authtoken token = factory.getAuthtokenDAO().getByID(authtoken);

		if(token.isExpired())
			throw new AuthtokenExpiredException("Authtoken has expired. Please log in again.");

		User user = factory.getUserDAO().getByID(token);

		if(user.tier.equals(Tiers.FREE) && requestSize > MAX_FREE_QUERY)
			throw new ChatMessageTooLargeException("Request is too large. Try using a more concise request or upgrade your account to remove restrictions.");

		user.useService();

		factory.getUserDAO().save(user);

		return user;
	}
}
