package com.melorriaga.pokemon.model;

import java.util.List;

/**
 * Created by melorriaga on 2/2/17.
 */
public class APIResourceList extends Model {

    public Integer count;
    public String next;
    public Boolean previous;
    public List<NamedAPIResource> results;

}
