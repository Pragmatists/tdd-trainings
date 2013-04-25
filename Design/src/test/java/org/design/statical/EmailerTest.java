package org.design.statical;

import org.junit.Test;


/**
 * TODO:
 * Twoim celem jest napisanie testu na komponent wysyłający mejle systemowe
 * Uruchom testy 
 *    - zaobserwuj że wywołanie prawdziwego kodu spowoduje błąd
 *    - zaobserwuje że testy nie mają asercji
 *  Zrefaktoryzuj kod tak, aby dało się go przetestować
 *  Napisz asercje i zaimplementuj w pełni komponent Emailer
 *
 */
public class EmailerTest {

	@Test
	public void shouldSendHelloEmail() throws Exception {
		
		new Emailer().sendGreetingEmailTo(aUser("email@gmail.com", "User"));
		
		//TODO: verify email was sent to email@gmail.com with subject "hello, User"
		//sth like
//		Assertions.assertThat(getMessageTo("email@gmail.com",)).isEqualTo("hello, User");
	}
	
	@Test
	public void shouldSendUnsubscribeEmailAndNotifyAdminAboutUserLeaving() throws Exception {
		
		new Emailer().sendByeByeEmailTo(aUser("email@gmail.com","User"));
		
		//TODO: verify email was sent to email@gmail.com with subject "bye, User"
		//TODO: verify email was sent to admin@gmail.com with subject "user User left the system"
	}

	private User aUser(String email, String name) {
		return new User(email,name);
	}
	
}
