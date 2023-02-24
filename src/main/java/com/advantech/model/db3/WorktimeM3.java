/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.model.db3;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.AutoPopulatingList;

/**
 *
 * @author Justin.Yeh
 * http://www.cnblogs.com/chenssy/archive/2012/09/09/2677279.html How to use
 * BigDecimal
 */
@Entity
@Table(name = "Worktime",
        uniqueConstraints = @UniqueConstraint(columnNames = "model_name")
)
@DynamicInsert(true)
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = WorktimeM3.class)
@Audited(targetAuditMode = NOT_AUDITED, withModifiedFlag = true)
public class WorktimeM3 implements java.io.Serializable {

    //@JsonView(View.Public.class)
    private int id;

    //@JsonView(View.Public.class)
    private String modelName;

    //@JsonView(View.Public.class)
    private BigDecimal totalModule = BigDecimal.ZERO;

    //@JsonView(View.Public.class)
    private BigDecimal cleanPanel = BigDecimal.ZERO;

    private int preAssyModuleQty;

    private int keypartA = 0;

    @Column(name = "keypart_a")
    public Integer getKeypartA() {
        return keypartA;
    }

    public void setKeypartA(Integer keypartA) {
        this.keypartA = keypartA;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull
    @NotEmpty
    @Size(min = 0, max = 50)
    @Column(name = "model_name", unique = true, nullable = false, length = 50)
    public String getModelName() {
        return this.modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "total_module", precision = 10, scale = 1)
    public BigDecimal getTotalModule() {
        return this.totalModule;
    }

    public void setTotalModule(BigDecimal totalModule) {
        this.totalModule = autoFixScale(totalModule, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "clean_panel", precision = 10, scale = 1)
    public BigDecimal getCleanPanel() {
        return this.cleanPanel;
    }

    public void setCleanPanel(BigDecimal cleanPanel) {
        this.cleanPanel = autoFixScale(cleanPanel, 1);
    }

    @Column(name = "pre_assy_moduleQty", nullable = true)
    public int getPreAssyModuleQty() {
        return preAssyModuleQty;
    }

    public void setPreAssyModuleQty(int preAssyModuleQty) {
        this.preAssyModuleQty = preAssyModuleQty;
    }

    private BigDecimal autoFixScale(BigDecimal d, int scale) {
        return d == null ? null : d.setScale(scale, RoundingMode.HALF_UP);
    }
}
