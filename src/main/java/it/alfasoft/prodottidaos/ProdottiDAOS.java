package it.alfasoft.prodottidaos;

import it.alfasoft.daosimple.DaoException;
import it.alfasoft.daosimple.DaoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProdottiDAOS extends DaoImpl<Prodotto,Integer> {

    public ProdottiDAOS(){
        cateogrieDAO.setTableName("food_delivery.categorie_prodotto");
    }


    CategorieDAOS cateogrieDAO = new CategorieDAOS();
    @Override
    public Prodotto convertToDto(ResultSet resultSet) throws DaoException {
        Prodotto u = null;
        try{
            //Categoria prodotto
            int idCategoriaProdotto = resultSet.getInt("id_categoria_prodotto");
            CategoriaProdotto categoriaAssociata = null;
            if(idCategoriaProdotto!=0){
                categoriaAssociata = cateogrieDAO.getById(idCategoriaProdotto);
            }

            u = new Prodotto(
                    resultSet.getInt("id_prodotto"),
                    resultSet.getString("nome"),
                    resultSet.getString("descrizione"),
                    resultSet.getDouble("prezzo")
            );

            if(categoriaAssociata!=null){ u.setCategoriaProdotto(categoriaAssociata); }
            return u;
        }catch(Exception sqle){ sqle.printStackTrace(); throw new DaoException();}
    }

    @Override
    public boolean checkOggetto(Prodotto prodotto) throws DaoException {
        if(prodotto.getNomeProdotto()==null){ throw new DaoException("Il nuovo nome non puo' essere vuoto"); }
        if(prodotto.getPrezzo()==null || prodotto.getPrezzo()<=0 ){ throw new DaoException("Il nuovo prezzo non è valido");}
        return true;
    }

    @Override
    public Integer getGeneratedKey(Statement statement) throws DaoException {
        try{
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        }catch (SQLException e) { e.printStackTrace(); throw new DaoException(); }
    }

    @Override
    public String getSelectByIdQuery(Integer id) {
        return "SELECT * FROM " + this.getTableName() + " x WHERE x.id_prodotto = " + id;
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM " + this.getTableName();
    }

    @Override
    public String getInsertQuery(Prodotto prodotto) {
        CategoriaProdotto category = prodotto.getCategoriaProdotto();
        Integer idCat = 0;

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + this.getTableName() + " (");
        //INTESTAZIONE
        if(category!=null){
            idCat = prodotto.getCategoriaProdotto().getId();
            queryBuilder.append("id_categoria_prodotto,");
        }
        queryBuilder.append("nome,");
        if(prodotto.getDescrizione()!=null){ queryBuilder.append("descrizione,");}
        queryBuilder.append("prezzo) VALUES (");
        //VALORI
        if(category!=null){ queryBuilder.append(idCat +",");}
        queryBuilder.append("'" + prodotto.getNomeProdotto() + "',");
        if(prodotto.getDescrizione()!=null){ queryBuilder.append("'" + prodotto.getDescrizione() + "',");}
        queryBuilder.append(prodotto.getPrezzo() + ");");

        return queryBuilder.toString();
    }

    @Override
    public String getDeleteQuery(Integer id) {
        return "DELETE FROM " + this.getTableName() + " x WHERE x.id_prodotto = " + id + ";";
    }

    @Override
    public String getUpdateQuery(Integer id, Prodotto prodotto) {
        CategoriaProdotto category = prodotto.getCategoriaProdotto();
        Integer idCat = 0;

        StringBuilder queryBuilder = new StringBuilder("UPDATE " + this.getTableName() + " x SET ");
        //INTESTAZIONE
        if(category!=null){
            idCat = prodotto.getCategoriaProdotto().getId();
            queryBuilder.append("x.id_categoria_prodotto = " + idCat + ",");
        }
        queryBuilder.append("x.nome = '" + prodotto.getNomeProdotto() + "',");
        if(prodotto.getDescrizione()!=null){ queryBuilder.append("x.descrizione = '" + prodotto.getDescrizione() + "',");}
        queryBuilder.append("x.prezzo = " + prodotto.getPrezzo() );

        queryBuilder.append(" WHERE x.id_prodotto = " + id);

        return queryBuilder.toString();
    }

    @Override
    public String getReplaceQuery(Integer id, Prodotto prodotto) {
        Integer idCat = prodotto.getCategoriaProdotto().getId();
        String nomeProdotto = prodotto.getNomeProdotto();
        String descrizione = prodotto.getDescrizione();
        Double prezzo = prodotto.getPrezzo();
        StringBuilder queryBuilder = new StringBuilder("UPDATE " + this.getTableName() + " x SET ");

        if(idCat!=null){queryBuilder.append(" x.id_categoria_prodotto = " + idCat + ",");}
        if(nomeProdotto!=null){queryBuilder.append(" x.nome = '" + nomeProdotto + "',");}
        if(descrizione!=null){queryBuilder.append(" x.descrizione = '" + descrizione + "',");}

        if(prezzo!=null){ queryBuilder.append(" x.prezzo = " + prezzo);}

        //cancella ultima virgola "," se esiste
        int lastIndex = queryBuilder.length() - 1; //lunghezza di ","
        if(lastIndex > 0 && queryBuilder.substring(lastIndex).equals(",")){
            queryBuilder.delete(lastIndex,lastIndex+1);
        }

        queryBuilder.append(" WHERE x.id_prodotto = " + id);
        return queryBuilder.toString();
    }


    @Override
    public String getSearchByStringQuery(String searchText) {
        return "SELECT * FROM " + this.getTableName() + " x WHERE x.nome LIKE '%" + searchText + "%'"
                + " OR x.descrizione LIKE '%" + searchText + "%';";
    }

    @Override
    public String getSearchByObjectQuery(Prodotto searchObj) {
        Integer idCat = searchObj.getCategoriaProdotto().getId();
        String nomeProdotto = searchObj.getNomeProdotto();
        String descrizione = searchObj.getDescrizione();
        Double prezzo = searchObj.getPrezzo();
        //Eccezione : oggetto passato non valido perché è tutto vuoto
        if(idCat==null && nomeProdotto==null && descrizione==null && prezzo==null){ return "SELECT * FROM " + this.getTableName() + " x WHERE x.id_prodotto = 0";}

        StringBuilder qb = new StringBuilder("SELECT * FROM " + this.getTableName() + " x WHERE" );
        if(idCat!=null){qb.append(" x.id_categoria_prodotto = " + idCat + " AND");}
        if(nomeProdotto!=null){qb.append(" x.nome LIKE '%" + nomeProdotto + "%' AND");}
        if(descrizione!=null){qb.append(" x.descrizione LIKE '%" + descrizione + "%' AND");}
        if(prezzo!=null){qb.append(" x.prezzo = " + prezzo + " AND");}

        // rimuovi l'ultimo "AND" se esiste
        int lastIndex = qb.length() - 4; // lunghezza di " AND"
        if (lastIndex > 0 && qb.substring(lastIndex).equals(" AND")) {
            qb.delete(lastIndex, lastIndex + 4);
        }
        return qb.toString();
    }

    public int assegnaCategoria(String s, Integer integer) throws DaoException {
        try {
            // Controlla se la categoria esiste:
            List<CategoriaProdotto> categorieEsistenti = cateogrieDAO.read();
            CategoriaProdotto category = checkIfCategoryIsAlreadyExistent(categorieEsistenti, s.toUpperCase());

            if (category != null) {// La categoria esiste già
                associazioneCategoria(integer, category);
                return 0;
            } else {// Se la categoria non esiste, la aggiungo alla tabella categorie
                CategoriaProdotto newCategory = new CategoriaProdotto(categorieEsistenti.size() + 1, s.toUpperCase() );
                cateogrieDAO.create(newCategory);
                associazioneCategoria(integer, newCategory);
                return 1; // Ritorno desiderato se la categoria non esisteva
            }
        }catch (Exception e) { e.printStackTrace();throw new DaoException(); }
    }

    public void associazioneCategoria(Integer integer, CategoriaProdotto newCategory) throws DaoException {
        Prodotto replaceCategoryProduct = this.getById(integer);
        replaceCategoryProduct.setCategoriaProdotto(newCategory);
        try{ this.replace(integer,replaceCategoryProduct); }
        catch (Exception e) { throw new DaoException(); }
    }

    public CategoriaProdotto checkIfCategoryIsAlreadyExistent(List<CategoriaProdotto> categorieProdottoEsistenti, String s){
        return categorieProdottoEsistenti.stream()
                .filter(categoriaProdotto -> categoriaProdotto.getNomeCategoria().equalsIgnoreCase(s))
                .findFirst()
                .orElse(null);
    }

    public static String generateCategoriaName(String tableName) {
        // Ottieni il nome del DTO basato sul nome della tabella
        String dtoClassName = tableName.substring(tableName.lastIndexOf(".") + 1);

        // Fai la prima lettera maiuscola
        dtoClassName = dtoClassName.substring(0, 1).toUpperCase() + dtoClassName.substring(1);

        // Se l'ultima lettera è 'e', cambia l'ultima lettera in 'a', altrimenti se finisce con 'i', cambia in 'o'
        if (dtoClassName.endsWith("e")) {
            dtoClassName = dtoClassName.substring(0, dtoClassName.length() - 1) + "a";
        } else if (dtoClassName.endsWith("i")) {
            dtoClassName = dtoClassName.substring(0, dtoClassName.length() - 1) + "o";
        }

        return dtoClassName;
    }
}
