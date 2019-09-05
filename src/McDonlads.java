/**
 * McDonlads.java
 * Ernest Ho
 * Date: November 11,2018
 * The subclass of managers under the superclass Employee
 */
//import built-in java classes to use
import java.util.ArrayList;
import java.util.Scanner;

public class McDonlads {
	//create a new Scanner to use for input
	private static Scanner input = new Scanner(System.in);
	public static void main(String[] args) {//the main where everything is ran
		Schedule data = new Schedule("src/companyHrs.txt", "src/employeeInfo.txt");//create a new Schedule
		boolean exit = false;//boolean for exiting the program loop
		while (!exit) {//the program loop
			//ask user for their option
			System.out.println("\n1. Add/edit/remove worker\n" + "2. Add/edit/remove manager\n"
					+ "3. List all Employees (managers first, workers next. In order of pay, highest first)\n"
					+ "4. Run Scheduling Algorithm\n" + "5. Display Employee Weekly Schedule/Pay (by employee #)\n"
					+ "6. Quit \nEnter Option:");
			int option = -1;//initialize the option variable
			try {//check to see if it is a integer
				option = Integer.parseInt(input.nextLine());
				if (option > 6 || option < 1) {
					System.out.println("Invalid Input");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid Input");
			}
			if (option == 1 || option == 2) {//Add/edit/remove
				String type;//initialize the type
				if (option == 2) {
					type = "Manager";
				} else {
					type = "Worker";
				}
				System.out.println("\n1.Add \n2.Edit \n3.Remove");//asks which of the three options they want to do
				option = input.nextInt();
				if (option == 1) {//Add
					//Ask for basic Employee info
					System.out.println("Enter Name:");
					String name = input.nextLine();
					System.out.println("Enter Id:");
					String id = input.nextLine();
					System.out.println("Enter Address:");
					String address = input.nextLine();
					System.out.println("Wage/Pay:");
					Double wage = input.nextDouble();
					input.nextLine();
					//add the employee to the schedule
					data.addEmployee(type, name, id, address, wage);
					//edit the availability of the employee
					editAvailability(id, data);
				} else if (option == 2) {//edit
					System.out.println("id?");//asks for the id of the user they want to change
					String id = input.nextLine();
					System.out.println("\n1.Name \n2.Id \n3.Address \n4.Pay \n5.Availability");//ask what info they want to edit
					option = input.nextInt();
					input.nextLine();
					if (option == 1) {
						System.out.println("New Name:");
						data.getEmployee(id).setName(input.nextLine());
					} else if (option == 2) {
						System.out.println("New Id:");
						data.getEmployee(id).setId(input.nextLine());
					} else if (option == 3) {
						System.out.println("New Address:");
						data.getEmployee(id).setAddress(input.nextLine());
					} else if (option == 4) {
						System.out.println("New Wage:");
						if (type.equals("Manager")) {
							((Manager) data.getEmployee(id)).setSalary(input.nextDouble());
						} else {
							((Worker) data.getEmployee(id)).setWage(input.nextDouble());
						}
					} else if (option == 5) {
						editAvailability(id, data);
					}
				} else if (option == 3) {//delete
					System.out.println("id?");//ask for the id of the employee to remove
					data.removeEmployee(input.nextLine());//removes that employee from scheduling
				}
			} else if (option == 3) {//list all the employees
				ArrayList<Employee> list = data.listEmployees();//create the sorted array
				for (int i = 0; i < list.size(); i++) {
					System.out.println(list.get(i).getName());//print out the sorted array
				}
			} else if (option == 4) {//create a schedule
				if (!data.createSchedule()) {//if the schedule cannot be made
					System.out.println("Schedule cannot be made");
				} else {
					System.out.println("Schedule made");
				}
			} else if (option == 5) {//Display the weekly schedule of an employee and their pay
				System.out.println("id?");//ask for their id
				System.out.println(data.getEmployee(input.nextLine()).getTimeTable());//prints the info
			} else if (option == 6) {//exit
				exit = true;//exit the while loop
				data.exportEmployeeInfo();//export the data
			}
		}
		input.close();//close the scanner
	}

	/**
	 * editAvailability
	 * allows the same code for editing availability to be used twice for editing and adding
	 * @param id String, the id of the employee that needs to be edited
	 * @param data Schedule, the schedule with the employees
	 */
	private static void editAvailability(String id, Schedule data) {
		System.out.println("Setting Availability");
		boolean done = false;
		while (!done) {//while the user is not done making edits
			System.out.println("1.Add Time Frame\n2.Always Available\n3.Exit Availabity Adding");//asks for what to add
			int option = input.nextInt();
			input.nextLine();
			if (option == 1) {//timeFrame
				System.out.println("Enter time frame:");
				System.out.println("1.(Not)Available from... \n2.(Not)Available only on...");
				option = input.nextInt();
				System.out.println("1.Not Available \n2.Available");
				boolean on = true;//whether they are or are not available during this time frame
				if (input.nextInt() == 1) {
					on = false;
				}
				System.out.println("Start Day(1=Monday,2=Tuesday etc):");
				int startDay = input.nextInt() - 1;
				System.out.println("End Day(inclusive):");
				int endDay = input.nextInt() - 1;
				System.out.println("Start hour(24 hr time):");
				int startHour = input.nextInt();
				System.out.println("End Hour(24 hr time):");
				int endHour = input.nextInt();
				if (option == 1) {//(Not)Available from...
					for (int i = startDay; i <= endDay; i++) {
						data.getEmployee(id).setAvailability(i, startHour, endHour, on);
					}
				} else {//(Not)Available only on
					for (int i = 0; i < 7; i++) {
						data.getEmployee(id).setAvailability(i, 0, 24, !on);
					}
					for (int i = startDay; i <= endDay; i++) {
						data.getEmployee(id).setAvailability(i, startHour, endHour, on);
					}
					done = true;
				}
			} else if (option == 2) {//Always Available
				for (int i = 0; i < 7; i++) {
					data.getEmployee(id).setAvailability(i, 0, 24, true);
				}
			} else if (option == 3) {//exit editing availability
				done = true;
			}
		}
	}
}
