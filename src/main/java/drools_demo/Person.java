package drools_demo;

public class Person {
	private String firstName;
	private String lastName;
	private int age;	
	private String jobTitle;
	private boolean hired = false;
	
	
	public Person(String firstName, String lastName, int age, String jobTitle) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.jobTitle = jobTitle;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	
	public void hire() {
		System.out.println("" + firstName + " " + lastName + " was hired.");
		setHired(true);
	}

	public boolean isHired() {
		return hired;
	}

	public void setHired(boolean hired) {
		this.hired = hired;
	}	
	
}
