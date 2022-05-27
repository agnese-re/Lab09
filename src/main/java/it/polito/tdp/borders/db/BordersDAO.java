package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public List<Country> loadAllCountries() {	// public void loadAllCountries(Map<Integer,Country> idMap) {
		
		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				// System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				result.add(new Country(rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme")));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno,Map<Integer,Country> idMap) {
		String sql = "SELECT state1no, state2no "
				+ "FROM contiguity "
				+ "WHERE contiguity.year <= ? AND contiguity.conttype = 1 "
				+ "	AND state1no > state2no";
		List<Border> result = new ArrayList<Border>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();
			
			while(rs.next())
				result.add(new Border( idMap.get(rs.getInt("state1no")),idMap.get(rs.getInt("state2no")) ));
			conn.close();
			return result;
		} catch(SQLException e) {
			throw new RuntimeException("Errore nella query",e);
		}

	}
	
	public List<Country> getVertici(int anno, Map<Integer,Country> idMap) {
		String sql = "SELECT DISTINCT(state1no) "
				+ "FROM contiguity "
				+ "WHERE contiguity.year <= ?";
		List<Country> result = new ArrayList<Country>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();
			
			while(rs.next())
				result.add(idMap.get(rs.getInt("state1no")));
			conn.close();
			return result;
		} catch(SQLException e) {
			throw new RuntimeException("Errore nella query",e);
		}
	}
}
