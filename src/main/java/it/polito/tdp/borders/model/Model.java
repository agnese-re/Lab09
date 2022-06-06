package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

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
	
	public List<Country> getVertici() {
		List<Country> countries = new ArrayList<Country>(this.grafo.vertexSet());
		Collections.sort(countries);
		return countries;
	}
	
	public List<CountryDegree> getPaesiConGrado() {
		List<CountryDegree> result = new ArrayList<CountryDegree>();
		for(Country c: this.grafo.vertexSet())
			result.add(new CountryDegree(c,this.grafo.degreeOf(c)));
		Collections.sort(result);
		return result;	
	}
	
	public int numComponentiConnesse() {
		ConnectivityInspector<Country,DefaultEdge> ci =
				new ConnectivityInspector<>(this.grafo);
		List<Set<Country>> compConnesse = ci.connectedSets();
		return compConnesse.size();
	}
	
	/* PUNTO 2) STATI RAGGIUNGIBILI */
	public List<Country> statiRaggiungibili(Country country) {
		ConnectivityInspector<Country,DefaultEdge> ci =
				new ConnectivityInspector<>(this.grafo);
		List<Country> raggiungibili = new ArrayList<>(ci.connectedSetOf(country));
		raggiungibili.remove(country);		// una sola copia di ogni paese
		Collections.sort(raggiungibili);
		return raggiungibili;
	}
	
	public List<Country> getReachableCountries(Country country) {
		
		List<Country> raggiungibili = new ArrayList<>(statiRaggiungibiliJGT(country));
		raggiungibili.remove(country);
		Collections.sort(raggiungibili); 
		
		/* List<Country> parziale = new ArrayList<>();
		parziale.add(country);	// aggiungo a parziale il vertice di partenza
		this.statiRaggiungibiliRicorsione(parziale);
		parziale.remove(country);
		Collections.sort(parziale);
		return parziale; */
		
		/* List<Country> raggiungibili = new ArrayList<>(statiRaggiungibiliIterativa(country));
		raggiungibili.remove(country);
		Collections.sort(raggiungibili); */
		
		return raggiungibili;
	}
	
	/* METODI LIBRERIA JGRAPHT */
	private List<Country> statiRaggiungibiliJGT(Country country) {
		/* iteratore che visita il grafo a partire dal vertice specificato. La visita si
		 	limita alla componente connessa del grafo, contenente tale vertice. Se il ver-
		 	tice di partenza non viene specificato, la visita inizia da un vertice arbitra-
		 	rio e attraversa l'intero grafo, non essendo limitata ad una comp. connessa */
		
		List<Country> raggiungibili = new ArrayList<Country>();
		
		// Versione 1: utilizzo un BreadthFirstIterator
		GraphIterator<Country,DefaultEdge> bfi =
				new BreadthFirstIterator<Country,DefaultEdge>(this.grafo,country);
		while(bfi.hasNext()) 
			raggiungibili.add(bfi.next());
		
		
		// Versione 2: utilizzo un DepthFirstIterator
		/* GraphIterator<Country,DefaultEdge> dfi =
				new DepthFirstIterator<Country,DefaultEdge>(this.grafo,country);
		while(dfi.hasNext())
			raggiungibili.add(dfi.next()); */
		
		return raggiungibili;
	}
	
	/* METODO RICORSIVO */
	private void statiRaggiungibiliRicorsione(List<Country> parziale) {
		
		List<Country> vicini = Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1));
		for(Country vicino: vicini) 
			if(!parziale.contains(vicino)) {
				parziale.add(vicino);
				statiRaggiungibiliRicorsione(parziale);
				// NO BACKTRACKING! Ogni stato che puo' essere raggiunto dal country di partenza rimane nella lista
				// Avrei rimosso l'ultimo stato nel caso in cui avessi ricercato un cammino massimo/minimo.
			}	
	}
	
	/* METODO ITERATIVO */
	private List<Country> statiRaggiungibiliIterativa(Country countryPartenza) {
		List<Country> visitati = new ArrayList<Country>();	// quali vertici ho visitato
		List<Country> daVisitare = new ArrayList<Country>();
		
		// Visito il nodo di partenza
		visitati.add(countryPartenza);
		
		// Aggiungo ai vertici da visitare tutti i vicini del vertice appena inserito
		// daVisitare.addAll(Graphs.neighborListOf(this.grafo,countryPartenza));
		List<Country> vicini = Graphs.neighborListOf(this.grafo, countryPartenza);
		for(Country vicino: vicini)
			daVisitare.add(vicino);
		
		while(daVisitare.size() != 0)	{	// while(!daVisitare.isEmpty())
			
			// Rimuovo il vertice in testa alla coda e lo memorizzo in una variabile temporanea
			Country temp = daVisitare.remove(0);
			
			// Aggiungo il vertice appena rimosso alla lista dei vertici visitati
			visitati.add(temp);
			
			// Prendo tutti i vicini del vertice rimosso da 'daVisitare' e aggiunto a 'visitati'
			List<Country> viciniDelVicino = Graphs.neighborListOf(this.grafo, temp);
			
			// Rimuovo da questa lista i vertici che sono gia' stati visitati
			viciniDelVicino.removeAll(visitati);
			
			// Rimuovo quelli che so gia' che devo visitare
			viciniDelVicino.removeAll(daVisitare);
			
			// Aggiungo i rimanenti alla coda dei vertici da visitare
			daVisitare.addAll(viciniDelVicino);
		}
		
		return visitati;
	}
		
	
}
