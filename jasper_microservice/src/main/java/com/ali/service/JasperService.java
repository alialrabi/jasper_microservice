package com.ali.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class JasperService {
	
	
	public Connection getConnection(){
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? "+ "Include in your library path!");
			e.printStackTrace();
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/jasper_microservice", "root",
					"root");
		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
		return connection;
	}

	
	public void generatePDFOutput( HttpServletResponse resp, Map parameters, JasperReport jasperReport,Connection conn) throws JRException, NamingException, SQLException, IOException {
		File destFile = new File("/home/ali/reports/pdfReport.pdf");
		byte[] bytes = null;
		bytes = JasperRunManager.runReportToPdf(jasperReport,parameters,conn);
		resp.reset();
		resp.resetBuffer();
		resp.setContentType("application/pdf");
		resp.setContentLength(bytes.length);
		FileOutputStream ouputStream = new  FileOutputStream(destFile);
		ouputStream.write(bytes, 0, bytes.length);
		ouputStream.flush();
		ouputStream.close();
		}
	 
	 public  JasperReport getCompiledFile( HttpServletRequest request) throws JRException, FileNotFoundException {
		
		 ClassLoader classLoader = getClass().getClassLoader();
         InputStream inputStream = new FileInputStream (classLoader.getResource("jasper/jasperview.jrxml").getPath());
         JasperDesign jasperDesign;
         jasperDesign = JRXmlLoader.load(inputStream);
	     JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			return jasperReport;
		}
	
}
