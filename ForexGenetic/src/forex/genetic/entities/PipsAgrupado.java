package forex.genetic.entities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PipsAgrupado {
	private String format;
	private int tempCount, count;
	private double tempPips, pips;
	private DateFormat dateFormat;
	private String currentValue;

	public PipsAgrupado(String format) {
		super();
		this.format = format;
		this.dateFormat = new SimpleDateFormat(format);
	}

	public void addOrder(Order order) {
		if (order == null) {
			throw new IllegalArgumentException("Order cannot be null");
		}
		String strDate = dateFormat.format(order.getOpenDate());
		if (strDate.equals(currentValue)) {
			tempCount++;
			tempPips += order.getPips();
		} else {
			if (currentValue != null) {
				pips += (tempPips / tempCount);
				count++;
			}
			currentValue = strDate;
			tempCount = 1;
			tempPips = order.getPips();
		}
	}

	public void finish() {
		if (tempCount > 0) {
			pips += (tempPips / tempCount);
			count++;
		}
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getTempCount() {
		return tempCount;
	}

	public void setTempCount(int tempCount) {
		this.tempCount = tempCount;
	}

	public double getTempPips() {
		return tempPips;
	}

	public void setTempPips(double tempPips) {
		this.tempPips = tempPips;
	}

	public double getPips() {
		return pips;
	}

	public void setPips(double pips) {
		this.pips = pips;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
