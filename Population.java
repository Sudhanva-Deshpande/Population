import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *	Population - This program allows the user to navigate the us population database, which contains 
 *  data of most of the states and cities in the US, along with their city types and population. The user
 *  is allowed to pick one of six categories provided to sort the database in a different way. Each
 *  category uses different sorting methods to sort the database.
 *
 *	Requires FileUtils and Prompt classes.
 *
 *	@author	Sudhanva Deshpande
 *	@since	January 9, 2023
 */
public class Population {
	
	// List of cities
	private List<City> cities;
    private boolean quit; //boolean that indicates when the user wants to quit
    private int numMatchingCities; //number of citites that match a city name given by the user
	
	// US data file
	private final String DATA_FILE = "usPopData2017.txt";
	private final int NUM_CITIES = 50; //number of cities to be printed from the database

    public Population()
    {
        cities = new ArrayList<City>();  
        quit = false;
        numMatchingCities = 0;
    }
    
	public static void main(String[] args)
	{
		Population pop = new Population();
		pop.run();
	}
	
    /** 
     *  Calls all necessary methods. Uses a while loop to get user input until user decides to quit.
     */
	public void run()
	{
		printIntroduction();
        readAndLoadData();
		printMenu();
        while(!quit)
        {
            getInput();        
        }
		System.out.println("\nThank you for using Population!");
	}
	/**	Prints the introduction to Population */
	public void printIntroduction() {
		System.out.println("   ___                  _       _   _");
		System.out.println("  / _ \\___  _ __  _   _| | __ _| |_(_) ___  _ __ ");
		System.out.println(" / /_)/ _ \\| '_ \\| | | | |/ _` | __| |/ _ \\| '_ \\ ");
		System.out.println("/ ___/ (_) | |_) | |_| | | (_| | |_| | (_) | | | |");
		System.out.println("\\/    \\___/| .__/ \\__,_|_|\\__,_|\\__|_|\\___/|_| |_|");
		System.out.println("           |_|");
		System.out.println();
	}
	
	/**	Print out the choices for population sorting */
	public void printMenu() {
		System.out.println("1. Fifty least populous cities in USA (Selection Sort)");
		System.out.println("2. Fifty most populous cities in USA (Merge Sort)");
		System.out.println("3. First fifty cities sorted by name (Insertion Sort)");
		System.out.println("4. Last fifty cities sorted by name descending (Merge Sort)");
		System.out.println("5. Fifty most populous cities in named state");
		System.out.println("6. All cities matching a name sorted by population");
		System.out.println("9. Quit");
	}
	
    /**
     *  This method uses the FileUtil class to open the data file and reads and saves the contents in
     *  the cities list. Uses string methods to split the line in parts, before saving it in the list.
     */
	public void readAndLoadData()
	{
		Scanner inFile = FileUtils.openToRead(DATA_FILE);
        String line = "";
		int len = 0;
        int count = 0;
		
		while(inFile.hasNext())
		{
			line = inFile.nextLine();
			cities.add(new City());
            len = cities.size()-1;

            cities.get(len).setStateName(line.substring(0,line.indexOf("\t")));
            line = line.substring(line.indexOf("\t")+1);

            cities.get(len).setCityName(line.substring(0,line.indexOf("\t")));
            line = line.substring(line.indexOf("\t")+1);

            cities.get(len).setCityType(line.substring(0,line.indexOf("\t")));
            line = line.substring(line.indexOf("\t")+1);

            cities.get(len).setPopulation(Integer.parseInt(line.trim()));
            count++;
		}
        System.out.println("\n"+count+" cities in database");
	}

    /**
     *  This method gets user input for the category selection using the Prompt class. Handles bad input.
     *  Then uses switch statement to call appropriate method based on category inputted.
     */
    public void getInput()
    {
        int category = Prompt.getInt("Enter selection");
        while(category!=9 && (category<1 || category>6))
        {
            System.out.println("Invalid input. Try again");
            category = Prompt.getInt("Enter selection");
        }
        switch(category)
        {
            case 1:
                leastPop();
				break;
            case 2:
                mostPop(false);
				break;
            case 3:
                nameSortIncreasing();
				break;
            case 4:
                nameSortDecrease();
				break;
            case 5:
                mostPopInState();
				break;
            case 6:
                citiesMatchingName();
				break;
            case 9:
                quit = true;
        }
    }

    /**
     *  Method to sort database by least population. Uses the selection sort method to sort list.
     *  Prints data and the time taken to sort the database.
     */
    public void leastPop()
    {
        System.out.println("\n\nFifty least populus cities");

        long startMilliSec = System.currentTimeMillis();

        for(int outer = cities.size(); outer>1; outer--)
        {
            int innerMax = 0;
            for(int inner = 1; inner < outer; inner++)
            {
                if(cities.get(inner).compareTo(cities.get(innerMax)) > 0)
                    innerMax = inner;
            }
            
			swap(innerMax, outer-1);
        }

        long endMilliSec = System.currentTimeMillis();
        printData();

        System.out.println("Elapsed time "+(endMilliSec-startMilliSec)+" milliseconds");
    }

    /**
     *  Method to sort database by greatest population. Uses the merge sort method to sort list.
     *  Calls the method to sort, prints data and the time taken to sort the database.
     *  Before printing, checks to make sure that the method wasn't called from 
     *  another method(method other than getInput()).
     *
     *  @param fromOther   indicates whether mostPop was called from another method
     */
    public void mostPop(boolean fromOther)
    {
        System.out.println("\n\nFifty most populus cities");

        int len = cities.size();
        Integer[] temp = new Integer[len];
        String[] city = new String[len];
        String[] state = new String[len];
        String[] type = new String[len];

        long startMilliSec = System.currentTimeMillis();
        sort(temp, city, state, type, 0, len-1);
        long endMilliSec = System.currentTimeMillis();

        if(!fromOther)
        {
            printData();
            System.out.println("Elapsed time "+(endMilliSec-startMilliSec)+" milliseconds");
        }
    }

    /**
     *  Recursive method to sort the list. Compares the elements at the end and start positions of the list.
     *  Then decides whether the list needs to be swaped, or sorted and merged again.
     *
     *  Uses the start, middle, and end indices to determine the positions to merge the smaller arrays.
     *
     *  @param temp     temp array that stores population(later merged with other temp arrays)
     *  @param city     temp array that stores city names(later merged with other temp arrays)
     *  @param state    temp array that stores state names(later merged with other temp arrays)
     *  @param type     temp array that stores city type(later merged with other temp arrays)
     *  @param start    position to start merge
     *  @param middle   middle position -> between start and end
     *  @param end      position to end merge
     */
    public void sort(Integer[] temp, String[] city, String[] state, String[] type, int start, int end)
    {
        if(end - start < 2)
		{
			if(end > start && cities.get(end).compareTo(cities.get(start)) > 0)
			{
                swap(start, end);
            }
        }
        else
		{
			int middle = (start+end)/2;
			sort(temp, city, state, type, start, middle);
			sort(temp, city, state, type, middle+1, end);
			merge(temp, city, state, type, start, middle, end);
		}
    }

    /**
     *  Uses the start, middle, and end indices to determine the positions to merge the smaller arrays.
     *
     *  @param temp     temp array that stores population(later merged with other temp arrays)
     *  @param city     temp array that stores city names(later merged with other temp arrays)
     *  @param state    temp array that stores state names(later merged with other temp arrays)
     *  @param type     temp array that stores city type(later merged with other temp arrays)
     *  @param start    position to start merge
     *  @param middle   middle position -> between start and end
     *  @param end      position to end merge
     */
    public void merge(Integer[] temp, String[] city, String[] state, String[] type, int start, int middle, int end)
	{
		int i = start;
		int j = middle+1;
		int k = start;
		
		while(i<=middle && j<=end)
		{
			if(cities.get(i).compareTo(cities.get(j)) > 0)
			{
				temp[k] = cities.get(i).getPopulation();
                city[k] = cities.get(i).getCityName();
                state[k] = cities.get(i).getStateName();
                type[k] = cities.get(i).getCityType();

				i++;
			}
			else
			{
				temp[k] = cities.get(j).getPopulation();
                city[k] = cities.get(j).getCityName();
                state[k] = cities.get(j).getStateName();
                type[k] = cities.get(j).getCityType();

				j++;
			}
			k++;
		}
		
		while(i<=middle)
		{
			temp[k] = cities.get(i).getPopulation();
            city[k] = cities.get(i).getCityName();
            state[k] = cities.get(i).getStateName();
            type[k] = cities.get(i).getCityType();

			i++;
			k++;
		}
		while(j<=end)
		{
			temp[k] = cities.get(j).getPopulation();
            city[k] = cities.get(j).getCityName();
            state[k] = cities.get(j).getStateName();
            type[k] = cities.get(j).getCityType();

			j++;
			k++;
		}
		
		for(k=start; k<=end; k++)
		{
            cities.get(k).setPopulation(temp[k]);
            cities.get(k).setCityName(city[k]);
            cities.get(k).setStateName(state[k]);
            cities.get(k).setCityType(type[k]);
		}
	}

    /**
     *  Method sorts the database's cities in increasing name order(A-Z). Uses the insertion sort method to sort.
     *  Prints the data and time taken for the sort.
     */
    public void nameSortIncreasing()
    {
        System.out.println("\n\nFifty cities sorted by name");

        long startMilliSec = System.currentTimeMillis();
        for(int outer = 1; outer<cities.size(); outer++)
        {
            String tempCity = cities.get(outer).getCityName();
            String tempState = cities.get(outer).getStateName();
            String tempType = cities.get(outer).getCityType();
            int tempPop = cities.get(outer).getPopulation();

            int count = outer;

            while(count>0 && tempCity.compareTo(cities.get(count-1).getCityName()) < 0)
            {
                cities.get(count).setCityName(cities.get(count-1).getCityName());
                cities.get(count).setStateName(cities.get(count-1).getStateName());
                cities.get(count).setCityType(cities.get(count-1).getCityType());
                cities.get(count).setPopulation(cities.get(count-1).getPopulation());

                count--;
            }

            cities.get(count).setCityName(tempCity);
            cities.get(count).setStateName(tempState);
            cities.get(count).setCityType(tempType);
            cities.get(count).setPopulation(tempPop);
        }

        long endMilliSec = System.currentTimeMillis();

        printData();
        System.out.println("Elapsed time "+(endMilliSec-startMilliSec)+" milliseconds");
    }

    /**
     *  Sorts the database's cities in decreasing name order(Z-A). Uses the merge sort method to sort.
     *  calls the sortArr method to sort and merge the list, before printing the database and time taken 
     *  for the sort.
     */
    public void nameSortDecrease()
    {
        System.out.println("\n\nFifty cities sorted by name descending");

        int len = cities.size();
        Integer[] temp = new Integer[len];
        String[] city = new String[len];
        String[] state = new String[len];
        String[] type = new String[len];

        long startMilliSec = System.currentTimeMillis();
        sortArr(temp, city, state, type, 0, len-1);
        long endMilliSec = System.currentTimeMillis();

        printData();
        System.out.println("Elapsed time "+(endMilliSec-startMilliSec)+" milliseconds");
    }

    /**
     *  Recursive method to sort the list. Compares the elements at the end and start positions of the list.
     *  Then decides whether the list needs to be swaped, or sorted and merged again.
     *
     *  @param temp     temp array that stores population(later merged with other temp arrays)
     *  @param city     temp array that stores city names(later merged with other temp arrays)
     *  @param state    temp array that stores state names(later merged with other temp arrays)
     *  @param type     temp array that stores city type(later merged with other temp arrays)
     *  @param start    position to start merge
     *  @param middle   middle position -> between start and end
     *  @param end      position to end merge
     */
    public void sortArr(Integer[] temp, String[] city, String[] state, String[] type, int start, int end)
    {
        if(end - start < 2)
		{
			if(end > start && cities.get(end).getCityName().compareTo(cities.get(start).getCityName()) > 0)
			{
                swap(start, end);
            }
        }
        else
		{
			int middle = (start+end)/2;
			sortArr(temp, city, state, type, start, middle);
			sortArr(temp, city, state, type, middle+1, end);
			mergeArr(temp, city, state, type, start, middle, end);
		}
    }

    /**
     *  Uses the start, middle, and end indices to determine the positions to merge the smaller arrays.
     *
     *  @param temp     temp array that stores population(later merged with other temp arrays)
     *  @param city     temp array that stores city names(later merged with other temp arrays)
     *  @param state    temp array that stores state names(later merged with other temp arrays)
     *  @param type     temp array that stores city type(later merged with other temp arrays)
     *  @param start    position to start merge
     *  @param middle   middle position -> between start and end
     *  @param end      position to end merge
     */
    public void mergeArr(Integer[] temp, String[] city, String[] state, String[] type, int start, int middle, int end)
	{
		int i = start;
		int j = middle+1;
		int k = start;
		
		while(i<=middle && j<=end)
		{
			if(cities.get(i).getCityName().compareTo(cities.get(j).getCityName()) > 0)
			{
				temp[k] = (cities.get(i).getPopulation());
                city[k] = cities.get(i).getCityName();
                state[k] = cities.get(i).getStateName();
                type[k] = cities.get(i).getCityType();

				i++;
			}
			else
			{
				temp[k] = cities.get(j).getPopulation();
                city[k] = cities.get(j).getCityName();
                state[k] = cities.get(j).getStateName();
                type[k] = cities.get(j).getCityType();

				j++;
			}
			k++;
		}
		
		while(i<=middle)
		{
			temp[k] = cities.get(i).getPopulation();
            city[k] = cities.get(i).getCityName();
            state[k] = cities.get(i).getStateName();
            type[k] = cities.get(i).getCityType();

			i++;
			k++;
		}
		while(j<=end)
		{
			temp[k] = cities.get(j).getPopulation();
            city[k] = cities.get(j).getCityName();
            state[k] = cities.get(j).getStateName();
            type[k] = cities.get(j).getCityType();

			j++;
			k++;
		}
		
		for(k=start; k<=end; k++)
		{
            cities.get(k).setPopulation(temp[k]);
            cities.get(k).setCityName(city[k]);
            cities.get(k).setStateName(state[k]);
            cities.get(k).setCityType(type[k]);
		}
	}

    /**
     *  Calls a method that gets user input for the state they want to sort by, and gets a string returned
     *  with that state. Then, calls the mostPop method to sort the cities database by most population,
     *  and calls the method to print the database.
     */
    public void mostPopInState()
    {
        String state = getStateData();

        System.out.println("\n\nFifty most populus cities in "+state);

        mostPop(true);
        printStateData(state);
    }

    /**
     *  Calls a method that gets user input for the city they want to sort by, and gets a string returned
     *  with that city. Then, calls the mostPop method to sort the cities database by most population,
     *  and calls the method to print the database.
     */
    public void citiesMatchingName()
    {
        String city = getCityData();

        System.out.println("\n\nCity "+city+" by population");
        mostPop(true);

        printCityData(city);
    }

    /**
     *  This method iterates through the database to first make sure that the inputted state exists.
     *  Then, it adds all the data from the database for the specific state, to the new created list.
     *
     *  @return state   name of the state entered by the user
     */
    public String getStateData()
    {
        String state = Prompt.getString("\nEnter state name (ie. Alabama)");
        boolean stateIsValid = false;
        int count = 0;

        while(count<cities.size() && !stateIsValid)
        {
            for(int i=0; i<cities.size(); i++)
            {
                if(cities.get(i).getStateName().equalsIgnoreCase(state))
                    stateIsValid = true;
            }

            if(!stateIsValid)
            {
                System.out.println("ERROR: "+state+" is not valid");
                state = Prompt.getString("Enter state name (ie. Alabama)");
            }
        }

        return state;
    }

    /**
     *  Takes elements at the innerMax and outer positions and swaps them in the cities database.
     *
     *  @param innerMax     index of first element to swap
     *  @param outer        index of second element to swap
     */
    public void swap(int innerMax, int outer)
    {
        String tempState = cities.get(innerMax).getStateName();
        cities.get(innerMax).setStateName(cities.get(outer).getStateName());
        cities.get(outer).setStateName(tempState);

        String tempCity = cities.get(innerMax).getCityName();
        cities.get(innerMax).setCityName(cities.get(outer).getCityName());
        cities.get(outer).setCityName(tempCity);

        String tempType = cities.get(innerMax).getCityType();
        cities.get(innerMax).setCityType(cities.get(outer).getCityType());
        cities.get(outer).setCityType(tempType);

        Integer temp = cities.get(innerMax).getPopulation();
        cities.get(innerMax).setPopulation(cities.get(outer).getPopulation());
        cities.get(outer).setPopulation(temp);

    }

    /**
     * This method iterates through the database to first make sure that the inputted city exists.
     * Then, it adds all the data from the database for the specific city, to the new created list.
     *
     * @return city     name of the city entered by user
     */
    public String getCityData()
    {
        String city = Prompt.getString("\nEnter city name");
        boolean cityIsValid = false;
        int count = 0;
        numMatchingCities = 0;

        while(count<cities.size() && !cityIsValid)
        {
            for(int i=0; i<cities.size(); i++)
            {
                if(cities.get(i).getCityName().equalsIgnoreCase(city))
                    cityIsValid = true;
            }

            if(!cityIsValid)
            {
                System.out.println("ERROR: "+city+" is not valid");
                city = Prompt.getString("\nEnter city name");
            }
        }

        count = 0;

        while(count<cities.size())
        {
            if(cities.get(count).getCityName().equalsIgnoreCase(city))
            {
                numMatchingCities++;
            }
            count++;
        }

        return city;
    }

    /**
     *  Iterates through the cities list and print data from database
     */
    public void printData()
    {
        System.out.printf("    %-22s %-22s %-12s %12s\n", "State", "City", "Type", "Population");
        for(int i=0; i<NUM_CITIES; i++)
        {
            if(i<9)
                System.out.print(" ");
            System.out.println((i+1)+": "+cities.get(i).toString());
        }
		System.out.println("\n");
    }

    /**
     *  Iterates through state list and prints database
     *
     *  @param state    name of state inputted by user
     */
    public void printStateData(String state)
    {
        System.out.printf("    %-22s %-22s %-12s %12s\n", "State", "City", "Type", "Population");
        int count = 0;
        int loops = 0;

       while(count<NUM_CITIES && loops<cities.size())
        {
            if(cities.get(loops).getStateName().equalsIgnoreCase(state))
            {
                if(count<9)
                    System.out.print(" ");
                System.out.println((count+1)+": "+cities.get(loops).toString());
                count++;
            }
            loops++;
        }
		System.out.println("\n");
    }

    /**
     *  Iterates through city list and prints database
     *
     *  @param city     name of city inputted by user
     */
    public void printCityData(String city)
    {
        System.out.printf("    %-22s %-22s %-12s %12s\n", "State", "City", "Type", "Population");
        int count = 0;
        int loops = 0;

        while(count<numMatchingCities)
        {
            if(cities.get(loops).getCityName().equalsIgnoreCase(city))
            {
                if(count<9)
                    System.out.print(" ");
                System.out.println((count+1)+": "+cities.get(loops).toString());
                count++;
            }
            loops++;
        }
		System.out.println("\n");
    }
}