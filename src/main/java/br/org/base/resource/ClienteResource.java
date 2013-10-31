package br.org.base.resource;

import br.org.base.model.Cliente;
import br.org.base.server.Server;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.WriteResult;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: guilherme
 * Date: 10/31/13
 * Time: 2:13 AM
 * To change this template use File | Settings | File Templates.
 */

@Singleton
@Path("clientes")
public class ClienteResource implements Crud<Cliente>{


    static final JacksonDBCollection<Cliente, String> jacksonDB = JacksonDBCollection.wrap(Server.mongoDB.getCollection(Cliente.class.getSimpleName().toLowerCase()), Cliente.class, String.class);

    public ClienteResource() {

    }

    private static final Logger LOGGER = Logger.getLogger(ClienteResource.class.getName());


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Cliente create(Cliente cliente) {


        WriteResult<Cliente, String> res = jacksonDB.insert(cliente);
        return (Cliente) res.getSavedObject();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Cliente read(@PathParam("id") String id){




        return new Cliente("1", "asdasd");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cliente> readAll() {
        List<Cliente> lst = new ArrayList<Cliente>();

        for(int i=0;i<10;i++){

            lst.add(new Cliente(""+i,"asdasd"));
        }


        return lst;
    }


    public void update() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void delete() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}