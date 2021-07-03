package Population;

import Country.Settlement;
import Location.Point;
import Simulation.Clock;
import Virus.IVirus;
import Virus.VirusManagement;

import java.util.ArrayList;
import java.util.List;

public class Vaccinated extends Person{
	private long vaccinationTime;
	
	public Vaccinated(int age, Point location, Settlement settlement, Long vaccinationTime) {
		super(age, location, settlement);
		this.vaccinationTime= vaccinationTime;

	}
	public long GetVaccinationTime(){return vaccinationTime;}
	public String tostring() {
		return super.toString() + " Vaccination Time:" + this.vaccinationTime;
	}
	public double contagionProbability() {
		long t= Clock.now() - this.vaccinationTime; 
		if(t<21) {
			double p = 0.56+ (0.15* Math.sqrt(21-t));
			return Math.min(1, p);
		}
		else {
			double k = 1.05 / (t-14);
			return Math.min(0.05, k);
		}
	}
	public String toString(){
		return  "Vaccinated: Age:" + GetAge() ;		
	}
	public Sick contagion(IVirus virus) {
		/*List<IVirus> mutations=new ArrayList<IVirus>();
		mutations.add(virus);
		for (int i =0 ;i<virus.getMutations().length; i++)
			if(virus.getMutations()[i])
				mutations.add(virus.getMutations_types()[i]);
		IVirus new_virus = mutations.get((int)(Math.random()*mutations.size()));*/
		IVirus new_virus = VirusManagement.randomVariantToContagion(virus);
		Point locationCopy = new Point(Getlocation().getX(),Getlocation().getY());
		return new Sick(this.GetAge(),locationCopy,this.GetSettlement(),Clock.now(),new_virus);
	}
	

}
