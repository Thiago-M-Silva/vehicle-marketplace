package org.acme.services;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.acme.abstracts.Vehicles;
import org.acme.dtos.BikesRequestDTO;
import org.acme.dtos.BoatsRequestDTO;
import org.acme.dtos.CarsRequestDTO;
import org.acme.dtos.PlanesRequestDTO;
import org.acme.dtos.VehicleSearchDTO;
import org.acme.enums.ECategory;
import org.acme.enums.EColors;
import org.acme.enums.EFuelType;
import org.acme.enums.EStatus;
import org.acme.interfaces.VehicleMapper;
import org.acme.model.Bikes;
import org.acme.model.Boats;
import org.acme.model.Cars;
import org.acme.model.Planes;
import org.acme.model.VehicleDocuments;
import org.acme.repositories.BikesRepository;
import org.acme.repositories.BoatsRepository;
import org.acme.repositories.CarsRepository;
import org.acme.repositories.PlanesRepository;
import org.acme.repositories.VehicleDocumentsRepository;
import org.bson.types.ObjectId;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class VehicleService {
    @Inject BikesRepository bikesRepository;
    @Inject CarsRepository carsRepository;
    @Inject BoatsRepository boatsRepository;
    @Inject PlanesRepository planesRepository;
    @Inject GridFSService gridFSService;
    @Inject VehicleDocumentsRepository repository;
    @Inject VehicleMapper vehicleMapper;
    @Inject UserService userService;

    /**
     * Retrieves the appropriate PanacheRepositoryBase instance for the specified vehicle type.
     * <p>
     * This method returns a repository corresponding to the given vehicle type string.
     * Supported types are "bikes", "cars", "boats", and "planes". The returned repository
     * is cast to the generic type T which must extend Vehicles.
     * </p>
     * 
     * @param vehicleType the type of vehicle (e.g., "bikes", "cars", "boats", "planes")
     * @param <T> the type of vehicle entity, extending Vehicles
     * @return the repository for the specified vehicle type
     * @throws IllegalArgumentException if the vehicle type is unknown
     * @suppressWarnings("unchecked") because of the generic cast based on runtime type
     */
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

    /**
     * Retrieves a list of all vehicles of the specified type.
     *
     * @param type the type of vehicles to list (e.g., "car", "truck", etc.)
     * @return a list of {@link Vehicles} matching the specified type
     * @throws RuntimeException if an error occurs while retrieving the vehicles
     */
    public List<Vehicles> listAll(String type) {
        try {
            List<Vehicles> vehicles = getRepository(type).listAll();
            return vehicles;
        } catch (Exception e) {
            throw new RuntimeException("Failed to list vehicles for type: " + type, e);
        }
    }

    /**
     * Finds a vehicle entity by its type and unique identifier.
     *
     * @param type the type of the vehicle (used to determine the repository)
     * @param id the unique identifier of the vehicle to find; must not be null
     * @param <T> the type of the vehicle entity extending {@link Vehicles}
     * @return the vehicle entity of type {@code T} with the specified ID, or {@code null} if not found
     * @throws IllegalArgumentException if {@code id} is {@code null}
     */
    @SuppressWarnings("unchecked")
    public <T extends Vehicles> T findById(String type, UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        var repo = getRepository(type);
        return (T) repo.findById(id);
    }

    /**
     * Saves the given vehicle entity to the appropriate repository based on the specified type.
     *
     * @param type the type of the vehicle, used to determine the correct repository
     * @param vehicle the vehicle entity to be persisted; must not be null
     * @param <T> the type of the vehicle, extending {@link Vehicles}
     * @return the persisted vehicle entity
     * @throws IllegalArgumentException if the vehicle is null
     */
    @Transactional
    public <T extends Vehicles> T save(String type, T vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        var repo = getRepository(type);
        repo.persist(vehicle);
        return vehicle;
    }

    /**
     * Persists a list of vehicles of the specified type into the corresponding repository.
     *
     * @param type the type of vehicles to be saved, used to determine the appropriate repository
     * @param vehicles the list of vehicles to be persisted; must not be null or empty
     * @return the number of vehicles successfully persisted
     * @throws IllegalArgumentException if the vehicles list is null or empty
     */
    @Transactional
    public int saveMultipleVehicles(String type, List<Vehicles> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            throw new IllegalArgumentException("Lista de veículos não pode ser nula ou vazia");
        }

        var repo = getRepository(type);
        vehicles.forEach(repo::persist);
        return vehicles.size();
    }

    /**
     * Saves a vehicle of the specified type along with its associated document, if provided.
     *
     * <p>This method first persists the given vehicle entity. If a document InputStream and filename are provided,
     * it also saves the document associated with the saved vehicle.</p>
     *
     * @param <T>         the type of the vehicle, extending {@link Vehicles}
     * @param type        the type identifier for the vehicle
     * @param vehicle     the vehicle entity to be saved
     * @param fileStream  the InputStream of the document to be saved (can be {@code null})
     * @param filename    the name of the document file (can be {@code null})
     * @param contentType the MIME type of the document
     * @return the saved vehicle entity
     */
    @Transactional
    public <T extends Vehicles> T saveVehicleWithDocuments(String type, T vehicle, InputStream fileStream, String filename, String contentType){
        T savedVehicle = save(type, vehicle);

        if(fileStream != null && filename != null) {
            saveDocument(savedVehicle.getId(), filename, contentType, fileStream);
        }

        return savedVehicle;
    }

    /**
     * Saves a document associated with a vehicle by uploading the file to GridFS and persisting its metadata.
     *
     * @param vehicleId   the UUID of the vehicle to associate the document with; may be null
     * @param filename    the name of the file to be saved
     * @param contentType the MIME type of the file
     * @param fileStream  the InputStream of the file to be uploaded
     * @return the persisted {@link VehicleDocuments} entity containing metadata about the uploaded document
     * @throws RuntimeException if the file upload fails
     */
    @Transactional
    public VehicleDocuments saveDocument(UUID vehicleId, String filename, String contentType, InputStream fileStream){
        ObjectId fileObjectId = null;
        try (InputStream is = fileStream){
            fileObjectId = gridFSService.uploadFile(filename, contentType, is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload document for vehicle ID: " + vehicleId, e);
        }
        
        VehicleDocuments doc = new VehicleDocuments();

        doc.vehicleId = vehicleId == null ? null : vehicleId.toString();
        doc.fileName = filename;
        doc.contentType = contentType;

        doc.id = fileObjectId.toHexString();
        doc.uploadDate = java.time.Instant.now();

        repository.persist(doc);
        return doc;
    }

    /**
     * Deletes a vehicle entity by its unique identifier and type.
     *
     * @param type the type of the vehicle repository to use
     * @param id the unique identifier of the vehicle to delete; must not be null
     * @return true if the entity was successfully deleted, false otherwise
     * @throws IllegalArgumentException if the provided id is null
     */
    @Transactional
    public boolean deleteById(String type, UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        var repo = getRepository(type);
        return repo.deleteById(id);
    }

    /**
     * Deletes multiple vehicles of the specified type whose IDs are provided in the list.
     *
     * @param type   the type of vehicle repository to use for deletion
     * @param idList the list of UUIDs representing the vehicles to be deleted; must not be empty
     * @return the number of vehicles deleted
     * @throws IllegalArgumentException if {@code idList} is empty
     */
    @Transactional
    public Long deleteManyVehicles(String type, List<UUID> idList){
        if(idList.isEmpty()){
            throw new IllegalArgumentException("IdList cannot be empty");
        }

        //TODO: delete many isn't working
        var repo = getRepository(type);
        return repo.delete("id in ?1", idList);
    }

    /**
     * Edits the information of an existing vehicle based on its type and ID.
     * <p>
     * This method updates the details of a vehicle (bike, boat, car, or plane) by mapping the provided
     * {@link Vehicles} object to the appropriate request DTO and applying the changes to the existing entity.
     * </p>
     *
     * @param type    the type of the vehicle ("bikes", "boats", "cars", or "planes")
     * @param id      the unique identifier of the vehicle to be edited
     * @param vehicle the updated vehicle information; must not be {@code null}
     * @throws IllegalArgumentException if {@code vehicle} is {@code null}, if the vehicle with the specified
     *                                  {@code id} does not exist, or if the {@code type} is unknown
     */
    @Transactional
    public void editVehicleInfo(String type, UUID id, Vehicles vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        
        var existingVehicle = findById(type, id);
        if (existingVehicle == null) {
            throw new IllegalArgumentException("Vehicle not found with ID: " + id);
        }

        switch (type.toLowerCase()) {
            case "bikes" -> {
                BikesRequestDTO dto = vehicleMapper.toBikesRequestDTO((Bikes) vehicle);
                vehicleMapper.updateBikesFromDTO(dto, existingVehicle);
            }
            case "boats" -> {
                BoatsRequestDTO dto = vehicleMapper.toBoatsRequestDTO((Boats) vehicle);
                vehicleMapper.updateBoatsFromDTO(dto, existingVehicle);
            }
            case "cars"  -> {
                CarsRequestDTO dto = vehicleMapper.toCarsRequestDTO((Cars) vehicle);
                vehicleMapper.updateCarsFromDTO(dto, existingVehicle);
            }
            case "planes"-> {
                PlanesRequestDTO dto = vehicleMapper.toPlanesRequestDTO((Planes) vehicle);
                vehicleMapper.updatePlanesFromDTO(dto, existingVehicle);
            }
            default -> throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
        
    }

    /**
     * Updates the owner of a vehicle to the specified customer and persists the change.
     *
     * <p>This method performs the following steps within a transactional boundary:
     * <ol>
     *   <li>Validates that {@code customerEmail} is not null or blank.</li>
     *   <li>Attempts to locate the vehicle identified by {@code type} and {@code id}.</li>
     *   <li>Retrieves the customer by email and maps the customer DTO to a domain/user object.</li>
     *   <li>Sets the located vehicle's owner to the resolved buyer and updates the corresponding
     *       user record via {@code userService.editUser(...)}.</li>
     * </ol>
     *
     * <p>Because the method is transactional, all changes (vehicle owner assignment and user edits)
     * are applied atomically and will be committed or rolled back together.
     *
     * @param type the vehicle type used to look up the vehicle (for example "car", "motorcycle")
     * @param id the UUID of the vehicle to update
     * @param customerEmail the email address of the customer who will become the vehicle owner;
     *                      must not be {@code null} or blank
     * @throws IllegalArgumentException if {@code customerEmail} is {@code null} or blank,
     *                                  if no vehicle is found for the given {@code type} and {@code id},
     *                                  or if no customer is found for {@code customerEmail}
     */
    @Transactional
    public void updateVehicleSold(String type, UUID id, String customerEmail) {
        if (customerEmail == null || customerEmail.isBlank()) {
            throw new IllegalArgumentException("Customer email cannot be null or blank");
        }

        var vehicle = findById(type, id);
        
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle not found with ID: " + id);
        }
        
        var customer = userService.getUserByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        var buyer = userService.userMapper.toUser(customer);
        vehicle.setOwner(buyer);
        userService.editUser(customer.id(), userService.userMapper.toUserRequestDTO(buyer));
    }

    /**
     * Searches for vehicles based on the provided vehicle type and search parameters.
     * Dynamically builds a query using the fields from the {@link VehicleSearchDTO} to filter results.
     * Supports filtering by brand, model, year range, price range, category, color, fuel type, and vehicle status.
     * Also supports sorting and pagination.
     *
     * @param vehicleType the type of vehicle to search for (e.g., "car", "truck", etc.)
     * @param searchParams the search parameters containing filter, sort, and pagination options
     * @return a list of vehicles matching the search criteria
     */
    public List<? extends Vehicles> searchVehicle(
        String vehicleType,
        VehicleSearchDTO searchParams
    ){
        StringBuilder query = new StringBuilder("1 = 1");
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
        if (searchParams.getFuelType() != null && !searchParams.getFuelType().isBlank()) {
            query.append(" AND fuelType = :fuelType");
            params.put("fuelType", EFuelType.valueOf(searchParams.getFuelType().toUpperCase()));
        }
        if (searchParams.getOwnerId() != null && !searchParams.getOwnerId().isBlank()) {
            UUID ownerId = UUID.fromString(searchParams.getOwnerId());
            query.append(" AND owner = :ownerId");
            params.put("ownerId", ownerId);
        }
        if (searchParams.getVehicleStatus() != null && !searchParams.getVehicleStatus().isBlank()) {
            query.append(" AND vehicleStatus = :vehicleStatus");
            params.put("vehicleStatus", EStatus.valueOf(searchParams.getVehicleStatus().toUpperCase()));
        }

        String sortBy = (searchParams.getSortBy() == null || searchParams.getSortBy().isBlank())
                ? "createDate" : searchParams.getSortBy();

        String direction = (searchParams.getDirection() == null || searchParams.getDirection().isBlank())
                ? "ASC" : searchParams.getDirection().toUpperCase();

        Sort sort = direction.equals("DESC")
                ? Sort.descending(sortBy)
                : Sort.ascending(sortBy);

        var repo = getRepository(vehicleType);

        var panacheQuery = repo.find(query.toString(), sort, params);

        var page = Page.of(Math.max(0, searchParams.getPage()), Math.max(1, searchParams.getSize()));
        List<? extends Vehicles> result = panacheQuery
                .page(page)
                .list();

        return result;
    }
}