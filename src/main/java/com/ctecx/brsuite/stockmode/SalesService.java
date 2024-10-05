package com.ctecx.brsuite.stockmode;

import com.ctecx.brsuite.customers.Customer;
import com.ctecx.brsuite.customers.CustomerService;
import com.ctecx.brsuite.customproductsmanager.CustomManagerProductService;
import com.ctecx.brsuite.products.Product;
import com.ctecx.brsuite.products.ProductRepository;
import com.ctecx.brsuite.transactions.OrderState;
import com.ctecx.brsuite.transactions.PaymentState;
import com.ctecx.brsuite.transactions.SaleTransactionDTO;
import com.ctecx.brsuite.transactions.TransactionService;
import com.ctecx.brsuite.util.SalesDateTimeManager;
import com.ctecx.brsuite.warehouse.StoreService;
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
import java.util.Optional;

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
    private final StoreService storeService;
    private final TransactionService transactionService;

    @Transactional
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
    }

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
        String serialNumber = generateUniqueSerialNumber();
        int updatedCount = customManagerProductService.updateSerialNumbers(serialNumber, splitOrderDTO.getIds());
        if (updatedCount != splitOrderDTO.getIds().size()) {
            throw new RuntimeException("Not all orders were updated. Expected: " + splitOrderDTO.getIds().size() + ", Actual: " + updatedCount);
        }
        return serialNumber;
    }

/*    private StockTransaction createStockTransaction(SaleStock saleStock, Product product, String sn,String oderNumber, SalesStockDTO salesStockDTO) {
        StockTransaction stockTransaction = new StockTransaction();
        ZonedDateTime transactionDateTime = salesDateTimeManager.getCurrentTransactionDateTime();
        LocalDate salesDate = salesDateTimeManager.getSalesDate(transactionDateTime);
        System.out.println("Zone Time"+transactionDateTime);
        System.out.println("Date"+salesDate);
        log.info("Processing sale at {} for sales date {}", transactionDateTime, salesDate);



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
        System.out.println("Current Mode"+salesStockDTO.isAddItems());
        System.out.println("ExistingSerialNumber"+salesStockDTO.getExistingSerialNumber());

        // Check if addItems is true and use existingSerialNumber if available
        if (salesStockDTO.isAddItems()){
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
        stockTransaction.setOrderNumber(oderNumber);

        return stockTransaction;
    }*/



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
