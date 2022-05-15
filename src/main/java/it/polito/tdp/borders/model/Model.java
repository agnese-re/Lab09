package it.polito.tdp.borders.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	Graph<Country,DefaultEdge> grafo;
	List<Country> vertici;
	
	List<Country> allCountries;
	Map<Integer,Country> countriesIdMap;
	
	public List<Country> getAllCountries() {
		BordersDAO dao = new BordersDAO();	// istanza a costo zero
		
		if(allCountries == null) {	// i paesi sono sempre gli stessi
			allCountries = dao.loadAllCountries();
			countriesIdMap = new HashMap<Integer,Country>();
			for(Country country: allCountries)
				countriesIdMap.put(country.getStateCode(), country);
		}
		
		return allCountries;
	}

	public String creaGrafo(Year anno) {
		grafo = new SimpleGraph<Country,DefaultEdge>(DefaultEdge.class);
		
		getAllCountries();
		BordersDAO bordersDao = new BordersDAO();
		List<Integer> codiceStati = bordersDao.getCountriesCodeYear(anno);
		vertici = new ArrayList<Country>();
		for(Integer code: codiceStati) {
			vertici.add(countriesIdMap.get(code));
			grafo.addVertex(countriesIdMap.get(code));
		}
		
		List<Border> confini = bordersDao.getCountryPairs(anno);
		for(Border border: confini)
			/* il grafo e' non orientato. Se esiste gia' un arco che collega un vertice ad un altro vertice, non viene aggiunto */
			grafo.addEdge(countriesIdMap.get(border.getId1()), countriesIdMap.get(border.getId2()));
		
		return String.format("Creato grafo con %d vertici e %d archi",grafo.vertexSet().size(),grafo.edgeSet().size());
		
	}
	
	public List<Country> getCountries(Year anno) {
		Collections.sort(vertici);
		return vertici;
	}
	
	public List<Confine> elencoStati(Year anno) {
		
		List<Confine> confini = new ArrayList<Confine>();
		
		for(Country c: vertici)
			confini.add(new Confine(c,grafo.edgesOf(c).size()));
		
		Collections.sort(confini);
		return confini;
	}
}
