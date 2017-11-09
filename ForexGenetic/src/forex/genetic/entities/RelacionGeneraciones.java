/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.io.Serializable;

/**
 *
 * @author ricardorq85
 */
public class RelacionGeneraciones implements Comparable<RelacionGeneraciones>, Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 201205232209L;

    /**
     *
     */
    protected LearningParametrosIndividuo learningParametrosIndividuo = null;
    private float value = 0.0F;

    /**
     *
     * @param learningParametrosIndividuo
     */
    public RelacionGeneraciones(LearningParametrosIndividuo learningParametrosIndividuo) {
        this.learningParametrosIndividuo = learningParametrosIndividuo;
    }

    /**
     *
     * @return
     */
    public LearningParametrosIndividuo getLearningParametrosIndividuo() {
        return learningParametrosIndividuo;
    }

    /**
     *
     * @param learningParametrosIndividuo
     */
    public void setLearningParametrosIndividuo(LearningParametrosIndividuo learningParametrosIndividuo) {
        this.learningParametrosIndividuo = learningParametrosIndividuo;
    }

    /**
     *
     * @return
     */
    public float getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RelacionGeneraciones other = (RelacionGeneraciones) obj;
        if (this.learningParametrosIndividuo != other.learningParametrosIndividuo && (this.learningParametrosIndividuo == null || !this.learningParametrosIndividuo.equals(other.learningParametrosIndividuo))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.learningParametrosIndividuo != null ? this.learningParametrosIndividuo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "RelacionGeneraciones{" + "learningParametrosIndividuo=" + learningParametrosIndividuo + ", value=" + value + '}';
    }

    @Override
    public int compareTo(RelacionGeneraciones o) {
        return Float.compare(this.getValue(), o.getValue());
    }
}
