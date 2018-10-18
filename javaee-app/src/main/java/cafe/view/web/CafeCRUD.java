package cafe.view.web;

import cafe.model.ejb.CafeEJBBean;
import cafe.model.entity.Coffee;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

@Named
@SessionScoped
public class CafeCRUD implements Serializable {

	private static final Logger logger = Logger.getLogger( "cafe.web.CafeCRUD" );
	private String baseUri;
	private Client client;

	@NotNull
	@NotEmpty
	protected String name;
	@NotNull
	protected Double price;

	public List<Coffee> getCoffeeList() {
		return coffeeList;
	}

	/**
	 * Creates a new instance of CafeCRUD
	 */
	public CafeCRUD() {
	}

	@PostConstruct
	private void constructor() {
		try {
			InetAddress inetAddress = InetAddress.getByName(((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName());

			baseUri = FacesContext.getCurrentInstance().getExternalContext().getRequestScheme()+"://"+inetAddress.getHostName() + ":" +
					FacesContext.getCurrentInstance().getExternalContext().getRequestServerPort() + "/javaee-cafe-demo/webapi/cafeRS";

			System.out.println("baseUri : " + baseUri);

			this.client = ClientBuilder.newClient();
			this.getAllCoffees();
		}
		catch (IllegalArgumentException | NullPointerException | WebApplicationException | UnknownHostException ex) {
			logger.severe( "processing of HTTP response failed" );
			ex.printStackTrace();
		}
	}

	private void getAllCoffees() {
		this.coffeeList = this.client.target( this.baseUri )
				.path( "/all" )
				.get( new GenericType<List<Coffee>>() {
				} );
	}

	public void addCoffeeAction() {
		Coffee coffee = new Coffee( this.name, this.price );
		this.client.target( baseUri )
				.request( MediaType.APPLICATION_XML )
				.post( Entity.json( coffee ) );
		this.name = null;
		this.price = null;
		this.getAllCoffees();
	}

	public void removeCoffeeAction(Coffee coffee) {
		this.client.target( baseUri )
				.path( coffee.getId().toString() )
				.request( )
				.delete( );
		this.getAllCoffees();
	}

	/**
	 * Get the value of name
	 *
	 * @return the value of name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the value of name
	 *
	 * @param birthday new value of name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the value of price
	 *
	 * @return the value of price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * Set the value of price
	 *
	 * @param price new value of price
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * Get the value of coffeeList
	 *
	 * @return the value of coffeeList
	 */
	protected List<Coffee> coffeeList;
}
