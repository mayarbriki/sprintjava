package services;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import models.Livraison;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServicePDF {

    public static void generateCommandePDF(String path, Livraison livraison) throws FileNotFoundException, MalformedURLException {
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);

        String imagePath = "src/main/resources/com/example/demo3/images/output-onlinepngtools.png";
        ImageData imagedata = ImageDataFactory.create(imagePath);
        Image image = new Image(imagedata);

        float x = pdfDocument.getDefaultPageSize().getWidth()/2;
        float y = pdfDocument.getDefaultPageSize().getHeight()/2;
        image.setFixedPosition(x-200,y-370);
        image.setOpacity(.5f);
        document.add(image);

        float threecol = 190f;
        float twocol = 285f;
        float twocol150 = twocol + 150f;
        float[] twocolumnWidth = {twocol150, twocol};
        float[] fullwidth = {threecol * 3};
        Paragraph onesp = new Paragraph("\n");
        float[] twoColumnWidth1 = {twocol, twocol};

//        String name = livraison.getNom();
        String address = livraison.getAdresseLiv();
        String description = livraison.getDescription();
        String dateLiv = String.valueOf(livraison.getDateLiv());



        Table table = new Table(twocolumnWidth);
        table.addCell(new Cell().add("Commande").setFontSize(20f).setBorder(Border.NO_BORDER));
        Table nestedtable = new Table(new float[]{twocol / 2, twocol / 2});
        nestedtable.addCell(getHeaderTextCell("Commande N°."));
        nestedtable.addCell(getHeaderTextValue("#21165"));
        nestedtable.addCell(getHeaderTextCell("Date Commande"));
        nestedtable.addCell(getHeaderTextValue(getCurrentDate()));

        table.addCell(new Cell().add(nestedtable).setBorder(Border.NO_BORDER));

        Border gb = new SolidBorder(Color.GRAY, 2f);
        Table divider = new Table(fullwidth);
        divider.setBorder(gb);
        document.add(table);
        document.add(onesp);
        document.add(divider);
        document.add(onesp);

        Table twocolTable = new Table(twocolumnWidth);
        twocolTable.addCell(getBillingShippingCell("Informations de facturation"));
        twocolTable.addCell(getBillingShippingCell("Informations de livraison"));
        document.add(twocolTable.setMarginBottom(12f));

        Table twocolTable2 = new Table(twocolumnWidth);
        twocolTable2.addCell(getCell10Left("Entreprise", true));
        twocolTable2.addCell(getCell10Left("Nom de livreur", true));
        twocolTable2.addCell(getCell10Left("Parapharma+", false));
        twocolTable2.addCell(getCell10Left("livreur", false));
        document.add(twocolTable2);

        Table twocolTable3 = new Table(twocolumnWidth);
        twocolTable3.addCell(getCell10Left("Site Web", true));
        twocolTable3.addCell(getCell10Left("Adresse", true));
        twocolTable3.addCell(getCell10Left("www.parapharma+.com", false));
        twocolTable3.addCell(getCell10Left(address, false));
        document.add(twocolTable3);

        float[] oneColumnWidth = {twocol150};

        Table oneColTable1 = new Table(oneColumnWidth);
        oneColTable1.addCell(getCell10Left("Adresse", true));
        oneColTable1.addCell(getCell10Left("1, 2 rue André Ampère - 2083 - Pôle Technologique - El Ghazala", false));
        oneColTable1.addCell(getCell10Left("Email", true));
        oneColTable1.addCell(getCell10Left("parapharma+@gmail.com", false));
        document.add(oneColTable1.setMarginBottom(10f));

        Table tableDivider2 = new Table(fullwidth);
        Border dgb = new DashedBorder(Color.GRAY, 0.5f);
        document.add(tableDivider2.setBorder(dgb));
        Paragraph producPara = new Paragraph("Livraison");

        document.add(producPara.setBold());
        Table twoColTable1 = new Table(twoColumnWidth1);
        twoColTable1.setBackgroundColor(Color.BLACK, 0.7f);

        twoColTable1.addCell(new Cell().add("Description").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER));
        twoColTable1.addCell(new Cell().add("Date de livraison").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));


        document.add(twoColTable1);


        Table twoColTable2 = new Table(twoColumnWidth1);
        twoColTable2.setBackgroundColor(Color.GRAY, 0.25f);

        twoColTable2.addCell(new Cell().add(description).setFontColor(Color.BLACK).setBorder(Border.NO_BORDER));
        twoColTable2.addCell(new Cell().add(dateLiv).setFontColor(Color.BLACK).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

        document.add(twoColTable2.setMarginBottom(20f));
        document.add(tableDivider2);
        document.add(new Paragraph("\n"));
        document.add(divider.setBorder(new SolidBorder(Color.GRAY, 1)).setMarginBottom(15f));
        Table tb = new Table(fullwidth);
        tb.addCell(new Cell().add("Termes et conditions\n"));
        List<String> TncList = new ArrayList<>();
        TncList.add("1. le vendeur ne sera pas responsable envers l'acheteur directement ou indirectement de toute perte ou dommage subi");
        TncList.add("2. le vendeur garantit le produit pendant un (1) an à compter de la date d'expédition");

        for(String tnc:TncList){
            tb.addCell(new Cell().add(tnc).setBorder(Border.NO_BORDER));
        }
        document.add(tb);



        document.close();
    }

    static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDate.format(formatter);
    }

    static Cell getHeaderTextCell(String textValue) {
        return new Cell().add(textValue).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    static Cell getHeaderTextValue(String textValue) {
        return new Cell().add(textValue).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getBillingShippingCell(String textValue){
        return new Cell().add(textValue).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getCell10Left(String textValue, Boolean isBold){
        Cell myCell = new Cell().add(textValue).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ?myCell.setBold():myCell;

    }
}
