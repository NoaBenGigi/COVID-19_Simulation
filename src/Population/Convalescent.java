package Population;
import Country.Settlement;
import Location.Point;
import Simulation.Clock;
import Virus.IVirus;
import Virus.VirusManagement;

import java.util.ArrayList;
import java.util.List;

public class Convalescent extends Person{
	private IVirus virus;

	public Convalescent(int age, Point location, Settlement settlement, IVirus virus){
		super(age, location, settlement);
		this.virus = virus;
	}
	public IVirus GetVirus() {return virus;}
	public String tostring() {
		return super.toString() + " Virus:"+ this.virus;
	}
	public double contagionProbability(){return 0.2;}
	public String toString(){
		
		return  "Convalescent: Age:" + GetAge() ;
		
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
		return new Sick(this.GetAge(),locationCopy,this.GetSettlement(), Clock.now(),new_virus);
	}

}
