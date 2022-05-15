package it.polito.tdp.borders.model;

public class Border {

	private Integer id1;
	private Integer id2;
	
	public Border(Integer id1, Integer id2) {
		this.id1 = id1;
		this.id2 = id2;
	}

	public Integer getId1() {
		return id1;
	}

	public void setId1(Integer id1) {
		this.id1 = id1;
	}

	public Integer getId2() {
		return id2;
	}

	public void setId2(Integer id2) {
		this.id2 = id2;
	}
	
}
