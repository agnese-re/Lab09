package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private Graph<Country,DefaultEdge> grafo;
	private List<Country> countryLista;
	private Map<Integer,Country> countryMappa;
	
	private BordersDAO dao;
	
	public Model() {
		dao = new BordersDAO();
		
		countryLista = dao.loadAllCountries();		// tutti i paesi, non sono quelli inclusi nel grafo
		countryMappa = new HashMap<Integer,Country>();
		for(Country c: countryLista)
			countryMappa.put(c.getStateCode(), c);
	}

	public void creaGrafo(int annoScelto) {
		grafo = new SimpleGraph<Country,DefaultEdge>(DefaultEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.getVertici(annoScelto, countryMappa));
		
		List<Border> adiacenze = dao.getCountryPairs(annoScelto, countryMappa);
		for(Border b: adiacenze)
			this.grafo.addEdge(b.getC1(), b.getC2());
		
	}
	
	public int numeroVertici() {
		return grafo.vertexSet().size();
	}
	
	public int numeroArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<CountryDegree> getPaesiConGrado() {
		List<CountryDegree> result = new ArrayList<CountryDegree>();
		for(Country c: this.grafo.vertexSet())
			result.add(new CountryDegree(c,this.grafo.degreeOf(c)));
		Collections.sort(result);
		return result;	
	}
	
	public int numeroCompConnesse() {
		ConnectivityInspector<Country,DefaultEdge> ci = new ConnectivityInspector<>(this.grafo);
		return ci.connectedSets().size();
	}
}
