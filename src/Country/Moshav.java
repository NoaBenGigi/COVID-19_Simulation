package Country;
import java.util.List;
import Location.Location;
import Population.Person;

public class Moshav extends Settlement{

	public Moshav(String name, Location location, List<Person> people) {
		super(name, location, people);
	}
	
	public RamzorColor calculateRamzorGrade() {
		double y= Math.pow(1.2, this.GetRamzorColor().GetValue());
		double x= 0.3 + 3*(Math.pow(y*(this.contagiousPercent()-0.35),5));
		RamzorColor a=RamzorColor.GetColor(x);
	    SetRamzorColor(a);
	    return a;
    }
  
}
