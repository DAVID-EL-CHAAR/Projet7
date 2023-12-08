package com.nnk.springboot.domain;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "curvepoint")
public class CurvePoint {
    // TODO: Map columns in data table CURVEPOINT with corresponding java fields
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id")
	    private Integer id;

	    @Column(name = "curveId")
	    private Integer curveId;

	    @Column(name = "asOfDate")
	    private Timestamp asOfDate;

	    @Column(name = "term")
	    private Double term;

	    @Column(name = "value")
	    private Double value;

	    @Column(name = "creationDate")
	    private Timestamp creationDate;

	    // Constructeur par défaut
	    public CurvePoint() {
	    }

	    // Getters et setters
	    public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public Integer getCurveId() {
	        return curveId;
	    }

	    public void setCurveId(Integer curveId) {
	        this.curveId = curveId;
	    }

	    public Timestamp getAsOfDate() {
	        return asOfDate;
	    }

	    public void setAsOfDate(Timestamp asOfDate) {
	        this.asOfDate = asOfDate;
	    }

	    public Double getTerm() {
	        return term;
	    }

	    public void setTerm(Double term) {
	        this.term = term;
	    }

	    public Double getValue() {
	        return value;
	    }

	    public void setValue(Double value) {
	        this.value = value;
	    }

	    public Timestamp getCreationDate() {
	        return creationDate;
	    }

	    public void setCreationDate(Timestamp creationDate) {
	        this.creationDate = creationDate;
	    }

}
