package Population;

import Country.Settlement;
import Location.Point;
import Simulation.Clock;
import Virus.IVirus;

public class Sick extends Person{
	private long contagiousTime;
	private IVirus virus;
	public Sick(int age, Point location, Settlement settlement, long contagiousTime, IVirus virus) {
		super(age, location, settlement);
		this.contagiousTime= contagiousTime;
		this.virus= virus;
	}
	public long GetContagiousTime() {return contagiousTime;}
	public long getFromContagionTime(){return Clock.TicksPerDay(contagiousTime); }
	public IVirus GetVirus(){return virus;}
	public String tostring() {
		return super.toString() + "  Contagious Time:" + this.contagiousTime+ " Virus:"+ this.virus;
	}
	public double contagionProbability() {
		throw new RuntimeException("Sick preson never get sick again!");
	}
	public Sick contagion(IVirus virus) {
		throw new RuntimeException("Sick preson never get sick again!");
	}
	public Person recover() {
		Convalescent c= new Convalescent(this.GetAge(), this.Getlocation(),this.GetSettlement(), this.virus);
		return c;
	}
	public boolean tryToDie() {
		if(virus.tryToKill(this)){
			this.GetSettlement().killSucceeded();
			return true;
		}
		return false;
	}
	public String toString(){
		return  "Sick: Age:" + GetAge() ;
	}
}
