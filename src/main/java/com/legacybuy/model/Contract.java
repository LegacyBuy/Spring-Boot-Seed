package com.legacybuy.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.legacybuy.enums.Category;
import com.legacybuy.enums.ContractStatus;
import com.legacybuy.enums.CustomerType;

@Entity
@Audited
@Data
@EqualsAndHashCode(callSuper = false)
public class Contract extends AbstractEntity {

	private static final long serialVersionUID = 2667067547300690933L;

	@OneToOne
	User buyer;

	@OneToOne
	User seller;

	ContractStatus contractStatus;

	int inspectionDays; // How many days need to complete to contract
	Date expiryDate; // Set expire after payment is received

	String name;
	String description;

	Category category;

	CustomerType scPayer;
	
	Boolean buyerApproved = false;
	
	Boolean sellerApproved = false;
	
	List<String> notes;

	@OneToMany
	@JoinColumn(name = "contract_id")
	@NotAudited
	List<Transaction> transactions;
}
