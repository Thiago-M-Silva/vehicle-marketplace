package org.acme.controllers;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.acme.abstracts.Vehicles;
import org.acme.dtos.VehicleDocumentRequestDTO;
import org.acme.dtos.VehicleSearchDTO;
import org.acme.middlewares.ApiMiddleware;
import org.acme.services.GridFSService;
import org.acme.services.VehicleService;
import org.jboss.resteasy.reactive.MultipartForm;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleController {
    @Inject VehicleService vehicleService;
    @Inject ApiMiddleware apiMiddleware;
    @Inject GridFSService gridFSService;

    /**
     * Retrieves a list of all vehicles of a specified type.
     * @param vehicleType the type of vehicles to retrieve (e.g., "car", "truck")
     * @return a {@link Response} containing a list of vehicles if successful,
     *         or an error message with HTTP 500 status if an exception occurs.
     */
    @GET
    @Path("/get/{vehicleType}")
    public Response getAllVehicles(
        @PathParam("vehicleType") String vehicleType
    ) {
       try {
            List<Vehicles> vehicles = vehicleService.listAll(vehicleType);
            var responseDTOs = apiMiddleware.manageVehicleTypeResponseDTO(vehicleType, vehicles);
            return Response.ok(responseDTOs).build();
       } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error retrieving vehicles: " + e.getMessage())
                           .build();
       }
    }

    /**
     * Downloads a vehicle document by its ID.
     * @param vehicleId the ID of the vehicle
     * @return a {@link Response} containing the vehicle document if successful,
     *         or an error message with HTTP 500 status if an exception occurs.
     */
    @GET
    @Path("/get/download/{vehicleId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("vehicleId") String vehicleId){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridFSService.downloadFile(vehicleId, outputStream);
        
        return Response.ok(outputStream.toByteArray())
            .header("Content-Disposition", "attachment; filename=\"" + vehicleId + "\"")
            .build();
    }

    /**
     * Retrieves a vehicle by its unique identifier and type.
     * @param vehicleType
     * @param id
     * @return a {@link Response} containing the vehicle data if found, or an appropriate error message if not found or if an error occurs
     */
    @GET
    @Path("/get/{vehicleType}/{id}")
    public Response getVehiclesById(
        @PathParam("vehicleType") String vehicleType, 
        @PathParam("id") UUID id
    ) {
        try {
            Vehicles vehicle = vehicleService.findById(vehicleType, id);
            return Response.ok(vehicle).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Vehicle not found: " + e.getMessage())
                           .build();
        }
    }

    /**
     * Search vehicles by type and various parameters.
     * @param vehicleType
     * @param searchParams
     * @return a {@link Response} containing a list of vehicles matching the search criteria if successful,
     *         or an error message with HTTP 400 status if an exception occurs.
     */
    @GET
    @Path("/get/search/{vehicleType}")
    public Response search(
        @PathParam("vehicleType") String vehicleType,
        @BeanParam VehicleSearchDTO searchParams
    ) {
        try {
            var vehicles = vehicleService.searchVehicle(vehicleType, searchParams);
            return Response.ok(vehicles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Searching error: " + e.getMessage())
                        .build();
        }
    }

    /**
     * Save multiple vehicles of a specified type.
     * @param vehicleType
     * @param vehicles
     * @return a {@link Response} with the number of vehicles saved if successful,
     *         or an error message with HTTP 400 status if an exception occurs.
     */
    @POST
    @Path("/save/saveAllVehicles/{vehicleType}")
    public Response saveAllVehicles(
        @PathParam("vehicleType") String vehicleType, 
        List<Map<String, Object>> vehicles
    ) {
        try {
            var vehiclesRequestDTO = apiMiddleware.manageListVehiclesTypeRequestDTO(vehicleType, vehicles);
            int savedVehicles = vehicleService.saveMultipleVehicles(vehicleType, vehiclesRequestDTO);
            return Response.status(Response.Status.CREATED).entity(savedVehicles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Error saving vehicle: " + e.getMessage())
                           .build();
        }
    }

    /**
     * Handles HTTP POST requests to create a new vehicle of a specified type.
     * @param vehicleType the type of vehicle to create (e.g., "car", "truck")
     * @param body a {@link JsonObject} containing the vehicle data
     * @return a {@link Response} with the created vehicle if successful,
     *         or an error message with HTTP 400 status if an exception occurs.
     */
    @POST
    @Path("/save/{vehicleType}")
    public Response addVehicle(
        @PathParam("vehicleType") String vehicleType,
        JsonObject body 
    ) {
        try {
            Vehicles vehicle = apiMiddleware.manageVehiclesTypeRequestDTO(vehicleType, body);

            Vehicles savedVehicle = vehicleService.save(vehicleType, vehicle);
            return Response.status(Response.Status.CREATED).entity(savedVehicle).build();
        } catch (Exception e) {
            e.printStackTrace(); 
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Error saving vehicle: " + e.getMessage())
                           .build();
        }
    }

    /**
     * Handles HTTP POST requests to create a new vehicle of a specified type with associated documents.
     * @param vehicleType the type of vehicle to create (e.g., "car", "truck")
     * @param data a {@link VehicleDocumentRequestDTO} containing the vehicle data and document file
     * @return a {@link Response} with the created vehicle if successful,
     *         or an error message with HTTP 400 status if an exception occurs.
     */
    @POST
    @Path("/save/{vehicleType}/docs")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response saveVehicleWithDocs(
        @PathParam("vehicleType") String vehicleType,
        @MultipartForm VehicleDocumentRequestDTO data
    ){
        try {
            if (data == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Form data is required")
                    .build();
            }

            if (data.vehicles == null || data.vehicles.isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Field 'vehicles' (JSON) is required in the multipart form")
                    .build();
            }

            JsonObject json = Json.createReader(new StringReader(data.vehicles)).readObject();

            var vehicleRequestDTO = apiMiddleware.manageVehiclesTypeRequestDTO(vehicleType, json);

            if (data.file == null || data.filename == null || data.filename.isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("File and filename are required")
                    .build();
            }

            Vehicles savedVehicles = vehicleService.saveVehicleWithDocuments(
                vehicleType,
                (Vehicles) vehicleRequestDTO,
                data.file,
                data.filename,
                data.contentType
            );

            return Response.status(Response.Status.CREATED).entity(savedVehicles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Erro ao salvar veículo e documentos: " + e.getMessage())
                .build();
        }
    }

    /**
     * Handles HTTP DELETE requests to delete a vehicle by its unique identifier and type.
     * @param vehicleType
     * @param id
     * @return Response with HTTP 204 status if successful, or an error message with HTTP 404 status if an exception occurs.
     */
    @DELETE
    @Path("/delete/{vehicleType}/{id}")
    public Response deleteVehicle(
        @PathParam("vehicleType") String vehicleType, 
        @PathParam("id") UUID id
    ) {
        try {
            vehicleService.deleteById(vehicleType, id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Error deleting vehicle: " + e.getMessage())
                           .build();
        }
    }
    
    /**
     * Handles HTTP DELETE requests to delete multiple vehicles by their unique identifiers and type.
     * @param vehicleType
     * @param id
     * @return Response with HTTP 204 status if successful, or an error message with HTTP 404 status if an exception occurs.
     */
    @DELETE
    @Path("/delete/{vehicleType}")
    public Response deleteManyVehicles(
        @PathParam("vehicleType") String vehicleType, 
        List<UUID> id
    ) {
        try {
            vehicleService.deleteManyVehicles(vehicleType, id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Error deleting vehicle: " + e.getMessage())
                           .build();
        }
    }

    /**
     * Handles HTTP PUT requests to edit a vehicle by its unique identifier and type.
     * @param vehicleType
     * @param id
     * @param body a {@link JsonObject} containing the updated vehicle data
     * @return Response with HTTP 204 status if successful, or an error message with HTTP 404 status if an exception occurs.
     */
    @PUT
    @Path("/edit/{vehicleType}/{id}")
    public Response editVehicle(
        @PathParam("vehicleType") String vehicleType,
        @PathParam("id") UUID id,
        JsonObject body
    ){
        try {
            Vehicles vehicle = apiMiddleware.manageVehiclesTypeRequestDTO(vehicleType, body);

            vehicleService.editVehicleInfo(vehicleType, id, vehicle);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Error editing vehicle: " + e.getMessage())
                           .build();
        }
    }

}
