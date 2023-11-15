package com.cdjmdev.regex.chatservice;

import com.cdjmdev.oracle.dao.DAOFactory;
import com.cdjmdev.oracle.exception.UserLimitedException;
import com.cdjmdev.oracle.model.Authtoken;
import com.cdjmdev.oracle.model.Tiers;
import com.cdjmdev.oracle.model.User;
import com.cdjmdev.oracle.util.Utilities;
import com.cdjmdev.regex.exception.AuthtokenExpiredException;

public class UserService {

	private DAOFactory factory;

	public UserService(DAOFactory factory) {
		this.factory = factory;
	}

	public void invokeService(String authtoken) throws UserLimitedException, AuthtokenExpiredException {
		Authtoken token = factory.getAuthtokenDAO().getByID(authtoken);

		if(token.isExpired())
			throw new AuthtokenExpiredException("Authtoken has expired. Please log in again.");

		User user = factory.getUserDAO().getByID(token);

		user.useService();

		factory.getUserDAO().save(user);
	}
}
