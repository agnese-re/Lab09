
package it.polito.tdp.borders;

import java.net.URL;
import java.time.Year;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Confine;
import it.polito.tdp.borders.model.Country;
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

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML // fx:id="cmbStato"
    private ComboBox<Country> cmbStato; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	String annoStringa = txtAnno.getText();
    	// controlli anno stringa
    	String msg = model.creaGrafo(Year.of(Integer.parseInt(annoStringa)));
    	cmbStato.getItems().addAll(model.getCountries(Year.of(Integer.parseInt(annoStringa))));
    	txtResult.appendText(msg+"\n");
    	
    	List<Confine> confini = model.elencoStati(Year.of(Integer.parseInt(annoStringa)));
    	for(Confine c: confini)
    		txtResult.appendText(c.toString()+"\n");
    }

    @FXML
    void handleStatiRaggiungibili(ActionEvent event) {
    	Country countryScelto = cmbStato.getValue();
    	// controllo stato scelto diverso da null
    	model.calcolaPercorso();
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	assert cmbStato != null : "fx:id=\"cmbStato\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
