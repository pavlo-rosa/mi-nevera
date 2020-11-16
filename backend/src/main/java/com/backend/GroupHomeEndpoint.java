package com.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "groupHomeApi",
        version = "v1",
        resource = "groupHome",
        namespace = @ApiNamespace(
                ownerDomain = "backend.com",
                ownerName = "backend.com",
                packagePath = ""
        )
)
public class GroupHomeEndpoint {

    private static final Logger logger = Logger.getLogger(GroupHomeEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(GroupHome.class);
    }

    /**
     * Returns the {@link GroupHome} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code GroupHome} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "groupHome/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public GroupHome get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting GroupHome with ID: " + id);
        GroupHome groupHome = ofy().load().type(GroupHome.class).id(id).now();
        if (groupHome == null) {
            throw new NotFoundException("Could not find GroupHome with ID: " + id);
        }
        return groupHome;
    }

    /**
     * Inserts a new {@code GroupHome}.
     */
    @ApiMethod(
            name = "insert",
            path = "groupHome",
            httpMethod = ApiMethod.HttpMethod.POST)
    public GroupHome insert(GroupHome groupHome) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that groupHome.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(groupHome).now();
        logger.info("Created GroupHome with ID: " + groupHome.getId());

        return ofy().load().entity(groupHome).now();
    }

    /**
     * Updates an existing {@code GroupHome}.
     *
     * @param id        the ID of the entity to be updated
     * @param groupHome the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code GroupHome}
     */
    @ApiMethod(
            name = "update",
            path = "groupHome/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public GroupHome update(@Named("id") Long id, GroupHome groupHome) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(groupHome).now();
        logger.info("Updated GroupHome: " + groupHome);
        return ofy().load().entity(groupHome).now();
    }

    /**
     * Deletes the specified {@code GroupHome}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code GroupHome}
     */
    @ApiMethod(
            name = "remove",
            path = "groupHome/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(GroupHome.class).id(id).now();
        logger.info("Deleted GroupHome with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "groupHome",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<GroupHome> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<GroupHome> query = ofy().load().type(GroupHome.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<GroupHome> queryIterator = query.iterator();
        List<GroupHome> groupHomeList = new ArrayList<GroupHome>(limit);
        while (queryIterator.hasNext()) {
            groupHomeList.add(queryIterator.next());
        }
        return CollectionResponse.<GroupHome>builder().setItems(groupHomeList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(GroupHome.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find GroupHome with ID: " + id);
        }
    }
    /****************************************************************************************************************/

    @ApiMethod(
            name = "searchUser",
            httpMethod = ApiMethod.HttpMethod.GET)
    public GroupHome searchUser(@Named("email") String email) {
        logger.info("Getting GroupHome by email user: " + email);
        List<GroupHome> groupHome = ofy().load().type(GroupHome.class).list();
        for (int i = 0; i < groupHome.size(); i++){
            for (int j = 0; j < groupHome.get(i).getUsersList().size(); j++){
                if(groupHome.get(i).getUsersList().get(j).getEmail().equals(email)){
                    return groupHome.get(i);
                }
            }
        }
        return null;
    }

    @ApiMethod(
            name = "addUserInGroupHome",
            httpMethod = ApiMethod.HttpMethod.POST)
    public GroupHome addUserInGroupHome(@Named("id") Long id, @Named("password") String password, User user) throws NotFoundException {
        if(!checkExists2(id)){
            return null;
        }else {
            GroupHome groupHome = ofy().load().type(GroupHome.class).id(id).now();
            if (groupHome.getPassword().equals(password)) {
                logger.info("Found GroupHome with ID: " + groupHome.getId() + " and password is correct");
                groupHome.getUsersList().add(user);
                ofy().save().entity(groupHome).now();
                return ofy().load().entity(groupHome).now();
            } else {
                return null;
            }
        }
    }
    private boolean checkExists2(Long id) throws NotFoundException {
        try {
            ofy().load().type(GroupHome.class).id(id).safe();
            return true;
        } catch (com.googlecode.objectify.NotFoundException e) {
            return false;
        }
    }


    @ApiMethod(
            name = "addProduct",
            path = "gh/{id}",
            httpMethod = ApiMethod.HttpMethod.POST)
    public GroupHome addProduct(@Named("id") Long id, Product product) throws NotFoundException {
        checkExists(id);
        GroupHome groupHome = ofy().load().type(GroupHome.class).id(id).now();
        groupHome.getProductsList().add(product);
        ofy().save().entity(groupHome).now();

        return ofy().load().entity(groupHome).now();
    }

    @ApiMethod(
            name = "addProductHome",
            path = "ghPH/{id}",
            httpMethod = ApiMethod.HttpMethod.POST)
    public GroupHome addProductHome(@Named("id") Long id, ProductHome product) throws NotFoundException {
        checkExists(id);
        GroupHome groupHome = ofy().load().type(GroupHome.class).id(id).now();
        groupHome.getHomeList().add(product);
        ofy().save().entity(groupHome).now();

        return ofy().load().entity(groupHome).now();
    }
    

    @ApiMethod(
            name = "listExpiredProduct",
            httpMethod = ApiMethod.HttpMethod.POST)
    public List<ProductHome> listExpiredProductbyGH(@Named("id") Long id) throws NotFoundException {
        if(!checkExists2(id)){
            return null;
        }else {
            List<ProductHome> result = new ArrayList<ProductHome>();
            GroupHome groupHome = ofy().load().type(GroupHome.class).id(id).now();
            for (int i = 0; i < groupHome.getHomeList().size(); i++){
                /*if(!groupHome.getHomeList().get(i).getInfoUnits().isEmpty()){
                    for (int j = 0; j < groupHome.getHomeList().get(i).getInfoUnits().size(); j++) {
                        if (isProductExpired(groupHome.getHomeList().get(i).getInfoUnits().get(j).getDateExpired())){
                       // El producto: No consumido y Si expirado --> add result
                            result.add(groupHome.getHomeList().get(i));
                        }
                    }
                }*/
            }
            return result;
        }
    }

    private boolean isProductExpired(String expiredDate) {

        String resultado="";
        try {
            /**Obtenemos las fechas enviadas en el formato a comparar*/
            Date currentDate = new Date();
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            String systemDate=formateador.format(currentDate);
            Date fechaDate1 = formateador.parse(systemDate);
            Date fechaDate2 = formateador.parse(expiredDate);

            System.out.println("Parametro Date Fecha 1 = "+fechaDate1+"\n" +
                    "Parametro Date fechaActual = "+fechaDate2+"\n");

            if ( fechaDate1.before(fechaDate2) ){
                resultado= "La Fecha 1 es menor ";
                return false;
            }else{
                if ( fechaDate2.before(fechaDate1) ){
                    resultado= "La Fecha 1 es Mayor ";
                    return true;
                }else{
                    resultado= "Las Fechas Son iguales ";
                    return true;
                }
            }
        } catch (ParseException e) {
            System.out.println("Se Produjo un Error!!!  "+e.getMessage());
        }
        return false;
    }


    @ApiMethod(
            name = "searchProductHome",
            path = "groupHome/{id}",
            httpMethod = ApiMethod.HttpMethod.POST)
    public ProductHome searchProductHome(@Named("id") Long id, @Named("nameProduct") String nameProduct) throws NotFoundException{
        if(!checkExists2(id)){
            return null;
        }else {
            GroupHome groupHome = ofy().load().type(GroupHome.class).id(id).now();
            try{
                for (int i = 0; i < groupHome.getHomeList().size(); i++){
                    if(groupHome.getHomeList().get(i).getNameProduct().equals(nameProduct)){
                        return groupHome.getHomeList().get(i);
                    }
                }
            }catch (NullPointerException e){
                return null;
            }
            return null;
        }
    }


    @ApiMethod(
            name = "searchProduct",
            path = "ghProducts",
            httpMethod = ApiMethod.HttpMethod.POST)
    public ProductHome searchProduct(@Named("id") Long id, @Named("nameProduct") String nameProduct) throws NotFoundException {
        if(!checkExists2(id)){
            return null;
        }else {
            GroupHome groupHome = ofy().load().type(GroupHome.class).id(id).now();
            for (int i = 0; i < groupHome.getHomeList().size(); i++){
                if(groupHome.getHomeList().get(i).getNameProduct().equals(nameProduct)){
                    return groupHome.getHomeList().get(i);
                }
            }
            return null;
        }
    }
}