package com.ctecx.brsuite.purchases;

import com.ctecx.brsuite.products.Product;
import com.ctecx.brsuite.products.ProductRepository;
import com.ctecx.brsuite.products.ProductService;
import com.ctecx.brsuite.stockmode.OpeningStock;
import com.ctecx.brsuite.stockmode.OpeningStockDTO;
import com.ctecx.brsuite.stockmode.StockService;
import com.ctecx.brsuite.suppliers.SupplierService;
import com.ctecx.brsuite.suppliers.Vendor;
import com.ctecx.brsuite.warehouse.Store;
import com.ctecx.brsuite.warehouse.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainStoreTransactionService {

    private final ProductRepository productRepository;
    private final MainStoreTransactionRepository mainStoreTransactionRepository;
    private final ProductService productService;
    private final SupplierService supplierService;
    private final StoreRepository storeRepository;
    private final StockService stockService;

    @Transactional
    public void createOpeningStock(StoreOpeningStockDTO storeOpeningStockDTO) {
        String sn = generateSerialNumber();
        Store store = getStoreFromDTO(storeOpeningStockDTO.getBranchId());

        for (StoreOpeningStock storeOpeningStock : storeOpeningStockDTO.getStoreOpeningStocks()) {
            Product product = getProductByCode(storeOpeningStock.getProductCode());
            MainStoreTransaction transaction = createBaseTransaction(product, sn, store, "Opening Stock");

            if ("ADD".equals(storeOpeningStockDTO.getOpcode())) {
                transaction.setStockIn(storeOpeningStock.getQty());
                transaction.setStockOut(0);
                transaction.setDescription("Adding opening stock for " + product.getProductName());
            } else if ("SUBTRACT".equals(storeOpeningStockDTO.getOpcode())) {
                transaction.setStockIn(0);
                transaction.setStockOut(storeOpeningStock.getQty());
                transaction.setDescription("Subtracting opening stock for " + product.getProductName());
            } else {
                throw new IllegalArgumentException("Invalid opcode: " + storeOpeningStockDTO.getOpcode());
            }

            mainStoreTransactionRepository.save(transaction);
        }
    }

    @Transactional
    public void createPurchase(PurchaseStockDTO purchaseStockDTO) {
        String sn = generateSerialNumber();
        Store store = getStoreFromDTO(purchaseStockDTO.getBranchId());
        Vendor vendor = getSupplierFromDTO(purchaseStockDTO.getSupplierId());

        for (PurchaseStock purchaseStock : purchaseStockDTO.getPurchaseStocks()) {
            Product product = getProductByCode(purchaseStock.getProductCode());
            MainStoreTransaction transaction = createBaseTransaction(product, sn, store, "Purchase");

            transaction.setStockIn(purchaseStock.getQty());
            transaction.setStockOut(0);
            transaction.setDescription("Stock Purchase for " + product.getProductName());
            transaction.setProductCost(purchaseStock.getUnitcost());
            transaction.setDiscount(purchaseStock.getDiscount());
            transaction.setTax(purchaseStock.getTax());
            transaction.setVendor(vendor);
            transaction.setTransactionDate(purchaseStockDTO.getPurchaseDate());

            mainStoreTransactionRepository.save(transaction);
        }
    }

/*    @Transactional
    public void createIssuance(IssuanceStockDTO issuanceStockDTO) {
        String sn = generateSerialNumber();
        Store fromStore = getStoreFromDTO(issuanceStockDTO.getFromStoreId());
        Store toStore = getStoreFromDTO(issuanceStockDTO.getToStoreId());

        for (IssuanceStock issuanceStock : issuanceStockDTO.getIssuanceStocks()) {
            Product product = getProductByCode(issuanceStock.getProductCode());

            // Create outgoing transaction from main store
            MainStoreTransaction outTransaction = createBaseTransaction(product, sn, fromStore, "Issuance Out");
            outTransaction.setStockIn(0);
            outTransaction.setStockOut(issuanceStock.getQty());
            outTransaction.setDescription("Stock Issuance from Main Store to " + toStore.getStoreName());
            outTransaction.setTransactionDate(issuanceStockDTO.getIssuanceDate());
            mainStoreTransactionRepository.save(outTransaction);

            // Create incoming transaction to destination store
            MainStoreTransaction inTransaction = createBaseTransaction(product, sn, toStore, "Issuance In");
            inTransaction.setStockIn(issuanceStock.getQty());
            inTransaction.setStockOut(0);
            inTransaction.setDescription("Stock Received from Main Store");
            inTransaction.setTransactionDate(issuanceStockDTO.getIssuanceDate());
            mainStoreTransactionRepository.save(inTransaction);
        }
    }*/


    @Transactional
    public void createIssuance(IssuanceStockDTO issuanceStockDTO) {
        String sn = generateSerialNumber();
        Store fromStore = getStoreFromDTO(issuanceStockDTO.getFromStoreId());
        Store toStore = getStoreFromDTO(issuanceStockDTO.getToStoreId());

        for (IssuanceStock issuanceStock : issuanceStockDTO.getIssuanceStocks()) {
            Product product = getProductByCode(issuanceStock.getProductCode());

            // Create outgoing transaction from main store
            MainStoreTransaction outTransaction = createBaseTransaction(product, sn, fromStore, "Issuance Out");
            outTransaction.setStockIn(0);
            outTransaction.setStockOut(issuanceStock.getQty());
            outTransaction.setDescription("Stock Issuance from Main Store to " + toStore.getStoreName());
            outTransaction.setTransactionDate(issuanceStockDTO.getIssuanceDate());
            mainStoreTransactionRepository.save(outTransaction);

            // Create incoming transaction to destination store using stock service
            OpeningStockDTO storeOpeningStockDTO = new OpeningStockDTO();
            storeOpeningStockDTO.setOpcode("ADD");
            storeOpeningStockDTO.setBranchId(toStore.getId());

            OpeningStock storeOpeningStock = new OpeningStock();
            storeOpeningStock.setProductCode(product.getProductCode());
            storeOpeningStock.setProductName(product.getProductName());
            storeOpeningStock.setBuyPrice(product.getProductCost());
            storeOpeningStock.setSalePrice(product.getProductPrice());
            storeOpeningStock.setQty(issuanceStock.getQty());

            storeOpeningStockDTO.setOpeningStocks(Collections.singletonList(storeOpeningStock));

            // Call stock service to create incoming transaction
            stockService.createOpeningStock(storeOpeningStockDTO);
        }
    }

    private MainStoreTransaction createBaseTransaction(Product product, String serialNumber, Store store, String module) {
        MainStoreTransaction transaction = new MainStoreTransaction();
        transaction.setProduct(product);
        transaction.setProductCode(product.getProductCode());
        transaction.setProductName(product.getProductName());
        transaction.setProductCost(product.getProductCost());
        transaction.setProductSalePrice(product.getProductPrice());
        transaction.setSerialNumber(serialNumber);
        transaction.setStatus("Active");
        transaction.setStore(store);
        transaction.setModule(module);
        transaction.setTransactionDate(LocalDate.now());
        return transaction;
    }

    private Product getProductByCode(String productCode) {
        Product product = productRepository.findByProductCode(productCode);
        if (product == null) {
            throw new RuntimeException("Product not found with code: " + productCode);
        }
        return product;
    }

    private Store getStoreFromDTO(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));
    }

    private Vendor getSupplierFromDTO(Long supplierId) {
        return supplierService.getSupplierById(supplierId)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + supplierId));
    }

    public String generateSerialNumber() {
        String prefix = "STK-";
        int numDigits = 6;
        String format = "%s%0" + numDigits + "d";

        List<String> existingSerialNumbers = mainStoreTransactionRepository.findSerialNumbersWithPrefix(prefix);

        int nextNumber = existingSerialNumbers.isEmpty() ? 1 :
                Integer.parseInt(existingSerialNumbers.get(0).substring(prefix.length())) + 1;

        return String.format(format, prefix, nextNumber);
    }
}