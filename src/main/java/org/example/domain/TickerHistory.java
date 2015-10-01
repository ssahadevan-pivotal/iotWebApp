package org.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;

@Entity
@Table(name="tickerhistory")
public class TickerHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String ticker;

	@Column(nullable = false)
	private String price;

	@Column(nullable = false)
	private String pe;

	@Column
	private String recommendation;

	@Column
	private String yield;
	
	/* To Do
	@Column
	@CreatedDate
	private DateTime createdDate;
	*/

	public TickerHistory() {
	}

	public TickerHistory(String ticker
			             , String name
			             , String price
			             , String pe
			             , String recommendation
			             , String yield
			             ) {
		this.name = name;
		this.ticker = ticker;
		this.price = price;
		this.pe = pe;
		this.recommendation = recommendation;
		this.yield = yield;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPe() {
		return pe;
	}

	public void setPe(String pe) {
		this.pe = pe;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public String getYield() {
		return yield;
	}

	public void setYield(String yield) {
		this.yield = yield;
	}
}
