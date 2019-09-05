/**
 * Employee.java
 * Ernest Ho
 * Date: November 11,2018
 * The superclass for all employees in the company
 */
public class Employee implements Comparable<Employee> {
	// Employee information variables
	private String name;
	private String id;
	private String address;
	private int hours;
	// boolean arrays for availability and working times
	// true if available or working
	private boolean[][] availability = new boolean[7][24];
	private boolean[][] timeTable = new boolean[7][24];

	/**
	 * Employee 
	 * The employee constructor
	 * 
	 * @param name String, name of the employee being made
	 * @param id String, id of the employee being made
	 * @param address String, address of the employee being made
	 */
	public Employee(String name, String id, String address) {
		// setting information into the private variables
		this.name = name;
		this.id = id;
		this.address = address;
		// Initializing all times to be available
		for (int i = 0; i < availability.length; i++) {
			for (int j = 0; j < availability[0].length; j++) {
				availability[i][j] = true;
			}
		}
	}

	/**
	 * @Overide 
	 * compareTo
	 * Implementing the compareTo method which will be overridden in the subclasses
	 * @param other Employee, the other employee being compared
	 * @return Integer, 0 or nothing
	 */
	public int compareTo(Employee other) {
		return 0;
	}

	/**
	 * getName
	 * @return String, the name of the employee
	 */
	public String getName() {
		return name;
	}

	/**
	 * setName
	 * @param name String, new name for the employee
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * getId
	 * @return String, the id of the employee
	 */
	public String getId() {
		return id;
	}

	/**
	 * setId
	 * @param id String, new id of the employee
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * getAddress
	 * @return String, the employee's address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * setAddress
	 * @param address String, the new address of the employee
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * getHours
	 * @return Integer, the hours done for the week by an employee
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * getHours
	 * @param day Integer, day of the week
	 * @return Integer, the hours worked on a certain day of the week
	 */
	public int getHours(int day) {
		int dayHours = 0;// counter for hours
		for (int i = 0; i < 24; i++) {// looping through hours in a day
			if (timeTable[day][i]) {// if they work this hour
				dayHours++;// then add 1 to the counter
			}
		}
		return dayHours;
	}

	/**
	 * getAvailability 
	 * checks if an employee is available on a certain day of the week
	 * @param day Integer, a certain day of the week
	 * @return Boolean, true if they are available that day. False if not
	 */
	public boolean getAvailability(int day) {
		for (int i = 0; i < 24; i++) {// looping through hours in a day
			if (availability[day][i]) {// if they are available at some point
				return true;// return true;
			}
		}
		return false;
	}

	/**
	 * getAvailability 
	 * checks availability of a certain hour
	 * @param day Integer, day for checking
	 * @param hour Integer, hour for checking
	 * @return Boolean, true if they are available. False if not
	 */
	public boolean getAvailability(int day, int hour) {
		return availability[day][hour];
	}

	/**
	 * setAvailability
	 * sets when an employee is/isn't available
	 * @param day Integer, day to set
	 * @param hourStart Integer, hourStart is the starting hour for a given time frame
	 * @param hourEnd Integer, hourEnd the ending over of a given time frame. Not inclusive.
	 * @param Boolean, on is true if available. False if not
	 */
	public void setAvailability(int day, int hourStart, int hourEnd, boolean on) {
		for (int i = hourStart; i < hourEnd; i++) {// looping from start to end of the time frame
			this.availability[day][i] = on;// setting every hour in the time frame to the desired availability
		}
	}

	/**
	 * setAvailability
	 * Copies a 2d boolean array as the new availability array
	 * @param availabilty the 2d array to copy from
	 */
	public void setAvailability(boolean[][] availabilty) {
		// nested for loop to copy the new availability array
		for (int i = 0; i < availabilty.length; i++) {
			for (int j = 0; j < availabilty[0].length; j++) {
				this.availability[i][j] = availabilty[i][j];
			}
		}
	}

	/**
	 * getTimeTable
	 * @param day Integer, a certain day to check
	 * @param hour Integer,a certain hour of the day to check
	 * @return Boolean, true if available. False if not
	 */
	public boolean getTimeTable(int day, int hour) {
		return timeTable[day][hour];
	}

	/**
	 * getTimeTable
	 * @return String, to be displayed in the console
	 */
	public String getTimeTable() {
		String schedule = (this.name + " #" + this.id + "\nPay: " + this.getPay());// Start of the string with name id
																					// and pay
		// initializing start and end times to be used for time frames
		String startTime = null;
		String endTime = null;
		// Looping through the days of the week
		for (int i = 0; i < timeTable.length; i++) {
			// If it is currently on 0 then add M for Monday, 1 for Tuesday etc.
			if (i == 0) {
				schedule += "\n M";
			} else if (i == 1) {
				schedule += "\n T";
			} else if (i == 2) {
				schedule += "\n W";
			} else if (i == 3) {
				schedule += "\n R";
			} else if (i == 4) {
				schedule += "\n F";
			} else if (i == 5) {
				schedule += "\n S";
			} else if (i == 6) {
				schedule += "\n U";
			}
			// initializing boolean if the for loop is currently looping for an end time
			boolean findingEnd = false;
			for (int j = 0; j < timeTable[0].length; j++) {// Looping through the hours of a certain day
				if (!findingEnd && timeTable[i][j]) {// if found beginning
					// remember startTime as a string
					if (j < 10) {
						startTime = "0" + j + ":00";
					} else {
						startTime = j + ":00";
					}
					findingEnd = true;// now finding the end of the time frame
				} else if (findingEnd && (!timeTable[i][j] || j == 23)) {// if found end
					// remember endTime as a string
					if (j < 10) {
						endTime = "0" + j + ":00";
					} else if (j == 23) {// 23 means they work till the morning or hour 0
						endTime = "00:00";
					} else {
						endTime = j + ":00";
					}
					schedule += "\n " + startTime + " - " + endTime;// add time frame to string
					findingEnd = false;// now finding beginning
				}
			}
		}
		return schedule;
	}

	/**
	 * setTimeTable
	 * sets working days
	 * @param day Integer, certain day to check
	 * @param hour Integer, certain hour to check
	 * @param Boolean, true if working,false if not working
	 */
	public void setTimeTable(int day, int hour, boolean on) {
		// set employee on this hour to working/not working
		this.timeTable[day][hour] = on;
		if (on) {// if working
			this.hours++;// add hours
		} else {// not working
			this.hours--;// subtract horus
		}
	}

	/**
	 * getPay
	 * placeHolder method to be overridden in subclasses
	 * @return Double, the pay of an employee
	 */
	public double getPay() {
		return 0;
	}
}
