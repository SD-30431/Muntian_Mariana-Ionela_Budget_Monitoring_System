package com.example.demo.BusinessLogic;

import com.example.demo.DTO.CategoryExpenseRequest;
import com.example.demo.DTO.MonthlyExpenseDTO;
import com.example.demo.DTO.ProductXmlDTO;
import com.example.demo.Model.Product;
import com.example.demo.Model.ProductsWrapper;
import com.example.demo.Model.User;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Product save(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (product.getDate() == null) {
            throw new IllegalArgumentException("Date is required");
        }
        if (product.getCategory() == null) {
            throw new IllegalArgumentException("Category is required");
        }
        if (product.getUser() == null || product.getUser().getId() == null) {
            throw new IllegalArgumentException("User must be attached to the product.");
        }

        Optional<User> userOptional = userRepository.findById(product.getUser().getId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID: " + product.getUser().getId());
        }

        product.setUser(userOptional.get());

        return productRepository.save(product);
    }

    public List<Product> findAllWithCategory() {
        return productRepository.findAllWithCategory();
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existing = productRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Product with ID " + id + " not found.");
        }

        Product product = existing.get();
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setDate(updatedProduct.getDate());
        product.setCategory(updatedProduct.getCategory());

        if (updatedProduct.getUser() != null && updatedProduct.getUser().getId() != null) {
            Optional<User> userOptional = userRepository.findById(updatedProduct.getUser().getId());
            userOptional.ifPresent(product::setUser);
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product with ID " + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }

    public List<CategoryExpenseRequest> getExpensesGroupedByCategory() {
        return productRepository.findExpensesGroupedByCategory();
    }

    public List<Product> getProductsByUser(Long userId) {
        return productRepository.findByUserId(userId);
    }

    public File generateXmlForUser(String username) throws Exception {
        List<Product> products = productRepository.findByUserUsername(username);

        if (products.isEmpty()) {
            throw new IllegalArgumentException("No products found for user: " + username);
        }

        List<ProductXmlDTO> productDtos = products.stream().map(p ->
                new ProductXmlDTO(
                        p.getName(),
                        p.getPrice(),
                        p.getDate(),
                        p.getCategory() != null ? p.getCategory().getName() : "Uncategorized"
                )
        ).toList();

        ProductsWrapper wrapper = new ProductsWrapper(productDtos);

        String outputDir = "C:/Users/Mariana Ionela/MATERII/AN 3 - UTCN/SEM2/Software Design/A1/Frontend/my-angular-app/src/resources";
        File directory = new File(outputDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(outputDir + "/products_" + username + ".xml");

        JAXBContext context = JAXBContext.newInstance(ProductsWrapper.class, ProductXmlDTO.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, file);

        return file;
    }

    public List<MonthlyExpenseDTO> getMonthlyExpensesForUser(String username) {
        return productRepository.findMonthlyExpensesByUsername(username);
    }

}
