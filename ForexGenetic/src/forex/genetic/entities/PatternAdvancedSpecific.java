/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

/**
 *
 * @author rrojasq
 */
public class PatternAdvancedSpecific implements Comparable<PatternAdvancedSpecific> {

    private PatternAdvanced patternAdvanced = null;
    private int index = 0;

    /**
     *
     * @param patternAdvanced
     * @param index
     */
    public PatternAdvancedSpecific(PatternAdvanced patternAdvanced, int index) {
        this.patternAdvanced = patternAdvanced;
        this.index = index;
    }

    /**
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     *
     * @return
     */
    public PatternAdvanced getPatternAdvanced() {
        return patternAdvanced;
    }

    /**
     *
     * @param patternAdvanced
     */
    public void setPatternAdvanced(PatternAdvanced patternAdvanced) {
        this.patternAdvanced = patternAdvanced;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PatternAdvancedSpecific other = (PatternAdvancedSpecific) obj;
        if (this.patternAdvanced != other.patternAdvanced && (this.patternAdvanced == null || !this.patternAdvanced.equals(other.patternAdvanced))) {
            return false;
        }
        if (this.index != other.index) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.patternAdvanced != null ? this.patternAdvanced.hashCode() : 0);
        hash = 37 * hash + this.index;
        return hash;
    }

    @Override
    public String toString() {
        Order nextOrder = patternAdvanced.getPattern().get(index);
        return "{nextOrder=" + nextOrder.getPips() + ", value=" + patternAdvanced.getValue() + '}';
    }

    @Override
    public int compareTo(PatternAdvancedSpecific o) {
        return (new Integer(this.getPatternAdvanced().getPattern().size()).compareTo(new Integer(o.getPatternAdvanced().getPattern().size())));
    }
}
