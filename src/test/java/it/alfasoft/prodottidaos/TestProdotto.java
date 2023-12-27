package it.alfasoft.prodottidaos;

import it.alfasoft.daosimple.DaoException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class TestProdotto {

    private static ProdottiDAOS prodottiDAO = new ProdottiDAOS();

    private static Prodotto productDeleted;

    @BeforeAll
    public static void beforeAll() throws DaoException {

        prodottiDAO.setTableName("food_delivery.prodotti");
        productDeleted = prodottiDAO.getById(1);
    }
    /**
     * Scenario Base
     * TEST CREATE PRODOTTO ( INSERT )
     * @throws DaoException
     */
    @Test
    public void testCreaProdotto() throws DaoException {
        Prodotto prod1 = new Prodotto("Panettone",5.00);
        //CategoriaProdotto dolce_category = new CategoriaProdotto("DOLCE");
        //prod1.setCategoriaProdotto(dolce_category);
        Assertions.assertTrue( prodottiDAO.create(prod1) > 0 );
    }

    /**
     * Variante Scenario
     * TEST CREATE ENTITA' VUOTA
     */
    @Test
    public void testCreaProdottoNull() {
        Assertions.assertThrows(
                DaoException.class,
                () -> prodottiDAO.create(null),
                "Il null non restituisce un errore corretto!!"
        );
    }


    /**
     * Scenario Base
     * TEST ASSEGNA CATEGORIA ESISTENTE AD UN PRODOTTO
     * @throws DaoException
     */
    @Test
    public void testAssegnaCategoriaProdotto() throws DaoException {
        //Prendi prodotto con idProdotto = 11
        Prodotto dolce = prodottiDAO.getById(11);
        CategoriaProdotto dolce_category = new CategoriaProdotto("DOLCE");
        dolce.setCategoriaProdotto(dolce_category);

        //Controlla se il metodo ritorna 0 (mi aspetto questo se la categoria esiste già)
        Assertions.assertTrue(prodottiDAO.assegnaCategoria("DOLCE",dolce.getId())==0 );
    }

    /**
     * Scenario Base
     * TEST READ PRODOTTO ( READ ALL )
     * @throws DaoException
     */
    @Test
    public void testReadProdotto() throws DaoException {
        String sql = "SELECT COUNT(*) FROM food_delivery.prodotti;";
        int qty = 0;
        try( Statement stmt = prodottiDAO.getConnection().createStatement();){
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            qty = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace();throw new DaoException();}

        List<Prodotto> prodotti = prodottiDAO.read();
        Assertions.assertTrue(qty == prodotti.size());
    }

    /**
     * Scenario Base
     * TEST UPDATE PRODOTTO (nome e descrizione ; idProdotto=3)
     */
    @Test
    public void testUpdateProdotto() throws DaoException {
        String newName = "Pandoro";
        String newDescription = "Pandoro fatto in casa";
        Prodotto nuoveCaratteristicheProdotto = new Prodotto(newName,newDescription);
        nuoveCaratteristicheProdotto.setPrezzo(4.50);
        Assertions.assertTrue(prodottiDAO.update(3,nuoveCaratteristicheProdotto) == 1);
    }

    /**
     * Variante Scenario
     * TEST UPDATE PRODOTTO NULL(mail o password ; idProdotto=5)
     */
    @Test
    public void testUpdateProdottoNull() throws DaoException {

        String newDescription = "Crostata fatta in casa";
        Prodotto nuoveCaratteristicheProdotto = new Prodotto(null,newDescription);
        Assertions.assertThrows(
                DaoException.class,
                () -> prodottiDAO.update(5,nuoveCaratteristicheProdotto),
                "Il null non restituisce un errore corretto!!"
        );
    }

    /**
     * Scenario Base
     * TEST DELETE PRODOTTO (idProdotto=1)
     */
    @Test
    public void testDeleteProdotto() throws DaoException {
        Assertions.assertTrue(prodottiDAO.delete(1)>0);
    }


    @AfterAll
    public static void reinserisciProdottoRimosso() throws SQLException {
        try ( Statement stmt = prodottiDAO.getConnection().createStatement() )
        {
            stmt.executeUpdate("insert into food_delivery.prodotti (id_prodotto,id_categoria_prodotto,nome,descrizione,prezzo) VALUES (1,1, 'Margherita', 'Pizza con pomodoro, mozzarella e basilico', 8.50);");
        }
    }
}