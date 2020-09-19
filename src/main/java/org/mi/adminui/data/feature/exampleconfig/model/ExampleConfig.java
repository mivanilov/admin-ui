package org.mi.adminui.data.feature.exampleconfig.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.mi.adminui.data.core.model.CrudEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(schema = "admin_ui", name = "example_config")
public class ExampleConfig implements CrudEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "example_config_type_id")
    @JsonIgnore
    private ExampleConfigType exampleConfigType;

    private boolean active;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(final Visibility visibility) {
        this.visibility = visibility;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(final LocalDate createDate) {
        this.createDate = createDate;
    }

    public ExampleConfigType getExampleConfigType() {
        return exampleConfigType;
    }

    public void setExampleConfigType(final ExampleConfigType exampleConfigType) {
        this.exampleConfigType = exampleConfigType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public String getExampleConfigTypeValue() {
        return getExampleConfigType() != null ? getExampleConfigType().getValue() : null;
    }

    public void setExampleConfigTypeValue(final String exampleConfigTypeValue) {
        ExampleConfigType exampleConfigType = new ExampleConfigType();
        exampleConfigType.setId(Long.valueOf(exampleConfigTypeValue));
        setExampleConfigType(exampleConfigType);
    }

    public String getExampleConfigTypeText() {
        return getExampleConfigType().getText();
    }

    @Override
    public boolean equals(final Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public enum Visibility {
        PUBLIC,
        PRIVATE
    }
}
