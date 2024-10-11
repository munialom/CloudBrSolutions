package com.ctecx.brsuite.stockmode;

import com.ctecx.brsuite.customers.Customer;
import com.ctecx.brsuite.customers.CustomerService;
import com.ctecx.brsuite.customproductsmanager.CustomManagerProductService;
import com.ctecx.brsuite.customproductsmanager.CustomProductManagerRepository;
import com.ctecx.brsuite.products.Product;
import com.ctecx.brsuite.products.ProductRepository;
import com.ctecx.brsuite.revenue.Revenue;
import com.ctecx.brsuite.transactions.OrderState;
import com.ctecx.brsuite.transactions.PaymentState;
import com.ctecx.brsuite.transactions.SaleTransactionDTO;
import com.ctecx.brsuite.transactions.TransactionService;
import com.ctecx.brsuite.util.ResourceNotFoundException;
import com.ctecx.brsuite.util.SalesDateTimeManager;
import com.ctecx.brsuite.warehouse.StoreService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class SalesService {
    private final ProductRepository productRepository;
    private final StockTransactionRepository stockTransactionRepository;
    private final CustomerService customerService;
    private final SalesDateTimeManager salesDateTimeManager;
    private final TransactionTemplate transactionTemplate;
    private final CustomManagerProductService customManagerProductService;
    private  final CustomProductManagerRepository customProductManagerRepository;
    private final StoreService storeService;
    private final TransactionService transactionService;

    private final ConcurrentHashMap<String, Product> productCache = new ConcurrentHashMap<>();



    @PostConstruct
    public void initializeCache() {
        refreshCache();
    }

    public void refreshCache() {
        List<Map<String, Object>> allProducts = customManagerProductService.GetAllProducts();
        productCache.clear();
        productCache.putAll(allProducts.stream()
                .map(this::mapProductDataToProduct)
                .collect(Collectors.toMap(Product::getProductCode, product -> product)));
    }

    private Product getProductByCode(String productCode) {
        Product product = productCache.get(productCode);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with code: " + productCode);
        }
        return product;
    }

    @Transactional
    public String createCounterSales(SalesStockDTO salesStockDTO) {


        String sn = customProductManagerRepository.generateUniqueSerialNumber("");
        String orderNumber = customProductManagerRepository.generateNewOrderNumber();

        BigDecimal totalAmount = salesStockDTO.getTotalAmount();
        BigDecimal receivedAmount = salesStockDTO.getAmountPaid();
        BigDecimal changeOut = calculateChangeOut(totalAmount, receivedAmount);

        List<StockTransaction> stockTransactions = new ArrayList<>();
        for (SaleStock saleStock : salesStockDTO.getSaleStocks()) {
            Product product = getProductByCode(saleStock.getProductCode());
            StockTransaction stockTransaction = createStockTransaction(saleStock, product, sn, orderNumber, salesStockDTO, changeOut);
            stockTransactions.add(stockTransaction);
        }
        stockTransactionRepository.saveAll(stockTransactions);

        // Create and process financial transactions
        SaleTransactionDTO saleTransactionDTO = createSaleTransactionDTO(salesStockDTO, sn, changeOut);
        transactionService.processSaleTransaction(saleTransactionDTO);

        return orderNumber;
    }
/*    @Transactional
    public String createCounterSales(SalesStockDTO salesStockDTO) {
        String sn = generateUniqueSerialNumber();
        String orderNumber = generateNewOrderNumber();

        BigDecimal totalAmount = salesStockDTO.getTotalAmount();
        BigDecimal receivedAmount = salesStockDTO.getAmountPaid();
        BigDecimal changeOut = calculateChangeOut(totalAmount, receivedAmount);

        List<StockTransaction> stockTransactions = new ArrayList<>();
        for (SaleStock saleStock : salesStockDTO.getSaleStocks()) {
            Product product = productRepository.findByProductCode(saleStock.getProductCode());
            StockTransaction stockTransaction = createStockTransaction(saleStock, product, sn, orderNumber, salesStockDTO, changeOut);
            stockTransactions.add(stockTransaction);
        }
        stockTransactionRepository.saveAll(stockTransactions);

        // Create and process financial transactions
        SaleTransactionDTO saleTransactionDTO = createSaleTransactionDTO(salesStockDTO, sn, changeOut);
        transactionService.processSaleTransaction(saleTransactionDTO);

        return orderNumber;
    }*/

    private BigDecimal calculateChangeOut(BigDecimal totalAmount, BigDecimal receivedAmount) {
        return receivedAmount.subtract(totalAmount).max(BigDecimal.ZERO);
    }

    private SaleTransactionDTO createSaleTransactionDTO(SalesStockDTO salesStockDTO, String serialNumber, BigDecimal changeOut) {
        SaleTransactionDTO dto = new SaleTransactionDTO();
        dto.setSerialNumber(serialNumber);
        dto.setTotalAmount(salesStockDTO.getTotalAmount());
        dto.setReceivedAmount(salesStockDTO.getAmountPaid());
        dto.setPaymentModes(salesStockDTO.getPayMode());
        dto.setDescription("Counter Sale");
        dto.setChangeOut(changeOut);
        return dto;
    }

    private StockTransaction createStockTransaction(SaleStock saleStock, Product product, String sn, String orderNumber, SalesStockDTO salesStockDTO, BigDecimal changeOut) {
        StockTransaction stockTransaction = new StockTransaction();
        ZonedDateTime transactionDateTime = salesDateTimeManager.getCurrentTransactionDateTime();
        LocalDate salesDate = salesDateTimeManager.getSalesDate(transactionDateTime);

        stockTransaction.setProductCode(product.getProductCode());
        stockTransaction.setProductName(product.getProductName());
        stockTransaction.setTransactionDate(salesDate);
        stockTransaction.setProduct(product);
        stockTransaction.setModule("SALES");
        stockTransaction.setSubModule("CASH SALE");
        stockTransaction.setStockIn(0);
        stockTransaction.setRevenue(product.getRevenue());
        stockTransaction.setRevenue_code(product.getRevenue().getRevenueName());

        Optional<Customer> optionalCustomer = Optional.ofNullable(customerService.getCustomerById(salesStockDTO.getCustomerId()));
        optionalCustomer.ifPresent(stockTransaction::setCustomer);

        stockTransaction.setStockOut(saleStock.getQty());
        stockTransaction.setDescription("Stock Sale for " + product.getProductName());
        stockTransaction.setStatus("Active");

        if (salesStockDTO.isAddItems()) {
            stockTransaction.setSerialNumber(salesStockDTO.getExistingSerialNumber());
        } else {
            stockTransaction.setSerialNumber(sn);
        }

        stockTransaction.setProductCost(product.getProductCost());
        stockTransaction.setProductSalePrice(product.getProductPrice());
        stockTransaction.setDiscount(saleStock.getDiscount());
        stockTransaction.setTax(saleStock.getTax());
        stockTransaction.setNetTax(saleStock.getNetTax());
        stockTransaction.setSubtotal(saleStock.getSubtotal());
        stockTransaction.setChangeOut(changeOut);
        stockTransaction.setPaymentState(PaymentState.PENDING);
        stockTransaction.setOrderState(OrderState.OPEN);
        stockTransaction.setTransactionDate(salesDate);
        stockTransaction.setStore(storeService.getDefaultCounterStore());
        stockTransaction.setOrderNumber(orderNumber);

        return stockTransaction;
    }



    @Transactional
    public String splitOrders(SplitOrderDTO splitOrderDTO) {
        String serialNumber = customProductManagerRepository.generateUniqueSerialNumber("");
        int updatedCount = customManagerProductService.updateSerialNumbers(serialNumber, splitOrderDTO.getIds());
        if (updatedCount != splitOrderDTO.getIds().size()) {
            throw new RuntimeException("Not all orders were updated. Expected: " + splitOrderDTO.getIds().size() + ", Actual: " + updatedCount);
        }
        return serialNumber;
    }

    private Product mapProductDataToProduct(Map<String, Object> productData) {
        Product product = new Product();
        product.setId(((Number) productData.get("id")).longValue());
        product.setProductCode((String) productData.get("product_code"));
        product.setProductName((String) productData.get("product_name"));
        product.setProductType((String) productData.get("product_type"));
        product.setProductCost(((Number) productData.get("product_cost")).doubleValue());
        product.setProductPrice(((Number) productData.get("product_price")).doubleValue());
        product.setAlertQuantity((Integer) productData.get("alert_qty"));
        product.setTaxMode((String) productData.get("tax_mode"));

        // Handle Revenue
        Long revenueId = productData.get("revenue_id") != null ? ((Number) productData.get("revenue_id")).longValue() : null;
        String revenueName = (String) productData.get("revenue_name");
        if (revenueId != null && revenueName != null) {
            Revenue revenue = new Revenue();
            revenue.setId(revenueId);
            revenue.setRevenueName(revenueName);
            product.setRevenue(revenue);
        }


        return product;
    }



    private String generateUniqueSerialNumber() {
        return transactionTemplate.execute(status -> {
            String sn = generateSerialNumber();
            while (stockTransactionRepository.existsBySerialNumber(sn)) {
                sn = generateSerialNumber();
            }
            return sn;
        });
    }

    public String generateSerialNumber() {
        String prefix = "SV-";
        int numDigits = 4;
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

    public String generateSerialNumber(String type) {
        String prefix;
        switch (type.toLowerCase()) {
            case "cashsale":
                prefix = "STK-";
                break;
            case "invoice":
                prefix = "INV-";
                break;
            case "quote":
                prefix = "QT-";
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        int numDigits = 6;
        String format = "%s%0" + numDigits + "d";

        List<String> existingSerialNumbers = stockTransactionRepository.findSerialNumbersWithPrefix(prefix);

        int nextNumber = 1;
        if (!existingSerialNumbers.isEmpty()) {
            String highestSerialNumber = existingSerialNumbers.get(0);
            int highestNumber = Integer.parseInt(highestSerialNumber.substring(prefix.length()));
            nextNumber = highestNumber + 1;
        }

        return String.format(format, prefix, nextNumber);
    }


    private String generateNewOrderNumber() {
        LocalDate currentDate = LocalDate.now();
        String prefix = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd-"));
        int numDigits = 4;
        String format = "%s%0" + numDigits + "d";

        List<String> existingOrderNumbers = stockTransactionRepository.findOrderNumbersWithPrefix(prefix);

        int nextNumber = 1;
        if (!existingOrderNumbers.isEmpty()) {
            String highestOrderNumber = existingOrderNumbers.get(0);
            int highestNumber = Integer.parseInt(highestOrderNumber.substring(prefix.length()));
            nextNumber = highestNumber + 1;
        }

        return String.format(format, prefix, nextNumber);
    }


}
