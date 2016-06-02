/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tddforex.test;

import forex.genetic.util.DateUtil;
import forex.genetic.util.NumberUtil;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ricardorq85
 */
public class UtilTest {

    public UtilTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void cuandoAdiciono1MesAEnero012015DebeRetornarFebrero012015() throws ParseException {
        // Arrange
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date expectedDate = format.parse("2015/02/01");

        Date date = format.parse("2015/01/01");

        // Act
        Date date2 = DateUtil.adicionarMes(date);

        // Assert
        Assert.assertEquals(expectedDate, date2);

    }

    @Test
    public void cuandoAdiciono1MesADiciembre012015DebeRetornarEnero012016() throws ParseException {
        // Arrange
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date expectedDate = format.parse("2016/01/01");

        Date date = format.parse("2015/12/01");

        // Act
        Date date2 = DateUtil.adicionarMes(date);

        // Assert
        Assert.assertEquals(expectedDate, date2);
    }

    @Test
    public void cuandoRedondeoPorDefecto() {
        double d = 3.14159;
        Assert.assertEquals("3.0", String.valueOf(NumberUtil.round(d)));
    }

    @Test
    public void cuandoRedondeo3Digitos() {
        double d = 3.14159;
        Assert.assertEquals("3.142", String.valueOf(NumberUtil.round(d, 3)));
    }
    
    @Test
    public void cuandoRedondeo4Digitos() {
        double d = 3.14159;
        Assert.assertEquals("3.1416", String.valueOf(NumberUtil.round(d, 4)));
    }

}
