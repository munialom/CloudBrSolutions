package com.ctecx.brsuite.warehouse;

import com.ctecx.brsuite.util.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Store getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));
    }

    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    public Store updateStore(Long id, Store storeDetails) {
        Store store = getStoreById(id);
        store.setStoreName(storeDetails.getStoreName());
        store.setCounterStore(storeDetails.isCounterStore());
        return storeRepository.save(store);
    }

    public void deleteStore(Long id) {
        Store store = getStoreById(id);
        storeRepository.delete(store);
    }


    public List<Store> searchStores(String keyword) {
        List<Store> stores = storeRepository.findByStoreNameContainingIgnoreCase(keyword);
        return stores.stream()
                .map(store -> new Store(store.getId(), store.getStoreName(), store.isCounterStore()))
                .collect(Collectors.toList());
    }

    public Store getDefaultCounterStore() {
        return storeRepository.findFirstByCounterStoreTrue()
                .orElseGet(() -> createDefaultCounterStore());
    }

    private Store createDefaultCounterStore() {
        Store defaultStore = new Store();
        defaultStore.setStoreName("Default Counter Store");
        defaultStore.setCounterStore(true);
        return storeRepository.save(defaultStore);
    }
}
