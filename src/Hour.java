/**
 * Hour.java
 * Ernest Ho
 * Date: November 11,2018
 * A class which stores the a list of employees that work in a certain hour
 */
public class Hour {
	private Employee[] employeeList;//array for the list of employees
	private boolean noManager=true;//whether or no this hour has a manager
	
	/**
	 * Hour
	 * the hour constructor
	 * @param size Integer, the amount of employees required for this hour
	 */
	public Hour(int size) {
		employeeList = new Employee[size];//create a and employeeList of the specified amount
	}

	/**
	 * add
	 * add an employee to this hour
	 * @param employee Employee, the employee being added
	 */
	public void add(Employee employee) {
		int counter = 0;
		boolean placed = false;
		while (!placed && counter < employeeList.length) {//while not placed already and the entire array has not been checked
			if (employeeList[counter] == null) {//if there is an empty space
				employeeList[counter] = employee;//pass a reference of this employee to the array
				placed = true;//stop looping
			}
			counter++;//check next place in the array
		}
		if (placed) {//if placed
			if (employee instanceof Manager) {//if it's a Manager
				noManager = false;//there is Manager(s) in this hour
			}
		}
	}

	/**
	 * remove
	 * remove an employee from this hour
	 * @param employee Employee, the employee that needs to be removed
	 */
	public void remove(Employee employee) {
		int counter = 0;
		boolean found = false;
		while (!found && counter < employeeList.length) {//while not found and entire array has not been searched
			if (employeeList[counter] == employee) {//if a match has been found
				employeeList[counter] = null;//remove the reference to the employee
				found = true;//stop looping
			}
			counter++;//check next cell
		}
		int inputLength = employeeList.length;//save the list length
		boolean sorted = false;
		for (int i = 0; i < inputLength && !sorted; i++) {//go through for loop until the null has been moved to the back
			sorted = true;
			//basic bubble sort to move null to the back
			for (int j = 1; j < (inputLength - i); j++) {
				if (employeeList[j - 1] == null && employeeList[j] != null) {
					employeeList[j - 1] = employeeList[j];
					employeeList[j] = null;
					sorted = false;
				}
			}
		}
	}
	
	/**
	 * length
	 * @return Integer, the amount of needed people in this hour
	 */
	public int length() {
		return employeeList.length;//Returns the length
	}
	
	/**
	 * getSpace
	 * checks for spaces left in the hour
	 * @return Integer, the amount of spaces left
	 */
	public int getSpace() {
		int counter = 0;//amount of spaces
		for (int i = 0; i < employeeList.length; i++) {//looping through the list
			if (employeeList[i] == null) {//if the space is null
				counter++;//there is a space
			}
		}
		return counter;
	}

	/**
	 * getNoManager
	 * @return Boolean true if there is a manager in this hour, false if not
	 */
	public boolean getNoManager() {
		return noManager;
	}

	/**
	 * isHere
	 * checks if a employee already works in this hour
	 * @param employee Employee, the employee that may be in the hour
	 * @return Boolean, true if the employee already works here, false if not
	 */
	public boolean isHere(Employee employee) {
		for (int i = 0; i < employeeList.length; i++) {//loop through the array
			if (employee == employeeList[i]) {//if there is a match
				return true;
			}
		}
		return false;
	}
}
