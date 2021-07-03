package Population;

import Country.Settlement;
import Location.Point;
import Simulation.Clock;
import Virus.IVirus;
import Virus.VirusManagement;

import java.util.ArrayList;
import java.util.List;

public class Healthy extends Person {
	public Healthy(int age, Point location, Settlement settlement) {
		super(age, location, settlement);
	}
	public Person vaccinate() {
		Vaccinated v = new Vaccinated(this.GetAge(),this.Getlocation(), this.GetSettlement(), Clock.now());
		return v;
	}
	public String toString(){
		return  "Healthy: Age:" + GetAge() ;
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
		System.out.println("makor: " + virus.toString());
		System.out.println("yaad: " + new_virus.toString());
		return new Sick(this.GetAge(),locationCopy,this.GetSettlement(),Clock.now(),new_virus);
	}
}
