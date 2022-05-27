package it.polito.tdp.borders.model;

public class Country {

	private int stateCode;
	private String stateAbb;
	private String stateName;
	
	public Country(int stateCode, String stateAbb, String stateName) {
		this.stateCode = stateCode;
		this.stateAbb = stateAbb;
		this.stateName = stateName;
	}

	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateAbb() {
		return stateAbb;
	}

	public void setStateAbb(String stateAbb) {
		this.stateAbb = stateAbb;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@Override
	public String toString() {
		return "Country [stateCode=" + stateCode + ", stateAbb=" + stateAbb + ", stateName=" + stateName + "]";
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
	
}
