package MyTask;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimerTask;


public class TaskImpl extends TimerTask {
	Reservation rv;
	
	public TaskImpl(Reservation rv) {
		this.rv = rv;
	}

	public void run() {
		
//		rv.start();
		
		int StatusCode=302;
		try {
			StatusCode = rv.login();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(StatusCode==302){
			System.out.println("��½�ɹ�");
		}else{
			System.out.println("��½ʧ��");
		}
		
		while(true){
			Calendar calendar = new GregorianCalendar();
			System.out.println();
			System.out.println(calendar.getTime());
			
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			int Day = calendar.get(Calendar.DAY_OF_MONTH);
			int Month = calendar.get(Calendar.MONTH)+1;
			int Year = calendar.get(Calendar.YEAR);
						
			String param = rv.ParseHtml(Day,Month,Year);
			if(param.isEmpty()){
				int Seat1 = (int)(17+Math.random()*16);
				int Seat2 = (int)(49+Math.random()*51);
				int Seat3 = (int)(117+Math.random()*19);
				int[] Seat = {Seat1,Seat2,Seat3};
				int num = (int)(Math.random()*3);
				
				System.out.println("ԤԼʧ�ܣ���λ�����ѱ�Ԥ��,�������ԤԼ"+Seat[num]+"��");
				if(Seat[num]<100)
					rv.user.setSeat("0"+Seat[num]);
				else
					rv.user.setSeat(Seat[num]+"");
				continue;
			}
			
			StringBuffer result = rv.reserv();
			
			if(result.indexOf("�ɹ�")!=-1){
				System.out.println("��λԤԼ�ɹ������ڹ涨��ʱ����ˢ��ȷ�ϡ�");
				break;
			}
			else{
				System.out.println("ʧ����Ϣ"+result);	
				int Seat1 = (int)(17+Math.random()*16);
				int Seat2 = (int)(49+Math.random()*51);
				int Seat3 = (int)(117+Math.random()*19);
				int[] Seat = {Seat1,Seat2,Seat3};
				int num = (int)(Math.random()*3);
				
				System.out.println("ԤԼʧ�ܣ���λ�����ѱ�Ԥ��,�������ԤԼ"+Seat[num]+"��");
				if(Seat[num]<100)
					rv.user.setSeat("0"+Seat[num]);
				else
					rv.user.setSeat(Seat[num]+"");
			}
		}
		
	}

}
