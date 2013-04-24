package org.design.statical;

public class Emailer {

	public void sendGreetingEmailTo(User user) {
		RealEmailSender.sendEmailTo(user.email,"hello, "+ user.name);
	}

	public void sendByeByeEmailTo(User user) {
		RealEmailSender.sendEmailTo(user.email,"bye");
		
	}

}
