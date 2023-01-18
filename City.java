/**
 *	City data - the city name, state name, location designation,
 *				and population est. 2017
 *
 *	@author	Sudhanva Deshpande
 *	@since	January 9, 2023
 */
public class City implements Comparable<City> {
	
	// fields
	private String name;
	private String state;
	private String designation;
	private int population;
	
	// constructor
	public City()
	{
		name = "";
		state = "";
		designation = "";
		population = 0;
	}
		
	
	/**	Compare two cities populations
	 *	@param other		the other City to compare
	 *	@return				the following value:
	 *		If populations are different, then returns (this.population - other.population)
	 *		else if states are different, then returns (this.state - other.state)
	 *		else returns (this.name - other.name)
	 */
	
	public int compareTo(City other) 
	{
		if(this.getPopulation() != other.getPopulation())
			return this.getPopulation() - other.getPopulation();
		else if(!this.getStateName().equals(other.getStateName()))
			return this.getStateName().compareTo(other.getStateName());
		else
			return this.getCityName().compareTo(other.getCityName());
			
	}
	 
	/**	Equal city name and state name
	 *	@param other		the other City to compare
	 *	@return				true if city name and state name equal; false otherwise
	 */
	
	public boolean equals(City other)
	{
		if(this.getCityName().equals(other.getCityName()))	
					
			return true;
		else
			return false;
	}
	
	/**	Accessor methods */
	public void setCityName(String city) { name = city; }	
	public void setStateName(String stateName) { state = stateName; }
	public void setCityType(String type) { designation = type; }
	public void setPopulation(int pop) { population = pop; }
	
	public String getCityName() { return name; }
	public String getStateName() { return state; }
	public String getCityType() { return designation; }
	public int getPopulation() { return population; }
	
	/**	toString */
	@Override
	public String toString() {
		return String.format("%-22s %-22s %-12s %,12d", state, name, designation,
						population);
	}
}
