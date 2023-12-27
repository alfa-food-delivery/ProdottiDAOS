package it.alfasoft.prodottidaos;

import it.alfasoft.daosimple.DaoException;
import it.alfasoft.daosimple.DaoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CategorieDAOS extends DaoImpl<CategoriaProdotto,Integer> {

    public CategorieDAOS(){}

    @Override
    public String getSelectByIdQuery(Integer id) {
        return "SELECT * FROM " + this.getTableName() + " x WHERE x.id_categoria_prodotto = " + id;
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM " + this.getTableName();
    }

    @Override
    public String getInsertQuery(CategoriaProdotto categoriaProdotto) {
        return "INSERT INTO " + this.getTableName() + " (nome_categoria_prodotto) "
                + "VALUES ('" + categoriaProdotto.getNomeCategoria() + "');";
    }

    @Override
    public String getDeleteQuery(Integer id) {
        return "DELETE FROM " + this.getTableName() + " x WHERE x.id_categoria_prodotto = " + id + ";";
    }

    @Override
    public String getUpdateQuery(Integer id, CategoriaProdotto categoriaProdotto) {
        return "UPDATE " + this.getTableName()
                + " x SET x.nome_categoria_prodotto = '" + categoriaProdotto.getNomeCategoria() + "'"
                + " WHERE x.id_categoria_prodotto = " + id;
    }

    @Override
    public String getReplaceQuery(Integer id, CategoriaProdotto categoriaProdotto) {
        String nomeCategoria = categoriaProdotto.getNomeCategoria();
        StringBuilder queryBuilder = new StringBuilder("UPDATE " + this.getTableName() + " x SET ");
        if(categoriaProdotto!=null){queryBuilder.append(" x.nome_categoria_prodotto = '" + nomeCategoria + "'");}
        queryBuilder.append(" WHERE x.id_categoria_prodotto = " + id);
        return queryBuilder.toString();
    }


    @Override
    public String getSearchByStringQuery(String searchText) {
        return "SELECT * FROM " + this.getTableName() + " x WHERE x.nome_categoria_prodotto LIKE '%" + searchText + "%';";
    }

    @Override
    public String getSearchByObjectQuery(CategoriaProdotto searchObj) {
        String nomeCategoria = searchObj.getNomeCategoria();
        //Eccezione : oggetto passato non valido perché è tutto vuoto
        if(nomeCategoria==null){ return "SELECT * FROM " + this.getTableName() + " x WHERE x.id_categoria_prodotto = 0";}

        StringBuilder qb = new StringBuilder("SELECT * FROM " + this.getTableName() + " x WHERE" );
        if(nomeCategoria!=null){qb.append(" x.nome_categoria_prodotto LIKE '%" + nomeCategoria + "%';");}

        return qb.toString();
    }

    @Override
    public CategoriaProdotto convertToDto(ResultSet resultSet) throws DaoException {
        CategoriaProdotto cp = null;
        try{
            cp = new CategoriaProdotto(
                    resultSet.getInt("id_categoria_prodotto"),
                    resultSet.getString("nome_categoria_prodotto")
            );
            return cp;
        }catch(Exception sqle){ sqle.printStackTrace(); throw new DaoException();}
    }

    @Override
    public boolean checkOggetto(CategoriaProdotto categoriaProdotto) throws DaoException {
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
}
