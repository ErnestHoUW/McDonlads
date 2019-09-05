/**
 * Schedule.java
 * Ernest Ho
 * Date: November 11,2018
 * The class where all the scheduling and employee list are stored and processed
 */

//Importing built in java classes to use
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Schedule {
	private ArrayList<Employee> employeeList = new ArrayList<Employee>();//the full list of employees
	private Hour[][] bigTimeTable = new Hour[7][24];//the company timetable with all the shifts
	private String employeeInfo;//location of the file to output employee information

	/**
	 * Schedule
	 * the schedule constructor
	 * @param companyHrs String, the location of companyHrs text file
	 * @param employeeInfo String, the location of employeeInfo text file
	 */
	public Schedule(String companyHrs, String employeeInfo) {
		this.employeeInfo = employeeInfo;//remember the location for employee information
		//Initialize the company timetable as hours with length 0
		for (int i = 0; i < bigTimeTable.length; i++) {
			for (int j = 0; j < bigTimeTable[0].length; j++) {
				bigTimeTable[i][j]=new Hour(0);
			}
		}
		//trying to import files
		try {
			Scanner inCompanyHrs = new Scanner(new File(companyHrs));//create scanner to read the companyHrs text file
			int dayOfWeek = -1;//the current day of the week index
			while (inCompanyHrs.hasNext()) {
				String currentLn = inCompanyHrs.nextLine();//remember the entire line
				if (currentLn.length() == 1) {//if the length of the line is one then the day of the week must have been changed
					dayOfWeek++;
				} else {
					//from the given time frames start to end
					for (int i = Integer.parseInt(currentLn.substring(0, 2)); i < Integer
							.parseInt(currentLn.substring(6, 8)); i++) {
						bigTimeTable[dayOfWeek][i] = new Hour(
								Integer.parseInt(currentLn.substring(currentLn.length() - 1)));//create hours with the specified amounts of people
					}
				}
			}
			inCompanyHrs.close();//close the scanner
			Scanner inEmployeeInfo = new Scanner(new File(employeeInfo));//create scanner to read the employeeInfro text file
			int lineCount = 0;//line counter
			//Initializing the employee information variables
			String name = "";
			String id = "";
			String address = "";
			Double pay = 0.0;
			boolean isManager = false;
			boolean[][] availabilty = new boolean[7][24];
			while (inEmployeeInfo.hasNext()) {
				lineCount++;//increase the line counter
				String currentLn = inEmployeeInfo.nextLine();//remember the current line
				if (currentLn.equals("*")) {//if the line is  * that means the employees information has ended and a new one has started
					//create the new employee
					if (isManager) {
						employeeList.add(new Manager(name, id, address, pay));
					} else {
						employeeList.add(new Worker(name, id, address, pay));
					}
					//set their availability
					employeeList.get(employeeList.size() - 1).setAvailability(availabilty);
					lineCount = 0;//reset the line counter
					isManager = false;//reset the type
				}
				// Line 1 type of employee,2 name, 3 id, 4 address, 5 and on is availability
				if (lineCount == 1) {
					if (currentLn.equals("Manager")) {
						isManager = true;
					}
				} else if (lineCount == 2) {
					name = currentLn;
				} else if (lineCount == 3) {
					id = currentLn;
				} else if (lineCount == 4) {
					address = currentLn;
				} else if (lineCount == 5) {
					pay = Double.parseDouble(currentLn);
				} else if (lineCount > 5) {
					//1 means available 0 means not available
					for (int i = 0; i < 24; i++) {//go through the line with 24 hours
						if (currentLn.charAt(i) == '0') {
							availabilty[lineCount - 6][i] = false;
						} else {
							availabilty[lineCount - 6][i] = true;
						}
					}
				}
			}
			inEmployeeInfo.close();//close the scanner
		} catch (IOException e) {//if the files are not found
			System.out.println("files not found");
		}
	}
	
	/**
	 * createSchedule
	 * creates a schedule within the bigTimeTable array
	 * @return boolean true if possible, false if not
	 */
	public boolean createSchedule() {
		Collections.sort(employeeList);//sort the employee list
		
		// finding managers index
		int indexLastManager = -1;
		//find the index of the last manager
		for (int i = 0; i < employeeList.size() && indexLastManager == -1; i++) {
			if (employeeList.get(i) instanceof Worker) {
				indexLastManager = i - 1;
			}
		}
		
		// filling in all mandatory shifts and shifts where only one manager is available
		for (int day = 0; day < 7; day++) {// day
			for (int hour = 0; hour < 24; hour++) {// hour
				if (bigTimeTable[day][hour].getSpace() != 0) {//if there is people needed in the hour
					ArrayList<Integer> thisHour = new ArrayList<Integer>();//create an arrayList of possible employees in the hour
					ArrayList<Integer> thisHourManagers = new ArrayList<Integer>();//create an arrayList of possible managers in the hour
					for (int i = 0; i < employeeList.size() - 1; i++) {//looping through the list of employees
						if (employeeList.get(i).getAvailability(day, hour)
								&& employeeList.get(i).getHours(day) < 10) {//if they are available for this day and have less that ten hours today
							thisHour.add(i);//then remember them
						}
						if (employeeList.get(i).getAvailability(day, hour) && employeeList.get(i) instanceof Manager
								&& employeeList.get(i).getHours() < 40 && employeeList.get(i).getHours(day) < 10) {//if they are a manager and can work that hour
							thisHourManagers.add(i);//then remember them
						}
					}
					if (thisHour.size() < bigTimeTable[day][hour].getSpace()) {//if there are not enough workers for this hour
						return false;//then a schedule cannot be made
					}
					if (thisHourManagers.size() == 1) {//if only one manager is available this hour
						addToTimeTable(employeeList.get(thisHourManagers.get(0)), day, hour);//give them the hour to work
					}
				}
			}
		}
		
		// filling gaps in manager times
		for (int i = 0; i <= indexLastManager; i++) {//loop through the managers
			for (int day = 0; day < 7; day++) {//loop through the days of the week
				if (employeeList.get(i).getHours() <= 40 
						&& employeeList.get(i).getHours(day) > 0) {//if they work today and have not reached their quota 
					//a block as in a shift
					int startBlock = 0;
					int endBlock = 0;// inclusive so 1pm - 2pm means they work till 3pm
					//found the starting point of when they work
					boolean found = false;
					for (int hour = 0; hour < 24 && !found; hour++) {
						if (employeeList.get(i).getTimeTable(day, hour) && startBlock == 0) {//if they work this hour
							found = true;//then it is found
							startBlock = hour;//set the start of the shift to this hour
							endBlock = hour;//set the end of the shift to this hour
						}
					}
					//finding the start and end of a possible working shift
					boolean foundStart = false;//found start of a shift
					boolean foundEnd = false;//found the end of the shift
					while ((!foundStart || !foundEnd) && endBlock - startBlock + 1 <= 10
							&& employeeList.get(i).getHours() + endBlock - startBlock + 1 < 40) {//while the start or end has not been found and they can still work more hours
						if (!foundStart && startBlock != 0 && employeeList.get(i).getAvailability(day, startBlock - 1)//if start has not been found and they can work earlier
								&& bigTimeTable[day][startBlock - 1].getSpace() > 0
								&& bigTimeTable[day][startBlock - 1].getNoManager()) {
							startBlock--;//then start the shift earlier
						} else {
							foundStart = true;
						}
						if (!foundEnd && endBlock != 23 && employeeList.get(i).getAvailability(day, endBlock + 1)//if the end has not been found and they can work later
								&& bigTimeTable[day][endBlock + 1].getSpace() > 0
								&& bigTimeTable[day][endBlock + 1].getNoManager()) {
							endBlock++;//then end the shift later
						} else {
							foundEnd = true;
						}
					}
					for (int j = startBlock; j <= endBlock; j++) {//give them the shift
						addToTimeTable(employeeList.get(i), day, j);
					}
				}
			}
		}
		// getting managers everywhere they can be and evening out their hours
		for (int i = 0; i <= indexLastManager; i++) {//looping through the managers
			for (int day = 0; day < 7; day++) {//looping through the days
				if (employeeList.get(i).getHours(day) == 0&&employeeList.get(i).getAvailability(day)) {//if they do not work this day but can
					for (int hour = 0; hour < 24; hour++) {//loop through the hours
						if (employeeList.get(i).getHours() < 40 && employeeList.get(i).getHours(day) < 10
								&& bigTimeTable[day][hour].getSpace() > 0 && bigTimeTable[day][hour].getNoManager()) {//if they can take the shift
							addToTimeTable(employeeList.get(i), day, hour);//then give it to them
						}
					}
				}
			}
			for(int m=0;m<40;m++) {//optimize their schedule 40 times or the maximum amount of discrepancy between hours 
				int dayLeastHrs = -1;//index for the day with the least hours
				//find the day with the least hours where they can work
				for (int day = 0; day < 7; day++) {
					if (dayLeastHrs == -1
							|| employeeList.get(i).getHours(day) < employeeList.get(i).getHours(dayLeastHrs)
									&& employeeList.get(i).getAvailability(day)) {
						dayLeastHrs = day;
					}
				}
				//if they can work at least 1 day
				if (dayLeastHrs != -1) {
					// finding existing working times
					int startBlock = -1;
					int endBlock = 0;// inclusive
					for (int j = 0; j < 24; j++) {
						if (employeeList.get(i).getTimeTable(dayLeastHrs, j) && startBlock == -1) {
							startBlock = j;
							endBlock = -2;
						} else if ((j == 23 || !employeeList.get(i).getTimeTable(dayLeastHrs, j)) && endBlock == -2) {
							if (j == 23) {
								endBlock = 23;
							} else {
								endBlock = j - 1;
							}
						}
					}
					//if they work that day
					if (startBlock != -1) {
						//add 1 hour to the day with the least hours
						if (startBlock == 0 && employeeList.get(i).getAvailability(dayLeastHrs, endBlock + 1)
								&& bigTimeTable[dayLeastHrs][endBlock + 1].getSpace() > 0) {
							addToTimeTable(employeeList.get(i), dayLeastHrs, endBlock + 1);
						} else if (endBlock == 23 && employeeList.get(i).getAvailability(dayLeastHrs, startBlock - 1)
								&& bigTimeTable[dayLeastHrs][startBlock + 1].getSpace() > 0) {
							addToTimeTable(employeeList.get(i), dayLeastHrs, startBlock - 1);
						} else if (startBlock != 0 && endBlock != 23) {
							if (employeeList.get(i).getAvailability(dayLeastHrs, endBlock + 1)
									&& bigTimeTable[dayLeastHrs][endBlock + 1].getSpace() > 0) {
								addToTimeTable(employeeList.get(i), dayLeastHrs, endBlock + 1);
							} else if (employeeList.get(i).getAvailability(dayLeastHrs, startBlock - 1)
									&& bigTimeTable[dayLeastHrs][startBlock + 1].getSpace() > 0) {
								addToTimeTable(employeeList.get(i), dayLeastHrs, endBlock + 1);
							}

						}
						if (employeeList.get(i).getHours() == 41) {//if the added hour exceeds 40 
							//find the day with the most hours and remove an hour from there
							int dayMostHrs = -1;//index for the day with the most hours
							for (int day = 0; day < 7; day++) {
								if (dayMostHrs == -1 || employeeList.get(i).getHours(day) > employeeList.get(i)
										.getHours(dayMostHrs)) {
									dayMostHrs = day;
								}
							}
							//find the shift they work then
							startBlock = -1;
							endBlock = 0;// inclusive
							for (int j = 0; j < 24; j++) {
								if (employeeList.get(i).getTimeTable(dayMostHrs, j) && startBlock == -1) {
									startBlock = j;
									endBlock = -2;
								} else if (j == 23
										|| !employeeList.get(i).getTimeTable(dayMostHrs, j) && endBlock == -2) {
									if (j == 23) {
										endBlock = 23;
									} else {
										endBlock = j - 1;
									}
								}
							}
							//remove an hour
							removeFromTimeTable(employeeList.get(i), dayMostHrs, endBlock);
						}
					}
				}
			}
		}
		//filling in left over hours with regular workers
		for (int i = employeeList.size() - 1; i > indexLastManager; i--) {//looping from the cheapest worker to the most expensive
			for (int day = 0; day < 7; day++) {//loop through the day
				for (int hour = 0; hour < 24; hour++) {//loop through the hour
					if (bigTimeTable[day][hour].getSpace() > 0 && employeeList.get(i).getAvailability(day, hour)//if they can work that day
							&& employeeList.get(i).getHours(day) < 10) {
						addToTimeTable(employeeList.get(i), day, hour);//then give the shift to them
					}
				}
			}
		}
		return true;//the schedule has been made
	}
	/**
	 * addEmployee
	 * add and employee to the employeeList data base
	 * @param type String,manager or worker
	 * @param name String, name of the employee being made
	 * @param id String, id of the employee being made
	 * @param address String, address of the employee being made
	 * @param pay
	 */
	public void addEmployee(String type, String name, String id, String address, Double pay) {
		if (type.toLowerCase().equals("manager")) {
			employeeList.add(new Manager(name, id, address, pay));
		} else if (type.toLowerCase().equals("worker")) {
			employeeList.add(new Worker(name, id, address, pay));
		}
	}
	
	/**
	 * exportEmployeeInfo
	 * transfer all employee information into a text file
	 */
	public void exportEmployeeInfo() {
		try {//try exporting files
			PrintWriter output = new PrintWriter(new File(employeeInfo));//create an PrintWriter to write with
			for (int i = 0; i < employeeList.size(); i++) {//looping through all the employees
				//output type of employee
				if (employeeList.get(i) instanceof Manager) {
					output.println("Manager");
				} else {
					output.println("Worker");
				}
				//output employee information
				output.println(employeeList.get(i).getName());
				output.println(employeeList.get(i).getId());
				output.println(employeeList.get(i).getAddress());
				//output the pay
				if (employeeList.get(i) instanceof Manager) {
					output.println(((Manager) employeeList.get(i)).getPay());
				} else {
					output.println(((Worker) employeeList.get(i)).getWage());
				}
				//outputting the array for availability
				//1 for available, 0 for unavailable
				for (int j = 0; j < 7; j++) {
					for (int k = 0; k < 24; k++) {
						if (employeeList.get(i).getAvailability(j, k)) {
							output.print("1");
						} else {
							output.print("0");
						}
					}
					output.println("");
				}
				output.println("*");//end of the current employee
			}
			output.close();//close the print writer
		} catch (IOException e) {
			System.out.println("output file not found");
		}
	}

	/**
	 * listEmployees
	 * sorts the employees then list them
	 * @return ArrayList<Employee> fully sorted list of employees
	 */
	public ArrayList<Employee> listEmployees() {
		Collections.sort(employeeList);//sort the employees using their compareTo method
		ArrayList<Employee> sorted=new ArrayList<Employee>();
		//copy the array
		for (int i = 0; i < employeeList.size(); i++) {
			sorted.add(employeeList.get(i));
		}
		return sorted;
	}

	/**
	 * addToTimeTable
	 * gives working hours to employees
	 * @param employee Employee, the employee to give the shift
	 * @param day Integer,a certain day 
	 * @param hour Integer, a certain hour of a certain day to work
	 */
	private void addToTimeTable(Employee employee, int day, int hour) {
		if (!employee.getTimeTable(day, hour)) {//if they don't already work then
			employee.setTimeTable(day, hour, true);//set their personal timetable array
			this.bigTimeTable[day][hour].add(employee);//add the employee to the hour
		}
	}

	/**
	 * removeFromTimeTable
	 * removes a employees working hour
	 * @param employee Employee, the employee to remove the shift
	 * @param day Integer,a certain day 
	 * @param hour Integer, a certain hour of a certain day to remove
	 */
	private void removeFromTimeTable(Employee employee, int day, int hour) {
		employee.setTimeTable(day, hour, false);//set their personal timetable array
		this.bigTimeTable[day][hour].remove(employee);//remove the employee from the hour
	}

	/**
	 * getEmployee
	 * takes an id and returns a reference to the employee
	 * @param id String, the id of the employee to get
	 * @return Employee, the employee that was called for
	 */
	public Employee getEmployee(String id) {
		for (int i = 0; i < employeeList.size(); i++) {//go through the employee list
			if (employeeList.get(i).getId().equals(id)) {//if their id matches the one asked for
				return employeeList.get(i);//then return that employee
			}
		}
		return null;//not found
	}

	/**
	 * removeEmployee
	 * remove a employee from the employeeList
	 * @param id
	 */
	public void removeEmployee(String id) {
		boolean removed = false;//while not removed 
		for (int i = 0; i < employeeList.size() && !removed; i++) {//loop through the employeeList
			if (employeeList.get(i).getId().equals(id)) {//if their id matches
				employeeList.remove(i);//remove that employee
				removed=true;
			}
		}
	}
}
