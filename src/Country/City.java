package Country;

import java.util.List;

import Location.Location;
import Population.Person;

public class City extends Settlement{

	public City(String name, Location location, List<Person> people) { super(name, location, people); }
	
	public RamzorColor calculateRamzorGrade() {
		double x= 0.2*( Math.pow(4, (this.contagiousPercent())*1.25));
		RamzorColor a=RamzorColor.GetColor(x);
	    SetRamzorColor(a);
	    return a;
    }
   

}
