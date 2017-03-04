package com.legacybuy.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.legacybuy.enums.TransactionStatus;
import com.legacybuy.enums.TransactionType;

@Entity
public class Transaction extends AbstractEntity {

	private static final long serialVersionUID = -3873366872089329698L;

	Double amount;
	String debitAccount;
	String creditAccount;
	String remarks;

	@ManyToOne
	Contract contract;

	TransactionStatus status;
	TransactionType transactionType;
}
