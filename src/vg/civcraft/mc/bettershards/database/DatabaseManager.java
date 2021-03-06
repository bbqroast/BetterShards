package vg.civcraft.mc.bettershards.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.PreparedStatement;

import vg.civcraft.mc.bettershards.BetterShardsPlugin;
import vg.civcraft.mc.bettershards.portal.Portal;
import vg.civcraft.mc.namelayer.NameAPI;
import vg.civcraft.mc.namelayer.config.NameConfigListener;
import vg.civcraft.mc.namelayer.config.NameConfigManager;
import vg.civcraft.mc.namelayer.config.annotations.NameConfig;
import vg.civcraft.mc.namelayer.config.annotations.NameConfigType;
import vg.civcraft.mc.namelayer.config.annotations.NameConfigs;

public class DatabaseManager implements NameConfigListener{

	private BetterShardsPlugin plugin = BetterShardsPlugin.getInstance();
	private NameConfigManager config;
	private Database db;
	
	private String[] databases = {
		"tables/createPlayerDataTable.sql",
		"tables/createPortalDataTable.sql",
		"tables/createPortalLocData.sql"
	};
	
	private String addPlayerData, getPlayerData, removePlayerData;
	private String addPortalData, getPortalData, removePortalData, getAllPortalData;
	
	public DatabaseManager(){
		config = NameAPI.getNameConfigManager();
		if (!isValidConnection())
			return;
		executeDatabaseStatements();
		loadPreparedStatements();
	}
	
	@NameConfigs({
		@NameConfig(name = "mysql.host", def = "localhost", type = NameConfigType.String),
		@NameConfig(name = "mysql.port", def = "3306", type = NameConfigType.Int),
		@NameConfig(name = "mysql.username", type = NameConfigType.String),
		@NameConfig(name = "mysql.password", type = NameConfigType.String),
		@NameConfig(name = "mysql.dbname", def = "BetterShardsDB", type = NameConfigType.String)
	})
	private boolean isValidConnection(){
		String username = config.get(plugin, "mysql.host").getString();
		String host = config.get(plugin, "mysql.host").getString();
		int port = config.get(plugin, "mysql.port").getInt();
		String password = config.get(plugin, "mysql.password").getString();
		String dbname = config.get(plugin, "mysql.dbname").getString();
		db = new Database(host, port, dbname, username, password, plugin.getLogger());
		return db.connect();
	}
	
	private void executeDatabaseStatements(){
		for (String mysql: databases){
			String querry = getQuerry(mysql);
			db.execute(querry);
		}
	}
	
	private String getQuerry(String path){
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("rescources/");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = "";
		StringBuilder builder = new StringBuilder();
		try {
			while ((line = reader.readLine()) != null)
				builder.append(line + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	private void loadPreparedStatements(){
		
	}
	
	/**
	 * Adds a portal instance to the database.  Should be called only when
	 * initially creating a Portal Object.
	 */
	public void addPortal(Portal portal){
		PreparedStatement addPortalData = db.prepareStatement(this.addPortalData);
	}
}
