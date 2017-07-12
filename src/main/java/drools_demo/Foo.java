package drools_demo;

public abstract class Foo {

	private int number;
	
	private boolean fired = false;

	public Foo(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isFired() {
		return fired;
	}

	public void setFired(boolean fired) {
		this.fired = fired;
	}	
	
}
