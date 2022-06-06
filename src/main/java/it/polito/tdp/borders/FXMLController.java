
package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.CountryDegree;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxStato"
    private ComboBox<Country> boxStato; // Value injected by FXMLLoader
    
    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	this.boxStato.getItems().clear();
    	int annoScelto;
    	try {
    		annoScelto = Integer.parseInt(txtAnno.getText());
    		if(annoScelto < 1816 || annoScelto > 2016)
    			txtResult.setText("L'anno deve essere compreso tra il 1816 e il 2016");
    		else {
    			model.creaGrafo(annoScelto);
    			this.boxStato.getItems().addAll(this.model.getVertici());
    			txtResult.appendText("Grafo creato!\n");
    			txtResult.appendText("# VERTICI: " + model.numeroVertici() + "\n");
    			txtResult.appendText("# ARCHI: " + model.numeroArchi() + "\n");
    			txtResult.appendText("\nLISTA DI PAESI CON NUMERO DI STATI CONFINANTI: \n");
    			List<CountryDegree> result = model.getPaesiConGrado();
    			for(CountryDegree cd: result)
    				txtResult.appendText(cd.toString() + "\n");
    			txtResult.appendText("\n# COMPONENTI CONNESSE: " + model.numComponentiConnesse());
    		}
    	} catch(NumberFormatException nfe) {
    		txtResult.setText("Devi inserire un valore numerico nel campo 'Anno', compreso tra 1816 e 2016");
    	}
    }

    @FXML
    void doStatiRaggiungibili(ActionEvent event) {
    	txtResult.clear();
    	Country statoScelto = this.boxStato.getValue();
    	List<Country> statiRaggiungibili = this.model.getReachableCountries(statoScelto);
    	txtResult.appendText("STATI RAGGIUGIBILI da " + statoScelto + ":\n");
    	int indice = 1;
    	for(Country c: statiRaggiungibili) {
    		txtResult.appendText("\n" + indice + ") " + c.toString());
    		indice++;
    	}
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
