package it.polito.tdp.borders.model;

public class Confine implements Comparable<Confine> {

	private Country country;
	private int numStatiConfinanti;
	
	public Confine(Country country, int numStatiConfinanti) {
		this.country = country;
		this.numStatiConfinanti = numStatiConfinanti;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public int getNumStatiConfinanti() {
		return numStatiConfinanti;
	}

	public void setNumStatiConfinanti(int numStatiConfinanti) {
		this.numStatiConfinanti = numStatiConfinanti;
	}

	@Override
	public String toString() {
		return country.getStateName() + " " + this.numStatiConfinanti;
	}

	@Override
	public int compareTo(Confine other) {
		return this.country.getStateName().compareTo(other.getCountry().getStateName());
	}

}
