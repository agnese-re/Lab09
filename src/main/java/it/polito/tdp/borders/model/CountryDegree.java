package it.polito.tdp.borders.model;

public class CountryDegree implements Comparable<CountryDegree> {

	private Country country;
	private int grado;
	
	public CountryDegree(Country country, int grado) {
		this.country = country;
		this.grado = grado;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public int getGrado() {
		return grado;
	}

	public void setGrado(int grado) {
		this.grado = grado;
	}

	@Override
	public String toString() {
		return this.country.getStateName() + " " + " grado= " + this.grado;
	}

	@Override
	public int compareTo(CountryDegree o) {
		return -(this.getGrado() - o.getGrado());
	}
	
}
