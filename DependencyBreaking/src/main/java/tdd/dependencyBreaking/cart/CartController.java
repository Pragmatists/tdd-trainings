package tdd.dependencyBreaking.cart;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CartController {

    private ProductCatalog productCatalog;

    private CartController(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    public void addProductToCart(HttpServletRequest req, HttpSession session) {

        Long productId = Long.valueOf(req.getParameter("productId"));
        Integer numberOfItems = Integer.valueOf(req.getParameter("items"));

        Product product = productCatalog.getProductById(productId);
        
        @SuppressWarnings("unchecked")
        Map<Product, Integer> items = (Map<Product, Integer>) session.getAttribute("cart");
        items.put(product, numberOfItems);
    }
}
