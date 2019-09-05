/**
 * Manager.java
 * Ernest Ho
 * Date: November 11,2018
 * The subclass of managers under the superclass Employee
 */
public class Manager extends Employee {
	//Manager's variable for salary in a year
	private double salary;
	
	/**
	 * Manager
	 * The manager constructor
	 * @param name String, the name of the Manager
	 * @param id String, the id of the Manager
	 * @param address String, the address of the Manager
	 * @param salary Double, the salary of the Manager
	 */
	public Manager(String name, String id, String address,double salary) {
		super(name, id, address);
		this.salary=salary;
	}
	/**
	 * @Override
	 * compareTo
	 * compares two employees
	 * @param other Employee, the other Employee to compare with
	 * @return Integer, 1 if this is lower in the array, -1 if this is higher in the array
	 */
	public int compareTo(Employee other){
		if (other instanceof Worker){//if the other is a worker
			return -1;//this is higher
		}else if (this.salary==((Manager)other).getPay()){//if they are not a worker and they have equal salary
			if (super.getName().compareTo(other.getName())<0){//if this managers name is lower alphabetically
			return 1;//this is lower
			}else {
				return -1;//this is higher
			}
		}else if (this.salary>((Manager)other).getPay()){//if this managers pay is higher
			return -1;//this is higher
		}else{
			return 1;//this is lower
		}
	}
	
	/**
	 * @Override
	 * getPay
	 * @return Double, the salary of this Manager
	 */
	public double getPay() {
		return salary;
	}
	
	/**
	 * setSalary
	 * @param salary Double, the new salary
	 */
	public void setSalary(double salary) {
		this.salary = salary;
	}
}
