package com.melorriaga.pokemon.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by melorriaga on 2/2/17.
 */
public abstract class Model {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
