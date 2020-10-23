/*
 * This file is generated by jOOQ.
 */
package com.freighttrust.jooq.tables.pojos;


import com.freighttrust.persistence.postgres.bindings.TsTzRange;

import java.io.Serializable;

import javax.annotation.processing.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.13.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TradingPartnerHistory implements Serializable {

    private static final long serialVersionUID = 166591244;

    private Long      id;
    private String    name;
    private String    email;
    private Long      keyPairId;
    private TsTzRange validity;

    public TradingPartnerHistory() {}

    public TradingPartnerHistory(TradingPartnerHistory value) {
        this.id = value.id;
        this.name = value.name;
        this.email = value.email;
        this.keyPairId = value.keyPairId;
        this.validity = value.validity;
    }

    public TradingPartnerHistory(
        Long      id,
        String    name,
        String    email,
        Long      keyPairId,
        TsTzRange validity
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.keyPairId = keyPairId;
        this.validity = validity;
    }

    public Long getId() {
        return this.id;
    }

    public TradingPartnerHistory setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public TradingPartnerHistory setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public TradingPartnerHistory setEmail(String email) {
        this.email = email;
        return this;
    }

    public Long getKeyPairId() {
        return this.keyPairId;
    }

    public TradingPartnerHistory setKeyPairId(Long keyPairId) {
        this.keyPairId = keyPairId;
        return this;
    }

    public TsTzRange getValidity() {
        return this.validity;
    }

    public TradingPartnerHistory setValidity(TsTzRange validity) {
        this.validity = validity;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TradingPartnerHistory other = (TradingPartnerHistory) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        }
        else if (!email.equals(other.email))
            return false;
        if (keyPairId == null) {
            if (other.keyPairId != null)
                return false;
        }
        else if (!keyPairId.equals(other.keyPairId))
            return false;
        if (validity == null) {
            if (other.validity != null)
                return false;
        }
        else if (!validity.equals(other.validity))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.email == null) ? 0 : this.email.hashCode());
        result = prime * result + ((this.keyPairId == null) ? 0 : this.keyPairId.hashCode());
        result = prime * result + ((this.validity == null) ? 0 : this.validity.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TradingPartnerHistory (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(email);
        sb.append(", ").append(keyPairId);
        sb.append(", ").append(validity);

        sb.append(")");
        return sb.toString();
    }
}
