package Country;
import java.awt.Color;
public enum RamzorColor {

	Green (0.4, 1.0, Color.GREEN),
    Yellow (0.6, 0.8, Color.YELLOW),
    Orange (0.8, 0.6, Color.ORANGE),
    Red (1.0, 0.4,Color.RED)
    ;
	private Color color;
	private double value;
	private double statistic;


	RamzorColor(double value, double statistic, Color color) {
		this.value=value;
		this.statistic = statistic;
		this.color= color;
	}
	public double GetStatistic(){return statistic;}
	public double GetValue()
	{
		return value;	
	}
	public static RamzorColor GetColor(double d) {
		if (d<= 0.4) {return Green;}
		else if(d <=0.6) {return Yellow;}
		else if( d<=0.8) {return Orange;}
		else {return Red;}
	}
	public Color GetColor() {
		if (value<= 0.4) {return Color.GREEN;}
		else if(value <=0.6) {return Color.YELLOW;}
		else if( value<=0.8) {return Color.ORANGE;}
		else {return Color.RED;}
	}
	public String toString(RamzorColor rc) {
		if(rc.value == 0.4)
			return "Green";
		else {
			if(rc.value == 0.6)
				return "Yellow";
			else {
				if(rc.value == 0.8)
					return "Orange";
				else {
					return "Red";
				}
			}
		}
	}
}
