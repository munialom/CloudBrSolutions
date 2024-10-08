package com.ctecx.brsuite.stockmode;

import com.ctecx.brsuite.branch.BranchRepository;
import com.ctecx.brsuite.branch.BranchService;
import com.ctecx.brsuite.products.Product;
import com.ctecx.brsuite.products.ProductRepository;
import com.ctecx.brsuite.products.ProductService;
import com.ctecx.brsuite.suppliers.Supplier;
import com.ctecx.brsuite.suppliers.SupplierService;
import com.ctecx.brsuite.warehouse.Store;
import com.ctecx.brsuite.warehouse.StoreRepository;
import com.ctecx.brsuite.warehouse.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepository;
    private final StockTransactionRepository stockTransactionRepository;
    private final ProductService productService;
    private  final SupplierService supplierService;
    private final BranchService branchService;
    private final BranchRepository branchRepository;
    private final StoreRepository storeRepository;
    private final StoreService storeService;






    public void createOpeningStock(OpeningStockDTO openingStockDTO) {
        String sn = generateSerialNumber();

        openingStockDTO.getOpeningStocks().forEach(openingStock -> {
            Product product = productRepository.findByProductCode(openingStock.getProductCode());
            if (product == null) {
                throw new RuntimeException("Product not found with code: " + openingStock.getProductCode());
            }

            StockTransaction stockTransaction = new StockTransaction();
            stockTransaction.setProduct(product);
            stockTransaction.setProductCode(product.getProductCode());
            stockTransaction.setProductName(product.getProductName());
            stockTransaction.setTransactionDate(LocalDate.now());
            /*stockTransaction.setProductCost(product.getProductCost());
            stockTransaction.setProductSalePrice(product.getProductPrice());*/
            stockTransaction.setSerialNumber(sn);
            stockTransaction.setStatus("Active");

            // Set branch if available in DTO
            if (openingStockDTO.getBranchId() != null) {
                Store branch = storeRepository.findById(openingStockDTO.getBranchId())
                        .orElseThrow(() -> new RuntimeException("Branch not found with id: " + openingStockDTO.getBranchId()));
                stockTransaction.setStore(branch);
            }

            if (openingStockDTO.getOpcode().equals("ADD")) {
                stockTransaction.setModule("Adding Stocks");
                stockTransaction.setStockIn(openingStock.getQty());
                stockTransaction.setStockOut(0);
                stockTransaction.setDescription("Adding stock for " + product.getProductName());
            } else if (openingStockDTO.getOpcode().equals("SUBTRACT")) {
                stockTransaction.setModule("Subtracting Stocks");
                stockTransaction.setStockIn(0);
                stockTransaction.setStockOut(openingStock.getQty());
                stockTransaction.setDescription("Subtracting stock for " + product.getProductName());
            } else {
                throw new IllegalArgumentException("Invalid opcode: " + openingStockDTO.getOpcode());
            }



            // Save the stock transaction
            stockTransactionRepository.save(stockTransaction);
        });
    }




    public void createPurchase(PurchaseStockDTO  purchaseStockDTO) {
        String sn = generateSerialNumber();
        purchaseStockDTO.purchaseStocks.forEach(purchaseStock -> {
            Product product=productRepository.findByProductCode(purchaseStock.getProductCode());

            StockTransaction stockTransaction = new StockTransaction();
            stockTransaction.setProductCode(purchaseStock.getProductCode());
            stockTransaction.setProductName(product.getProductName());
            stockTransaction.setTransactionDate(purchaseStockDTO.getPurchaseDate());
            stockTransaction.setProduct(product);
            stockTransaction.setModule("Purchases");
            stockTransaction.setStockIn(purchaseStock.getQty());
            stockTransaction.setStockOut(0);
            stockTransaction.setDescription("Stock Purchase for" +product.getProductName());
            stockTransaction.setStatus("Active");
            stockTransaction.setSerialNumber(sn);
            stockTransaction.setProductCost(purchaseStock.getUnitcost());
            stockTransaction.setDiscount(purchaseStock.getDiscount());
            stockTransaction.setTax(purchaseStock.getTax());
            Optional<Supplier> optionalSupplier = supplierService.getSupplierById(purchaseStockDTO.getSupplierId());
            optionalSupplier.ifPresent(stockTransaction::setSupplier);
            Optional<Store> optionalBranch = Optional.ofNullable(storeService.getStoreById(purchaseStockDTO.getBranchId()));
            optionalBranch.ifPresent(stockTransaction::setStore);

            stockTransactionRepository.save(stockTransaction);



            // Perform cost adjustment
            OpeningStockDTO openingStockDTO = new OpeningStockDTO();
            openingStockDTO.setOpcode("cost");


            OpeningStock openingStock = new OpeningStock();
            openingStock.setProductCode(purchaseStock.getProductCode());
            openingStock.setBuyPrice(purchaseStock.getUnitcost());
            openingStockDTO.setOpeningStocks(Collections.singletonList(openingStock));

            updatePrices(openingStockDTO);



        });





    }






    public void updatePrices(OpeningStockDTO openingStockDTO) {
        String sn = generateSerialNumber();
        openingStockDTO.getOpeningStocks().forEach(openingStock -> {
            Product product = productRepository.findByProductCode(openingStock.getProductCode());
            if (product == null) {
                throw new RuntimeException("Product not found with code: " + openingStock.getProductCode());
            }

            boolean priceChanged = false;
            StringBuilder description = new StringBuilder();

            double oldCostPrice = product.getProductCost();
            double oldSalePrice = product.getProductPrice();
            double newCostPrice = openingStock.getBuyPrice();
            double newSalePrice = openingStock.getSalePrice();

            if (oldCostPrice != newCostPrice) {
                priceChanged = true;
                description.append("Cost price adjusted from ").append(oldCostPrice).append(" to ").append(newCostPrice).append(". ");
            }

            if (oldSalePrice != newSalePrice) {
                priceChanged = true;
                description.append("Sale price adjusted from ").append(oldSalePrice).append(" to ").append(newSalePrice).append(". ");
            }

            if (priceChanged) {
                StockTransaction stockTransaction = new StockTransaction();
                stockTransaction.setProductCode(openingStock.getProductCode());
                stockTransaction.setProductName(product.getProductName());
                stockTransaction.setTransactionDate(LocalDate.now());
                stockTransaction.setProduct(product);
                stockTransaction.setStatus("Active");
                stockTransaction.setSerialNumber(sn);
                stockTransaction.setModule("Price Adjustment");
                stockTransaction.setStockIn(0);
                stockTransaction.setStockOut(0);
                stockTransaction.setDescription(description.toString().trim());





                // Update product prices
                productService.updateProductPrices(product.getId(), newSalePrice, newCostPrice);

                // Save the transaction
                stockTransactionRepository.save(stockTransaction);
            }
        });
    }




    public void uploadProductsFromExcel(MultipartFile file) {
        try {
            // Create a new workbook and sheet for the Excel file
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through each row in the sheet
            for (Row row : sheet) {
                // Skip the header row
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Get the product code and quantity from the row
                String productCode = row.getCell(0).getStringCellValue();
                int qty = (int) row.getCell(1).getNumericCellValue();

                // Create a new StoreOpeningStockDTO
                OpeningStockDTO openingStockDTO = new OpeningStockDTO();
                openingStockDTO.setOpcode("Add");

                // Initialize the OpeningStocks list
                List<OpeningStock> openingStocks = new ArrayList<>();

                // Create a new StoreOpeningStock
                OpeningStock openingStock = new OpeningStock();
                openingStock.setProductCode(productCode);
                openingStock.setQty(qty);

                // Add the StoreOpeningStock to the list
                openingStocks.add(openingStock);

                // Set the OpeningStocks list to the StoreOpeningStockDTO
                openingStockDTO.setOpeningStocks(openingStocks);

                // Call the createOpeningStock method with the StoreOpeningStockDTO
                createOpeningStock(openingStockDTO);
            }

            // Close the workbook
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    public String generateSerialNumber() {
        String prefix = "STK-";
        int numDigits = 6;
        String format = "%s%0" + numDigits + "d";

        // find all existing serial numbers with the specified prefix
        List<String> existingSerialNumbers = stockTransactionRepository.findSerialNumbersWithPrefix(prefix);

        // if no existing serial numbers, start at 1
        int nextNumber = 1;

        // if existing serial numbers, add 1 to the highest number
        if (!existingSerialNumbers.isEmpty()) {
            String highestSerialNumber = existingSerialNumbers.get(0);
            int highestNumber = Integer.parseInt(highestSerialNumber.substring(prefix.length()));
            nextNumber = highestNumber + 1;
        }

        // format the number with zero-padding and prefix


        return String.format(format, prefix, nextNumber);
    }
}
