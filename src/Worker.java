/**
 * Worker.java
 * Ernest Ho
 * Date: November 11,2018
 * The subclass of workers under the superclass Employee
 */
public class Worker extends Employee {
	// Workers private variable for hourly pay
	private double wage;

	/**
	 * Worker
	 * The Worker Constructor
	 * @param name String, the name of the Worker
	 * @param id String, the id of the Worker
	 * @param address String, the address of the Worker
	 * @param wage Double, the wage of the Worker
	 */
	public Worker(String name, String id, String address, double wage) {
		super(name, id, address);
		this.wage = wage;
	}

	/**
	 * @Override 
	 * compareTo
	 * compares two employees
	 * @param other Employee being compared with
	 * @return Integer, 1 if this is lower in the array, -1 if this is higher in the array
	 */
	public int compareTo(Employee other) {
		if (other instanceof Manager) {// if being compared to a manager
			return 1;// then this worker is lower
		} else if (this.wage == ((Worker) other).getWage()) {// if is another worker and their wage is equal
			if (super.getName().compareTo(other.getName()) < 0) {// if their name is higher alphabetically
				return 1;// then this worker is lower
			} else {
				return -1;// this worker is higher
			}
		} else if (this.wage > ((Worker) other).getWage()) {// if their wage is lower
			return -1;// this worker is higher
		} else {// their wage is lower
			return 1;// this worker is lower
		}
	}

	/**
	 * getWage
	 * @return Double, the wage of this Worker
	 */
	public double getWage() {
		return wage;
	}

	/**
	 * setWage
	 * @param wage Double, pay the new wage of the worker
	 */
	public void setWage(double wage) {
		this.wage = wage;
	}

	/**
	 * @Override 
	 * getPay
	 * @return Double, the weekly pay of this worker
	 */
	public double getPay() {
		return super.getHours() * wage;// Multiplying the hours worked in a week with this workers wage
	}
}
