package com.legacybuy.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EntityListeners({ AuditingEntityListener.class })
@Data
@ToString
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
@Audited
abstract public class AbstractEntity implements Serializable {

	private static final long serialVersionUID = -7368627524400450815L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	Long id;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable = true, updatable = false)
	Date dateCreated;

	@LastModifiedDate
	@Version
	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable = false, updatable = true)
	Date lastUpdated;

	@CreatedBy
	@Column(insertable = true, updatable = false)
	String createdBy;

	@LastModifiedBy
	@Column(insertable = false, updatable = true)
	String updatedBy;
}
