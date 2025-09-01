//package print;
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.*;
//
//
//import java.io.FileOutputStream;
//
//import java.util.List;
//
//import model.BillDetail;
//import model.Invoice;
//
//
//public class InvoicePDFGenerator {
//	
//
//	public InvoicePDFGenerator() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//
//	public boolean generateInvoicePDF(Invoice invoice, List<BillDetail> billDetails, String filePath) {
//        Document document = new Document(PageSize.A4);
//        try {
//            PdfWriter.getInstance(document, new FileOutputStream(filePath));
//            document.open();
//            
//            try {
//                String logoPath = "src/main/webapp/images/logo.png"; // your logo file
//                Image logo = Image.getInstance(logoPath);
//                logo.scaleToFit(100, 50);
//                logo.setAlignment(Element.ALIGN_LEFT);
//                document.add(logo);
//            } catch (Exception e) {
//                System.out.println("Logo not found or failed to load.");
//            }
//        
//      
//
//       
//            Font companyFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLUE);
//            Paragraph company = new Paragraph("Pahana Edu Bookshop", companyFont);
//            company.setAlignment(Element.ALIGN_CENTER);
//            document.add(company);
//
//            document.add(new Paragraph(" ")); // spacing
//
//            // Title
//            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
//            Paragraph title = new Paragraph("Invoice", titleFont);
//            title.setAlignment(Element.ALIGN_CENTER);
//            document.add(title);
//            document.add(new Paragraph(" "));
//
//            // Invoice Info
//            document.add(new Paragraph("Invoice No: " + invoice.getInvoiceNo()));
//            document.add(new Paragraph("Customer Name: " + invoice.getCustomerName()));
//            document.add(new Paragraph("Invoice Date: " + invoice.getInvoiceDate()));
//            document.add(new Paragraph("Due Date: " + invoice.getDueDate()));
//            document.add(new Paragraph("Status: " + invoice.getStatus()));
//            document.add(new Paragraph(" "));
//
//            // Bill Details Table
//            PdfPTable table = new PdfPTable(6); // item_code, item_name, desc, price, qty, total
//            table.setWidthPercentage(100);
//            table.setWidths(new int[]{15, 20, 25, 10, 10, 10});
//
//            // Header row
//            String[] headers = {"Item Code", "Item Name", "Description", "Price", "Qty", "Total"};
//            for (String header : headers) {
//                PdfPCell cell = new PdfPCell(new Phrase(header));
//                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cell);
//            }
//
//            // Rows from bill_details
//            for (BillDetail bd : billDetails) {
//                table.addCell(bd.getItemCode());
//                table.addCell(bd.getItemName());
//                table.addCell(bd.getDescription());
//                table.addCell(String.format("%.2f", bd.getPrice()));
//                table.addCell(String.valueOf(bd.getQuantity()));
//                table.addCell(String.format("%.2f", bd.getTotal()));
//            }
//            document.add(table);
//
//            document.add(new Paragraph(" "));
//            document.add(new Paragraph("Total Qty: " + invoice.getTotalQty()));
//            document.add(new Paragraph("Total Amount: " + invoice.getTotalAmount()));
//            document.add(new Paragraph("Cash Paid: " + invoice.getCash()));
//            document.add(new Paragraph("Balance: " + invoice.getBalance()));
//
//            document.close();
//            System.out.println("Invoice PDF created: " + filePath);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//		return false;
//    }
//
//}

package print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;


import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.BillDetail;
import model.Invoice;

public class InvoicePDFGenerator {

    public InvoicePDFGenerator() {
        super();
    }

    // ✅ Added HttpServletRequest so we can resolve logo path correctly
    public boolean generateInvoicePDF(HttpServletRequest request, Invoice invoice, List<BillDetail> billDetails, String filePath, String username) {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // ✅ Load logo from deployed webapp folder
            try {
                String logoPath = request.getServletContext().getRealPath("/images/logo.png");
                if (logoPath != null) {
                    Image logo = Image.getInstance(logoPath);
                    logo.scaleToFit(200, 100);
                    logo.setAlignment(Element.ALIGN_CENTER);
                    document.add(logo);
                } else {
                    System.out.println("Logo path is null. Check if /images/logo.png exists in webapp.");
                }
            } catch (Exception e) {
                System.out.println("Logo not found or failed to load: " + e.getMessage());
            }

            // Company Name
            Font companyFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLUE);
            Paragraph company = new Paragraph("Pahana Edu Bookshop", companyFont);
            company.setAlignment(Element.ALIGN_CENTER);
            document.add(company);

            document.add(new Paragraph(" ")); // spacing
            
         // Officer name
            Paragraph officer = new Paragraph("Officer: " + username, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
            officer.setAlignment(Element.ALIGN_RIGHT);
            document.add(officer);

            document.add(new Paragraph(" ")); // spacing

            // Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Invoice Info
            document.add(new Paragraph("Invoice No: " + invoice.getInvoiceNo()));
            document.add(new Paragraph("Customer Name: " + invoice.getCustomerName()));
            document.add(new Paragraph("Invoice Date: " + invoice.getInvoiceDate()));
            document.add(new Paragraph("Due Date: " + invoice.getDueDate()));
            document.add(new Paragraph("Status: " + invoice.getStatus()));
            document.add(new Paragraph(" "));

            // Bill Details Table
            PdfPTable table = new PdfPTable(6); // item_code, item_name, desc, price, qty, total
            table.setWidthPercentage(100);
            table.setWidths(new int[]{15, 20, 25, 10, 10, 10});

            // Header row
            String[] headers = {"Item Code", "Item Name", "Description", "Price", "Qty", "Total"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Rows from bill_details
            for (BillDetail bd : billDetails) {
                table.addCell(bd.getItemCode());
                table.addCell(bd.getItemName());
                table.addCell(bd.getDescription());
                table.addCell(String.format("%.2f", bd.getPrice()));
                table.addCell(String.valueOf(bd.getQuantity()));
                table.addCell(String.format("%.2f", bd.getTotal()));
            }
            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total Qty: " + invoice.getTotalQty()));
            document.add(new Paragraph("Total Amount: " + invoice.getTotalAmount()));
            document.add(new Paragraph("Cash Paid: " + invoice.getCash()));
            document.add(new Paragraph("Balance: " + invoice.getBalance()));

            document.close();
            System.out.println("Invoice PDF created: " + filePath);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

