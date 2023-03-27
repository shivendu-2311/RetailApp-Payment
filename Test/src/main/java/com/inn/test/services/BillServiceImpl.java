package com.inn.test.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.inn.test.JWT.JwtFilter;
import com.inn.test.POJO.Bill;
import com.inn.test.constents.CafeConstents;
import com.inn.test.dao.BillDao;
import com.inn.test.utils.CafeUtills;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


@Service
public class BillServiceImpl implements BillService{

	@Autowired
	JwtFilter jwtFilter;
	@Autowired
	BillDao billDao;
	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
		try {
			String fileName;
			if(validateRequestMap(requestMap))
			{
			if(requestMap.containsKey("isGenerate") && !(Boolean)requestMap.containsKey("isGenerate"))	
			{
				fileName = (String)requestMap.get("isGenerate");
			}
			else
			{
				fileName = CafeUtills.getUUID();
				requestMap.put("uuid", fileName);
				insertBill(requestMap);
			}
			//designing the pdf
			String data = "Name: "+ requestMap.get("name") + "\n"+ "ContactNumber: "+ requestMap.get("contactNumber") + "\n"+
			"Email: " + requestMap.get("email") + "\n"+ "Payment Method: " + requestMap.get("paymentMethod")+"\n";
			
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(CafeConstents.STORE_LOC +"\\" + fileName +".pdf"));
			
			document.open();
			
			setRectangleInPdf(document);
			Paragraph chunk = new Paragraph("CAfe Management SYstem",getFont("Header") );
			chunk.setAlignment(Element.ALIGN_CENTER);
			document.add(chunk);
			
			Paragraph paragraph = new Paragraph(data + "\n" +"\n", getFont("Data"));
			document.add(paragraph);
			
			PdfPTable table = new PdfPTable(5);
			table.setWidthPercentage(100);
			addTableHeader(table);
			
			//whenever we extract a json from json profuct array it will be json so we have to ectract that to map<STring,obj>)
			JSONArray jsonArray = CafeUtills.getJsonArrayFromStrng((String) requestMap.get("productDetails"));
			
			//fetching data and filling them into the table
			for(int i =0; i< jsonArray.length(); i++)
			{
			addRows(table,CafeUtills.getMapFromJson(jsonArray.getString(i)));
			}
			document.add(table);
			
			Paragraph footer= new Paragraph("TotalAmount: " + requestMap.get("totalAmount")+ "\n" +"Thankyou for visiting, Please Visit"
					+ "Again");	
			document.add(footer);
			document.close();
			return new ResponseEntity<>("{\"uuid\":\""+fileName+"\"}", HttpStatus.OK);
}
			else
			{
				return CafeUtills.getResponseEntity(CafeConstents.INVALID_REQUEST, HttpStatus.OK);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}








	private boolean validateRequestMap(Map<String, Object> requestMap) {
		return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")&&requestMap.containsKey("email")&&
				requestMap.containsKey("paymentMethod")&&requestMap.containsKey("productDetails")&&requestMap.containsKey("totalAmount");
				
	}
    

	private void insertBill(Map<String, Object> requestMap) {
		try {
			Bill bill = new Bill();
			bill.setUuid((String) requestMap.get("uuid"));
			bill.setName((String) requestMap.get("name"));
			bill.setEmail((String) requestMap.get("email"));
			bill.setContactNumber((String) requestMap.get("contactNumber"));
			bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
			bill.setTotal(Integer.parseInt((String)requestMap.get("totalAmount")));
			bill.setProductDetails((String)requestMap.get("productDetails"));
			bill.setCreatedBy(jwtFilter.getCurrentUser());
		billDao.save(bill);
		}
		
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	  private void setRectangleInPdf(Document document) {
			System.out.println("Inside Set rectangular");
			
			Rectangle rect = new Rectangle(575,825,15,17);
			rect.enableBorderSide(1);
			rect.enableBorderSide(2);
			rect.enableBorderSide(4);
			rect.enableBorderSide(8);
			
			rect.setBackgroundColor(BaseColor.BLACK);
			rect.setBorderWidth(1);
			try {
				document.add(rect);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	  private Font getFont(String type) {
			System.out.println("In font");
			
			switch(type) {
			case "Header": {
				Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
				headerFont.setStyle(Font.BOLD);
				return headerFont;
			}
			case "Data":{
				Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,11, BaseColor.BLACK);
				dataFont.setStyle(Font.BOLD);
				return dataFont;
				
			}
			default:{
				return new Font();
			}
			}
		}
	  

		private void addTableHeader(PdfPTable table) {
			System.out.println("Inside addTableHeader");
			Stream.of("Name", "Category","Quantity","Price","SubTotal" ).
			forEach(columnTitle->{
				
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(columnTitle));
			header.setBackgroundColor(BaseColor.YELLOW);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setVerticalAlignment(Element.ALIGN_CENTER);
			table.addCell(header);
			});
			
		}
		

		private void addRows(PdfPTable table, Map<String, Object>data) {
			
			System.out.println("Inside Addrows");
			
			table.addCell((String)data.get("name"));
			table.addCell((String) data.get("category"));
			table.addCell((String) data.get("quantity"));
			table.addCell(Double.toString((Double) data.get("price")));
			table.addCell(Double.toString((Double) data.get("total")));
			
		}








		@Override
		public ResponseEntity<List<Bill>> getBills() {
			
		List<Bill>list = new ArrayList<>();
		
		if(jwtFilter.isAdmin())
		{
			list = billDao.getAllBills();
		}
		else
		{
			list = billDao.getBillByUsername(jwtFilter.getCurrentUser());
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
		}








		@Override
		public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
			System.out.println("InsideGetPdf");
			
			try {
				byte[] byteArray = new byte[0];
				if(!requestMap.containsKey("uuid")&& validateRequestMap(requestMap))
				{
					return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
				}
				String filePath = CafeConstents.STORE_LOC + "\\" + (String) requestMap.get("uuid")+ ".pdf";
				
				if(CafeUtills.isFileExist(filePath))
				{
					byteArray = getByteArray(filePath);
					return new ResponseEntity(byteArray, HttpStatus.OK);
				}
				else
				{
					requestMap.put("isGenerate", false);
					generateReport(requestMap);
					byteArray = getByteArray(filePath);
					return new ResponseEntity(byteArray, HttpStatus.OK);
					
				}
				
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			return null;
			
		}








		private byte[] getByteArray(String filePath) throws IOException {
			File intialFile = new File(filePath);
			InputStream targetStream = new FileInputStream(intialFile);
			byte[] byteArray = IOUtils.toByteArray(targetStream);
			targetStream.close();
			return byteArray;
		}








		@Override
		public ResponseEntity<String> deleteBill(Integer id) {
			try {
				Optional optional = billDao.findById(id);
				
				if(!optional.isEmpty())
				{
					billDao.deleteById(id);
					return CafeUtills.getResponseEntity("Deleted sucessFully", HttpStatus.OK);
				}
				else
				{

					return CafeUtills.getResponseEntity("Bill id Doesn't exist", HttpStatus.OK);
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		}

}
