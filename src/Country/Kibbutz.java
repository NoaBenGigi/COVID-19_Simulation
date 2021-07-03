package Country;

import java.util.List;
import Location.Location;
import Population.Person;

public class Kibbutz extends Settlement{
	public Kibbutz(String name, Location location, List<Person> people) { super(name, location, people); }
	
	public RamzorColor calculateRamzorGrade() {
		double y= Math.pow(1.5, this.GetRamzorColor().GetValue());
		double x= 0.45 + Math.pow(y*(this.contagiousPercent()-0.4),3);
		RamzorColor a=RamzorColor.GetColor(x);
	    SetRamzorColor(a);
	    return a;
    }
}
