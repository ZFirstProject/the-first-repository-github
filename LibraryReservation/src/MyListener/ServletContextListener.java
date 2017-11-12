package MyListener;

import java.util.GregorianCalendar;
import java.util.Timer;

import javax.servlet.ServletContextEvent;

import Domain.User;
import MyTask.Reservation;
import MyTask.TaskImpl;

public class ServletContextListener implements
		javax.servlet.ServletContextListener {

	Timer timer;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		timer.cancel();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		User user = new User();
		user.setUsername("PlsXoz4zuVseGEBR0ZvcJw==");
		user.setPassword("PlsXoz4zuVseGEBR0ZvcJw==");
		user.setFloor("05");
		user.setSeat("065");
		
		timer = new Timer();
		Reservation rv = new Reservation(user);	
		timer.schedule(new TaskImpl(rv), new GregorianCalendar(2017, 9, 20, 21, 01, 1).getTime(),1000*3600*24);
		System.out.println("ÒÑÆô¶¯");
	}

}
