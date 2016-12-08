package util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import forex.genetic.util.RandomUtil;

public class RandomUtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void siSon2NumerosEnterosPositivosDebeRetornarPositivoEntreEsosNumeros() {
		int min = 1;
		int max = 10;
		int value = RandomUtil.generateNegativePositive(min, max);
		System.out.println("siSon2NumerosEnterosPositivosDebeRetornarPositivoEntreEsosNumeros=[" + min
				+ "," + max + "]" + value);
		Assert.assertTrue("Value is too low." + value, value >= min);
		Assert.assertTrue("Value is too high." + value, value <= max);
	}

	@Test
	public final void siSon2NumerosEnterosPositivosYElMinimoEsCeroDebeRetornarPositivoOCeroEntreEsosNumeros() {
		int min = 0;
		int max = 10;
		int value = RandomUtil.generateNegativePositive(min, max);
		System.out.println(
				"siSon2NumerosEnterosPositivosYElMinimoEsCeroDebeRetornarPositivoOCeroEntreEsosNumeros=[" + min + "," + max + "]" + value);
		Assert.assertTrue("Value is too low." + value, value >= min);
		Assert.assertTrue("Value is too high." + value, value <= max);
	}

	@Test
	public final void siSon2NumerosEnterosNegativosDebeRetornarNegativoEntreEsosNumeros() {
		int min = -10;
		int max = -1;
		int value = RandomUtil.generateNegativePositive(min, max);
		System.out.println("siSon2NumerosEnterosNegativosDebeRetornarNegativoEntreEsosNumeros=[" + min + "," + max + "]" + value);
		Assert.assertTrue("Value is too low." + value, value >= min);
		Assert.assertTrue("Value is too high." + value, value <= max);
	}

	@Test
	public final void siElMinimoEsEnteroNegativoYElMaximoEsPositivoDebeRetornarNegativoOPositivoEntreEsosNumeros() {
		int min = -1000;
		int max = 4000;
		int value = RandomUtil.generateNegativePositive(min, max);
		System.out.println(
				"MIN MAX SEMANA siElMinimoEsEnteroNegativoYElMaximoEsPositivoDebeRetornarNegativoOPositivoEntreEsosNumeros=[" + min + "," + max + "]" + value);
		Assert.assertTrue("Value is too low." + value, value >= min);
		Assert.assertTrue("Value is too high." + value, value <= max);
	}

	@Test
	public final void siElMinimoEsEnteroNegativoYElMaximoEsCeroDebeRetornarNegativoOCeroEntreEsosNumeros() {
		int min = -1000;
		int max = 0;
		int value = RandomUtil.generateNegativePositive(min, max);
		System.out
				.println("siElMinimoEsEnteroNegativoYElMaximoEsCeroDebeRetornarNegativoOCeroEntreEsosNumeros=[" + min + "," + max + "]" + value);
		Assert.assertTrue("Value is too low." + value, value >= min);
		Assert.assertTrue("Value is too high." + value, value <= max);
	}

	@Test
	public final void siSon2NumerosDoublesPositivosDebeRetornarPositivoEntreEsosNumeros() {
		double min = 1.0D;
		double max = 10.0D;
		double value = RandomUtil.generateNegativePositive(min, max);
		System.out.println("siSon2NumerosDoublesPositivosDebeRetornarPositivoEntreEsosNumeros=[" + min + "," + max + "]" + value);
		Assert.assertTrue("Value is too low." + value, value >= min);
		Assert.assertTrue("Value is too high." + value, value <= max);
	}

	@Test
	public final void siSon2NumerosDoublesPositivosYElMinimoEsCeroDebeRetornarPositivoOCeroEntreEsosNumeros() {
		double min = 0.0D;
		double max = 10.0D;
		double value = RandomUtil.generateNegativePositive(min, max);
		System.out.println(
				"siSon2NumerosDoublesPositivosYElMinimoEsCeroDebeRetornarPositivoOCeroEntreEsosNumeros=[" + min + "," + max + "]" + value);
		Assert.assertTrue("Value is too low." + value, value >= min);
		Assert.assertTrue("Value is too high." + value, value <= max);
	}

	@Test
	public final void siSon2NumerosDoublesNegativosDebeRetornarNegativoEntreEsosNumeros() {
		double min = -10.0D;
		double max = -1.0D;
		double value = RandomUtil.generateNegativePositive(min, max);
		System.out.println("siSon2NumerosDoublesNegativosDebeRetornarNegativoEntreEsosNumeros=[" + min + "," + max + "]" + value);
		Assert.assertTrue("Value is too low." + value, value >= min);
		Assert.assertTrue("Value is too high." + value, value <= max);
	}

	@Test
	public final void siElMinimoEsDoubleNegativoYElMaximoEsPositivoDebeRetornarNegativoOPositivoEntreEsosNumeros() {
		double min = -1000.0D;
		double max = 4000.0D;
		double value = RandomUtil.generateNegativePositive(min, max);
		System.out.println(
				"MIN MAX SEMANA siElMinimoEsDoubleNegativoYElMaximoEsPositivoDebeRetornarNegativoOPositivoEntreEsosNumeros=[" + min + "," + max + "]" + value);
		Assert.assertTrue("Value is too low." + value, value >= min);
		Assert.assertTrue("Value is too high." + value, value <= max);
	}

	@Test
	public final void siElMinimoEsDoubleNegativoYElMaximoEsCeroDebeRetornarNegativoOCeroEntreEsosNumeros() {
		double min = -1000.0D;
		double max = 0.0D;
		double value = RandomUtil.generateNegativePositive(min, max);
		System.out.println("siElMinimoEsDoubleNegativoYElMaximoEsCeroDebeRetornarNegativoOCeroEntreEsosNumeros=[" + min + "," + max + "]" + value);
		Assert.assertTrue("Value is too low." + value, value >= min);
		Assert.assertTrue("Value is too high." + value, value <= max);
	}

}
