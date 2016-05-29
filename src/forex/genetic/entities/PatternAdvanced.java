/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rrojasq
 */
public class PatternAdvanced {

    private List<Order> pattern = new ArrayList<Order>();
    private double value = 1.0D;

    public PatternAdvanced() {
        this(null);
    }

    public PatternAdvanced(List<Order> pattern) {
        this.pattern = pattern;
    }

    public boolean contains(PatternAdvanced other) {
        if ((this.pattern == null) || (other.pattern == null)) {
            return false;
        }
        if (this.pattern.size() < other.pattern.size()) {
            return false;
        }
        boolean contains = true;
        for (int i = 0; (contains) && (i < this.pattern.size()) && (i < other.pattern.size()); i++) {
            Order order = this.pattern.get(i);
            Order otherOrder = other.pattern.get(i);
            contains = ((Double.compare(order.getPips(), 0.0D) == Double.compare(otherOrder.getPips(), 0.0D)));
        }
        return contains;
    }

    public boolean containsBreakOrder() {
        boolean valueBreak = false;
        if (this.pattern.size() > 0) {
            Order firstOrder = this.pattern.get(0);
            for (int i = 1; i < this.pattern.size() && !valueBreak; i++) {
                Order order = this.pattern.get(i);
                valueBreak = !(Double.compare(firstOrder.getPips(), 0.0D) == Double.compare(order.getPips(), 0.0D));
            }
        }
        return valueBreak;
    }

    public boolean containsCurrent(List<Order> currentPattern) {
        if (this.pattern.size() > currentPattern.size()) {
            List<Order> tempPatternList = this.pattern.subList(0, currentPattern.size());
            PatternAdvanced tempThisPattern = new PatternAdvanced(tempPatternList);
            PatternAdvanced tempOtherPattern = new PatternAdvanced(currentPattern);
            return (tempThisPattern.equalsPattern(tempOtherPattern));
        } else {
            return false;
        }
    }

    public List<Order> getPattern() {
        return pattern;
    }

    public void setPattern(List<Order> pattern) {
        this.pattern = pattern;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PatternAdvanced{" + "pattern size=" + pattern.size() + ", value=" + value + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PatternAdvanced other = (PatternAdvanced) obj;
        boolean valueEquals = (this.pattern.size() == other.pattern.size());
        valueEquals = valueEquals && (this.equalsPattern(other));
        return valueEquals;
    }

    private boolean equalsPattern(PatternAdvanced other) {
        boolean valueEquals = true;
        for (int i = 0; i < this.pattern.size() && valueEquals; i++) {
            Order order = this.pattern.get(i);
            Order order2 = other.pattern.get(i);
            valueEquals = (order.comparePattern(order2));
        }
        return valueEquals;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash;
        return hash;
    }
}
