package billing_services;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;



import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PDF_maker {
	
	static String invoiceId=UUID.randomUUID().toString().substring(0, 10);
	static String currentId=invoiceId;
	static String clientPhoneNumber= randomPhoneNumber();
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");  
	static LocalDateTime now = LocalDateTime.now(); 
	static String path=MessageFormat.format("C:\\Users\\Tylor Gonzalez\\Desktop\\pdfs\\{0}.pdf",currentId);
	static ArrayList<String> cartToPdf;
	
	public PDF_maker(ArrayList<String> cart, String recipient){
		cartToPdf=cart;
		try {
			PDF_maker.main(null);
			PDF_maker.sendEmail(recipient);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}


	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static String randomPhoneNumber() {
		String phoneNumber="";
		String digit="";
		for(int iteration=0; iteration<9;iteration++) {
		int randomDigit=(int)((Math.random()*100)%10)+1;
		digit=String.valueOf(randomDigit);
		}
		phoneNumber+=digit+digit+digit+"-"+digit+digit+digit+"-"+digit+digit+digit+digit;		
		return phoneNumber;
	}
	
	
	public static Table TableGenerator(ArrayList<String>cart) {
		
		//Table styling
		float itemInfoColWidth[]= {140,140,140,140,140};
		Table itemInfo=new Table(UnitValue.createPercentArray(itemInfoColWidth));
		itemInfo.setWidth(UnitValue.createPercentValue(100));
		
		//Overall total variable
		double overallTotal=0.0;
		
		//Iterating over cart ArrayList
		for (int index=0; index<cart.size(); index++) {
			
			String current_element=cart.get(index);
			String[] elems=current_element.split(" ");
			List<String>data=Arrays.asList(elems); 
			ArrayList<String>listS=new ArrayList<String>(data);

			//Parsing string to double to make calculations
			double total= Double.parseDouble(listS.get(2))*Double.parseDouble(listS.get(3));
		
			//Retrieving items from respective index in listS
			itemInfo.addCell(new Cell().add(new Paragraph(listS.get(0))));
			itemInfo.addCell(new Cell().add(new Paragraph(listS.get(1))));
			itemInfo.addCell(new Cell().add(new Paragraph(listS.get(2))));
			itemInfo.addCell(new Cell().add(new Paragraph(listS.get(3))));
			itemInfo.addCell(new Cell().add(new Paragraph(String.valueOf(total))));
			overallTotal+=total;//Adding totals to overallTotal
			
			}
		
		//Creating buffer cells for styling
		itemInfo.addCell(new Cell().add(new Paragraph()));
		itemInfo.addCell(new Cell().add(new Paragraph()));
		itemInfo.addCell(new Cell().add(new Paragraph()));
		itemInfo.addCell(new Cell().add(new Paragraph("Total")));
		itemInfo.addCell(new Cell().add(new Paragraph(String.valueOf(round(overallTotal,3)))));
		
		//Returning object of type table to add into documents main body
		return itemInfo;
			
		}
	
	public static void sendEmail( String recipient) {
	
		String message=MessageFormat.format("Dear {0}, '\n' You will find invoice {1} attached to this email.\nThank you for your preference.\n Best regards,\n ASAN INCORPORATED", recipient, invoiceId);
		String subject=MessageFormat.format("Invoice {0}- ASAN INCORPORATED", invoiceId);
		Email email=new Email("asan.incorporated@gmail.com", "NasaWho?",path);
		email.sendEmail(recipient, subject ,message);
	}
	


	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		PdfWriter pdfWriter= new PdfWriter(path);
		PdfDocument pdfDoc = new PdfDocument(pdfWriter);
		Document document=new Document(pdfDoc);
		pdfDoc.setDefaultPageSize(PageSize.Default);
		float col=280f;
		float columnWidth[]={col,col};
		Table table = new Table(columnWidth);
		
		table.setBackgroundColor(new DeviceRgb(63,169,219))
		.setFontColor(ColorConstants.WHITE);
		
		table.addCell(new Cell().add(new Paragraph( "INVOICE")
					.setBorder(Border.NO_BORDER)
					.setTextAlignment(TextAlignment.CENTER)
					.setVerticalAlignment(VerticalAlignment.MIDDLE)
					.setMarginTop(30f)
					.setMarginBottom(30f)
					.setFontSize(30f)
				).setBorder(Border.NO_BORDER));
		
		table.addCell(new Cell().add(new Paragraph( "ASAN\n300 E Street SW\r\n"
				+ "Washington DC\n20024-3210")
				.setTextAlignment(TextAlignment.RIGHT)
				.setMarginTop(30f)
				.setMarginBottom(30f)
				.setBorder(Border.NO_BORDER)
				.setMarginRight(10f))
				.setTextAlignment(TextAlignment.RIGHT)
				.setMarginTop(30f)
				.setMarginBottom(30f)
				.setBorder(Border.NO_BORDER)
				.setMarginRight(10f)
				);
		
		float colWidth[]= {80,300,100,801};
		Table customerInfo= new Table(colWidth);
		customerInfo.addCell(new Cell(0,4).add(new Paragraph( "Customer information"))
				
			.setBold()
			.setBorder(Border.NO_BORDER));
				
		customerInfo.addCell(new Cell().add(new Paragraph( "Name:")).setBorder(Border.NO_BORDER));
		customerInfo.addCell(new Cell().add(new Paragraph( "Sample person")).setBorder(Border.NO_BORDER));
		customerInfo.addCell(new Cell().add(new Paragraph( "Id:")).setBorder(Border.NO_BORDER));
		customerInfo.addCell(new Cell().add(new Paragraph(invoiceId)).setBorder(Border.NO_BORDER));
		customerInfo.addCell(new Cell().add(new Paragraph( "Phone number:")).setBorder(Border.NO_BORDER));
		customerInfo.addCell(new Cell().add(new Paragraph(clientPhoneNumber)).setBorder(Border.NO_BORDER));
		customerInfo.addCell(new Cell().add(new Paragraph( "Date:")).setBorder(Border.NO_BORDER));
		customerInfo.addCell(new Cell().add(new Paragraph(dtf.format(now))).setBorder(Border.NO_BORDER));
		
		
		float itemInfoColWidth[]= {140,140,140,140,140};
		Table itemInfo=new Table(UnitValue.createPercentArray(itemInfoColWidth));
		itemInfo.setWidth(UnitValue.createPercentValue(100));

		itemInfo.addCell(new Cell().add(new Paragraph("ID"))
				.setBackgroundColor(new DeviceRgb(63,169,219))
				.setFontColor(ColorConstants.WHITE));
		itemInfo.addCell(new Cell().add(new Paragraph("Item"))
				.setBackgroundColor(new DeviceRgb(63,169,219))
				.setFontColor(ColorConstants.WHITE));
		itemInfo.addCell(new Cell().add(new Paragraph("Per-unit cost"))
				.setBackgroundColor(new DeviceRgb(63,169,219))
				.setFontColor(ColorConstants.WHITE));
		itemInfo.addCell(new Cell().add(new Paragraph("Quantity"))
				.setBackgroundColor(new DeviceRgb(63,169,219))
				.setFontColor(ColorConstants.WHITE));
		itemInfo.addCell(new Cell().add(new Paragraph("Total"))
				.setBackgroundColor(new DeviceRgb(63,169,219))
				.setFontColor(ColorConstants.WHITE));
		
		

		Connection con;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			con = DriverManager.getConnection("jdbc:mysql://34.70.87.189:3306/materials", "root", "access");
			document.add(table);
			document.add(new Paragraph("\n"));
			document.add(customerInfo);
			document.add(new Paragraph("\n"));
			
			
			

			
			document.add(itemInfo);
			document.add(TableGenerator(cartToPdf));		
			document.close();
			System.out.println("Pdf created");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Connecting
		
		
//		String client= "gonzatylor@gmail.com";
//		String message=MessageFormat.format("Dear {0}, '\n' You will find invoice {1} attached to this email.\nThank you for your preference.\n Best regards,\n ASAN INCORPORATED",client, invoiceId);
//		String subject=MessageFormat.format("Invoice {0}- ASAN INCORPORATED", invoiceId);
//		Email email=new Email("asan.incorporated@gmail.com", "NasaWho?",path);
//		email.sendEmail(client, subject ,message);
		
		
		
	}
	
	
	

}
