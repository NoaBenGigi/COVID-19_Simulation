package IO;
import java.io.*;
import java.util.Random;
import Country.*;
import Location.*;
import Population.Person;
import Population.Healthy;
import Simulation.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class SimulationFile {
	private Map map;
	public SimulationFile(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {// If the file doesn't exists a FileNotFoundException is thrown
			String st = br.readLine(); // read the first line in the file
			String words[] = null;
			int numOfPeopleInSettlement = 0;//this variable save the amount of people in every settlement
			List<Settlement> settlemnetList = new ArrayList<Settlement>();//empty list of settlements
			Settlement settlementRef = null;
			Settlement settlementRef2 = null;
			SettlementFactory sf= new SettlementFactory();
			Thread st_tread;
			System.out.println("~~~~~~~~~~~" + file.getName() + "~~~~~~~~~~~");
			while (st != null) {
				words = st.split(";"); //the words is separating by ";"
				for (int i = 0; i < words.length; i++)
					words[i] = words[i].trim();

				if (words[0].equals("#")) {
					st = br.readLine();
					continue;
				}
				List<Person> people = new ArrayList<Person>();
				Point p = new Point(Integer.parseInt(words[2]), Integer.parseInt(words[3]));
				Size size = new Size(Integer.parseInt(words[4]), Integer.parseInt(words[5]));
				Location loc = new Location(p, size);
				numOfPeopleInSettlement = Integer.parseInt(words[6]);
				settlementRef = sf.getSettlement(words[0],words[1],loc,people);
				settlemnetList.add(settlementRef);
				assert settlementRef != null;
				for (int j = 0; j < numOfPeopleInSettlement; j++)
					settlementRef.addPerson(new Healthy(randomAge(), settlementRef.randomLocation(), settlementRef));
				st = br.readLine(); // read new line
			}
			this.map = new Map(settlemnetList);
			BufferedReader br1= new BufferedReader(new FileReader(file));
			st=br1.readLine();
			while (st != null){
				words = st.split(";"); //the words is separating by ";"
				for (int i = 0; i < words.length; i++)
					words[i] = words[i].trim();
				if (words[0].equals("#")){
					if((map.SettlementByname(words[1]) )&& (map.SettlementByname(words[2]))){//if two of the settlemnts are found
						int index1= map.SettlementByIndex(words[1]);
						int index2= map.SettlementByIndex(words[2]);
						settlementRef = map.at(index1);
						settlementRef2= map.at(index2);
						settlementRef.SetNeighbors(settlementRef2 );
						settlementRef2.SetNeighbors(settlementRef);
					}
				}
				st=br1.readLine();
			}
		} catch (IOException | RuntimeException e) {
			System.err.println(e.getMessage());
		}

	}
	public Map GetMap(){return this.map;}
	public static int randomAge(){
		double y =  Math.random()*4;
		Random x = new Random();
		double pseudorand_val=x.nextGaussian();
		while (pseudorand_val <= -1 || pseudorand_val>=1){
			pseudorand_val = x.nextGaussian();
		}
		return (int)((5*((pseudorand_val*6)+9))+y);
	}
}//SimulationFile