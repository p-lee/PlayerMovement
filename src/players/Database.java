package players;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.Arrays;

public class Database {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "players";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "wampuser";
		String password = "players";
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url + dbName,
					userName, password);
			Statement st = conn.createStatement();

			File dir = new File("files");
			File[] files = dir.listFiles();
			if (files == null) {
				System.out.println("problem");
				return;
			}
			
			for (File f : files) {
				String fileName = f.getName();
				String[] info = fileName.split("#");
				String team = info[0];
				String year = info[1];
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String line = br.readLine();
				while (line != null) {
					ResultSet res = st.executeQuery("SELECT EXISTS(SELECT 1 FROM movement WHERE Name='"+line+"')");
					res.next();
					int found = res.getInt(1);
					if (found == 1) {
						int val = st.executeUpdate("UPDATE movement SET " + year + "='" + team + "' WHERE Name='"+line+"'");
						if (val == 1) System.out.print("Successfully updated value\n");
					}
					else {
						int val = st.executeUpdate("INSERT INTO movement (Name, " + year + ") VALUES('"+line+"', '"+team+"')");
						if (val == 1) System.out.print("Successfully inserted value\n");
					}
					line = br.readLine();
				}
				br.close();
	//			ResultSet res = st
	//					.executeQuery("SELECT * FROM movement ORDER BY ID LIMIT 2,1");
	//			res.next();
	//			//"SELECT * FROM movement WHERE Id = 20"
	//			ResultSetMetaData rsmd = res.getMetaData();
	//			int columnsNumber = rsmd.getColumnCount();
	//			System.out.println(columnsNumber);
	//			String[] player = new String[23];
	//			for (int i = 0; i < 23; i++)
	//				player[i] = res.getString(i + 1);
	//			System.out.println(Arrays.toString(player));
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
