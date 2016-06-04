/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.dao.IndividuoDAO;
import forex.genetic.entities.Individuo;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class BorradoInconsistentesStopLossManager extends BorradoManager {
        
    public BorradoInconsistentesStopLossManager() {
    }
    
    @Override
    public List<Individuo> consultarIndividuos() throws ClassNotFoundException, SQLException {
        List<Individuo> individuos;
        IndividuoDAO individuoDAO = new IndividuoDAO(conn);
        individuos = individuoDAO.consultarIndividuosStopLossInconsistente(100);
        return individuos;
    }
    
    @Override
    public void borrarIndividuos() throws ClassNotFoundException, SQLException {
        String tipoProceso = "STOP_LOSS_MINIMO";
        super.borrarIndividuos(tipoProceso);
    }
    
}
