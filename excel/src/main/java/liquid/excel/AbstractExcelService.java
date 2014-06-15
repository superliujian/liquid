package liquid.excel;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by redbrick9 on 6/14/14.
 */
public class AbstractExcelService<T> {
    public List<T> extract(InputStream inputStream, RowMapper<T> rowMapper, final Class<T> clazz) throws IOException, OpenXML4JException, SAXException {
        final List<T> list = new ArrayList<>();

        OPCPackage opcPackage = OPCPackage.open(inputStream);
        XSSFReader reader = new XSSFReader(opcPackage);
        final SharedStringsTable sharedStringsTable = reader.getSharedStringsTable();

        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");

        ContentHandler handler = new DefaultHandler() {
            private String lastContents;
            private boolean nextIsString;
            T entity;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equals("row")) {
                    //
                    try {
                        entity = clazz.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                // c => cell
                if (qName.equals("c")) {
                    // Print the cell reference
                    System.out.print(attributes.getValue("r") + " - ");
                    // Figure out if the value is an index in the SST
                    String cellType = attributes.getValue("t");
                    if (cellType != null && cellType.equals("s")) {
                        nextIsString = true;
                    } else {
                        nextIsString = false;
                    }
                }
                // Clear contents cache
                lastContents = "";
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                // Process the last contents as required.
                // Do now, as characters() may be called more than once
                if (nextIsString) {
                    int idx = Integer.parseInt(lastContents);
                    lastContents = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx)).toString();
                    nextIsString = false;
                }

                // v => contents of a cell
                // Output after we've seen the string contents
                if (qName.equals("v")) {
                    System.out.println(lastContents);
                }
                if (qName.equals("row")) {
                    list.add(entity);
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                lastContents += new String(ch, start, length);
            }
        };

        parser.setContentHandler(handler);

        reader.getWorkbookData();

        Iterator<InputStream> sheets = reader.getSheetsData();
        while (sheets.hasNext()) {
            System.out.println("Processing new sheet:\n");
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
            System.out.println("");
        }
        return list;
    }
}
