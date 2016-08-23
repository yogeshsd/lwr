package com.lwr.software.reporter.renderer.report;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.data.general.Dataset;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.renderer.element.ElementRendererFactory;
import com.lwr.software.reporter.renderer.element.IELementRendererFactory;
import com.lwr.software.reporter.renderer.element.IElementRenderer;
import com.lwr.software.reporter.renderer.html.jfreechart.HTMLJFreeAbstract;
import com.lwr.software.reporter.reportmgmt.Element;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.RowElement;

public class PDFReportRenderer extends AbstractReportRenderer {

	public PDFReportRenderer(Report report) {
		super(report);
	}

	@Override
	public String render() {
		throw new RuntimeException("Not Implemented");
	}

	@Override
	public void render(OutputStream stream) {
		PDDocument document = new PDDocument();
		List<RowElement> rowElements = report.getRows();
		try {
			for (RowElement rowElement : rowElements) {
				List<Element> elements = rowElement.getElements();
				for (Element element : elements) {
					PDPage blankPage = new PDPage();
					PDRectangle pageSize = blankPage.getMediaBox();
					PDPageContentStream contentStream = new PDPageContentStream(document, blankPage);
					contentStream.beginText();
					contentStream.moveTextPositionByAmount(50,pageSize.getUpperRightY()-50);
					contentStream.setFont(PDType1Font.TIMES_BOLD, 14);
					contentStream.drawString(element.getTitle());
					contentStream.endText();
					contentStream.close();


					contentStream = new PDPageContentStream(document, blankPage, AppendMode.APPEND, true, false);
					IELementRendererFactory factory = ElementRendererFactory.getRendererFactory(DashboardConstants.HTML_JFREE);
					IElementRenderer renderer = factory.getRenderer(element);
					
					Dataset dataSet = ((HTMLJFreeAbstract) renderer).getDataset();
					File file = ((HTMLJFreeAbstract) renderer).getImage(dataSet);
					if (file == null)
						continue;
					PDImageXObject pdImage = PDImageXObject.createFromFile(file.getAbsolutePath(), document);
					int imageWidth = pdImage.getWidth();
					int imageHeight = pdImage.getHeight();
					
					float widthScale = imageWidth/pageSize.getUpperRightX()>1?1:imageWidth/pageSize.getUpperRightX();
					float heightScale = imageHeight/pageSize.getUpperRightY()>1?1:imageHeight/pageSize.getUpperRightY();

					System.out.println(" Page Width = "+pageSize.getUpperRightX());
					System.out.println(" Page Height = "+pageSize.getUpperRightY());
					System.out.println(" Image Width = "+imageWidth);
					System.out.println(" Image Height = "+imageHeight);
					System.out.println(" Width Scale = "+widthScale);
					System.out.println(" Height Scale = "+heightScale);
					contentStream.drawXObject(pdImage, 50, pageSize.getUpperRightY()-(imageHeight+100), pdImage.getWidth() * heightScale, pdImage.getHeight() * widthScale);
					contentStream.close();
					document.addPage(blankPage);
				}
			}
			document.save(stream);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
