/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import forex.genetic.dao.helper.IndicatorHelper;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.RangoOperacionIndividuo;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class IndicatorDAO {

    protected Connection connection = null;

    /**
     *
     * @param connection
     */
    public IndicatorDAO(Connection connection) {
        this.connection = connection;
    }

}
