package org.mi.adminui.data.feature.user.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.mi.adminui.data.core.model.CrudEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "admin_ui", name = "user")
public class User implements CrudEntity<String> {

    @Id
    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    public User() {
    }

    public User(final String email, final String name, final RoleType role) {
        this.email = email;
        this.name = name;
        this.role = role;
    }

    @Override
    public String getId() {
        return getEmail();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(final RoleType role) {
        this.role = role;
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

    public enum RoleType {
        ADMIN("ROLE_ADMIN"),
        BUSINESS("ROLE_BUSINESS");

        private String securityName;

        RoleType(final String securityName) {
            this.securityName = securityName;
        }

        public String getSecurityName() {
            return securityName;
        }
    }
}

