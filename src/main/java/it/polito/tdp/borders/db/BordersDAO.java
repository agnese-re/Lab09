package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public List<Country> loadAllCountries() {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country countryAggiungere = new Country(rs.getString("StateAbb"), rs.getInt("ccode"), rs.getString("StateNme"));
				result.add(countryAggiungere);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(Year anno) {
		String sql = "SELECT state1no, state2no "
				+ "FROM contiguity "
				+ "WHERE contiguity.year <= ? "
				+ "	AND contiguity.conttype = 1";
		List<Border> result = new ArrayList<Border>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno.getValue()); 	// valore numerico oggetto Year
			ResultSet rs = st.executeQuery();

			while (rs.next()) 
				result.add(new Border(rs.getInt("state1no"),rs.getInt("state2no")));
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Integer> getCountriesCodeYear(Year anno) {
		String sql = "SELECT DISTINCT(contiguity.state1no) "
				+ "FROM contiguity "
				+ "WHERE contiguity.year <= ? "
				+ "	AND contiguity.conttype = 1";
		List<Integer> result = new ArrayList<Integer>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno.getValue()); 	// valore numerico oggetto Year
			ResultSet rs = st.executeQuery();

			while (rs.next()) 
				result.add(rs.getInt("state1no"));
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
}
