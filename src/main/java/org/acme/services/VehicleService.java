package org.acme.services;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.acme.abstracts.Vehicles;
import org.acme.dtos.VehicleSearchDTO;
import org.acme.enums.ECategory;
import org.acme.enums.EColors;
import org.acme.middlewares.ApiMiddleware;
import org.acme.model.VehicleDocuments;
import org.acme.repositories.BikesRepository;
import org.acme.repositories.BoatsRepository;
import org.acme.repositories.CarsRepository;
import org.acme.repositories.PlanesRepository;
import org.acme.repositories.VehicleDocumentsRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class VehicleService {
    @Inject 
    ApiMiddleware apiMiddleware;

    @Inject 
    BikesRepository bikesRepository;
    
    @Inject 
    CarsRepository carsRepository;
    
    @Inject 
    BoatsRepository boatsRepository;
    
    @Inject 
    PlanesRepository planesRepository;

    @Inject
    GridFSService gridFSService;

    @Inject
    VehicleDocumentsRepository repository;

    @SuppressWarnings("unchecked")
    private <T extends Vehicles> PanacheRepositoryBase<T, UUID> getRepository(String vehicleType) {
        return (PanacheRepositoryBase<T, UUID>) switch (vehicleType.toLowerCase()) {
            case "bikes" -> bikesRepository;
            case "cars"  -> carsRepository;
            case "boats" -> boatsRepository;
            case "planes"-> planesRepository;
            default      -> throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        };
    }

    public <T extends Vehicles> T save(String type, T vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        var repository = getRepository(type);
        repository.persist(vehicle);
        return vehicle;
    }

    public int saveMultipleVehicles(String type, List<Vehicles> vehicles){
        if (type == null || vehicles.isEmpty()) {
            throw new IllegalArgumentException("Type or vehicles cannot be null");
        }
        var repository = getRepository(type);
        repository.persist(vehicles);
        return vehicles.size();
    }

    public List<Vehicles> listAll(String type) {
        try {
            List<Vehicles> vehicles = getRepository(type).listAll();
            return vehicles;
        } catch (Exception e) {
            throw new RuntimeException("Failed to list vehicles for type: " + type, e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Vehicles> T findById(String type, UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        var repository = getRepository(type);
        return (T) repository.findById(id);
    }

    public <T extends Vehicles> T saveVehicleWithDocuments(String type, T vehicle, InputStream fileStream, String filename, String contentType){
        T savedVehicle = save(type, vehicle);

        if(fileStream != null && filename != null) {
            saveDocument(savedVehicle.getId(), filename, contentType, fileStream);
        }

        return savedVehicle;
    }

    public VehicleDocuments saveDocument(UUID vehicleId, String filename, String contentType, InputStream fileStream){
        gridFSService.uploadFile(filename, contentType, fileStream);
        
        VehicleDocuments doc = new VehicleDocuments();
        doc.vehicleId = vehicleId;
        doc.fileName = filename;
        doc.contentType = contentType;

        repository.persist(doc);
        return doc;
    }

    public boolean deleteById(String type, UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        var repository = getRepository(type);
        return repository.deleteById(id);
    }

    public Long deleteManyVehicles(String type, List<UUID> idList){
        if(idList.isEmpty()){
            throw new IllegalArgumentException("IdList cannot be empty");
        }
        var repository = getRepository(type);
        return repository.delete("id in ?1", idList);
    }

    public <T extends Vehicles> T editVehicleInfo(String type, UUID id, T vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        var repository = getRepository(type);
        repository.update("id like ?1", id, vehicle);
        return vehicle;
    }

    //TODO: Adaptar para todos os parametros do banco de dados
    public <T extends Vehicles> T searchVehicle(
        VehicleSearchDTO searchParams
    ){
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (searchParams.getBrand() != null && !searchParams.getBrand().isBlank()) {
            query.append(" AND brand = :brand");
            params.put("brand", searchParams.getBrand());
        }
        if (searchParams.getModel() != null && !searchParams.getModel().isBlank()) {
            query.append(" AND model ILIKE :model");
            params.put("model", "%" + searchParams.getModel() + "%");
        }
        if (searchParams.getYearMin() != null) {
            query.append(" AND year >= :yearMin");
            params.put("yearMin", searchParams.getYearMin());
        }
        if (searchParams.getYearMax() != null) {
            query.append(" AND year <= :yearMax");
            params.put("yearMax", searchParams.getYearMax());
        }
        if (searchParams.getPriceMin() != null) {
            query.append(" AND price >= :priceMin");
            params.put("priceMin", searchParams.getPriceMin());
        }
        if (searchParams.getPriceMax() != null) {
            query.append(" AND price <= :priceMax");
            params.put("priceMax", searchParams.getPriceMax());
        }
        if (searchParams.getCategory() != null && !searchParams.getCategory().isBlank()) {
            query.append(" AND category = :category");
            params.put("category", ECategory.valueOf(searchParams.getCategory().toUpperCase()));
        }
        if (searchParams.getColor() != null && !searchParams.getColor().isBlank()) {
            query.append(" AND color = :color");
            params.put("color", EColors.valueOf(searchParams.getColor().toUpperCase()));
        }

        // Sorting
        Sort sort = searchParams.getDirection().equalsIgnoreCase("DESC")
                ? Sort.descending(searchParams.getSortBy())
                : Sort.ascending(searchParams.getSortBy());

        // Execute query with pagination
        var vehicles = Vehicles.find(query.toString(), sort, params)
                               .page(Page.of(searchParams.getPage(), searchParams.getSize()))
                               .list();

        return (T) vehicles;
    }
}