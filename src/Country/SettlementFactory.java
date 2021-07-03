package Country;

import Location.Location;
import Population.Person;

import java.awt.*;
import java.util.List;

public class SettlementFactory {
    public Settlement getSettlement(String settlementType, String nameOfSettlement, Location location, List<Person> numOfPeople){
        if(settlementType == null){return null;}
        if (settlementType.equalsIgnoreCase("CITY")) { return new City(nameOfSettlement,location,numOfPeople); }
        else if(settlementType.equalsIgnoreCase("MOSHAV")){return new Moshav(nameOfSettlement,location,numOfPeople);}
        else if(settlementType.equalsIgnoreCase("KIBBUTZ")){return new Kibbutz(nameOfSettlement,location,numOfPeople);}
        return null;
    }
}
