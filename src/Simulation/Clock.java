package Simulation;
//import java.util.Scanner;

public class Clock {
	private static long current_time;
	private static int ticks_per_day=1;
	public Clock(){
		current_time=0;
	}

	public static int GetTicksPerDay(){return ticks_per_day;}
	public static void SetTicksPerDays(int tick_number){
		ticks_per_day= tick_number;
	}
	public static long now(){
		//Scanner time= new Scanner(System.in);
		//System.out.println("Please enter the current_time: ");
		//current_time= time.nextLong();
		//time.close();
		return current_time;
	}
	public static void nextTick() {
		current_time++;
	}
	public static long TicksPerDay(long start_time){//start_time is previous time. when person got sick
		long tick = Clock.now()-start_time;
		return (long)Math.ceil((current_time-start_time)/ticks_per_day);
	}
	public static void resetClock(){current_time = 0;}
}
