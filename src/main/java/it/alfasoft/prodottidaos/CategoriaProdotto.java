package it.alfasoft.prodottidaos;

import it.alfasoft.daosimple.IDto;

import java.util.HashSet;
import java.util.Set;

public class CategoriaProdotto implements IDto<Integer> {
    private Integer idCategoria;
    private String nomeCategoria;
    private Set<Prodotto> prodotti = new HashSet<>();

    public CategoriaProdotto(){
    }

    public CategoriaProdotto(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
    public CategoriaProdotto(int idCategoria, String nomeCategoria){
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
    }

    public Integer getId() { return this.idCategoria; }
    public String getNomeCategoria() { return this.nomeCategoria;}
    public Set<Prodotto> getProdotti(){ return this.prodotti;}
    public void setIdCategoria(int idRuolo) { this.idCategoria = idCategoria;}
    public void setNomeCategoria(String nomeCategoria) { this.nomeCategoria = nomeCategoria;}
    public void setUtenti(Set<Prodotto> prodotti){ this.prodotti = prodotti;}

    public void addProdotto(Prodotto prodotto){
        this.getProdotti().add(prodotto);
    }
    public void removeProdotto(Prodotto prodotto){
        this.getProdotti().remove(prodotto);
    }

    @Override
    public String toString(){
        return "Categoria: " + this.getNomeCategoria();
    }
}
