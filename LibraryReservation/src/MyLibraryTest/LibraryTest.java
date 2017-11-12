package MyLibraryTest;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

import Domain.User;
import MyTask.Reservation;

public class LibraryTest {
	@Test
	public void fun() throws ClientProtocolException, IOException{
		User user = new User();
		user.setUsername("PlsXoz4zuVseGEBR0ZvcJw==");
		user.setPassword("PlsXoz4zuVseGEBR0ZvcJw==");
		user.setFloor("03");
		user.setSeat("026");
		Reservation rv = new Reservation(user);
		rv.login();
		rv.ParseHtml(12, 11, 2017);
		rv.reserv();
	}
}
