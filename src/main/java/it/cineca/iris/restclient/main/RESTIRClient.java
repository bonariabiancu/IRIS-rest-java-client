/*
 *  Java Iris Rest Client, pratical example for use IRIS REST API
 * 
 *  Copyright (c) 2015, CINECA and third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by CINECA.
 * 
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU
 *  Lesser General Public License v3 or any later version, as published 
 *  by the Free Software Foundation, Inc. <http://fsf.org/>.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this distribution; if not, write to:
 *  Free Software Foundation, Inc.
 *  51 Franklin Street, Fifth Floor
 *  Boston, MA  02110-1301  USA
 */
package it.cineca.iris.restclient.main;

import it.cineca.iris.ir.rest.command.model.OptionBitStreamDTO;
import it.cineca.iris.ir.rest.search.model.AnceSearchRestDTO;
import it.cineca.iris.ir.rest.search.model.SearchIdsRestDTO;
import it.cineca.iris.ir.rest.search.model.SearchRestDTO;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

/**
 * 
 * @author pmeriggi
 *
 */
public class RESTIRClient {

    private WebTarget webTarget;
    private final Client client;
    private final String baseURI;
    private final String pathIR;
    private final String pathRM;
    
    //2 min
    public static final Integer CONNECT_TIMEOUT = 120000;
    public static final Integer READ_TIMEOUT = 120000;

    public RESTIRClient(String baseURI, String pathIR, String pathRM, String username, String password) {
        this.client = ClientBuilder.newClient().register(new Authenticator(username, password)).register(MultiPartFeature.class);
        
        //Connect timeout interval, in milliseconds.
        this.client.property(ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        //Read timeout interval, in milliseconds.
        this.client.property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT);
        
        this.baseURI = baseURI;
        this.pathIR = pathIR;
        this.pathRM = pathRM;
    }

    /**
     * Echo IR test
     * 
     * @return
     */
    public Response echoIR() {
        this.webTarget = this.client.target(baseURI+pathIR).path("echo");

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Echo RM test
     * 
     * @return
     */
    public Response echoRM() {
        this.webTarget = this.client.target(baseURI+pathRM).path("echo");

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get all communities
     * 
     * @return
     */
    public Response communities() {
        this.webTarget = this.client.target(baseURI+pathIR).path("communities");

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }

    /**
     * Get community give id
     * 
     * @param communityId
     * @return
     */
    public Response community(String communityId) {
        this.webTarget = this.client.target(baseURI+pathIR).path("communities/" + communityId);

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get all collection
     * 
     * @return
     */
    public Response collections() {
        this.webTarget = this.client.target(baseURI+pathIR).path("collections");

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get collection by id
     * 
     * @param collectionId
     * @return
     */
    public Response collection(String collectionId) {
        this.webTarget = this.client.target(baseURI+pathIR).path("collections/" + collectionId);

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
  
    /**
     * Get item by Id
     * 
     * @param itemId
     * @return
     */
    public Response item(String itemId) {
        this.webTarget = this.client.target(baseURI+pathIR).path("items/" + itemId);

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }

    /**
     * Get all item data by Id
     * 
     * @param itemId
     * @return
     */
    public Response itemAll(String itemId) {
        this.webTarget = this.client.target(baseURI+pathIR).path("items/" + itemId).queryParam("expand", "all");

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get all items data by Id
     * 
     * @param limit
     * @param offset
     * @return
     */
    public Response itemsAll(Integer limit, Integer offset) {
        this.webTarget = this.client.target(baseURI+pathIR).path("items/").queryParam("expand", "all").queryParam("limit", limit).queryParam("offset", offset);

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get all items by Id
     * 
     * @param limit
     * @param offset
     * @return
     */
    public Response items(Integer limit, Integer offset) {
        this.webTarget = this.client.target(baseURI+pathIR).path("items/").queryParam("limit", limit).queryParam("offset", offset);

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }

    /**
     * Get item metadata by item id
     * 
     * @param itemId
     * @return
     */
    public Response itemWithMetadata(String itemId) {
        this.webTarget = this.client.target(baseURI+pathIR).path("items/" + itemId).queryParam("expand", "metadata");

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }

    /**
     * Get input form by id
     * 
     * @param inputFormId
     * @return
     */
    public Response inputFormAll(String inputFormId) {
        this.webTarget = this.client.target(baseURI+pathIR).path("inputforms/" + inputFormId).queryParam("expand", "all");

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }

    /**
     * Search item by dto
     * 
     * @param searchDTO
     * @return
     * @throws IOException
     */
    public Response items(SearchRestDTO searchDTO) throws IOException {
        this.webTarget = this.client.target(baseURI+pathIR).path("items/search");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonSearchDTO = ow.writeValueAsString(searchDTO);

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").post(Entity.entity(jsonSearchDTO, MediaType.APPLICATION_JSON));
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get list item id by dto
     * 
     * @param searchDTO
     * @return
     * @throws IOException
     */
    public Response itemIds(SearchIdsRestDTO searchDTO) throws IOException {
        this.webTarget = this.client.target(baseURI+pathIR).path("items/ids/search");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonSearchDTO = ow.writeValueAsString(searchDTO);

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").post(Entity.entity(jsonSearchDTO, MediaType.APPLICATION_JSON));
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }

    /**
     * Get ance journal by dto
     * 
     * @param searchDTO
     * @return
     * @throws IOException
     */
    public Response journals(AnceSearchRestDTO searchDTO) throws IOException {
        this.webTarget = this.client.target(baseURI+pathIR).path("ance/search");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonSearchDTO = ow.writeValueAsString(searchDTO);

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").post(Entity.entity(jsonSearchDTO, MediaType.APPLICATION_JSON));
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }

    /**
     * Get ance journal by id
     * 
     * @param anceId
     * @return
     * @throws IOException
     */
    public Response journal(String anceId) throws IOException {
        this.webTarget = this.client.target(baseURI+pathIR).path("ance/" + anceId);

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    
    /**
     * Get person by id
     * 
     * @param personId
     * @return
     * @throws IOException
     */
    public Response personById(String personId) throws IOException {
        this.webTarget = this.client.target(baseURI+pathRM).path("personsbyid/" + personId);

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get person by cris id
     * 
     * @param crisId
     * @return
     * @throws IOException
     */
    public Response personByCris(String crisId) throws IOException {
        this.webTarget = this.client.target(baseURI+pathRM).path("personsbyrpid/" + crisId);

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get person position by person id
     * 
     * @param personId
     * @return
     * @throws IOException
     */
    public Response positionsById(String personId) throws IOException {
        this.webTarget = this.client.target(baseURI+pathRM).path("personsbyid/" + personId + "/positions");

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get person position by cris id
     * 
     * @param crisId
     * @return
     * @throws IOException
     */
    public Response positionsByCris(String crisId) throws IOException {
        this.webTarget = this.client.target(baseURI+pathRM).path("personsbyrpid/" + crisId + "/positions");

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get current person position by person id
     * 
     * @param personId
     * @return
     * @throws IOException
     */
    public Response positioncurrentById(String personId) throws IOException {
        this.webTarget = this.client.target(baseURI+pathRM).path("personsbyid/" + personId + "/positioncurrent");

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get current person position by cris id
     * 
     * @param crisId
     * @return
     * @throws IOException
     */
    public Response positioncurrentByCris(String crisId) throws IOException {
        this.webTarget = this.client.target(baseURI+pathRM).path("personsbyrpid/" + crisId + "/positioncurrent");

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get person by id (person or cris)
     * 
     * @param id
     * @return
     * @throws IOException
     */
    public Response person(String id) throws IOException {
        this.webTarget = this.client.target(baseURI+pathRM).path("persons/" + id);

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get person position by id (person or cris)
     * 
     * @param id
     * @return
     * @throws IOException
     */
    public Response positions(String id) throws IOException {
        this.webTarget = this.client.target(baseURI+pathRM).path("persons/" + id + "/positions");

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Get current person position by id (person or cris)
     * 
     * @param id
     * @return
     * @throws IOException
     */
    public Response positioncurrent(String id) throws IOException {
        this.webTarget = this.client.target(baseURI+pathRM).path("persons/" + id + "/positioncurrent");

        Response response = this.webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN").get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }
    
    /**
     * Upload attachment with licence metadata for item
     * 
     * @param itemId
     * @param optionBitStreamDTO
     * @param fileName
     * @return
     * @throws IOException
     */
    public Response uploadStream(Integer itemId, OptionBitStreamDTO optionBitStreamDTO, String fileName) throws IOException {	    
		WebTarget webTarget = client.target(baseURI+pathIR).path("items/"+itemId+"/streams/upload");
	    
		//Compose multipart request (multipart/form-data)
		//See http://stackoverflow.com/questions/27609569/file-upload-along-with-other-object-in-jersey-restful-web-service/27614403#27614403
		//See https://jersey.java.net/documentation/latest/media.html#multipart
	    
		MultiPart multipartEntity = null;
		try {
			FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
		            new File(fileName),
		            MediaType.APPLICATION_OCTET_STREAM_TYPE);
		    fileDataBodyPart.setContentDisposition(FormDataContentDisposition.name("file").fileName(fileName).build());
		   	
	        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	        String jsonOptionBitStreamDTO = ow.writeValueAsString(optionBitStreamDTO);
		    
		    multipartEntity = new FormDataMultiPart()
		    .field("optionBitStreamDTO", jsonOptionBitStreamDTO, MediaType.APPLICATION_JSON_TYPE)
	        .bodyPart(fileDataBodyPart);
		    	    
		    Response response = webTarget.request(MediaType.APPLICATION_JSON).header("scope", "ROLE_ADMIN")
		            .post(Entity.entity(multipartEntity, MediaType.MULTIPART_FORM_DATA_TYPE));
	        
		    if (response.getStatus() != 200) {
	            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	        }
        	
		    return response;
		} finally {
			if (multipartEntity != null) {
				multipartEntity.close();
			}
		}
	}
    
    public void close() {
        this.client.close();
    }
}