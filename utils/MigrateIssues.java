/*
 * Updates Demographic Note Issues from the default icd10 which was hardcoded in oscar to the actual
 * issue type.  
 * 
 * issues.txt is a key value listing of all issues in icd9, cpp, custom and snmomed.
 * If you are using icd10, please specify that as the first paramter to the program as ICD10 else ICD9
 */


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MigrateIssues {
    
    private static String FILENAME = "issues.txt";  //List of custom and system issues loaded into issue table
    private static Map<String,String> typeMap = new HashMap<String,String>(); //map from oscar issue types to integrator code types
    static {
	typeMap.put("userDefined", "CUSTOM_ISSUE");
	typeMap.put("system", "SYSTEM");
	typeMap.put("SnomedCore", "SNOMED");
	typeMap.put("icd9", "ICD9");
	typeMap.put("icd10", "ICD10");
    }
    
    public static void main(String[] args) throws SQLException {
	if( args.length != 4 ) {
	    System.err.println("Usage java -jar MigrateIssues defaultIssueType(ICD9 or ICD10) dbuser dbname dbpassword");
	    return;
	}
	
	ResultSet result = null;
	PreparedStatement preparedStmt = null;
	Connection con = null;
	
	try {
	    InputStream in = new FileInputStream(FILENAME);
	
	    Properties prop = new Properties();
	    prop.load(in);
	    	 
	    String defaultIssueType = args[0];
	    String user = args[1];
	    String uri = "jdbc:mysql:///" + args[2];
	    String passwd = args[3];
	    
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(uri, user, passwd);	    
	    String sql = "select caisiDemographicId, codeType, integratorFacilityId, issueCode from CachedDemographicIssue";
	    String update = "update CachedDemographicIssue set codeType = ? where caisiDemographicId = ? and codeType = ? and integratorFacilityId = ? and issueCode = ?";
	    
	    Statement stmt = con.createStatement();
	    
	    result = stmt.executeQuery(sql);
	    
	    preparedStmt = con.prepareStatement(update);
	    
	    String codeType, code, properType;
	    Integer demoId, facility, rowsUpdated;
	    
	    while(result.next()) {
		code = result.getString("issueCode");
		codeType = result.getString("codeType");
		
		properType = prop.getProperty(code,defaultIssueType);
		
		if( !properType.equalsIgnoreCase(codeType)) {
		    preparedStmt.clearParameters();
		    
		    demoId = result.getInt("caisiDemographicId");
		    facility = result.getInt("integratorFacilityId");
		    
		    preparedStmt.setString(1, typeMap.get(properType));
		    preparedStmt.setInt(2, demoId);
		    preparedStmt.setString(3, codeType);
		    preparedStmt.setInt(4, facility);
		    preparedStmt.setString(5, code);
		    
		    rowsUpdated = preparedStmt.executeUpdate();
		    
		    if( rowsUpdated != 1 ) {
			System.out.println("SQL UPDATE FAILED FOR DEMO:" + demoId + " codeType:" + codeType + " Facility:" + demoId + " Code:" + code);
		    }
		    
		    
		}
		
		
	    }
	    
	    result.close();
	    sql = "select uuid, noteIssue from CachedDemographicNoteIssues";
	    update = "update CachedDemographicNoteIssues set noteIssue = ? where uuid = ?";
	    preparedStmt = con.prepareStatement(update);
	    String[] tmp;
	    String uuid;
	    
	    result = stmt.executeQuery(sql);
	    while(result.next()) {
		tmp = result.getString("noteIssue").split("\\.");
		codeType = tmp[0];
		code = tmp[1];
		
		properType = prop.getProperty(code,defaultIssueType);
		properType = typeMap.get(properType);
		
		if( !properType.equalsIgnoreCase(codeType)) {
		    preparedStmt.clearParameters();
		    uuid = result.getString("uuid");
		    
		    preparedStmt.setString(1, properType + "." + code);
		    preparedStmt.setString(2, uuid);
		    
		    rowsUpdated = preparedStmt.executeUpdate();
		    
		    if( rowsUpdated != 1 ) {
			System.out.println("SQL UPDATE FAILED FOR UUID: " + uuid);
		    }
		    
		}
		
	    }	    	    
	    
	}
	catch( FileNotFoundException e ) {
	    System.err.println("Cannot find issues.txt.  Place in same directory as run directory.");	    
	}
	catch( IOException e ) {
	    System.err.println("Cannot load file issues.txt.  Is it corrupt?");
	}
	catch( ClassNotFoundException e ) {
	    System.err.println("Cannot find mysql jar file.");
	}
	finally {
	    if( result != null ) result.close();
	    if( preparedStmt != null ) preparedStmt.close();
	    if( con != null ) con.close();
	}
	
    }
}