package domain.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import domain.model.Country;

public class CountryDbSql implements CountryDb {
	private Properties properties;
	private String url;
	
	public CountryDbSql(Properties properties) {
		this.properties = properties;
		try {
			Class.forName("org.postgresql.Driver");
			this.properties = properties;
			this.url = properties.getProperty("url");
		} catch (ClassNotFoundException e) {
			throw new DbException(e.getMessage(), e);
		}

	}
	
	public void add(Country country){
		if(country == null){
			throw new DbException("Nothing to add.");
		}
		try (
			Connection connection = DriverManager.getConnection(url, properties);
			Statement statement = connection.createStatement();
		) {
			String sql = "INSERT INTO country (name, capital, inhabitants, votes) VALUES ('"
				+ country.getName() + "', '" + country.getCapital() + "', "
				+ country.getNumberInhabitants() + ", "+ country.getVotes() + ")";
			statement.execute(sql);
		} 
		catch (SQLException e) {
			throw new DbException(e);
		}
	}
	
	public List<Country> getAll(){
		List<Country> countries = new ArrayList<Country>();
		try (
			Connection connection = DriverManager.getConnection(url, properties);
			Statement statement = connection.createStatement();
		) {
			ResultSet result = statement.executeQuery( "SELECT * FROM country" );
			while(result.next()){
				String name = result.getString("name");
				String capital = result.getString("capital");
				int numberOfVotes = result.getInt("votes");
				int numberOfInhabitants = result.getInt("inhabitants");
				Country country= new Country(name, numberOfInhabitants,capital, numberOfVotes);
				countries.add(country);
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage(), e);
		}
		return countries;
	}
}
