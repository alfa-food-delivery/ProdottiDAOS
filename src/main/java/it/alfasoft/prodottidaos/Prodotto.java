package it.alfasoft.prodottidaos;

import it.alfasoft.daosimple.IDto;

import java.util.HashSet;
import java.util.Set;

public class Prodotto implements IDto<Integer> {
    private Integer idProdotto;
    private String nomeProdotto;
    private String descrizione;
    private Double prezzo;
    private CategoriaProdotto categoriaProdotto;

    public Prodotto(){ }

    public Prodotto(String nomeProdotto, String descrizione){
        this.nomeProdotto = nomeProdotto;
        this.descrizione = descrizione;
    }

    public Prodotto(String nomeProdotto, Double prezzo){
        this.nomeProdotto = nomeProdotto;
        this.prezzo = prezzo;
    }

    public Prodotto(String nomeProdotto, String descrizione, Double prezzo){
        this.nomeProdotto = nomeProdotto;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
    }
    public Prodotto(CategoriaProdotto categoriaProdotto, String nomeProdotto, String descrizione, Double prezzo){
        this.categoriaProdotto = categoriaProdotto;
        this.nomeProdotto = nomeProdotto;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
    }
    public Prodotto(int idProdotto, String nomeProdotto, String descrizione, Double prezzo){
        this.idProdotto = idProdotto;
        this.nomeProdotto = nomeProdotto;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
    }
    public Prodotto(int idProdotto, CategoriaProdotto categoriaProdotto, String nomeProdotto, String descrizione, Double prezzo){
        this.idProdotto = idProdotto;
        this.categoriaProdotto = categoriaProdotto;
        this.nomeProdotto = nomeProdotto;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
    }
    //GETTERS
    @Override
    public Integer getId(){ return this.idProdotto; }
    public String getNomeProdotto(){
        return this.nomeProdotto;
    }
    public String getDescrizione(){ return this.descrizione; }
    public Double getPrezzo(){ return this.prezzo;}
    public CategoriaProdotto getCategoriaProdotto(){ return this.categoriaProdotto;}
    //SETTERS
    public void setIdProdotto(Integer idProdotto){ this.idProdotto = idProdotto; }
    public void setNomeProdotto(String nomeProdotto){ this.nomeProdotto = nomeProdotto; }
    public void setDescrizione(String descrizione){ this.descrizione = descrizione;}
    public void setPrezzo(Double prezzo){ this.prezzo = prezzo;}
    public void setCategoriaProdotto(CategoriaProdotto categoriaProdotto){ this.categoriaProdotto = categoriaProdotto;}

    @Override
    public String toString(){
        return "Prodotto: " + this.getNomeProdotto() + " , " + this.getCategoriaProdotto() + " | Prezzo: " + this.getPrezzo();
    }
}
