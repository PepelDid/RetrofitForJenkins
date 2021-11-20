package retrofit_tests;

import org.junit.jupiter.api.Test;
import retrofit.db.model.Products;
import retrofit.dto.Product;
import retrofit.utils.DbUtils;
import retrofit.utils.PrettyLogger;
import retrofit2.Response;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ModifyProductTests extends BaseTests {


    @Test
    void modifyProductByBdMethodTest() {
        Products record = new Products();
        record.setId(Long.valueOf(idProduct));
        record.setTitle(faker.food().vegetable());
        record.setPrice((int) ((Math.random() + 1) + 100));
        record.setCategory_id(2l);
        DbUtils.updateProductByKey(record);
        //обратилась к обновленному продукту для проверок
        Products updatedProductFromDB = DbUtils.selectByPrimaryKey(productsMapper, (Long.valueOf(idProduct)));
        //проверила
        assertThat(record.getId(), equalTo((Long.valueOf(updatedProductFromDB.getId()))));
        assertThat(record.getTitle(), equalTo(updatedProductFromDB.getTitle()));
        assertThat(record.getPrice(), equalTo(updatedProductFromDB.getPrice()));
        //удалила
        DbUtils.deleteProductByKey(productsMapper, record.getId());
    }

    @Test
    void modifyProductByIdTest() throws IOException {
        product.setId(idProduct);
        product.setTitle(faker.superhero().name());
        product.setPrice(500);

        System.out.println("This is info about updated product: " + product);
        Response<Product> response = productService.modifyProduct(product).execute();
        PrettyLogger.DEFAULT.log(response.body().toString());

        assertThat(response.body().getId(), equalTo(idProduct));
        assertThat(response.body().getPrice(), equalTo(500));

        Long id = Long.valueOf(idProduct);
        DbUtils.deleteProductByKey(productsMapper, id);
    }
}

