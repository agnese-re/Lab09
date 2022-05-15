package it.polito.tdp.borders.model;

public class Country implements Comparable<Country> {

	private String stateAbb;
	private int stateCode;
	private String stateName;
	
	public Country(String stateAbb, int stateCode, String stateName) {
		this.stateAbb = stateAbb;
		this.stateCode = stateCode;
		this.stateName = stateName;
	}

	public String getStateAbb() {
		return stateAbb;
	}

	public void setStateAbb(String stateAbb) {
		this.stateAbb = stateAbb;
	}

	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + stateCode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Country other = (Country) obj;
		if (stateCode != other.stateCode) {
			return false;
		}
		return true;
	}
	
	public String toString() {
		return this.getStateName();
	}

	@Override
	public int compareTo(Country o) {
		return this.getStateName().compareTo(o.getStateName());
	}
	
}
