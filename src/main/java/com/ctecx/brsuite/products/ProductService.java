package com.ctecx.brsuite.products;

import com.ctecx.brsuite.brands.Brand;
import com.ctecx.brsuite.brands.BrandRepository;
import com.ctecx.brsuite.brands.BrandService;
import com.ctecx.brsuite.productcategory.Category;
import com.ctecx.brsuite.productcategory.CategoryRepository;
import com.ctecx.brsuite.productcategory.CategoryService;
import com.ctecx.brsuite.revenue.RevenueRepository;
import com.ctecx.brsuite.stockmode.StockTransaction;
import com.ctecx.brsuite.stockmode.StockTransactionRepository;
import com.ctecx.brsuite.taxes.TaxClass;
import com.ctecx.brsuite.taxes.TaxClassRepository;
import com.ctecx.brsuite.taxes.TaxClassService;
import com.ctecx.brsuite.uom.Uom;
import com.ctecx.brsuite.uom.UomRepository;
import com.ctecx.brsuite.uom.UomService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final StockTransactionRepository stockTransactionRepository;
    private final BrandRepository brandRepository;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final UomService uomService;
    private final TaxClassService taxClassService;
    private final CategoryRepository categoryRepository;
    private final RevenueRepository revenueRepository;
    private final TaxClassRepository taxClassRepository;
    private final UomRepository uomRepository;


    public List<ProductValueDTO> stockValue() {
        List<Product> products = productRepository.findAll();

        List<StockTransaction> stockTransactions = stockTransactionRepository.findAll();

        Map<Long, Integer> stockInMap = stockTransactions.stream()
                .filter(stockTransaction -> stockTransaction.getProduct() != null)
                .collect(Collectors.groupingBy(
                        stockTransaction -> stockTransaction.getProduct().getId(),
                        Collectors.summingInt(StockTransaction::getStockIn)
                ));

        Map<Long, Integer> stockOutMap = stockTransactions.stream()
                .filter(stockTransaction -> stockTransaction.getProduct() != null)
                .collect(Collectors.groupingBy(
                        stockTransaction -> stockTransaction.getProduct().getId(),
                        Collectors.summingInt(StockTransaction::getStockOut)
                ));

        double totalTax = calculateInclusiveTax(products);

        List<ProductValueDTO> productValueDTOs = products.stream()
                .map(product -> {
                    long productId = product.getId();
                    int stockIn = stockInMap.getOrDefault(productId, 0);
                    int stockOut = stockOutMap.getOrDefault(productId, 0);
                    int currentStock = stockIn - stockOut;
                    double productPrice = product.getProductPrice();
                    double productCost = product.getProductCost();
                    double tax = applyTax(totalTax);

                    double purchaseValue = productCost * currentStock;
                    double salesValue = productPrice * currentStock;
                    double profitMargin = productPrice - productCost;
                    double profit = salesValue-purchaseValue;

                    return new ProductValueDTO(
                            product.getProductName(),
                            product.getProductCode(),
                            productCost,
                            productPrice,
                            currentStock,
                            purchaseValue,
                            salesValue,
                            profitMargin,
                            profit
                    );
                })
                .collect(Collectors.toList());

        return productValueDTOs;
    }


    public Product saveProduct(ProductDTO productDTO) {
        Product product = new Product();

        product.setBrand(brandRepository.findById(productDTO.getBrandId()).orElse(null));
        product.setCategory(categoryRepository.findById(Math.toIntExact(productDTO.getCategoryId())).orElse(null));
        product.setRevenue(revenueRepository.findById(productDTO.getRevenueId()).orElse(null));

        Set<TaxClass> taxClasses = new HashSet<>();
        for (Long taxClassId : productDTO.getTaxClassIds()) {
            taxClassRepository.findById(taxClassId).ifPresent(taxClasses::add);
        }
        product.setTaxClasses(taxClasses);

        product.setUom(uomRepository.findById(productDTO.getUomId()).orElse(null));
        product.setProductType(productDTO.getProductType());
        product.setProductName(productDTO.getProductName());
        product.setProductCode(productDTO.getProductCode());
        product.setProductCost(productDTO.getProductCost());
        product.setProductPrice(productDTO.getProductPrice());
        product.setAlertQuantity(productDTO.getAlertQuantity());
        product.setTaxMode(productDTO.getTaxMode());

        // Check if the generated code is unique
        while (productRepository.existsByProductCode(product.getProductCode())) {
            product.setProductCode(ProductCodeGenerator.generateProductCode());
        }

        return productRepository.save(product);
    }





    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product generateProductCodeAndPrepareForCreation() {
        // Generate unique product code logic
        String productCode = ProductCodeGenerator.generateProductCode();

        Product product = new Product();
        product.setProductCode(productCode);

        return product;
    }

    public void saveProduct(Product product) {


        // Check if the generated code is unique
        while (productRepository.existsByProductCode(product.getProductCode())) {
            product.setProductCode( ProductCodeGenerator.generateProductCode());
        }


         productRepository.save(product);
    }


    public Product updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        existingProduct.setBrand(updatedProduct.getBrand());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setTaxClasses(updatedProduct.getTaxClasses());
        existingProduct.setUom(updatedProduct.getUom());
        existingProduct.setProductType(updatedProduct.getProductType());
        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setProductCode(updatedProduct.getProductCode());
        existingProduct.setProductCost(updatedProduct.getProductCost());
        existingProduct.setProductPrice(updatedProduct.getProductPrice());
        existingProduct.setAlertQuantity(updatedProduct.getAlertQuantity());
        existingProduct.setTaxMode(updatedProduct.getTaxMode());

        return productRepository.save(existingProduct);
    }

    public Product updateProductPrices(Long productId, double newProductPrice, double newProductCost) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        existingProduct.setProductPrice(newProductPrice);
        existingProduct.setProductCost(newProductCost);

        return productRepository.save(existingProduct);
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }















    public List<ProductInfoDTO> calculateProductStock(String productName) {
        List<Product> products = productRepository.searchProductsByName(productName);
        List<StockTransaction> stockTransactions = stockTransactionRepository.findAll();

        Map<Long, Integer> stockInMap = stockTransactions.stream()
                .filter(stockTransaction -> stockTransaction.getProduct() != null)
                .collect(Collectors.groupingBy(
                        stockTransaction -> stockTransaction.getProduct().getId(),
                        Collectors.summingInt(StockTransaction::getStockIn)
                ));

        Map<Long, Integer> stockOutMap = stockTransactions.stream()
                .filter(stockTransaction -> stockTransaction.getProduct() != null)
                .collect(Collectors.groupingBy(
                        stockTransaction -> stockTransaction.getProduct().getId(),
                        Collectors.summingInt(StockTransaction::getStockOut)
                ));

        return products.stream()
                .map(product -> {
                    long productId = product.getId();
                    int stockIn = stockInMap.getOrDefault(productId, 0);
                    int stockOut = stockOutMap.getOrDefault(productId, 0);
                    int currentStock = stockIn - stockOut;

                    return new ProductInfoDTO(
                            product.getProductName(),
                            product.getProductCode(),
                            product.getProductCost(),
                            product.getProductPrice(),
                            currentStock
                    );
                })
                .collect(Collectors.toList());
    }





    private double calculateInclusiveTax(List<Product> products) {
        double totalTax = 0.0;

        for (Product product : products) {
            for (TaxClass taxClass : product.getTaxClasses()) {
                double rate = taxClass.getRate();
                double productPrice = product.getProductPrice();
                double vatAmount = (rate / (100 + rate)) * productPrice;
                totalTax += vatAmount;
            }
        }

        // Round off to 2 decimal places
        totalTax = Math.round(totalTax * 100.0) / 100.0;

        return totalTax;
    }






    public List<ProductSaleDTO> salesMode(String productName) {
        List<Product> products = productRepository.searchProductsByName(productName);
        List<StockTransaction> stockTransactions = stockTransactionRepository.findAll();

        Map<Long, Integer> stockInMap = stockTransactions.stream()
                .filter(stockTransaction -> stockTransaction.getProduct() != null)
                .collect(Collectors.groupingBy(
                        stockTransaction -> stockTransaction.getProduct().getId(),
                        Collectors.summingInt(StockTransaction::getStockIn)
                ));

        Map<Long, Integer> stockOutMap = stockTransactions.stream()
                .filter(stockTransaction -> stockTransaction.getProduct() != null)
                .collect(Collectors.groupingBy(
                        stockTransaction -> stockTransaction.getProduct().getId(),
                        Collectors.summingInt(StockTransaction::getStockOut)
                ));

        double totalTax = calculateInclusiveTax(products);

        return products.stream()
                .map(product -> {
                    long productId = product.getId();
                    int stockIn = stockInMap.getOrDefault(productId, 0);
                    int stockOut = stockOutMap.getOrDefault(productId, 0);
                    int currentStock = stockIn - stockOut;
                    double productPrice = product.getProductPrice();
                    double tax = applyTax(totalTax);

                    return new ProductSaleDTO(
                            product.getProductName(),
                            product.getProductCode(),
                            product.getProductCost(),
                            productPrice,
                            currentStock,
                            tax

                    );
                })
                .collect(Collectors.toList());
    }





  public List<ProductSaleDTO> salesByProductNameAndCode(String searchKey, int page, int size) {
      Pageable pageable = PageRequest.of(page, size);
      Page<Product> productsPage = productRepository.searchProductsByNameOrCode(searchKey, pageable);
      List<Product> products = productsPage.getContent();

      List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());
      List<StockTransaction> stockTransactions = stockTransactionRepository.findByProductIdIn(productIds);

      Map<Long, Integer> stockInMap = stockTransactions.stream()
              .filter(stockTransaction -> stockTransaction.getProduct() != null)
              .collect(Collectors.groupingBy(
                      stockTransaction -> stockTransaction.getProduct().getId(),
                      Collectors.summingInt(StockTransaction::getStockIn)
              ));

      Map<Long, Integer> stockOutMap = stockTransactions.stream()
              .filter(stockTransaction -> stockTransaction.getProduct() != null)
              .collect(Collectors.groupingBy(
                      stockTransaction -> stockTransaction.getProduct().getId(),
                      Collectors.summingInt(StockTransaction::getStockOut)
              ));

      double totalTax = calculateInclusiveTax(products);

      return products.stream()
              .map(product -> {
                  long productId = product.getId();
                  int stockIn = stockInMap.getOrDefault(productId, 0);
                  int stockOut = stockOutMap.getOrDefault(productId, 0);
                  int currentStock = stockIn - stockOut;
                  double productPrice = product.getProductPrice();
                  double tax = applyTax(totalTax);

                  return new ProductSaleDTO(
                          product.getProductName(),
                          product.getProductCode(),
                          product.getProductCost(),
                          productPrice,
                          currentStock,
                          tax
                  );
              })
              .collect(Collectors.toList());
  }





    private double applyTax(double totalTax) {
        return  totalTax;
    }


    public Page<ProductDataDTO> getAllProductDTOs(String keyword, Pageable pageable) {
        Page<Product> productPage;
        if (keyword == null) {
            productPage = productRepository.findAll(pageable);
        } else {
            productPage = productRepository.findByProductNameContainingIgnoreCase(keyword, pageable);
        }

        return productPage.map(this::convertToProductDTO);
    }



public List<ProductDTOList> getAllProductsData() {
    return productRepository.findAll().stream()
            .sequential()
            .map(this::convertToProductList)
            .collect(Collectors.toList());
}

    private ProductDataDTO convertToProductDTO(Product product) {
        ProductDataDTO productDTO = new ProductDataDTO();

        productDTO.setId(product.getId());
        productDTO.setBrand(product.getBrand());
        productDTO.setCategory(product.getCategory());
        productDTO.setTaxClasses(product.getTaxClasses());
        productDTO.setUom(product.getUom());
        productDTO.setProductType(product.getProductType());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductCode(product.getProductCode());
        productDTO.setProductCost(product.getProductCost());
        productDTO.setProductPrice(product.getProductPrice());
        productDTO.setAlertQuantity(product.getAlertQuantity());
        productDTO.setTaxMode(product.getTaxMode());

        return productDTO;
    }


    private ProductDTOList convertToProductList(Product product) {
        ProductDTOList productDTO = new ProductDTOList();

        productDTO.setId(product.getId());
        productDTO.setProductCode(product.getProductCode());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductCost(product.getProductCost());
        productDTO.setProductPrice(product.getProductPrice());
        productDTO.setBrandName(product.getBrand().getName());
        productDTO.setCategoryName(product.getCategory().getName());
        productDTO.setUom(product.getUom().getUnit());
        productDTO.setProductType(product.getProductType());
        productDTO.setAlertQuantity(product.getAlertQuantity());
        productDTO.setTaxMode(product.getTaxMode());

        return productDTO;
    }


    public void uploadProducts(MultipartFile file) throws IOException {
        System.out.println("WORKS DATA->");
        List<Product> products = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                String productName = getStringCellValue(row, 0);
                if (productName == null || productName.isEmpty()) {
                    throw new IllegalArgumentException("Product name is missing in row " + row.getRowNum());
                }

                Double productCost = getNumericCellValue(row, 1);
                if (productCost == null) {
                    throw new IllegalArgumentException("Product cost is missing in row " + row.getRowNum());
                }

                Double productPrice = getNumericCellValue(row, 2);
                if (productPrice == null) {
                    throw new IllegalArgumentException("Product price is missing in row " + row.getRowNum());
                }

                Double alertQuantityValue = getNumericCellValue(row, 3);
                if (alertQuantityValue == null) {
                    throw new IllegalArgumentException("Alert quantity is missing in row " + row.getRowNum());
                }
                int alertQuantity = alertQuantityValue.intValue();

                String taxMode = getStringCellValue(row, 4);
                if (taxMode == null || taxMode.isEmpty()) {
                    throw new IllegalArgumentException("Tax mode is missing in row " + row.getRowNum());
                }

                // Get IDs or objects for brand, category, uom
                // Get IDs or objects for brand, category, uom
                Long brandId = getLongCellValue(row, 5);
                Integer categoryId = getIntegerCellValue(row, 6);
                Long uomId = getLongCellValue(row, 7);

                Optional<Brand> brandOpt = brandService.getBrandById(brandId);
                Optional<Category> categoryOpt = categoryService.getCategoryById(categoryId);
                Optional<Uom> uomOpt = uomService.getUomById(uomId);

                if (brandOpt.isEmpty() || categoryOpt.isEmpty() || uomOpt.isEmpty()) {
                    throw new IllegalArgumentException("Brand, Category or UOM is missing in row " + row.getRowNum());
                }

                Brand brand = brandOpt.get();
                Category category = categoryOpt.get();
                Uom uom = uomOpt.get();

                Set<TaxClass> taxClasses = new HashSet<>();
                String[] taxClassIds = Objects.requireNonNull(getStringCellValue(row, 8)).split(",");

                for (String taxClassId : taxClassIds) {
                    // Parse the string to a double first, then convert it to a long
                    long id = (long) Double.parseDouble(taxClassId.trim());
                    Optional<TaxClass> taxClassOpt = taxClassService.getTaxClassById(id);
                    taxClassOpt.ifPresent(taxClasses::add);
                }


                Product product = new Product();
                String productCode = ProductCodeGenerator.generateProductCode();

                product.setProductName(productName);
                product.setProductCode(productCode);
                product.setProductCost(productCost);
                product.setProductPrice(productPrice);
                product.setAlertQuantity(alertQuantity);
                product.setTaxMode(taxMode);
                product.setProductType("Inventory");
                // Set the relationships
                product.setBrand(brand);
                product.setCategory(category);
                product.setUom(uom);

                // Set the tax classes
               product.setTaxClasses(taxClasses);

                products.add(product);
            }
        } catch (Exception e) {
            System.out.println("Error reading Excel file: " + e.getMessage());
        }

        productRepository.saveAll(products);
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return null;
        }
    }

    private String getStringCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return getCellValueAsString(cell);
    }

    private Double getNumericCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }
        return null;
    }

    private Long getLongCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            if (cell.getCellType() == CellType.NUMERIC) {
                double numericValue = cell.getNumericCellValue();
                return (long) numericValue;
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.contains(".")) {
                        return Long.parseLong(cellValue.substring(0, cellValue.indexOf('.')));
                    } else {
                        return Long.parseLong(cellValue);
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where the string cannot be parsed to a Long
                    return null;
                }
            }
        }
        return null;
    }

    private Integer getIntegerCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            if (cell.getCellType() == CellType.NUMERIC) {
                double numericValue = cell.getNumericCellValue();
                return (int) numericValue;
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.contains(".")) {
                        return (int) Double.parseDouble(cellValue);
                    } else {
                        return Integer.parseInt(cellValue);
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where the string cannot be parsed to an Integer
                    return null;
                }
            }
        }
        return null;
    }


}
